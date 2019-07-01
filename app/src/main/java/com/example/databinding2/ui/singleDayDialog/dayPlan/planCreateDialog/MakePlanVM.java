package com.example.databinding2.ui.singleDayDialog.dayPlan.planCreateDialog;

import android.util.Log;

import com.example.databinding2.custom.YMD;
import com.example.databinding2.custom.types.DayPlanList;
import com.example.databinding2.custom.types.MonthPlanList;
import com.example.databinding2.domain.Plan;
import com.example.databinding2.domain.PlanCreator;
import com.example.databinding2.repository.CalendarRepository;
import com.example.databinding2.repository.PlanRepository;
import com.example.databinding2.ui.viewmodel.CalendarViewModel;
import com.example.databinding2.util.CalendarUtil;

import java.util.ArrayList;

public class MakePlanVM extends CalendarViewModel {

    public MakePlanVM() {

    }

    public void makeNewPlan(String textPlan){

        Plan plan = new Plan();
        plan.setTextPlan(textPlan)
                .setYear(getGlobalCurrentCalendarYear())
                .setMonth(getGlobalCurrentCalendarMonth())
                .setDay(getGlobalCurrentCalendarDay());



        //TODO 유틸 만들기
        DayPlanList org = PlanRepository.getCurrentDayPlanList();
        org.add(plan);

        PlanRepository.setLiveCurrentDayPlanList(org);


        YMD[] shouldPlannedDay = PlanCreator.getDenseMode(getGlobalSelectedYMD());
        registerPlanByYMD(getGlobalSelectedYMD(),shouldPlannedDay,plan);


    }


    private void registerPlanByYMD(YMD parentPlannedDay, YMD[] shouldPlannedDay,Plan newPlan){
        boolean isBeyondCurrentCalendar = false;
        int index = 0;


        MonthPlanList refreshedPlanList = PlanRepository.getCurrentMonthPlanList();

        for(index=0; index < shouldPlannedDay.length;index++){

            YMD date = shouldPlannedDay[index];
            Plan copied = newPlan.makeChild(date);

            if(CalendarUtil.isInRangeOfMonthInCalendar(date.getYear(),
                getGlobalCurrentCalendarMonth(),date.getMonth(),date.getDay())) {

                int indexOnCalendar = CalendarUtil.convertDateToIndex(date.getYear(), getGlobalCurrentCalendarMonth(),
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


