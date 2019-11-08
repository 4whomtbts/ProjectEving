package com.example.databinding2.model;

import androidx.room.Dao;
import androidx.room.Insert;

import com.example.databinding2.domain.DayClass;

@Dao
public interface CalendarMonthDAO {
    @Insert
    public void insert(DayClass... day);
}
