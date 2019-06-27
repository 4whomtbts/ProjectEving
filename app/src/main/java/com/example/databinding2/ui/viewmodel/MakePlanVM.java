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
        YMD[] shouldPlannedDay = PlanCreator.getDenseMode(getGlobalSelectedYMD());
     //
           registerPlanByYMD(getGlobalSelectedYMD(),shouldPlannedDay,plan);


    }


    private void registerPlanByYMD(YMD parentPlannedDay, YMD[] shouldPlannedDay,Plan newPlan){
        boolean isBeyondCurrentCalendar = false;
        int index = 0;




        ArrayList<TSLiveData<ArrayList<Plan>>> refreshedPlanList = CalendarRepository.getLiveCurrentMonthPlanList().getValue();

        for(index=0; index < shouldPlannedDay.length;index++){

            YMD date = shouldPlannedDay[index];
            Plan copied = newPlan.makeChild(date);

            if(CalendarUtil.isInRangeOfMonthInCalendar(date.getYear(),
                getGlobalCurrentCalendarMonth(),date.getMonth(),date.getDay())) {

                int indexOnCalendar = CalendarUtil.convertDateToIndex(date.getYear(), getGlobalCurrentCalendarMonth(),
                        date.getMonth(), date.getDay());

                ArrayList<Plan> currSingleDayPlanList = refreshedPlanList.get(indexOnCalendar).getValue();
                currSingleDayPlanList.add(copied);
                refreshedPlanList.get(indexOnCalendar).set(currSingleDayPlanList);


            }

            new PlanRepository.InsertPlan().execute(copied);

            ArrayList<Plan> result = null;
            try {
                result = new PlanRepository.GetPlanByDay().execute(date).get();
                Log.e("테스트",result.get(0).month+","+result.get(0).day);
            } catch (Exception e) {

            }



        }
    //    CalendarRepository.setLiveCurrentMonthPlanList(refreshedPlanList);

    }



}


