package com.example.databinding2.domain;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.databinding2.custom.YMD;
import com.example.databinding2.databinding.CalendarListBinding;

@Entity(tableName = "table_days")
public class DayClass {

    @PrimaryKey
    public long rootId;
    /*
    @Embedded
    public Plan plan;
    */
    public String category;
    public float progress_percentage;
    public int year;
    public int month;
    public int day;
    public int order;
    public int childCounter;
    public boolean isDone;
    public boolean isRoot;


    public DayClass() {
    }

    public DayClass(int day) {
        this.day = day;
    }
    public DayClass(YMD ymd){
        this(ymd.getYear(),ymd.getMonth(),ymd.getDay());
    }
    public DayClass(int year, int month, int day){
        this.year = year;
        this.month=  month;
        this.day = day;
        this.setRootId(System.currentTimeMillis());
    }
    public DayClass(int year, int month, int day,Plan plan){
        this.year = year;
        this.month=  month;
        this.day = day;
        //this.plan = plan;
        this.setRootId(System.currentTimeMillis());
    }
    /*

    public DayClass setPlan(Plan plan) {
        this.plan = plan;
        return this;
    }

    public Plan getPlan() {
        return plan;
    }
    */

    public DayClass setRootId(long rootId) {
        this.rootId = rootId;
        return this;
    }

    public String getCategory() {
        return category;
    }

    public DayClass setCategory(String category) {
        this.category = category;
        return this;
    }

    public float getProgress_percentage() {
        return progress_percentage;
    }

    public DayClass setProgress_percentage(float progress_percentage) {
        this.progress_percentage = progress_percentage;
        return this;
    }

    public int getYear() {
        return year;
    }

    public DayClass setYear(int year) {
        this.year = year;
        return this;
    }

    public int getMonth() {
        return month;
    }

    public DayClass setMonth(int month) {
        this.month = month;
        return this;
    }

    public int getOrder() {
        return order;
    }

    public DayClass setOrder(int order) {
        this.order = order;
        return this;
    }

    public int getChildCounter() {
        return childCounter;
    }

    public DayClass setChildCounter(int childCounter) {
        this.childCounter = childCounter;
        return this;
    }

    public boolean isDone() {
        return isDone;
    }

    public DayClass setDone(boolean done) {
        isDone = done;
        return this;
    }

    public boolean isRoot() {
        return isRoot;
    }

    public DayClass setRoot(boolean root) {
        isRoot = root;
        return this;
    }

    public long getRootId() {
        return rootId;
    }


    public DayClass setDay(int day){
        this.day = day;
        return this;
    }
    public String getDay(){
        return Integer.toString(this.day);
    }

}
