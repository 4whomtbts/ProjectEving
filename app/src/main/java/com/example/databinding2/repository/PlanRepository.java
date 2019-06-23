package com.example.databinding2.repository;

import android.os.AsyncTask;

import com.example.databinding2.TSLiveData;
import com.example.databinding2.custom.YMD;
import com.example.databinding2.domain.Plan;

import java.util.ArrayList;

public class PlanRepository {

    private static PlanRepository Inst;
    TSLiveData<ArrayList<Plan>> _planList;

    public static PlanRepository get(){
        if(Inst == null){
            Inst = new PlanRepository();
        }
        return Inst;
    }

    private PlanRepository(){
        _planList = new TSLiveData<>();

    }

    public TSLiveData<ArrayList<Plan>> getLivePlanList(){
        return this._planList;
    }

    private static void generatePlan(){
        //ArrayList<Plan> result = new GetPlanByDay().execute()
    }


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
