package com.example.evingPlanner.domain;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import lombok.Getter;
import lombok.Setter;

import static androidx.room.ForeignKey.CASCADE;

@Getter
@Setter
@Entity(
        tableName = "day_voca_join")
public class DayVocaJoin {

    @PrimaryKey
    public long id;

    @ColumnInfo(name = "day_id")
    public long dayId;

    @ColumnInfo(name = "voca_id")
    public long vocaId;

    public int order;

    public DayVocaJoin(long dayId, long vocaId, int order) {
        this.id = System.nanoTime();
        this.dayId = dayId;
        this.vocaId = vocaId;
        this.order = order;
    }
}
