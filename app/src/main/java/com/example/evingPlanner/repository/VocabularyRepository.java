package com.example.evingPlanner.repository;

import android.os.AsyncTask;

import com.example.evingPlanner.domain.Day;
import com.example.evingPlanner.domain.DayVocaJoin;
import com.example.evingPlanner.domain.VocaDayJoinWithVoca;
import com.example.evingPlanner.domain.Vocabulary;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class VocabularyRepository {

    private static VocabularyRepository Inst;

    public static VocabularyRepository get() {
        if (Inst == null) {
            Inst = new VocabularyRepository();
        }
        return Inst;
    }

    public static class InsertVocabularies extends AsyncTask<Vocabulary, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Vocabulary... vocabularies) {
            checkNotNull(vocabularies, "vocabulary couldn't be null");

            for (Vocabulary vocabulary : vocabularies) {
                RootRepository.getVocaTypeDatabase().getVocaDAO().insert(vocabulary);
            }

            return true;
        }
    }

    public static class InsertOneVocabulary extends AsyncTask<Vocabulary, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Vocabulary... vocabularies) {
            checkNotNull(vocabularies[0], "vocabulary couldn't be null");
            checkArgument(vocabularies.length == 1, "InsertOneVocabular requires single vocabulary");

            RootRepository.getVocaTypeDatabase().getVocaDAO().insert(vocabularies[0]);

            return true;
        }
    }

    public static class SelectAllVocabulary extends AsyncTask<Vocabulary, Void, List<Vocabulary>> {

        @Override
        protected List<Vocabulary> doInBackground(Vocabulary... vocabularies) {
            return RootRepository.getVocaTypeDatabase().getVocaDAO().selectAll();
        }
    }

    public static class SelectAllVocabularyByJoin extends AsyncTask<List<DayVocaJoin>, Void, List<Vocabulary>> {

        @Override
        protected List<Vocabulary> doInBackground(List<DayVocaJoin>... dayVocaJoins) {

            List<DayVocaJoin> target = dayVocaJoins[0];
            List<Vocabulary> result = new ArrayList<>();

            for (DayVocaJoin dayVocaJoin : target) {
                result.add(RootRepository.getVocaTypeDatabase().getVocaDAO().selectVocaById(dayVocaJoin.vocaId));
            }
            return result;
        }
    }

    public static class GetJoin extends AsyncTask<Day, Void, List<VocaDayJoinWithVoca>> {

        @Override
        protected List<VocaDayJoinWithVoca> doInBackground(Day ...days) {
            return RootRepository.getVocaTypeDatabase().getVocaDAO().getVocaForDay();
        }
    }
}

