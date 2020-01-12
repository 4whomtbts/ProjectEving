package com.example.evingPlanner.model;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.evingPlanner.domain.Plan;

@Database(entities = {Plan.class}, version = 2)
public abstract class PlanDatabase extends RoomDatabase {
    public abstract CalendarPlanDAO getPlanDAO();

    public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE table_plans"
                    + " ADD COLUMN group_uid INTEGER NOT NULL DEFAULT 0");
        }
    };
}
