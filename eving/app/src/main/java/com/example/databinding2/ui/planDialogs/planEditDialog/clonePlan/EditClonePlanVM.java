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
    public boolean isDone;

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

    String getProgress() {

        Plan parent = null;
        double progress = 0.0;

        try {
            parent = new PlanRepository.GetOnePlanByUID().execute(currentPlan.parentUID).get();
        }catch (Exception e) {
            System.out.println("Error!");
        }

        progress = ((double)parent.numberOfDoneChild/(double)parent.totalCycle)*100;
        return progress+"%";
    }

    void editPlan(){

        currentPlan.setTitle(this.title)
                   .setTextPlan(this.textPlan)
                   .setIsDone(this.isDone);

        new PlanRepository.UpdateOnePlanByUID().execute(currentPlan);
        CalendarRepository.refreshCalendar();
    }

    public String getTitle(){
        return this.currentPlan.getTitle();
    }
    public boolean isDone(){
        return this.currentPlan.isDone;
    }
}
