package com.example.databinding2.ui.viewmodel;

import androidx.lifecycle.ViewModel;

import com.example.databinding2.TSLiveData;
import com.example.databinding2.domain.DayClass;
import com.example.databinding2.repository.CalendarRepository;

public class CalendarDayVM extends ViewModel {

    public TSLiveData<DayClass> mCalendar = new TSLiveData<>();
    public void setCalendar(DayClass dayClass) {
        this.mCalendar.setValue(dayClass);
    }

    public void setGlobalCurrentDay(int day){
        CalendarRepository.get().setGlobalCurrentCalendarDay(day);
    }
    public void setGlobalCurrentDay(String day){
        CalendarRepository.get().setGlobalCurrentCalendarDay(
                Integer.parseInt(day));
    }
    public String getDay(){
        return this.mCalendar.getValue().getDay();
    }



}
