package com.example.evingPlanner.ui.mainCalendarItem;

import com.example.evingPlanner.TSLiveData;
import com.example.evingPlanner.custom.types.DayPlanList;
import com.example.evingPlanner.domain.DayClass;
import com.example.evingPlanner.ui.viewmodel.CalendarViewModel;
import com.example.evingPlanner.util.CalendarUtil;

import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;

public class CalendarDayVM extends CalendarViewModel {

    TSLiveData<DayPlanList> currentPlanList;
    TSLiveData<DayClass> mCalendar = new TSLiveData<>();
    private int year;
    private int month;
    private int day;

    CalendarDayVM(int itemPosition){
        super();
        this.currentPlanList = getLivePlanListAt(itemPosition);
    }

    // 빨간색으로 표시되어야 하는 날인지 아닌지 알려준다.
    boolean isRedDay() {
        return CalendarUtil.isRedDay(year, month, day);
    }

    // 굵은 글씨로 표시해야 하는 날인지 알려준다.
    boolean isBoldDay() {
        DateTime systemDate = DateTime.now();
        return (systemDate.year().get() == year) &&
                (systemDate.monthOfYear().get() == month) &&
                (systemDate.dayOfMonth().get() == day);
    }

    void setCalendar(DayClass dayClass) {
        this.mCalendar.setValue(dayClass);
        this.year = dayClass.getYear();
        this.month = dayClass.getMonth();
        this.day = Integer.parseInt(dayClass.getDay());

    }

    public void setGlobalCurrentMonth(int month){

    }

    public String getDay(){
        return Integer.toString(this.day);
    }

    public int getYear() {
        return this.year;
    }
    public int getMonth(){
        return this.month;
    }



}
