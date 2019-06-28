package com.example.databinding2.ui.main;

import androidx.lifecycle.ViewModel;

import com.example.databinding2.TSLiveData;
import com.example.databinding2.domain.DayClass;

import java.util.ArrayList;

public class mainVM extends ViewModel {

    public TSLiveData<String> mTitle = new TSLiveData<>();
    public TSLiveData<Integer> mMonth = new TSLiveData<>();
    public TSLiveData<ArrayList<DayClass>> mCalendarList = new TSLiveData<>(new ArrayList<DayClass>());
    public void setTitle(String title){
        this.mTitle.setValue(title);
    }

    public void setMonth(int month){
        this.mMonth.setValue(month);
    }

    public void setmCalendarList(DayClass cal){
         this.mCalendarList.getValue().add(cal);
    }

    public void initCalendar(){


    }


}
