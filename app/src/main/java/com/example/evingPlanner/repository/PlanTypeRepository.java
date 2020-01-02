package com.example.evingPlanner.repository;

import android.os.AsyncTask;

import com.example.evingPlanner.domain.planTypes.PlanType;

import java.util.ArrayList;

public class PlanTypeRepository {

    private static PlanTypeRepository Inst;

    public static PlanTypeRepository get(){
        if(Inst == null){
            Inst = new PlanTypeRepository();
        }
        return Inst;
    }

    public static class InsertOnePlanType extends AsyncTask<PlanType,Void,Boolean>{

        @Override
        protected Boolean doInBackground(PlanType... planTypes) {

            if(planTypes.length != 1){
                throw new Error("InsertOnePlanType receives only one planType");
            }

            PlanType planType = planTypes[0];

            RootRepository.getPlanTypeDatabase().getPlanTypeDAO().insert(planType);

            return true;
        }
    }
    public static class InsertPlanTypes extends AsyncTask<PlanType,Void,Void>{

        @Override
        protected Void doInBackground(PlanType... planTypes) {

            return null;
        }
    }


    public static class DeleteAllPlan extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            RootRepository.getPlanTypeDatabase().getPlanTypeDAO().deleteAll();
            return null;
        }
    }
    public static class DeleteOnePlanTypeByName extends AsyncTask<String,Void,Void> {

        @Override
        protected Void doInBackground(String... strings) {

            String typeName = strings[0];

            RootRepository.getPlanTypeDatabase().getPlanTypeDAO().deletePlanTypeByName(typeName);

            return null;
        }
    }

    public static class DeletePlanTypesByName extends AsyncTask<String,Void,Void> {

        @Override
        protected Void doInBackground(String... strings) {

            return null;
        }
    }

    public static class SelectAll extends AsyncTask<Void, Void, ArrayList<PlanType>> {

        @Override
        protected ArrayList<PlanType> doInBackground(Void... voids) {

            ArrayList<PlanType> newList = new ArrayList<>();

            newList = (ArrayList<PlanType>)RootRepository.getCalendarPlanTypeDAO().selectAll();
            return newList;
        }
    }

    public static class SelectOnePlanTypeByName extends AsyncTask<String, Void, PlanType>{

        @Override
        protected PlanType doInBackground(String... strings) {

            String planType = strings[0];

            PlanType result = RootRepository.getCalendarPlanTypeDAO().selectPlanTypeByName(planType);
            return result;
        }
    }



}
