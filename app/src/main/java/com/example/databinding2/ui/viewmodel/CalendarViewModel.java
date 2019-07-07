package com.example.databinding2.ui.viewmodel;

import androidx.lifecycle.ViewModel;

import com.example.databinding2.TSLiveData;
import com.example.databinding2.custom.YMD;
import com.example.databinding2.custom.types.DayPlanList;
import com.example.databinding2.custom.types.MonthPlanList;
import com.example.databinding2.domain.DayClass;
import com.example.databinding2.repository.CalendarRepository;
import com.example.databinding2.repository.PlanRepository;
import com.example.databinding2.repository.RootRepository;

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


    public   YMD getGlobalCurrentYMD(){
        return CalendarRepository.getGlobalCurrentYMD();
    }
    public YMD getGlobalSelectedYMD(){
        return CalendarRepository.getGlobalSelectedYMD();
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
    public   TSLiveData<ArrayList<TSLiveData<DayClass>>> getDaysArrayList(){
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
