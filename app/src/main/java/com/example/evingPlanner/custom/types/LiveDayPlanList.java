package com.example.evingPlanner.custom.types;

import com.example.evingPlanner.TSLiveData;

public class LiveDayPlanList extends TSLiveData<DayPlanList> {
    public LiveDayPlanList(DayPlanList initializer){
        this.setValue(initializer);
    }
    public LiveDayPlanList(){
        this.setValue(new DayPlanList());
    }

    @Override
    public String toString(){
        return this.getValue().toString();
    }
}