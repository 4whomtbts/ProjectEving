package com.example.evingPlanner.domain;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(tableName = "day_voca_join")
public class DayVocaJoin {

    @PrimaryKey
    private long id;

    @ColumnInfo(name = "day_id")
    private long dayId;

    @ColumnInfo(name = "voca_id")
    private long vocaId;

    private int order;

    @ColumnInfo(name = "created_at")
    private long createdAt;

    public DayVocaJoin(long dayId, long vocaId, int order) {
        this.id = System.nanoTime();
        this.dayId = dayId;
        this.vocaId = vocaId;
        this.order = order;
        this.createdAt = System.currentTimeMillis();
    }
}
