package com.example.evingPlanner.domain;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "table_category")
public class Category {

    @PrimaryKey
    public long uid;
    public long parentUID;
    public String categoryName;

    public Category() {
        this.uid = System.currentTimeMillis();
        this.parentUID = 0;
        this.categoryName = "";
    }
    
    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public long getParentUID() {
        return parentUID;
    }

    public void setParentUID(long parentUID) {
        this.parentUID = parentUID;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }



}
