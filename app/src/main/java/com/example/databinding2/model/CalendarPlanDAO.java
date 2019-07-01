package com.example.databinding2.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.databinding2.domain.Plan;

import java.util.List;

@Dao
public interface CalendarPlanDAO {
    @Insert
    public void insert(Plan... plan);

    @Delete
    public void delete(Plan... plan);

    @Query("DELETE FROM table_plans")
    public void deleteAll();

    @Query("SELECT * FROM table_plans")
    public List<Plan> selectAll();

    @Query("SELECT * FROM table_plans WHERE year = :year AND month = :month AND day = :day")
    //public ArrayList<TSLiveData<Plan>> getPlanByDay(int year, int month, int day);
    public List<Plan> getPlanByDay(int year, int month, int day);

    @Query("SELECT * FROM table_plans WHERE year = :year AND month = :month")
    public List<Plan> getPlanByMonthDefault(int year, int month);


    @Query("SELECT * FROM table_plans WHERE year = :year AND month = :month ORDER BY day ASC")
    public List<Plan> getPlanByMonth(int year, int month);

    @Query("SELECT * FROM table_plans WHERE year = :year AND month = :month AND day  BETWEEN :left AND :right")
    public List<Plan> getPlanByRangeOfDay(int year, int month, int left, int right);

}
