package com.example.evingPlanner.ui.viewmodel;

import com.example.evingPlanner.TSLiveData;
import com.example.evingPlanner.custom.YMD;
import com.example.evingPlanner.custom.types.DayPlanList;
import com.example.evingPlanner.custom.types.MonthPlanList;
import com.example.evingPlanner.custom.types.YMDList;
import com.example.evingPlanner.domain.Plan;
import com.example.evingPlanner.domain.planTypes.PlanType;
import com.example.evingPlanner.repository.PlanRepository;
import com.example.evingPlanner.repository.RootRepository;
import com.example.evingPlanner.repository.SingleDayDialogRepository;
import com.example.evingPlanner.util.CalendarUtil;

public class PlanMakeViewModel extends CalendarViewModel {

    private PlanType currentSelectedPlanType;
    private boolean[] listChecked;
    public TSLiveData<YMDList> willBeClonedDateList;
    private SingleDayDialogRepository repository;

    public PlanMakeViewModel() {

        this.currentSelectedPlanType = null;
        this.listChecked = null;
        this.willBeClonedDateList = new TSLiveData<>();
        this.repository = RootRepository.getSingleDayDialogRepository();

    }


    public TSLiveData<YMDList> getWillBeClonedDateList(){
        return repository.getClonePreViewList();
    }

    public void setCurrentSelectedPlanType(PlanType ptype){
        this.currentSelectedPlanType = ptype;

        if( ptype.isStudyPlan() ) {
            this.repository.setClonePreViewList(ptype.getPlanDatesFromNowArray(getGlobalSelectedYMD()));
            this.repository.setClonePreViewList(new YMDList());
        }
    }

    public YMDList getWillBePlannedDateListValue(){
        return this.repository.getClonePreViewList().getValue();
    }

    public void checkAll(){
        YMDList newList = getWillBeClonedDateList().getValue();
        newList.checkAll();
        this.repository.setClonePreViewList(newList);
    }

    public void unCheckAll(){
        YMDList newList = getWillBeClonedDateList().getValue();
        newList.unCheckAll();
        newList.printCheckedList();
        this.repository.setClonePreViewList(newList);
    }

    public void deleteCheckPreViewElement(){
        YMDList tempList = getWillBeClonedDateList().getValue();
        tempList =tempList.deleteAllChecked();
        this.repository.setClonePreViewList(tempList);
    }

    public void setClonePreViewList(YMDList list){
        this.repository.setClonePreViewList(list);
    }


    public void makeNewPlan(YMDList confirmedPlannedDay , String title, String content){

        Plan plan = new Plan();
        plan.setTitle(title)
                .setTextPlan(content)
                .setYear(getGlobalCurrentCalendarYear())
                .setMonth(getGlobalCurrentCalendarMonth())
                .setDay(getGlobalCurrentCalendarDay())
                .setTotalCycle(0)
                .setThisCycle(0);


        //TODO 유틸 만들기
        DayPlanList org = PlanRepository.getCurrentDayPlanList();
        org.add(plan);

        PlanRepository.setLiveCurrentDayPlanList(org);
        registerPlanByYMD(getGlobalSelectedYMD(),confirmedPlannedDay,plan);
    }

    protected DayPlanList getConfirmedPlanList(YMD parentPlannedDay, YMDList shouldPlannedDay, Plan newPlan){
        return null;
    }


    private void registerPlanByYMD(YMD parentPlannedDay, YMDList shouldPlannedDay, Plan newPlan){
        int index = 0;
        newPlan.setYMD(parentPlannedDay);
        MonthPlanList refreshedPlanList = PlanRepository.getCurrentMonthPlanList();
        newPlan.setTotalCycle(shouldPlannedDay.size());
        for(index=0; index < shouldPlannedDay.size();index++){

            YMD date = shouldPlannedDay.get(index);
            Plan copied = newPlan.makeChild(date);
            copied.setThisCycle(index+1);

            if(CalendarUtil.isInRangeOfMonthInCalendar(
                    getGlobalCurrentCalendarYear(),
                    getGlobalCurrentCalendarMonth(),
                    date.getYear(),
                    date.getMonth(),
                    date.getDay())) {

                int indexOnCalendar = CalendarUtil.convertDateToIndex(date.getYear(), getGlobalCurrentCalendarMonth(),
                        date.getYear(),
                        date.getMonth(), date.getDay());

                DayPlanList currSingleDayPlanList = refreshedPlanList.get(indexOnCalendar).getValue();
                currSingleDayPlanList.add(copied);
                refreshedPlanList.get(indexOnCalendar).set(currSingleDayPlanList);
            }

            new PlanRepository.InsertPlan().execute(copied);

        }

        PlanRepository.setCurrentMonthPlanList(refreshedPlanList);

    }



}
