package com.example.evingPlanner.ui.singleDayDialog;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.evingPlanner.domain.Day;
import com.example.evingPlanner.domain.DayVocaJoin;
import com.example.evingPlanner.domain.VocaDayJoinWithVoca;
import com.example.evingPlanner.domain.Vocabulary;
import com.example.evingPlanner.domain.planTypes.PlanType;
import com.example.evingPlanner.repository.DayRepository;
import com.example.evingPlanner.repository.DayVocaJoinRepsotiory;
import com.example.evingPlanner.repository.VocabularyRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class VocaAdapterViewModel extends AndroidViewModel {

    final MutableLiveData<List<Vocabulary>> vocabularies = new MutableLiveData<>();
    final MutableLiveData<List<VocaDayJoinWithVoca>> todayVocabulary = new MutableLiveData<>();
    final MutableLiveData<List<VocaDayJoinWithVoca>> reviewVocabulary = new MutableLiveData<>();

    public VocaAdapterViewModel(@NonNull Application application) {
        super(application);
        todayVocabulary.setValue(new ArrayList<VocaDayJoinWithVoca>());
        reviewVocabulary.setValue(new ArrayList<VocaDayJoinWithVoca>());
    }

    public void fetchVocas(final int year, final int month, final int day) throws ExecutionException, InterruptedException {

        final List<DayVocaJoin> vocaDayJoinWithVocas =
                new DayVocaJoinRepsotiory.SelectDayVocaJoinForDay().execute(new Day(year, month, day)).get();
        final List<Vocabulary> todayVocas =
                new VocabularyRepository.SelectAllVocabularyByJoin().execute(vocaDayJoinWithVocas).get();

        final List<VocaDayJoinWithVoca> todayVocaList = new ArrayList<>();
        final List<VocaDayJoinWithVoca> reviewVocaList = new ArrayList<>();

        for(int i=0; i < vocaDayJoinWithVocas.size(); i++) {
                Vocabulary voca = todayVocas.get(i);
                VocaDayJoinWithVoca vocaDayJoinWithVoca = new VocaDayJoinWithVoca(voca, 0);
                if (voca.isParent()) {
                    todayVocaList.add(vocaDayJoinWithVoca);
                } else {
                    reviewVocaList.add(vocaDayJoinWithVoca);
                }
        }

        todayVocabulary.setValue(todayVocaList);
        reviewVocabulary.setValue(reviewVocaList);
    }

    public void addVoca(String voca, String translation,
                        String description, PlanType planType,
                        int year, int month, int day) throws ExecutionException, InterruptedException {
        checkNotNull(voca, "voca shouldn't be null");
        checkNotNull(translation, "translation couldn't be null");
        checkNotNull(description, "description couldn't be null");
        checkNotNull(planType, "plan type couldn't be null");
        //checkArgument(voca.equals("g"), getApplication().getResources().getString(R.string.voca_couldnt_be_empty));

        checkArgument(year > 0, "year should be positive number");
        checkArgument(month > 0 && month < 13, "month is invalid");
        checkArgument(day > 0 && day < 32, "day is invalid");

        final int[] cycles = planType.cycles;

        Vocabulary vocabulary = new Vocabulary(voca, translation, description, cycles.length);
        List<Vocabulary> vocabularies = vocabulary.generateReplicas(vocabulary, cycles);

        Vocabulary[] vocaArray = new Vocabulary[vocabularies.size()];
        for (int i=0; i < vocabularies.size(); i++) {
            vocaArray[i] = vocabularies.get(i);
        }

        new VocabularyRepository.InsertVocabularies().execute(vocaArray);


        Day dayEntity = new Day(year, month, day);
        Day existDay = new DayRepository.SelectDay().execute(dayEntity).get();
        List<Day> allShouldBeMadeDay = dayEntity.getDayShouldBeMade(dayEntity, cycles);

        new DayRepository.UpsertDays().execute(allShouldBeMadeDay).get();

        List<DayVocaJoin> dayVocaJoins = new ArrayList<>();
        DayVocaJoin[] dayVocaJoinArray = new DayVocaJoin[cycles.length];
        for (int i = 0; i < cycles.length; i++) {
                DayVocaJoin dayVocaJoin =
                        new DayVocaJoin(allShouldBeMadeDay.get(i).getId(), vocabularies.get(i).getId(), i);
                dayVocaJoins.add(dayVocaJoin);
                dayVocaJoinArray[i] = dayVocaJoin;
        }

        new DayVocaJoinRepsotiory.InsertDayVocas().execute(dayVocaJoinArray);

        List<DayVocaJoin> result =  new DayVocaJoinRepsotiory.SelectDayVocaJoinForDay().execute(dayEntity).get();

        /*
        List<Vocabulary> vocabularyList = new VocabularyRepository.SelectAllVocabularyByJoin().execute(result).get();

        List<VocaDayJoinWithVoca> todayVocas = new ArrayList<>();
        for (int i=0; i < result.size(); i++) {
            todayVocas.add(new VocaDayJoinWithVoca(vocabularyList.get(i), result.get(i).order));
        }
         */


        List<VocaDayJoinWithVoca> refreshTodayVocas;
        refreshTodayVocas = todayVocabulary.getValue();

        if (refreshTodayVocas == null) {
            refreshTodayVocas = new ArrayList<>();
        }

        DayVocaJoin todayVocaJoin = dayVocaJoinArray[0];
        Vocabulary todayVoca = vocaArray[0];

        refreshTodayVocas.add(new VocaDayJoinWithVoca(todayVoca, todayVocaJoin.order));
        todayVocabulary.setValue(refreshTodayVocas);
    }

}
