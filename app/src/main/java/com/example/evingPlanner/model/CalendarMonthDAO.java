package com.example.evingPlanner.model;

import androidx.room.Dao;
import androidx.room.Insert;

import com.example.evingPlanner.domain.DayClass;

@Dao
public interface CalendarMonthDAO {
    @Insert
    public void insert(DayClass... day);
}
