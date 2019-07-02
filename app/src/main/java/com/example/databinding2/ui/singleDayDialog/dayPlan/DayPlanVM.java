package com.example.databinding2.ui.singleDayDialog.dayPlan;

import androidx.lifecycle.LiveData;

import com.example.databinding2.domain.Plan;
import com.example.databinding2.ui.viewmodel.CalendarViewModel;

public class DayPlanVM extends CalendarViewModel {
    /* TODO

     */
    Plan plan;
    private int position;
    public DayPlanVM(Plan plan,int position){
        this.plan = plan;
        this.position = position;
    }

    public String getGroup(){
        return this.plan.getGroup();
    }
    public String getTitle(){
        return this.plan.getTitle();
    }
    public String getTextPlan(){
        return this.plan.getTextPlan();
    }

    public String getCycleInfo(){
        return getThisCycle()+'/'+getTotalCycle();
    }
    private String getTotalCycle(){
        return Integer.toString(this.plan.getTotalCycle());
    }

    private String getThisCycle(){
        return Integer.toString(this.plan.getThisCycle());
    }







}
