package com.example.databinding2.domain;


import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.databinding2.custom.YMD;

@Entity(tableName = "table_plans")
public class Plan {

    @Ignore
    private YMD ymd;

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


    public YMD getYMD(){
        if(this.ymd != null){
            return this.ymd;
        }
        return new YMD(this.year,this.month,this.day);
    }

    public Plan(int year,int month, int day, long parentUId ,String textPlan) {
        this();
        this.parentUID=parentUId;
        this.year = year;
        this.month = month;
        this.day = day;
        this.textPlan = textPlan;
        this.ymd = new YMD(year,month,day);
    }
    public Plan(){
        this.uid = System.nanoTime();
    }
    public Plan(String textPlan){
        this.textPlan  = textPlan;
    }

    public void setNewUID(){
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

    public Plan makeChild(YMD date){
        Plan child = new Plan();
        child.setParentUID(this.getUid());
        child.setYear(date.getYear());
        child.setMonth(date.getMonth());
        child.setDay(date.getDay());
        child.setTextPlan(this.textPlan);
        return child;
    }


    public String getTextPlan() {
        return textPlan;
    }

    @Override
    public String toString(){
        return this.getYear()+"년"+this.getMonth()+"월"+this.getDay()+"일"+
                "  / "+this.getTextPlan()+"\n";

    }

}