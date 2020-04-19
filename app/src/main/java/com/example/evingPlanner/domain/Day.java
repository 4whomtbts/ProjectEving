package com.example.evingPlanner.domain;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;


import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@Entity(tableName = "table_day")
public class Day {

    @PrimaryKey
    @ColumnInfo(name = "table_day_id")
    private long id;

    public Day(int id) {
        this.id = id;
    }

    public Day(int year, int month, int day) {
        this.id = (year * 10000) + (month * 100) + day;
    }

    public List<Day> getDayShouldBeMade(Day day, int[] cycle) {
        List<Day> result = new ArrayList<>();
        result.add(day);
        for (int i=0; i < cycle.length; i++) {
            LocalDate newDate = new LocalDate(day.getYear(), day.getMonth(), day.getDay()).plusDays(cycle[i]);
            result.add(new Day(newDate.getYear(), newDate.getMonthOfYear(), newDate.getDayOfMonth()));
        }
        return result;
    }

    public int getYear() {
        return (int)(id / 10000);
    }

    public int getMonth() {
        return (int)((id - getYear() * 10000) / 100);
    }

    public int getDay() {
        return (int)((id - getYear() * 10000) - getMonth() * 100);
    }

}
