package com.example.databinding2.domain;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.databinding2.custom.YMD;
import com.example.databinding2.util.Constants;

import static com.example.databinding2.util.Constants.PLAN_TABLE;

@Entity(tableName = "table_plans")
public class Plan {

    @PrimaryKey
    public long uid;
    public long parentUID;

    public long getParentUID() {
        return parentUID;
    }

    public void setParentUID(long parentUID) {
        this.parentUID = parentUID;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public int year;
    public int month;
    public int day;
    public boolean isDone;
    public String textPlan;


    public Plan(int year,int month, int day, long parentUId ,String textPlan) {
        this();
        this.parentUID=parentUId;
        this.year = year;
        this.month = month;
        this.day = day;
        this.textPlan = textPlan;
    }
    public Plan(){
        this.uid = System.nanoTime();
    }
    public Plan(String textPlan){
        this.textPlan  = textPlan;
    }


    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public Plan setYear(int year){
        this.year = year;
        return this;
    }
    public Plan setMonth(int month){
        this.month= month;
        return this;
    }
    public Plan setDay(int day){
        this.day = day;
        return this;
    }
    public Plan setTextPlan(String textPlan) {
        this.textPlan = textPlan;
        return this;
}

    public String getTextPlan() {
        return textPlan;
    }

}
