package com.example.databinding2.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.databinding2.domain.planTypes.PlanType;

import java.util.List;

@Dao
public interface PlanTypeDAO {
    @Insert
    public void insert(PlanType... planTypes);

    @Delete
    public void delete(PlanType... planTypes);

    @Query("DELETE FROM table_planTypes WHERE planTypeName = :planTypeName ")
    public void deletePlanTypeByName(String planTypeName);

    @Query("DELETE FROM table_planTypes")
    public void deleteAll();

    @Query("SELECT * FROM table_planTypes")
    public List<PlanType> selectAll();

    @Query("SELECT * FROM table_planTypes WHERE planTypeName = :planTypeName")
    public PlanType selectPlanTypeByName(String planTypeName);






}
