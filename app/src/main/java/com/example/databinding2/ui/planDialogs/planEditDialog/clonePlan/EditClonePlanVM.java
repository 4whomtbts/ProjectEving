package com.example.databinding2.ui.planDialogs.planEditDialog.clonePlan;

import com.example.databinding2.custom.YMD;
import com.example.databinding2.custom.types.DayPlanList;
import com.example.databinding2.custom.types.YMDList;
import com.example.databinding2.domain.Plan;
import com.example.databinding2.repository.CalendarRepository;
import com.example.databinding2.repository.PlanRepository;
import com.example.databinding2.ui.viewmodel.PlanMakeViewModel;
import com.example.databinding2.util.CalendarUtil;

import java.util.ArrayList;

public class EditClonePlanVM extends PlanMakeViewModel {

    private Plan currentPlan;
    private String title;
    private String textPlan;

    public EditClonePlanVM() {   }



    public int getListIndexDayAt(){
        int index = CalendarUtil.convertDateToIndex(getGlobalCurrentCalendarYear()
                ,getGlobalCurrentCalendarMonth(),getGlobalSelectedMonth(),getGlobalSelectedDay());
        return index;
    }

    public void setCurrentPlan(Plan plan){
        this.currentPlan = plan;
    }

    public boolean isDataChanged(String title, String textPlan){

        Plan newPlan = makePlan(title,textPlan);
        if(!currentPlan.isSimilar(newPlan)) {
            this.title = title;
            this.textPlan = textPlan;
            return true;
        }
        return false;
    }

    //public void makePlan(String title, String textPlan, String group){
    private Plan makePlan(String title, String textPlan){
        return new Plan().setTitle(title).setTextPlan(textPlan);
    }



    private ArrayList<Plan> getRegisteredPlan(Long parentUID){
        ArrayList<Plan> list = new ArrayList<>();
        try {
            list =  new PlanRepository.GetAllPlanByPlanUID().execute(parentUID).get();
        }catch (Exception e){

        }
        return list;
    }

    public YMD getParentPlanYMD() {
        return currentPlan.getParentYMD();
    }

    public void editPlan(){

        new PlanRepository.DeletePlanByOneParentUID().execute(this.currentPlan.getUID());

        YMDList ymdList = getWillBePlannedDateListValue();

        Plan plan = new Plan().setTitle(this.title).setTextPlan(this.textPlan).setYMD(getGlobalSelectedYMD())
                .setTotalCycle(ymdList.size()).setThisCycle(0);

        ArrayList<Plan> newClonedPlanList = new ArrayList<>();
        for(int i=0; i < ymdList.size(); i++){
            Plan clonedPlan = plan.makeChild(ymdList.get(i)).setThisCycle(i+1);
            newClonedPlanList.add(clonedPlan);
        }



        ArrayList<Plan> refreshedCurrentDayPlanList=new ArrayList<>();
        try {
            new PlanRepository.InsertArrayListPlan().execute(newClonedPlanList).get();
            refreshedCurrentDayPlanList = new PlanRepository.GetPlanByDay().execute(currentPlan.getYMD()).get();
        }catch (Exception e){

        }

        DayPlanList newDayPlanList = new DayPlanList(refreshedCurrentDayPlanList);
        PlanRepository.getCurrentMonthPlanListAt(getListIndexDayAt()).set(newDayPlanList);
        CalendarRepository.refreshCalendar();
    }


    public String getCycleState(){
        return this.currentPlan.getCycleState();
    }

    public String getTitle(){
        return this.currentPlan.getTitle();
    }
    public String getTextPlan(){
        return this.currentPlan.getTextPlan();
    }

}
