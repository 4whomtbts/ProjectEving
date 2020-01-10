package com.example.evingPlanner.ui.planDialogs.planEditDialog.clonePlan;

import com.example.evingPlanner.domain.Plan;
import com.example.evingPlanner.repository.CalendarRepository;
import com.example.evingPlanner.repository.PlanRepository;
import com.example.evingPlanner.ui.viewmodel.PlanMakeViewModel;

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

        boolean originalIsDone = currentPlan.isDone;
        this.currentPlan = currentPlan.setTitle(this.title)
                   .setTextPlan(this.textPlan)
                   .setIsDone(this.isDone);

        if(originalIsDone != this.isDone) {
            new PlanRepository.UpdateOnePlanCheckState().execute(this.currentPlan);
        }
        new PlanRepository.UpdateOnePlanByUID().execute(this.currentPlan);
        CalendarRepository.refreshCalendar();
    }

    public String getTitle(){
        return this.currentPlan.getTitle();
    }
    public boolean isDone(){
        return this.currentPlan.isDone;
    }
}
