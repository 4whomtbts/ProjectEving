package com.example.evingPlanner.model;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.evingPlanner.domain.DayClass;

import java.util.List;

@Dao
public interface CalendarDayDAO {

    @Insert
    public void insert(DayClass... days);

    @Insert
    public void insertDay(DayClass day);

    @Update
    public void update(DayClass days);

    @Update
    public void updateDay(DayClass day);

    @Delete
    public void delete(DayClass days);

    @Delete
    public void deleteDay(DayClass day);


    @Query("SELECT * FROM table_days")
    public List<DayClass> getAllPlannedDay();

    @Query("SELECT * FROM table_days WHERE year = :year AND  month = :month AND day = :day")
    public List<DayClass> getDaysByDay(int year, int month, int day);

    @Query("SELECT * FROM table_days WHERE rootId = :rootId")
    public DayClass getDayByRootId(long rootId);

    @Query("SELECT * FROM table_days WHERE year = :year AND month = :month")
    public List<DayClass> getDaysByMonth(int year, int month);

    @Query("SELECT * FROM table_days WHERE year = :year AND month = :month")
    public DayClass getDayByMonth(int year, int month);



}
