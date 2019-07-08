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
    private YMD today;
    private YMDList originClonedPlanList;

    private String title;
    private String textPlan;
    private boolean isDone;


    public EditOriginPlanVM() {
    }




    public int getListIndexDayAt(){
        int index = CalendarUtil.convertDateToIndex(getGlobalCurrentCalendarYear()
                ,getGlobalCurrentCalendarMonth(),getGlobalSelectedMonth(),getGlobalSelectedDay());
        return index;
    }

    public boolean isDataChanged(String title, String textPlan, boolean isDone){

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
    //public void makePlan(String title, String textPlan, String group){
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

    public void initPreViewListWithDB(){

        ArrayList<Plan> list = getRegisteredPlan(thisPlan.getParentUID());
        YMDList ymdList = new YMDList();
        for(Plan plan : list){
            ymdList.add(plan.getYMD());
        }
        super.setClonePreViewList(ymdList);
        this.originClonedPlanList = ymdList;
    }

    public void editPlan(){

        new PlanRepository.DeletePlanByOneParentUID().execute(this.thisPlan.getUID());

        YMDList ymdList = getWillBePlannedDateListValue();

        Plan plan = new Plan().setTitle(this.title).setTextPlan(this.textPlan).setYMD(getGlobalSelectedYMD())
                              .setTotalCycle(ymdList.size()).setThisCycle(0).setIsDone(this.isDone);

        ArrayList<Plan> newClonedPlanList = new ArrayList<>();

        for(int i=0; i < ymdList.size(); i++){
            Plan clonedPlan = plan.makeChild(ymdList.get(i)).setThisCycle(i+1);
            newClonedPlanList.add(clonedPlan);
        }



        ArrayList<Plan> refreshedCurrentDayPlanList=new ArrayList<>();
        try {
            new PlanRepository.InsertArrayListPlan().execute(newClonedPlanList).get();
            refreshedCurrentDayPlanList = new PlanRepository.GetPlanByDay().execute(thisPlan.getYMD()).get();
        }catch (Exception e){

        }

        DayPlanList newDayPlanList = new DayPlanList(refreshedCurrentDayPlanList);
        PlanRepository.getCurrentMonthPlanListAt(getListIndexDayAt()).set(newDayPlanList);
        CalendarRepository.refreshCalendar();
    }

    public void setThisPlan(Plan thisPlan){
        this.thisPlan = thisPlan;
    }


    public String getCycleState(){
        return this.thisPlan.getCycleState();
    }

    public String getTitle(){
        return this.thisPlan.getTitle();
    }
    public String getTextPlan(){
        return this.thisPlan.getTextPlan();
    }

    public boolean isDone(){
        return this.thisPlan.isDone;
    }
}

