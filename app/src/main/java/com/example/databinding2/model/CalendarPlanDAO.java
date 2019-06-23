package com.example.databinding2.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.databinding2.TSLiveData;
import com.example.databinding2.custom.YMD;
import com.example.databinding2.domain.Plan;
import com.example.databinding2.util.Constants.*;

import java.util.ArrayList;
import java.util.List;

import static com.example.databinding2.util.Constants.PLAN_TABLE;

@Dao
public interface CalendarPlanDAO {
    @Insert
    public void insert(Plan... plan);

    @Delete
    public void delete(Plan... plan);

    @Query("SELECT * FROM table_plans WHERE year = :year AND month = :month AND day = :day")
    //public ArrayList<TSLiveData<Plan>> getPlanByDay(int year, int month, int day);
    public List<Plan> getPlanByDay(int year, int month, int day);

}
