package com.example.evingPlanner.model;

import androidx.annotation.NonNull;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.evingPlanner.domain.VocaDayJoinWithVoca;
import com.example.evingPlanner.domain.Vocabulary;
import com.example.evingPlanner.domain.planTypes.PlanType;

import java.util.List;

@Dao
public interface VocabularyDAO {
    @Insert
    public void insert(Vocabulary... vocabularies);

    @Delete
    public void delete(Vocabulary... vocabularies);

    @Update
    public void update(Vocabulary... vocabularies);

    @Query("SELECT * FROM table_vocabulary")
    public List<Vocabulary> selectAll();

    @Query("SELECT * FROM table_vocabulary WHERE table_vocabulary_id = :vocaid")
    public Vocabulary selectVocaById(long vocaid);

    @Query("SELECT * FROM table_vocabulary WHERE parentId = :parentId ORDER BY thisCycle ASC")
    public List<Vocabulary> selectVocaByParent(long parentId);

    @Query("SELECT * FROM day_voca_join d INNER JOIN table_vocabulary t ON d.voca_id = t.table_vocabulary_id")
    List<VocaDayJoinWithVoca> getVocaForDay();

    /*
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

     */
}

