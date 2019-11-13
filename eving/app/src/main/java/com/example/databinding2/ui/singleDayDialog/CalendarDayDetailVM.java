package com.example.databinding2.ui.singleDayDialog;

import com.example.databinding2.TSLiveData;
import com.example.databinding2.custom.types.DayPlanList;
import com.example.databinding2.repository.PlanRepository;
import com.example.databinding2.ui.viewmodel.CalendarViewModel;
import com.example.databinding2.util.CalendarUtil;

public class CalendarDayDetailVM extends CalendarViewModel {


    public CalendarDayDetailVM(){

        DayPlanList newPlanList = new DayPlanList();
        PlanRepository.setLiveCurrentDayPlanList(newPlanList);

    }

    // TODO 테스트도중 임시방편
    public int getListIndexDayAt(){
        return CalendarUtil.convertDateToIndex(getGlobalCurrentCalendarYear()
                ,getGlobalCurrentCalendarMonth(),
                getGlobalCurrentCalendarYear(),
                getGlobalSelectedMonth(),
                getGlobalSelectedDay());
    }

    public TSLiveData<DayPlanList> getLiveCurrentDayPlanList(){
        return super.getLivePlanListAt(getListIndexDayAt());
    }
}
