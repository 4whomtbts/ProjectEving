package com.example.evingPlanner.model;
import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.evingPlanner.domain.Category;

@Database(entities = {Category.class}, version = 1)
public abstract class CategoryDatabase extends RoomDatabase {
    public abstract CategoryDAO getCategoryDAO();
}
