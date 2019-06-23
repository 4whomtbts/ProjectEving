package com.example.databinding2.ui.viewmodel;

import androidx.lifecycle.LiveData;

import com.example.databinding2.domain.Plan;

public class DayPlanVM extends CalendarViewModel {
    /* TODO

     */
    private LiveData<Plan> plan;
    private int position;
    public DayPlanVM(int position){
        this.position = position;
    }


}
