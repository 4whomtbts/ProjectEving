package com.example.databinding2.ui.viewmodel;

import android.util.Log;

import com.example.databinding2.TSLiveData;
import com.example.databinding2.custom.YMD;
import com.example.databinding2.domain.Plan;
import com.example.databinding2.domain.PlanCreator;
import com.example.databinding2.repository.CalendarRepository;
import com.example.databinding2.repository.PlanRepository;
import com.example.databinding2.util.CalendarUtil;

import java.util.ArrayList;

import static com.example.databinding2.util.Constants.TIMEZONE_SEOUL;

public class MakePlanVM extends CalendarViewModel {

    public MakePlanVM() {

    }

    public void makeNewPlan(String textPlan){

        Plan plan = new Plan();
        plan.setTextPlan(textPlan)
                .setYear(getGlobalCurrentCalendarYear())
                .setMonth(getGlobalCurrentCalendarMonth())
                .setDay(getGlobalCurrentCalendarDay());

        TSLiveData<Plan> livePlan = new TSLiveData<>();
        livePlan.setValue(plan);

        new PlanRepository.InsertPlan().execute(plan);
        //TODO 유틸 만들기
        ArrayList<TSLiveData<Plan>> org = CalendarRepository.getLiveCurrentDayPlanList().getValue();
        org.add(livePlan);
        CalendarRepository.getLiveCurrentDayPlanList().setValue(org);

        for(int i=0; i < CalendarRepository.getLiveCurrentDayPlanList().getValue().size();i++){
            Log.e(""+i,CalendarRepository.getLiveCurrentDayPlanList().getValue().get(i).toString());
        }
        YMD[] shouldPlannedDay = PlanCreator.getTypicalMode(getGlobalCurrentYMD(),TIMEZONE_SEOUL);
     //
           registerPlanByYMD(getGlobalCurrentYMD(),shouldPlannedDay,plan);


    }

    private void registerPlanByYMD(YMD parentPlannedDay, YMD[] shouldPlannedDay,Plan newPlan){
        boolean isBeyondCurrentCalendar = false;
        int index = 0;


        ArrayList<TSLiveData<ArrayList<Plan>>> refreshedPlanList = CalendarRepository.getLiveCurrentMonthPlanList().getValue();

        for(index=0; index < shouldPlannedDay.length;index++){
            YMD date = shouldPlannedDay[index];
            if(CalendarUtil.isInRangeOfMonthInCalendar(date.getYear(),
                getGlobalCurrentCalendarMonth(),date.getMonth(),date.getDay())){

            int indexOnCalendar = CalendarUtil.convertDateToIndex(date.getYear(),getGlobalCurrentCalendarMonth(),
                    date.getMonth(),date.getDay());

                ArrayList<Plan> currSingleDayPlanList = refreshedPlanList.get(indexOnCalendar).getValue();
                currSingleDayPlanList.add(newPlan);
                refreshedPlanList.get(indexOnCalendar).set(currSingleDayPlanList);
                Log.e("갱신인덱스"+index,date.getMonth()+","+date.getDay());

            }else{
                isBeyondCurrentCalendar=true;
            }

        }
       // CalendarRepository.setLiveCurrentMonthPlanList(refreshedPlanList);

    }



}


