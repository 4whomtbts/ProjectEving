package com.example.databinding2.ui.viewmodel;

import androidx.lifecycle.ViewModel;

import com.example.databinding2.TSLiveData;
import com.example.databinding2.custom.YMD;
import com.example.databinding2.domain.DayClass;
import com.example.databinding2.domain.Plan;
import com.example.databinding2.repository.CalendarRepository;
import com.example.databinding2.repository.PlanRepository;
import com.example.databinding2.repository.RootRepository;
import com.example.databinding2.util.CalendarUtil;

import java.util.ArrayList;

public class CalendarViewModel extends ViewModel {


    private  RootRepository rootRepo;
    private  CalendarRepository calendarRepo;


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
    public   TSLiveData<Integer> getLiveGlsobalYear(){
        return CalendarRepository.getLiveGlobalYear();
    }
    public   TSLiveData<Integer> getLiveGlobalMonth(){
        return CalendarRepository.getLiveGlobalMonth();
    }
    public TSLiveData<ArrayList<TSLiveData<ArrayList<Plan>>>> getLiveCurrentMonthPlanList(){
        return PlanRepository.getLiveCurrentMonthPlanList();
    }
    public  TSLiveData<ArrayList<Plan>> getLiveCurrentMonthPlanListAt(int day){
        return PlanRepository.getLiveCurrentMonthPlanListAt(day);
    }

    public void setGlobalCurrentYear(int year){
        CalendarRepository.setGlobalCurrentCalendarYear(year);
    }
    public void setGlobalCurrentMonth(int month){
        CalendarRepository.setGlobalCurrentCalendarMonth(month);
    }



    public int getListIndexDayAt(){
        YMD selectedDate = getGlobalSelectedYMD();
        int currCalendarMonth = CalendarRepository.getGlobalCurrentCalendarMonth();
        int index = CalendarUtil.convertDateToIndex(selectedDate.getYear(),currCalendarMonth,selectedDate.getMonth(),
                selectedDate.getDay());
        return index;
    }

    public void initMonthPlanList(){
        PlanRepository.initMonthPlanList();
    }

}
