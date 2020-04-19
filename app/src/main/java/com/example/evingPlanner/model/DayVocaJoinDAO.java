package com.example.evingPlanner.model;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.evingPlanner.domain.DayVocaJoin;
import com.example.evingPlanner.domain.VocaDayJoinWithVoca;
import com.example.evingPlanner.domain.Vocabulary;

import java.util.List;

@Dao
public interface DayVocaJoinDAO {

    @Insert
    void insert(DayVocaJoin ...dayVocaJoin);

    @Update
    void update(DayVocaJoin ...dayVocaJoins);

    @Query("SELECT * FROM day_voca_join")
    List<DayVocaJoin> selectAll();

    @Query("SELECT * FROM day_voca_join d WHERE day_id = :dayId")
    List<DayVocaJoin> getDayVocaJoinForDay(long dayId);

    @Query("SELECT * FROM table_vocabulary")
    List<Vocabulary> getVoca();

}
