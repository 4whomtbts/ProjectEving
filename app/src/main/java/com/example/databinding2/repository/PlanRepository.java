package com.example.databinding2.repository;

import android.os.AsyncTask;
import android.util.Pair;

import com.example.databinding2.TSLiveData;
import com.example.databinding2.custom.YMD;
import com.example.databinding2.domain.Plan;
import com.example.databinding2.util.CalendarUtil;

import java.util.ArrayList;
import java.util.HashMap;

public class PlanRepository {

    private static PlanRepository Inst;

    private static TSLiveData<ArrayList<TSLiveData<Plan>>> _currentPlanList;
    private static TSLiveData<ArrayList<TSLiveData<ArrayList<Plan>>>> _currentMonthPlanList;
    private static HashMap<Pair<Integer,Integer>,ArrayList<ArrayList<Plan>>> _planStore;




    public static PlanRepository get(){
        if(Inst == null){
            Inst = new PlanRepository();
        }
        return Inst;
    }

    private PlanRepository(){
        _currentPlanList = new TSLiveData<>();

        _currentMonthPlanList = new TSLiveData<>();

        _planStore = new HashMap<>();

        initMonthPlanList();

    }

    public void addPlanStore(int year, int month, ArrayList<ArrayList<Plan>> list){
        _planStore.put(new Pair<>(year,month),list);
    }
    public ArrayList<ArrayList<Plan>> getPlanFromStore(int year, int month) {
        return _planStore.get(new Pair<>(year,month));
    }
    public void deleteFromPlanStore(int year, int month){
        _planStore.remove(new Pair<>(year,month));
    }


    /* currentMonthPlanList 관련 methods */
    public static void initMonthPlanList(){
        ArrayList<TSLiveData<ArrayList<Plan>>> rawList = new ArrayList<>();
        _currentMonthPlanList.setValue(rawList);

        for(int i = 0; i < CalendarUtil.DAY_IN_MONTH; i++){
            ArrayList<Plan> innerRawList = new ArrayList<>();
            TSLiveData<ArrayList<Plan>> planList = new TSLiveData<>(innerRawList);
            _currentMonthPlanList.getValue().add(planList);
        }
    }


    public static TSLiveData<ArrayList<Plan>> getLiveCurrentMonthPlanListAt(int day){
        return getLiveCurrentMonthPlanList().getValue().get(day);
    }

    public static TSLiveData<ArrayList<TSLiveData<ArrayList<Plan>>>> getLiveCurrentMonthPlanList(){
        return _currentMonthPlanList;
    }
    public static ArrayList<TSLiveData<ArrayList<Plan>>> getCurrentMonthPlanList(){
        return _currentMonthPlanList.getValue();
    }
    public static void setLiveCurrentMonthPlanList(ArrayList<TSLiveData<ArrayList<Plan>>> newList){
        _currentMonthPlanList.set(newList);
    }

    public static TSLiveData<ArrayList<TSLiveData<Plan>>> getLiveCurrentDayPlanList(){
        return _currentPlanList;
    }
    public static void setLiveCurrentDayPlanList(TSLiveData<ArrayList<TSLiveData<Plan>>> plan){
        _currentPlanList = plan;
    }

    ////////////////////////////////////////////////////////////////////////////////////


    public static class InsertPlan extends AsyncTask<Plan,Void, Plan> {

        @Override
        protected Plan doInBackground(Plan... plans) {
            Plan plan = plans[0];
            RootRepository.getCalendarPlanDAO().insert(plan);
            return plan;
        }
    }

    public static  class GetPlanByDay extends AsyncTask<YMD,Void,ArrayList<Plan>> {

        @Override
        protected ArrayList<Plan> doInBackground(YMD... dates) {
            ArrayList<Plan> result=new ArrayList<>();
            for(int i=0; i < dates.length; i++){

                RootRepository.getCalendarPlanDAO().getPlanByDay(
                        dates[i].getYear(),dates[i].getMonth(),dates[i].getDay()
                );
            }
            return result;
        }
    }


/*
    public static  class GetLivePlanByDay extends AsyncTask<YMD,Void,ArrayList<TSLiveData<Plan>>> {

        @Override
        protected ArrayList<TSLiveData<Plan>> doInBackground(YMD... dates) {
            ArrayList<Plan> result= new ArrayList<>();
            ArrayList<TSLiveData<Plan>> liveResult = new ArrayList<>();

                result = (ArrayList<Plan>)RootRepository.getCalendarPlanDAO().getPlanByDay(
                        dates[0].getYear(),dates[0].getMonth(),dates[0].getDay()
                );

                for(int i=0; i < result.size(); i++){
                    TSLiveData<Plan> currPlan = new TSLiveData<>();
                    currPlan.setValue(result.get(i););
                    liveResult.set(0,new TSLiveData<Plan>())
                }

            return result;
        }
    }
    */


}
