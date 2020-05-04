package com.example.evingPlanner.domain;

import androidx.room.Embedded;

import lombok.Getter;
import lombok.Setter;

import static com.google.common.base.Preconditions.checkNotNull;

@Getter
@Setter
public class VocaDayJoinWithVoca {

    @Embedded
    private final Vocabulary vocabulary;

    @Embedded
    private final DayVocaJoin dayVocaJoin;

    public VocaDayJoinWithVoca(Vocabulary vocabulary, DayVocaJoin dayVocaJoin) {
        checkNotNull(vocabulary, "vocabulary for VocaDayJoinWithVoca cannot be null");
        checkNotNull(dayVocaJoin, "dayVocaJoin for VocaDayJoinWithVoca cannot be null");

        this.vocabulary = vocabulary;
        this.dayVocaJoin = dayVocaJoin;
    }

}
