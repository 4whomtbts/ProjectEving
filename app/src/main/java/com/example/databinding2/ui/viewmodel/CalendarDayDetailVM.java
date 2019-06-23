package com.example.databinding2.ui.viewmodel;

import androidx.lifecycle.LiveData;

import com.example.databinding2.TSLiveData;
import com.example.databinding2.custom.YMD;
import com.example.databinding2.domain.DayClass;
import com.example.databinding2.domain.Plan;
import com.example.databinding2.model.CalendarDayDAO;
import com.example.databinding2.repository.CalendarRepository;
import com.example.databinding2.repository.PlanRepository;
import com.example.databinding2.repository.RootRepository;
import com.example.databinding2.ui.adapter.CalendarPageAdapter;
import com.example.databinding2.util.CalendarUtil;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;

public class CalendarDayDetailVM extends CalendarViewModel {

    private CalendarDayDAO DayDAO;
    private CalendarRepository Repo;
    public TSLiveData<ArrayList<TSLiveData<Plan>>> livePlanList;

    public CalendarDayDetailVM(){

        DayDAO = RootRepository.getCalendarDayDAO();
        Repo = RootRepository.getCalendarRepository();
        livePlanList = new TSLiveData<>();
        initLivePlanList();
        CalendarRepository.
                setLiveCurrentDayPlanList(livePlanList);

    }

    public TSLiveData<ArrayList<TSLiveData<Plan>>> getLivePlanList(){
        return CalendarRepository.getLiveCurrentDayPlanList();
    }


    public void initLivePlanList(){

        ArrayList<TSLiveData<Plan>> list = new ArrayList<>();
        try {
            ArrayList<Plan> result = new PlanRepository.GetPlanByDay().execute(getGlobalCurrentYMD()).get();
            for(int i=0; i < result.size(); i++){
                TSLiveData<Plan> livePlan = new TSLiveData<>();
                livePlan.setValue(result.get(i));
            }
        }catch(Exception e){
            e.printStackTrace();
        }


        this.livePlanList.setValue(list);
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


    public ArrayList<Plan> getAllPlansDayAt(int year, int month, int day){
        YMD ymd = new YMD(year,month,day);
        ArrayList<Plan> result = new ArrayList<>();
        try{
            result =  new PlanRepository.GetPlanByDay().execute(ymd).get();
        }catch(Exception e){
            e.printStackTrace();
        }

        return result;
    }

    public ArrayList<Plan> getAllPlansOfNow() {
        return getAllPlansDayAt(
                getGlobalCurrentCalendarYear(),
        getGlobalCurrentCalendarMonth(),
                getGlobalCurrentCalendarDay());
    }
}
