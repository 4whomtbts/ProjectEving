package com.example.evingPlanner.repository;

import android.os.AsyncTask;

import com.example.evingPlanner.domain.Day;

import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class DayRepository  {

    private static DayRepository Inst;

    public static DayRepository get() {
        if (Inst == null) {
            Inst = new DayRepository();
        }
        return Inst;
    }

    public static class InsertOneDay extends AsyncTask<Day, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Day... Days) {
            checkNotNull(Days, "vocabulary couldn't be null");

            for (Day day : Days) {
                RootRepository.getDayDatabase().getDayDAO().insert(day);
            }

            return true;
        }
    }

    public static class UpsertDays extends AsyncTask<List<Day>, Void, Boolean> {

        @Override
        protected Boolean doInBackground(List<Day>... Days) {
            checkNotNull(Days[0], "vocabulary couldn't be null");
            checkArgument(Days.length == 1, "InsertOneVocabular requires single vocabulary");

            Day[] days = new Day[Days[0].size()];
            for (int i=0; i < Days[0].size(); i++) {
                days[i] = Days[0].get(i);
            }
            RootRepository.getDayDatabase().getDayDAO().upsert(days);

            return true;
        }
    }

    public static class SelectAllDay extends AsyncTask<Day, Void, List<Day>> {

        @Override
        protected List<Day> doInBackground(Day... Days) {
            return RootRepository.getDayDatabase().getDayDAO().selectAll();
        }
    }

    public static class SelectDay extends AsyncTask<Day, Void, Day> {

        @Override
        protected Day doInBackground(Day... Days) {
            Day day = Days[0];
            return RootRepository.getDayDatabase().getDayDAO().selectDayFromId(day.getId());
        }
    }}
