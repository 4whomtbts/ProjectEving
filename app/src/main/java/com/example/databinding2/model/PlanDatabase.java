package com.example.databinding2.model;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.databinding2.domain.Plan;

@Database(entities = {Plan.class},version = 1)
public abstract class PlanDatabase extends RoomDatabase {
    public abstract CalendarPlanDAO getPlanDAO();
}
