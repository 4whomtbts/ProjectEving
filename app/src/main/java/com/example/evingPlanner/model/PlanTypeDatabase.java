package com.example.evingPlanner.model;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.evingPlanner.domain.planTypes.PlanType;

@Database(entities = {PlanType.class},version = 1)
@TypeConverters({PlanTypeConverters.class})
public abstract class PlanTypeDatabase extends RoomDatabase {
    public abstract PlanTypeDAO getPlanTypeDAO();

}
