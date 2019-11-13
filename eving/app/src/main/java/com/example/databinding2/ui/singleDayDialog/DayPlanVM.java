package com.example.databinding2.ui.singleDayDialog;

import android.util.Log;

import com.example.databinding2.domain.Plan;
import com.example.databinding2.repository.CalendarRepository;
import com.example.databinding2.repository.PlanRepository;
import com.example.databinding2.ui.viewmodel.CalendarViewModel;

import java.util.concurrent.ExecutionException;

public class DayPlanVM extends CalendarViewModel {

    public Plan plan;
    DayPlanVM(Plan plan, int position){
        this.plan = plan;
    }

    public void setIsDone(boolean isChecked) {
        /*
        try {
            boolean hasChecked = new PlanRepository.GetOnePlanByUID().execute(plan.uid).get();
        }catch (Exception e) {

        }
         */
        Plan parent = null;

        if(plan.isDone != isChecked) {
            plan.isDone = isChecked;
            new PlanRepository.UpdateOnePlanCheckState().execute(plan);
        }

        try {
            parent = new PlanRepository.GetOnePlanByUID().execute(plan.parentUID).get();
        }catch (Exception e) {

        }

        if(parent != null) {
            System.out.println("프로그레스 : "+ parent.totalCycle);
            System.out.println("프로그레스 : "+ parent.numberOfDoneChild);

        }
        CalendarRepository.refreshCalendar();
    }
    public boolean isParent() { return this.plan.isParentPlan();}

    public boolean isDone(){
        return this.plan.isDone();
    }

    public void refreshModel() {
        Plan refreshPlan = null;
        try {
             refreshPlan = new PlanRepository.GetOnePlanByUID().execute(this.plan.getUID()).get();
        }catch (Exception e){
            throw new RuntimeException("failed to get data from database");
        }
        this.plan = refreshPlan;
    }
    public Plan getPlan(){ return this.plan; }
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
