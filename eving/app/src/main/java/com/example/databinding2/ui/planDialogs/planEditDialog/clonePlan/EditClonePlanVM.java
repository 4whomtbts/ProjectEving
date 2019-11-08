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
    private boolean isDone;

    public EditClonePlanVM() {   }



    public void setCurrentPlan(Plan plan){
        this.currentPlan = plan;
    }

    private Plan makePlan(String title, String textPlan, boolean isDone){
        return new Plan().setTitle(title).setTextPlan(textPlan).setIsDone(isDone);
    }

    public boolean isDataChanged(String title, String textPlan, boolean isDone){

        Plan newPlan = makePlan(title,textPlan,isDone);

        boolean isPlanExactlyEqual = this.currentPlan.isSame(newPlan);
        boolean isPlanSimilar = this.currentPlan.isSimilar(newPlan);


        if(!isPlanSimilar || !(this.currentPlan.isDone()==isDone)) {

            this.title = title;
            this.textPlan = textPlan;
            this.isDone = isDone;
            return true;
        }else{
            return false;
        }

    }

    public void editPlan(){

        Plan newPlan = currentPlan.makeNewWithDifferentUserInputContent(this.title,this.textPlan,this.isDone);

        try {
            new PlanRepository.UpdateOnePlanUserInputDatas().execute(newPlan).get();
        }catch (Exception e){
            e.printStackTrace();
        }

        ArrayList<Plan> refreshedCurrentDayPlanList=new ArrayList<>();
        try {
            refreshedCurrentDayPlanList = new PlanRepository.GetPlanByDay().execute(currentPlan.getYMD()).get();
        }catch (Exception e1){
            e1.printStackTrace();
        }

        DayPlanList newDayPlanList = new DayPlanList(refreshedCurrentDayPlanList);
        PlanRepository.getCurrentMonthPlanListAt(getListIndexDayAt()).setValue(newDayPlanList);
        CalendarRepository.refreshCalendar();

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


    public String getCycleState(){
        return this.currentPlan.getCycleState();
    }

    public String getTitle(){
        return this.currentPlan.getTitle();
    }
    public String getTextPlan(){
        return this.currentPlan.getTextPlan();
    }

    public boolean isDone(){
        return this.currentPlan.isDone;
    }
}
