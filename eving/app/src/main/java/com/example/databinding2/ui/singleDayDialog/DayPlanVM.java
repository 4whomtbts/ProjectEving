package com.example.databinding2.ui.singleDayDialog;

import android.util.Log;

import com.example.databinding2.custom.YMD;
import com.example.databinding2.custom.types.DayPlanList;
import com.example.databinding2.domain.Plan;
import com.example.databinding2.repository.CalendarRepository;
import com.example.databinding2.repository.PlanRepository;
import com.example.databinding2.ui.viewmodel.CalendarViewModel;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class DayPlanVM extends CalendarViewModel {

    public Plan plan;
    DayPlanVM(Plan plan, int position){
        this.plan = plan;
    }

    public void setIsDone(boolean isChecked) {

        Plan parent = null;

        if(plan.isDone != isChecked) {
            plan.isDone = isChecked;
            new PlanRepository.UpdateOnePlanCheckState().execute(plan);
        }

        try {
            parent = new PlanRepository.GetOnePlanByUID().execute(plan.parentUID).get();
        }catch (Exception e) {

        }

        CalendarRepository.refreshCalendar();
    }

    void deleteCurrentPlan() {
        YMD planYMD = plan.getYMD();
        ArrayList<Plan> list;
        try {

            if(plan.isParentPlan()) {
                new PlanRepository.DeleteAllChildrenPlan().execute(plan).get();
            }else {
                new PlanRepository.DeletePlan().execute(plan).get();
            }

            list = new PlanRepository.GetPlanByDay()
                    .execute(planYMD)
                    .get();

            PlanRepository.getLiveCurrentDayPlanList().set(
                    new DayPlanList(list));
            new PlanRepository.ReOrderingPlanCycleByParentUID().execute(plan).get();

        }catch (Exception e) {

            System.out.println("Database error while delete plan");
        }

        CalendarRepository.refreshCalendar();

    }
    public boolean isParent() { return this.plan.isParentPlan();}

    public boolean isDone(){
        return this.plan.isDone();
    }

    void refreshModel() {
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
