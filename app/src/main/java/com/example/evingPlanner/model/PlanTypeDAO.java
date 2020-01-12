package com.example.evingPlanner.model;

import androidx.annotation.NonNull;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.evingPlanner.domain.planTypes.PlanType;

import java.util.List;

@Dao
public interface PlanTypeDAO {
    @Insert
    public void insert(PlanType... planTypes);

    @Delete
    public void delete(PlanType... planTypes);

    @Query("DELETE FROM table_planTypes WHERE planTypeName = :planTypeName ")
    public void deletePlanTypeByName(String planTypeName);

    @Query("DELETE FROM table_planTypes WHERE uid = :uid")
    public void deletePlanTypeByUID(long uid);

    @Query("DELETE FROM table_planTypes")
    public void deleteAll();

    @Query("SELECT * FROM table_planTypes")
    public List<PlanType> selectAll();

    @Query("SELECT * FROM table_planTypes WHERE planTypeName = :planTypeName")
    public PlanType selectPlanTypeByName(String planTypeName);

    @Query("SELECT * FROM table_planTypes WHERE uid = :uid")
    public PlanType selectPlanTypeByUID(long uid);

    @Query("UPDATE table_planTypes SET planTypeName = :newPlanTypeName, cycles = :cycles WHERE uid = :uid")
    public void updatePlanTypeByUID(long uid, String newPlanTypeName, String cycles);
}
