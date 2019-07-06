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



    public int year;
    public int month;
    public int day;
    public boolean isDone;
    public int totalCycle;
    public int thisCycle;
    public String title;
    public String textPlan;
    public String group;
    public String type;



    public YMD getYMD(){
        if(this.ymd != null){
            return this.ymd;
        }
        return new YMD(this.year,this.month,this.day);
    }


    public Plan(int year,int month, int day, long parentUId ,int totalCycle, int thisCycle, String group,String textPlan) {
        this();
        this.parentUID=parentUId;
        this.year = year;
        this.month = month;
        this.day = day;
        this.textPlan = textPlan;
        this.ymd = new YMD(year,month,day);
        this.totalCycle = totalCycle;
        this.thisCycle = thisCycle;
        this.group =  group;
    }

    public Plan(){

        this.uid = System.nanoTime();
        this.parentUID = this.uid;
        this.group = "분류없음";
    }
    public Plan(String textPlan){
        this.textPlan  = textPlan;
    }

    public void setNewUID(){
    }

    public void setYMD(YMD ymd){
        this.ymd = ymd;
    }
    public boolean isParentPlan(){
        return getParentUID()==getUID();
    }
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

    public String getGroup(){
        return this.group;
    }

    public String getTitle(){
        return this.title;
    }

    public int getTotalCycle(){
        return this.totalCycle;
    }

    public int getThisCycle(){
        return this.thisCycle;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public long getUID() {
        return uid;
    }

    public void setUID(long uid) {
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

    public Plan setTotalCycle(int totalCycle){
        this.totalCycle = totalCycle;
        return this;
    }
    public Plan setThisCycle(int thisCycle){
        this.thisCycle = thisCycle;
        return this;
    }

    public Plan setGroup(String group){
        this.group  = group;
        return this;
    }
    public Plan setTitle(String title){
        this.title = title;
        return this;
    }

    public String getCycleState(){
        return "현재 현황 : "+this.getThisCycle()+ " / " + this.getTotalCycle();
    }

    public Plan makeChild(YMD date) {
        Plan child = new Plan();
        child.setParentUID(this.getUID());
        if(this.getYMD().equals(date)){
            child.setUID(this.getUID());
        }else{
            child.setUID(System.nanoTime());
        }
        child.setYear(date.getYear());
        child.setMonth(date.getMonth());
        child.setDay(date.getDay());
        child.setTextPlan(this.getTextPlan());
        child.setTitle(this.getTitle());
        child.setTotalCycle(this.totalCycle);
        child.setThisCycle(this.getThisCycle());
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