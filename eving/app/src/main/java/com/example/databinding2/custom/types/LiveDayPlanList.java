package com.example.databinding2.custom.types;

import com.example.databinding2.TSLiveData;

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