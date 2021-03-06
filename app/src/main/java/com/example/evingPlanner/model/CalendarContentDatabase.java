package com.example.evingPlanner.model;


import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.evingPlanner.domain.DayClass;

@Database(entities = {DayClass.class}, version = 1)
public abstract class CalendarContentDatabase extends RoomDatabase {
    public abstract CalendarDayDAO getDayDAO();
    public abstract CalendarMonthDAO getMonthDAO();
}
