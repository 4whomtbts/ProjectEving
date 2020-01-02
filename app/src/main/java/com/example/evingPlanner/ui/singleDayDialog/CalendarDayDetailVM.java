package com.example.evingPlanner.ui.singleDayDialog;

import com.example.evingPlanner.TSLiveData;
import com.example.evingPlanner.custom.types.DayPlanList;
import com.example.evingPlanner.repository.PlanRepository;
import com.example.evingPlanner.ui.viewmodel.CalendarViewModel;
import com.example.evingPlanner.util.CalendarUtil;

public class CalendarDayDetailVM extends CalendarViewModel {


    public CalendarDayDetailVM(){

        DayPlanList newPlanList = new DayPlanList();
        PlanRepository.setLiveCurrentDayPlanList(newPlanList);

    }

    // TODO 테스트도중 임시방편
    public int getListIndexDayAt(){
        return CalendarUtil.convertDateToIndex(getGlobalCurrentCalendarYear()
                ,getGlobalCurrentCalendarMonth(),
                getGlobalSelectedYear(),
                getGlobalSelectedMonth(),
                getGlobalSelectedDay());
    }

    public TSLiveData<DayPlanList> getLiveCurrentDayPlanList(){
        return super.getLivePlanListAt(getListIndexDayAt());
    }
}
