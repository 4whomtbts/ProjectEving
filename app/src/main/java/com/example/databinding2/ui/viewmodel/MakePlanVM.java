package com.example.databinding2.ui.viewmodel;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.example.databinding2.TSLiveData;
import com.example.databinding2.custom.YMD;
import com.example.databinding2.domain.Plan;
import com.example.databinding2.repository.CalendarRepository;
import com.example.databinding2.repository.PlanRepository;
import com.example.databinding2.repository.RootRepository;

import java.lang.reflect.Array;
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

        TSLiveData<Plan> livePlan = new TSLiveData<>();
        livePlan.setValue(plan);

        new PlanRepository.InsertPlan().execute(plan);
        Log.e("새로운 플랜입력",Long.toString(plan.getUid()));
        //TODO 유틸 만들기
        ArrayList<TSLiveData<Plan>> org = CalendarRepository.getLiveCurrentDayPlanList().getValue();
        org.add(livePlan);
        CalendarRepository.getLiveCurrentDayPlanList().setValue(org);

        for(int i=0; i < CalendarRepository.getLiveCurrentDayPlanList().getValue().size();i++){
            Log.e(""+i,CalendarRepository.getLiveCurrentDayPlanList().getValue().get(i).toString());
        }
    }


}


