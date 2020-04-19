package com.example.evingPlanner.model;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.evingPlanner.domain.Day;
import com.example.evingPlanner.domain.DayVocaJoin;
import com.example.evingPlanner.domain.Vocabulary;

@Database(entities = {Vocabulary.class, Day.class, DayVocaJoin.class},
        version = 1)
public abstract class DayDatabase extends RoomDatabase {

    private static DayDatabase instance;

    public static synchronized DayDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), DayDatabase.class,
                    "day_db")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    public abstract DayDAO getDayDAO();
}
