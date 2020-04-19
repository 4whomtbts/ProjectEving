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

    private int order;

    public VocaDayJoinWithVoca(Vocabulary vocabulary, int order) {
        this.vocabulary = vocabulary;
        this.order = order;
    }

}
