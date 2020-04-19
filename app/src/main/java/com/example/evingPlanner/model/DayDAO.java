package com.example.evingPlanner.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.evingPlanner.domain.Category;
import com.example.evingPlanner.domain.Day;

import java.util.List;

@Dao
public abstract class DayDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public abstract long[] insert(Day... days);

    @Update(onConflict = OnConflictStrategy.IGNORE)
    public abstract void update(Day... days);

    @Delete
    public abstract void delete(Day... days);

    @Transaction
    public void upsert(Day... days) {
        long[] ids = insert(days);
        for (int i=0; i < ids.length; i++) {
            if (ids[i] == -1) {
                update(days[i]);
            }
        }
    }

    @Query("SELECT * FROM table_day")
    public abstract List<Day> selectAll();

    @Query("SELECT * FROM table_day WHERE table_day_id = :id ")
    public abstract Day selectDayFromId(long id);
}
