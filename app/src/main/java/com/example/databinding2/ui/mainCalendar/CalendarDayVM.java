package com.example.databinding2.ui.mainCalendar;

import com.example.databinding2.TSLiveData;
import com.example.databinding2.domain.DayClass;
import com.example.databinding2.repository.CalendarRepository;
import com.example.databinding2.ui.viewmodel.CalendarViewModel;

public class CalendarDayVM extends CalendarViewModel {

    public CalendarDayVM(){
        super();
    }
    public TSLiveData<DayClass> mCalendar = new TSLiveData<>();
    public void setCalendar(DayClass dayClass) {
        this.mCalendar.setValue(dayClass);
    }

    public void setGlobalCurrentDay(int day){
        CalendarRepository.setGlobalCurrentCalendarDay(day);
    }
    public void setGlobalCurrentDay(String day){
        CalendarRepository.setGlobalCurrentCalendarDay(
                Integer.parseInt(day));
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
