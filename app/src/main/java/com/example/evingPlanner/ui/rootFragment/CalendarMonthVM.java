package com.example.evingPlanner.ui.rootFragment;

import com.example.evingPlanner.TSLiveData;
import com.example.evingPlanner.custom.Pair;
import com.example.evingPlanner.domain.DayClass;
import com.example.evingPlanner.ui.viewmodel.CalendarViewModel;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.evingPlanner.repository.CalendarRepository.generateDaysListByDate;

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
            setGlobalCurrentMonth(1);
        } else {
            setGlobalCurrentMonth(month + 1);
        }
    }

    void initCalendar() {
        generateDaysListByDate(getGlobalCurrentCalendarYear(),
                getGlobalCurrentCalendarMonth());
    }
}
