package com.example.evingPlanner.repository;

import android.os.AsyncTask;

import com.example.evingPlanner.domain.Day;
import com.example.evingPlanner.domain.DayVocaJoin;
import com.example.evingPlanner.domain.VocaDayJoinWithVoca;
import com.example.evingPlanner.domain.Vocabulary;

import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class DayVocaJoinRepsotiory {

    private static DayVocaJoinRepsotiory Inst;

    public static DayVocaJoinRepsotiory get() {
        if (Inst == null) {
            Inst = new DayVocaJoinRepsotiory();
        }
        return Inst;
    }

    public static class InsertDayVocas extends AsyncTask<DayVocaJoin, Void, Boolean> {

        @Override
        protected Boolean doInBackground(DayVocaJoin... DayVocaJoins) {
            checkNotNull(DayVocaJoins, "vocabulary couldn't be null");
            RootRepository.getDayvocaJoinDatabase().getDayVocaJoinDAO().insert(DayVocaJoins);
            return true;
        }
    }

    public static class InsertOneDayVocaJoin extends AsyncTask<DayVocaJoin, Void, Boolean> {

        @Override
        protected Boolean doInBackground(DayVocaJoin... dayVocaJoins) {
            checkNotNull(dayVocaJoins[0], "vocabulary couldn't be null");
            checkArgument(dayVocaJoins.length == 1, "InsertOneVocabular requires single vocabulary");

            RootRepository.getDayvocaJoinDatabase().getDayVocaJoinDAO().insert(dayVocaJoins[0]);

            return true;
        }
    }

    public static class UpdateDayVocas extends AsyncTask<DayVocaJoin, Void, Boolean> {

        @Override
        protected Boolean doInBackground(DayVocaJoin... DayVocaJoins) {
            checkNotNull(DayVocaJoins, "vocabulary couldn't be null");

            RootRepository.getDayvocaJoinDatabase().getDayVocaJoinDAO().update(DayVocaJoins);

            return true;
        }
    }

    public static class SelectAllDayVocaJoin extends AsyncTask<DayVocaJoin, Void, List<DayVocaJoin>> {

        @Override
        protected List<DayVocaJoin> doInBackground(DayVocaJoin... dayVocaJoins) {
            return RootRepository.getDayvocaJoinDatabase().getDayVocaJoinDAO().selectAll();
        }
    }

    public static class SelectDayVocaJoinForDay extends AsyncTask<Day, Void, List<DayVocaJoin>> {

        @Override
        protected List<DayVocaJoin> doInBackground(Day... days) {
            return RootRepository.getDayvocaJoinDatabase().getDayVocaJoinDAO().getDayVocaJoinForDay(days[0].getId());
        }
    }

    public static class SelectVoacaFor extends AsyncTask<Day, Void, List<Vocabulary>> {

        @Override
        protected List<Vocabulary> doInBackground(Day... days) {
            return RootRepository.getDayvocaJoinDatabase().getDayVocaJoinDAO().getVoca();
        }
    }
}
