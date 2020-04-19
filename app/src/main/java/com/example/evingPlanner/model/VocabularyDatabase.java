package com.example.evingPlanner.model;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.evingPlanner.domain.Day;
import com.example.evingPlanner.domain.DayVocaJoin;
import com.example.evingPlanner.domain.Plan;
import com.example.evingPlanner.domain.Vocabulary;

import lombok.Synchronized;

@Database(entities = {Vocabulary.class, Day.class, DayVocaJoin.class},
        version = 1)
public abstract class VocabularyDatabase extends RoomDatabase {

    private static VocabularyDatabase instance;

    public abstract VocabularyDAO getVocaDAO();
    public abstract DayVocaJoinDAO getDayVocaJoinDAO();

    public static synchronized VocabularyDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), VocabularyDatabase.class,
                    "vocabulary_db")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

}
