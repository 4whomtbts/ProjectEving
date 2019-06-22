package com.example.databinding2.ui.viewmodel;

import androidx.lifecycle.ViewModel;

import com.example.databinding2.R;
import com.example.databinding2.domain.DayClass;
import com.example.databinding2.model.CalendarDayDAO;
import com.example.databinding2.repository.CalendarRepository;
import com.example.databinding2.repository.RootRepository;
import com.example.databinding2.ui.adapter.CalendarPageAdapter;

import java.util.concurrent.ExecutionException;

public class CalendarDayDetailVM extends CalendarViewModel {

    private CalendarDayDAO DayDAO;
    private CalendarRepository Repo;
    public CalendarDayDetailVM(){
        DayDAO = RootRepository.getCalendarDayDAO();
        Repo = RootRepository.get(null).getCalendarRepository();
    }


    public void insertDays(DayClass...days){
        new CalendarRepository.InsertDay().execute(days);
    }

    public DayClass getDayByDay(DayClass day){
        DayClass resultDay=null;
        try{
            resultDay =
                    new CalendarRepository.GetDayByDay().execute(day).get();

        }catch(Exception e){
            e.printStackTrace();
        }
        return resultDay;
    }



}
