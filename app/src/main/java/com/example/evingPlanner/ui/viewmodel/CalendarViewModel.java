package com.example.evingPlanner.ui.viewmodel;

import androidx.lifecycle.ViewModel;

import com.example.evingPlanner.TSLiveData;
import com.example.evingPlanner.custom.YMD;
import com.example.evingPlanner.custom.types.DayPlanList;
import com.example.evingPlanner.custom.types.MonthPlanList;
import com.example.evingPlanner.domain.DayClass;
import com.example.evingPlanner.repository.CalendarRepository;
import com.example.evingPlanner.repository.PlanRepository;
import com.example.evingPlanner.repository.RootRepository;

import java.util.ArrayList;

public class CalendarViewModel extends ViewModel {


    private  RootRepository rootRepo;
    private  CalendarRepository calendarRepo;

    protected int currentYear;
    protected int currentMonth;
    protected int year;
    protected int month;
    protected int day;

    protected CalendarViewModel(){
        this.currentYear = getGlobalCurrentCalendarYear();
        this.currentMonth = getGlobalCurrentCalendarMonth();
    }

    public int getGlobalCurrentCalendarYear(){
        return CalendarRepository.getGlobalCurrentCalendarYear();
    }
    public int getGlobalCurrentCalendarMonth(){
        return CalendarRepository.getGlobalCurrentCalendarMonth();
    }
    public   int getGlobalCurrentCalendarDay(){
        return CalendarRepository.getGlobalCurrentCalendarDay();
    }


    public int getListIndexDayAt(){/*
        int index = CalendarUtil.convertDateToIndex(getGlobalCurrentCalendarYear()
                ,getGlobalCurrentCalendarMonth(),getGlobalSelectedMonth(),getGlobalSelectedDay());
                */
        return -1;
    }

    public   YMD getGlobalCurrentYMD(){
        return CalendarRepository.getGlobalCurrentYMD();
    }
    public YMD getGlobalSelectedYMD(){
        return CalendarRepository.getGlobalSelectedYMD();
    }

    public int getGlobalSelectedYear() {
        return CalendarRepository.getGlobalCurrentSelectedYear();
    }
    public int getGlobalSelectedMonth(){
        return CalendarRepository.getGlobalCurrentSelectedMonth();
    }
    public int getGlobalSelectedDay(){
        return CalendarRepository.getGlobalCurrentSelectedDay();
    }

    public void setGlobalSelectedYear(int year){
        CalendarRepository.setGlobalCurrentSelectedYear(year);
    }
    public void setGlobalSelectedMonth(int month){
        CalendarRepository.setGlobalCurrentSelectedMonth(month);
    }
    public void setGlobalSelectedDay(int day){
        CalendarRepository.setGlobalCurrentSelectedDay(day);
    }
    public TSLiveData<ArrayList<TSLiveData<DayClass>>> getDaysArrayList(){
        return CalendarRepository.getLiveGlobalDaysList();
    }

    public TSLiveData<DayPlanList> getLivePlanListAt(int index){
        return PlanRepository.getCurrentMonthPlanListAt(index);
    }

    public void setGlobalCurrentYear(int year){
        CalendarRepository.setGlobalCurrentCalendarYear(year);
    }
    public void setGlobalCurrentMonth(int month){
        CalendarRepository.setGlobalCurrentCalendarMonth(month);
    }
    public TSLiveData<Integer> getLiveGlobalMonth(){
        return CalendarRepository.getLiveGlobalMonth();
    }
    public void setCurrentMonthPlanList(MonthPlanList list){
        PlanRepository.setCurrentMonthPlanList(list);
    }
    public MonthPlanList getCurrentMonthPlanList(){
        return PlanRepository.getCurrentMonthPlanList();
    }



    public void initMonthPlanList(){
        PlanRepository.initMonthPlanList();
    }

}
