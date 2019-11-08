package com.example.databinding2.ui.mainCalendarItem;

import com.example.databinding2.TSLiveData;
import com.example.databinding2.custom.types.DayPlanList;
import com.example.databinding2.domain.DayClass;
import com.example.databinding2.ui.viewmodel.CalendarViewModel;

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
    public int getMonth(){
        return this.mCalendar.getValue().getMonth();
    }



}
