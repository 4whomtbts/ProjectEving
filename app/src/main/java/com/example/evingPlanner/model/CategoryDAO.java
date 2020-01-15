package com.example.evingPlanner.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.evingPlanner.domain.Category;

import java.util.List;

@Dao
public interface CategoryDAO {

    @Insert
    void insert(Category... categories);

    @Delete
    void delete(Category... categories);

    @Query("SELECT * FROM table_category")
    List<Category> selectAll();

    @Query("SELECT * FROM table_category WHERE uid = :uid")
    Category selectByUID(long uid);

    @Query("UPDATE table_category SET categoryName = :categoryName, parentUID = :parentUID WHERE uid = :uid")
    void updateByUID(long uid, long parentUID, String categoryName);
}
