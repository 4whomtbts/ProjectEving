package com.example.evingPlanner.ui.mainCalendarItem;

import com.example.evingPlanner.TSLiveData;
import com.example.evingPlanner.custom.types.DayPlanList;
import com.example.evingPlanner.domain.DayClass;
import com.example.evingPlanner.ui.viewmodel.CalendarViewModel;

public class CalendarDayVM extends CalendarViewModel {

    public TSLiveData<DayPlanList> currentPlanList;
    private int itemPosition;


    public CalendarDayVM(int itemPosition ){
        super();
        this.itemPosition = itemPosition;

        this.currentPlanList = getLivePlanListAt(itemPosition);

    }
    public TSLiveData<DayClass> mCalendar = new TSLiveData<>();
    public void setCalendar(DayClass dayClass) {
        this.mCalendar.setValue(dayClass);
    }

    public void setGlobalCurrentMonth(int month){

    }

    public String getDay(){
        return this.mCalendar.getValue().getDay();
    }

    public int getYear() {
        return this.mCalendar.getValue().getYear();
    }
    public int getMonth(){
        return this.mCalendar.getValue().getMonth();
    }



}
