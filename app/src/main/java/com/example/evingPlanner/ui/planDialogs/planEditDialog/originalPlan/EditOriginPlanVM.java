package com.example.evingPlanner.ui.planDialogs.planEditDialog.originalPlan;

import com.example.evingPlanner.custom.types.YMDList;
import com.example.evingPlanner.domain.Plan;
import com.example.evingPlanner.repository.CalendarRepository;
import com.example.evingPlanner.repository.PlanRepository;
import com.example.evingPlanner.ui.viewmodel.PlanMakeViewModel;

import java.util.ArrayList;

public class EditOriginPlanVM extends PlanMakeViewModel {

    private Plan thisPlan;
    private YMDList originClonedPlanList;

    public String title;
    public String textPlan;
    public boolean isDone;


    public EditOriginPlanVM() {
    }



// TODO 임시방편
    public int getListIndexDayAt(){

        return -1;
    }

    boolean isDataChanged(String title, String textPlan, boolean isDone){

        Plan newPlan = makePlan(title,textPlan,isDone);

        boolean isClonedPlanListChanged = !originClonedPlanList.isSimilar(super.getWillBePlannedDateListValue());
        boolean isPlanExactlyEqual = thisPlan.isSame(newPlan);
        boolean isPlanSimilar = thisPlan.isSimilar(newPlan);


        if(!thisPlan.isSimilar(newPlan) ||
                !originClonedPlanList.isSimilar(super.getWillBePlannedDateListValue())
            || !(thisPlan.isDone()==isDone)
        ) {

            this.title = title;
            this.textPlan = textPlan;
            this.isDone = isDone;
            return true;
        }else{
            return false;
        }

    }

    private Plan makePlan(String title, String textPlan, boolean isDone){
        return new Plan().setTitle(title).setTextPlan(textPlan).setIsDone(isDone);
    }



    private ArrayList<Plan> getRegisteredPlan(Long parentUID){
        ArrayList<Plan> list = new ArrayList<>();
        try {
            list =  new PlanRepository.GetAllPlanByPlanUID().execute(parentUID).get();
        }catch (Exception e){

        }
        return list;
    }

    void initPreViewListWithDB(){

        ArrayList<Plan> list = getRegisteredPlan(thisPlan.getParentUID());
        YMDList ymdList = new YMDList();
        for(Plan plan : list){
            ymdList.add(plan.getYMD());
        }
        super.setClonePreViewList(ymdList);
        this.originClonedPlanList = ymdList;
    }

    void editPlan(){

        YMDList ymdList = getWillBePlannedDateListValue();
        Plan editedPlan = thisPlan.setTitle(this.title)
                                  .setTextPlan(this.textPlan)
                                  .setIsDone(this.isDone);
        new PlanRepository.UpdateParentAndAllChildrenOfPlan().execute(editedPlan);
        CalendarRepository.refreshCalendar();

    }

    String getProgress() {
        Plan parent = null;


        try {
            parent = new PlanRepository.GetOnePlanByUID().execute(thisPlan.uid).get();
        }catch (Exception e) {
            System.out.println("Error!");
        }
        double progress = ((double)parent.numberOfDoneChild/(double)parent.totalCycle)*100;
        double progress2 = Double.parseDouble(String.format("%.2f",progress));
        return progress2+"%";
    }

    void setThisPlan(Plan thisPlan){
        this.thisPlan = thisPlan;
    }


    String getCycleState(){
        return this.thisPlan.getCycleState();
    }

    public String getTitle(){
        return this.thisPlan.getTitle();
    }
    String getTextPlan(){
        return this.thisPlan.getTextPlan();
    }

    public boolean isDone(){
        return this.thisPlan.isDone;
    }
}

