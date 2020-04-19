package com.example.evingPlanner.domain;

import androidx.room.Embedded;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VocaDayJoinWithVoca {

    @Embedded
    private Vocabulary vocabulary;

    @Embedded
    private DayVocaJoin dayVocaJoin;

    public VocaDayJoinWithVoca(Vocabulary vocabulary, DayVocaJoin dayVocaJoin) {
        this.vocabulary = vocabulary;
        this.dayVocaJoin = dayVocaJoin;
    }

}
