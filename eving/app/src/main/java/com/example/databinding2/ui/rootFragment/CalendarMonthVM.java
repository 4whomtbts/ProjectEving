package com.example.databinding2.ui.rootFragment;

import com.example.databinding2.TSLiveData;
import com.example.databinding2.custom.Pair;
import com.example.databinding2.domain.DayClass;
import com.example.databinding2.repository.CalendarRepository;
import com.example.databinding2.ui.viewmodel.CalendarViewModel;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.databinding2.repository.CalendarRepository.generateDaysListByDate;

public class CalendarMonthVM extends CalendarViewModel {

    private TSLiveData<HashMap<Pair<Integer, Integer>, ArrayList<DayClass>>> TempStore;

    public CalendarMonthVM() {
        generateDaysListByDate(getGlobalCurrentCalendarYear(), getGlobalCurrentCalendarMonth());
    }

    void gotoPrevMonth() {
        int year = getGlobalCurrentCalendarYear();
        int month = getGlobalCurrentCalendarMonth();
        if (month == 1) {
            setGlobalCurrentYear(year - 1);
            setGlobalCurrentMonth(12);
        } else {
            setGlobalCurrentMonth(month - 1);
        }
    }

    void gotoNextMonth() {
        int year = getGlobalCurrentCalendarYear();
        int month = getGlobalCurrentCalendarMonth();
        if (month == 12) {
            setGlobalCurrentYear(year + 1);
        } else {
            setGlobalCurrentMonth(month + 1);
        }
    }

    void initCalendar() {
        generateDaysListByDate(getGlobalCurrentCalendarYear(),
                getGlobalCurrentCalendarMonth());
    }
}
