package com.example.databinding2.domain;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Insert;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.example.databinding2.custom.YMD;
import com.example.databinding2.domain.planTypes.PlanType;
import com.example.databinding2.model.PlanTypeConverters;

@TypeConverters({PlanTypeConverters.class})
@Entity(tableName = "table_plans")
public class Plan {

    private static final int DEFAULT_TOTAL_CYCLE = 10;
    private static final int DEFAULT_CURRENT_CYCLE = 0;

    @PrimaryKey
    public long uid;
    public long parentUID;
    public int year;
    public int month;
    public int day;
    public int thisCycle;
    public int totalCycle;
    @ColumnInfo(name ="isDone")
    public boolean isDone;
    public YMD ymd;
    public YMD parentYMD;
    @ColumnInfo(name ="title")
    public String title;
    @ColumnInfo(name ="textPlan")
    public String textPlan;
    @ColumnInfo(name="group")
    public String group;
    public String type;
    public String planTypeName = "반복계획";
    @ColumnInfo(name="progress")
    public double progress;
    @ColumnInfo(name="number_of_done_child")
    public int numberOfDoneChild;

    //@ColumnInfo(name="parentIndependence")
    //public boolean parentIndepence = false;


    public static PlanBuilder builder(YMD ymd) {
        return new PlanBuilder(ymd);
    }

    public YMD getYMD(){
        if(this.ymd != null){
            return this.ymd;
        }
        return new YMD(this.year,this.month,this.day);
    }

    public Plan(long parentUID, long uid, YMD ymd) {
        this(parentUID, uid, ymd, DEFAULT_TOTAL_CYCLE, DEFAULT_CURRENT_CYCLE);
        this.parentUID = parentUID;
        this.uid = uid;
        this.ymd = ymd;
    }

    public Plan(long parentUID, long uid, YMD ymd, int totalCycle, int currentCycle) {
        this.parentUID = parentUID;
        this.uid = uid;
        this.ymd = ymd;
        this.totalCycle = totalCycle;
        this.thisCycle = currentCycle;
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
        this.isDone=false;
    }

    public Plan(){
       // this.parentIndepence = false;
        this.uid = System.nanoTime();
        this.parentUID = this.uid;
        this.group = "분류없음";
        this.isDone=false;
        this.numberOfDoneChild = 0;
        this.progress = 0.0;
    }

    public Plan(String textPlan){
        this.textPlan  = textPlan;
    }

     public Plan setYMD(YMD ymd){
        this.ymd = ymd;
        return this;
    }
    public void setPlanType(String planTypeName){
        this.planTypeName = planTypeName;
    }
    public String getPlanType(){
        return this.planTypeName;
    }

    public void setParentYMD(YMD ymd){
        this.parentYMD = ymd;
    }
    public YMD getParentYMD(){
        return this.parentYMD;
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

    public Plan setIsDone(boolean isDone){
        this.isDone = isDone;
        return this;
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

    public Plan setUID(long uid) {
        this.uid = uid;
        return this;
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
            child.setIsDone(this.isDone());
        }else{
            child.setUID(System.nanoTime());
        }
        child.setParentYMD(this.getYMD());
        child.setYMD(date);
        child.setYear(date.getYear());
        child.setMonth(date.getMonth());
        child.setDay(date.getDay());
        child.setTextPlan(this.getTextPlan());
        child.setTitle(this.getTitle());
        child.setTotalCycle(this.totalCycle);
        child.setThisCycle(this.getThisCycle());
        child.setGroup(this.getGroup());
        return child;
    }

    public Plan makeNewWithDifferentUserInputContent(String title, String textPlan,boolean isDone){
        Plan newPlan = Plan.deepCopy(this);
        return newPlan.setTitle(title).setTextPlan(textPlan).setIsDone(isDone);
    }


    public String getTextPlan() {
        return textPlan;
    }

    @Override
    public String toString(){
        return this.getYear()+"년"+this.getMonth()+"월"+this.getDay()+"일"+
                "  / "+this.getTextPlan()+"\n";

    }

    public static Plan deepCopy(Plan target){
        Plan newPlan  = new Plan();
        newPlan.setUID(target.getUID());
        newPlan.setParentUID(target.getParentUID());
        newPlan.setYear(target.getYear());
        newPlan.setMonth(target.getMonth());
        newPlan.setDay(target.getDay());
        newPlan.setThisCycle(target.getThisCycle());
        newPlan.setTotalCycle(target.getTotalCycle());
        newPlan.setIsDone(target.isDone());
        newPlan.setYMD(YMD.deepCopy(target.getYMD()));
        newPlan.setParentYMD(YMD.deepCopy(target.getParentYMD()));
        newPlan.setTitle(target.getTitle());
        newPlan.setTextPlan(target.getTextPlan());
        newPlan.setGroup(target.getGroup());
        newPlan.setPlanType(target.getPlanType());
        newPlan.setIsDone(target.isDone());
        return newPlan;
    }


    public boolean isSimilar(Plan candidate){

        return (this.getTitle().equals(candidate.getTitle()))
                && (this.getTextPlan().equals(candidate.getTextPlan()));
    }

    public boolean isSame(Plan candidate){

        return isSimilar(candidate) && (candidate.isDone() == this.isDone());
    }

}