package com.example.databinding2.ui.viewmodel;

import android.widget.CalendarView;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import com.example.databinding2.TSLiveData;
import com.example.databinding2.custom.YMD;
import com.example.databinding2.domain.DayClass;
import com.example.databinding2.domain.Plan;
import com.example.databinding2.repository.CalendarRepository;
import com.example.databinding2.repository.RootRepository;
import com.example.databinding2.util.CalendarUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.CancellationException;

public class CalendarViewModel extends ViewModel {


    private  RootRepository rootRepo;
    private  CalendarRepository calendarRepo;


    int getGlobalCurrentCalendarYear(){
        return CalendarRepository.getGlobalCurrentCalendarYear();
    }
    int getGlobalCurrentCalendarMonth(){
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
    public   TSLiveData<Integer> getLiveGlobalYear(){
        return CalendarRepository.getLiveGlobalYear();
    }
    public   TSLiveData<Integer> getLiveGlobalMonth(){
        return CalendarRepository.getLiveGlobalMonth();
    }
    public TSLiveData<ArrayList<Plan>> getLiveGlobalCurrentPlanList()
    {
        return CalendarRepository.getGlobalCurrentPlanList();
    }
    public TSLiveData<ArrayList<TSLiveData<ArrayList<Plan>>>> getLiveCurrentMonthPlanList(){
        return CalendarRepository.getLiveCurrentMonthPlanList();
    }
    public  TSLiveData<ArrayList<Plan>> getLiveCurrentMonthPlanListAt(int day){
        return CalendarRepository.getLiveCurrentMonthPlanListAt(day);
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
        CalendarRepository.initMonthPlanList();
    }

}
