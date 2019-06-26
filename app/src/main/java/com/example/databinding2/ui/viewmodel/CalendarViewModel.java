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

import java.util.ArrayList;

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
    public   TSLiveData<ArrayList<TSLiveData<DayClass>>> getDaysArrayList(){
        return CalendarRepository.getLiveGlobalDaysList();
    }
    public   TSLiveData<Integer> getLiveGlobalYear(){
        return CalendarRepository.getLiveGlobalYear();
    }
    public   TSLiveData<Integer> getLiveGlobalMonth(){
        return CalendarRepository.getLiveGlobalMonth();
    }
    public TSLiveData<ArrayList<Plan>> getLiveGlobalCurrentPlanList(){
        return CalendarRepository.getGlobalCurrentPlanList();
    }

    public TSLiveData<ArrayList<TSLiveData<ArrayList<Plan>>>> getLiveCurrentMonthPlanList(){
        return CalendarRepository.getLiveCurrentMonthPlanList();
    }
    public  TSLiveData<ArrayList<Plan>> getLiveCurrentMonthPlanListAt(int day){
        return CalendarRepository.getLiveCurrentMonthPlanListAt(day);
    }

}
