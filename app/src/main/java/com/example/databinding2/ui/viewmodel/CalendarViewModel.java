package com.example.databinding2.ui.viewmodel;

import androidx.lifecycle.ViewModel;

import com.example.databinding2.TSLiveData;
import com.example.databinding2.custom.YMD;
import com.example.databinding2.domain.DayClass;
import com.example.databinding2.repository.CalendarRepository;
import com.example.databinding2.repository.RootRepository;

public class CalendarViewModel extends ViewModel {

    private static CalendarRepository CalendarRepo =
    CalendarRepository.get();

    public int getGlobalCurrentCalendarYear(){
        return CalendarRepository.getGlobalCurrentCalendarYear();
    }
    public int getGlobalCurrentCalendarMonth(){
        return CalendarRepository.getGlobalCurrentCalendarMonth();
    }
    public int getGlobalCurrentCalendarDay(){
        return CalendarRepository.getGlobalCurrentCalendarDay();
    }
    public YMD getGlobalCurrentYMD(){
        return CalendarRepository.getGlobalCurrentYMD();
    }

}
