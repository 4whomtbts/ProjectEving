package com.example.evingPlanner.model;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;


import com.example.evingPlanner.domain.Day;
import com.example.evingPlanner.domain.DayVocaJoin;
import com.example.evingPlanner.domain.Vocabulary;

import java.util.List;

public class DayWithVocas {

    @Embedded
    public Day day;

    @Relation(
            parentColumn = "day_id",
            entity = Vocabulary.class,
            entityColumn = "voca_id",
            associateBy = @Junction(
                    value = DayVocaJoin.class,
                    parentColumn = "day_id",
                    entityColumn = "voca_id"))
    List<Vocabulary> vocas;
}
