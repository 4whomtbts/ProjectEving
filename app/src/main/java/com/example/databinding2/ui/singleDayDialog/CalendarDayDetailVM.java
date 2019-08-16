package com.example.databinding2.ui.singleDayDialog;

import com.example.databinding2.TSLiveData;
import com.example.databinding2.custom.YMD;
import com.example.databinding2.custom.types.DayPlanList;
import com.example.databinding2.custom.types.LiveDayPlanList;
import com.example.databinding2.domain.DayClass;
import com.example.databinding2.domain.Plan;
import com.example.databinding2.model.CalendarDayDAO;
import com.example.databinding2.repository.CalendarRepository;
import com.example.databinding2.repository.PlanRepository;
import com.example.databinding2.repository.RootRepository;
import com.example.databinding2.ui.viewmodel.CalendarViewModel;
import com.example.databinding2.util.CalendarUtil;

import java.util.ArrayList;

public class CalendarDayDetailVM extends CalendarViewModel {

    private CalendarDayDAO DayDAO;
    private CalendarRepository Repo;
    public TSLiveData<DayPlanList> currentDayPlanList;
    private YMD _date;


    public CalendarDayDetailVM(){

        DayDAO = RootRepository.getCalendarDayDAO();
        Repo = RootRepository.getCalendarRepository();

        DayPlanList newPlanList = new DayPlanList();
        PlanRepository.setLiveCurrentDayPlanList(newPlanList);
        currentDayPlanList = PlanRepository.getLiveCurrentDayPlanList();

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

    public void initLivePlanList(){

        DayPlanList list = new DayPlanList();
        try {
            ArrayList<Plan> result = new PlanRepository.GetPlanByDay().execute(this._date).get();
            for(int i=0; i < result.size(); i++){
                TSLiveData<Plan> livePlan = new TSLiveData<>();
                livePlan.setValue(result.get(i));
            }
        }catch(Exception e){
            e.printStackTrace();
        }


        this.currentDayPlanList.setValue(list);
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
