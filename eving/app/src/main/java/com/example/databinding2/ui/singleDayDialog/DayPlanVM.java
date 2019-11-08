package com.example.databinding2.ui.singleDayDialog;

import com.example.databinding2.domain.Plan;
import com.example.databinding2.repository.PlanRepository;
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

    public boolean isParent() { return this.plan.isParentPlan();}

    public boolean isDone(){
        return this.plan.isDone();
    }

    public void refreshModel() {
        Plan refreshPlan = null;
        try {
             refreshPlan = new PlanRepository.GetOnePlanByUID().execute(this.plan.getUID()).get();
        }catch (Exception e){}
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
