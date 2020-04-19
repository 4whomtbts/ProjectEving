package com.example.evingPlanner.ui.singleDayDialog;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.evingPlanner.domain.VocaDayJoinWithVoca;
import com.example.evingPlanner.domain.Vocabulary;
import com.example.evingPlanner.repository.DayVocaJoinRepsotiory;
import com.example.evingPlanner.repository.VocabularyRepository;

import java.util.concurrent.ExecutionException;

import static com.google.common.base.Preconditions.checkNotNull;

public class VocabularyItemViewModel extends AndroidViewModel {

    public String voca;
    public String translation;
    public String description;

    public VocabularyItemViewModel(@NonNull Application application) {
        super(application);
    }

    public void init(VocaDayJoinWithVoca vocaDayJoin) {
        Vocabulary vocabulary = vocaDayJoin.getVocabulary();
        voca = vocabulary.getVoca();
        translation = vocabulary.getTranslation();
        description = vocabulary.getDescription();
    }

    public void modify(Vocabulary vocabulary) {
        checkNotNull(vocabulary, "vocabulary couldn't be null");
        new VocabularyRepository.UpdateVocabulary().execute(vocabulary);
        voca = vocabulary.getVoca();
        translation = vocabulary.getTranslation();
        description = vocabulary.getDescription();
    }

    public void delete(VocaDayJoinWithVoca vocaDayJoinWith) throws ExecutionException, InterruptedException {
        Vocabulary voca = vocaDayJoinWith.getVocabulary();
        new VocabularyRepository.DeleteVocabulary().execute(voca).get();
        new DayVocaJoinRepsotiory.Delete().execute(vocaDayJoinWith.getDayVocaJoin()).get();
        new VocabularyRepository.ReOrderingVocaCycle().execute(voca).get();
    }

}
