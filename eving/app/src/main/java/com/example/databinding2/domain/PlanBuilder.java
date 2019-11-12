package com.example.databinding2.domain;

import com.example.databinding2.custom.YMD;

public class PlanBuilder {
    private long uid = 0;
    private long parentUID = 0;
    private YMD planDate = null;
    private int thisCycle = 0;
    private int totalCycle = 0;
    private boolean isDone = false;
    private YMD ymd = null;
    private YMD parentYMD = null;
    private String title = "";
    private String textPlan = "";
    private String group = "";
    private String type = "";
    private String planTypeName = "";

    private PlanBuilder() {}

    PlanBuilder(YMD ymd) {
        this.ymd = ymd;
        this.uid = System.nanoTime();
    }

    public Plan build() {
        if(this.parentUID == 0) {
            this.parentUID = this.uid;

        }
        return null;

    }

    public Plan buildDefaultParent() {
        this.uid = System.nanoTime();
        return new Plan();
    }

    /*
    public PlanBuilder uid(long uid) {
        this.uid = uid;
        return this;
    }


    public PlanBuilder year(int year) {
        this.year = year;
        return this;
    }

    public PlanBuilder month(int month) {
        this.month = month;
        return this;
    }

    public PlanBuilder day(int day) {
        this.day = day;
        return this;
    }

    */

    public PlanBuilder parentUID(long parentUID) {
        this.parentUID = parentUID;
        return this;
    }

    public PlanBuilder thisCycle(int thisCycle) {
        this.thisCycle = thisCycle;
        return this;
    }

    public PlanBuilder totalCycle(int totalCycle) {
        this.totalCycle = totalCycle;
        return this;
    }

    public PlanBuilder title(String title) {
        this.title = title;
        return this;
    }

    public PlanBuilder textPlan(String textPaln) {
        this.textPlan = textPaln;
        return this;
    }

    public PlanBuilder group(String group) {
        this.group = group;
        return this;
    }

    public PlanBuilder type(String type) {
        this.type = type;
        return this;
    }

}
