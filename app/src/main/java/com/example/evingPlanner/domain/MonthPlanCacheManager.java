package com.example.evingPlanner.domain;

import android.util.Pair;

import com.example.evingPlanner.custom.types.MonthPlanList;
import com.example.evingPlanner.custom.types.PlanStorage;
import com.example.evingPlanner.repository.CalendarRepository;
import com.example.evingPlanner.repository.PlanRepository;

public class MonthPlanCacheManager {

    private int _year;
    private int _month;
    private int _day;
    private PlanStorage _storage;
    private MonthPlanList _currentMonthPlanList;

    public MonthPlanCacheManager(){
        _year = CalendarRepository.getGlobalCurrentCalendarYear();
        _month = CalendarRepository.getGlobalCurrentCalendarMonth();
        _day = CalendarRepository.getGlobalCurrentCalendarDay();
        _currentMonthPlanList = PlanRepository.getCurrentMonthPlanList();

    }

    public void CacheCurrentMonth() {

        if(_storage.size() <= 5){
            _storage.put(new Pair<>(_year,_month),_currentMonthPlanList);
        }

    }

    public MonthPlanList getCachedMonthList(){
        return _storage.get(new Pair<>(_year,_month));
    }

    public void clearCachedMonthList(){
        _storage.clear();
    }

}
