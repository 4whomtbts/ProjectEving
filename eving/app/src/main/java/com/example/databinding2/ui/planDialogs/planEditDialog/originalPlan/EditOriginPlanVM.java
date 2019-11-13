package com.example.databinding2.ui.planDialogs.planEditDialog.originalPlan;

import android.util.Pair;

import com.example.databinding2.custom.YMD;
import com.example.databinding2.custom.types.DayPlanList;
import com.example.databinding2.custom.types.YMDList;
import com.example.databinding2.domain.Plan;
import com.example.databinding2.repository.CalendarRepository;
import com.example.databinding2.repository.PlanRepository;
import com.example.databinding2.repository.RootRepository;
import com.example.databinding2.ui.viewmodel.PlanMakeViewModel;
import com.example.databinding2.util.CalendarUtil;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

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
        return progress+"%";
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

