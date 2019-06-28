package com.example.databinding2.ui.singleDayDialog.dayPlan;

import androidx.lifecycle.LiveData;

import com.example.databinding2.domain.Plan;
import com.example.databinding2.ui.viewmodel.CalendarViewModel;

public class DayPlanVM extends CalendarViewModel {
    /* TODO

     */
    private LiveData<Plan> plan;
    private int position;
    public DayPlanVM(int position){
        this.position = position;
    }


}
