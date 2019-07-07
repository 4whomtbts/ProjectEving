package com.example.databinding2.repository;

import android.os.AsyncTask;
import android.util.Pair;

import com.example.databinding2.TSLiveData;
import com.example.databinding2.custom.YMD;
import com.example.databinding2.custom.types.DayPlanList;
import com.example.databinding2.custom.types.LiveDayPlanList;
import com.example.databinding2.custom.types.MonthPlanList;
import com.example.databinding2.custom.types.PlanStorage;
import com.example.databinding2.domain.Plan;
import com.example.databinding2.util.CalendarUtil;

import java.util.ArrayList;

public class PlanRepository {

    private static PlanRepository Inst;

    private static TSLiveData<DayPlanList> _currentDayPlanList;
    private static TSLiveData<MonthPlanList> _currentMonthPlanList;
    private static PlanStorage _planStore;




    public static PlanRepository get(){
        if(Inst == null){
            Inst = new PlanRepository();
        }
        return Inst;
    }

    private PlanRepository(){
        _currentDayPlanList = new TSLiveData<>();

        _currentMonthPlanList = new TSLiveData<>();

        _planStore = new PlanStorage();

        initMonthPlanList();

    }

    public void refreshGlobalState(){




    }


    /////////////////////************ STORAGE 관련 ************* ////////////////////



    public void addPlanStore(int year, int month, MonthPlanList list){
        _planStore.put(new Pair<>(year,month),list);
    }
    public MonthPlanList getPlanFromStore(int year, int month) {
        return _planStore.get(new Pair<>(year,month));
    }
    public void deleteFromPlanStore(int year, int month){
        _planStore.remove(new Pair<>(year,month));
    }



    ////////////////////////////////////////////////////////////////////////////////




    /* currentMonthPlanList 관련 methods */
    public static void initMonthPlanList(){
        MonthPlanList rawList = new MonthPlanList();
        _currentMonthPlanList.setValue(rawList);

        for(int i = 0; i < CalendarUtil.DAY_IN_MONTH; i++){
            DayPlanList innerRawList = new DayPlanList();

            LiveDayPlanList planList = new LiveDayPlanList(innerRawList);
            _currentMonthPlanList.getValue().add(planList);
        }
    }

    public static TSLiveData<DayPlanList> getCurrentMonthPlanListAt(int day){
        return _currentMonthPlanList.getValue().get(day);
    }
    public static TSLiveData<DayPlanList> getLiveCurrentDayPlanList(){
        return _currentDayPlanList;
    }
    public static DayPlanList getCurrentDayPlanList(){
        return _currentDayPlanList.getValue();
    }

    public static MonthPlanList getCurrentMonthPlanList(){
        return _currentMonthPlanList.getValue();
    }
    public static void setCurrentMonthPlanList(MonthPlanList newList){
        _currentMonthPlanList.setValue(newList);
    }
    public static void setLiveCurrentDayPlanList(DayPlanList plan){
        _currentDayPlanList.setValue(plan);
    }

    ////////////////////////////////////////////////////////////////////////////////////


    public static class InsertPlan extends AsyncTask<Plan,Void, Plan> {

        @Override
        protected Plan doInBackground(Plan... plans) {
            System.out.println("InsertPlan 실행");
            Plan plan = plans[0];
            RootRepository.getCalendarPlanDAO().insert(plan);
            return plan;
        }
        @Override
        protected void onPostExecute(Plan plans) { // doInBackground 에서 받아온 total 값 사용 장소 }
            System.out.println("플랜삽입 종료");
            new PlanRepository.GetPlanByDay().execute(plans.getYMD());

        }

    }

    public static class InsertArrayListPlan extends AsyncTask<ArrayList<Plan>,Void, Plan> {

        @Override
        protected Plan doInBackground(ArrayList<Plan>... arrayLists) {

            ArrayList<Plan> list = arrayLists[0];

            for(int i=0; i < list.size(); i++){
                RootRepository.getCalendarPlanDAO().insert(list.get(i));
            }
            return null;
        }

        @Override
        protected void onPostExecute(Plan plans) { // doInBackground 에서 받아온 total 값 사용 장소 }
            System.out.println("플랜삽입 종료");

        }

    }


    public static class GetAllPlanByPlanUID extends  AsyncTask<Long,Void,ArrayList<Plan>>{

        @Override
        protected ArrayList<Plan> doInBackground(Long... uids) {
            Long parnetUID = uids[0];
            ArrayList<Plan> result = new ArrayList<>();
            result = (ArrayList<Plan>)RootRepository.getCalendarPlanDAO().getPlanByParentUID(
                    parnetUID);
            return result;
        }
    }

    public static  class GetPlanByDay extends AsyncTask<YMD,Void,ArrayList<Plan>> {

        @Override
        protected ArrayList<Plan> doInBackground(YMD... dates) {


            ArrayList<Plan> result=new ArrayList<>();
            for(int i=0; i < dates.length; i++){
                System.out.println("InsertPlan 실행" + dates[i]);
                result= (ArrayList<Plan>) RootRepository.getCalendarPlanDAO().getPlanByDay(
                        dates[i].getYear(),dates[i].getMonth(),dates[i].getDay()
                );

            }
            return result;
        }
        @Override
        protected void onPostExecute(ArrayList<Plan> plans) { // doInBackground 에서 받아온 total 값 사용 장소 }
            /*
            System.out.println("GetPlanByDay 종료");
            System.out.println("사이즈 : "+plans.size());
            for(Plan plan : plans){
                System.out.println("GetPlanByDay"+plan.getTextPlan());
            }
            */
        }
    }


    public static  class GetPlanByMonth extends AsyncTask<YMD,Void,ArrayList<Plan>> {

        @Override
        protected ArrayList<Plan> doInBackground(YMD... dates) {
            ArrayList<Plan> result=new ArrayList<>();
            for(int i=0; i < dates.length; i++){

                result = (ArrayList<Plan>)RootRepository.getCalendarPlanDAO().getPlanByMonth(
                        dates[i].getYear(),dates[i].getMonth()
                );
            }
            return result;
        }

        @Override
        protected void onPostExecute(ArrayList<Plan> plans) { // doInBackground 에서 받아온 total 값 사용 장소 }

        }

    }

    public static class GetVisiblePlanMonthAt extends AsyncTask<YMD,Void,ArrayList<Plan>>{

        @Override
        protected ArrayList<Plan> doInBackground(YMD... dates) {
            YMD date = dates[0];
            int visibleFirstDayOfLastMonth = CalendarUtil.getFirstDayOfLastMonth(date);
            int visibleLastDayOfLastMonth = CalendarUtil.getLastDayOfLastMonth(date);
            int visibleLastDayOfNextMonth = CalendarUtil.getLastVisibleDayOfNextMonth(date);
            ArrayList<Plan> visibleDaysListOfCurrentMonth = null;
            ArrayList<Plan> visibleDaysListOfLastMonth = null;
            ArrayList<Plan> visibleDaysListOfNextMonth = null;
            ArrayList<Plan> mergedResult = new ArrayList<>();

            visibleDaysListOfCurrentMonth = (ArrayList<Plan>)RootRepository.getCalendarPlanDAO().getPlanByMonth(
                    date.getYear(),date.getMonth());

            System.out.println("현재 달 \n "+visibleDaysListOfCurrentMonth);
            YMD prevMonthDate = date.prevMonth();
            visibleDaysListOfLastMonth = (ArrayList<Plan>)RootRepository.getCalendarPlanDAO().getPlanByRangeOfDay(
                                                prevMonthDate.getYear(),prevMonthDate.getMonth()
            ,visibleFirstDayOfLastMonth,visibleLastDayOfLastMonth);
            System.out.println("이전 달 \n "+visibleDaysListOfLastMonth);
            YMD nextMonthDate = date.nextMonth();
            visibleDaysListOfNextMonth = (ArrayList<Plan>)RootRepository.getCalendarPlanDAO().getPlanByRangeOfDay(
                    nextMonthDate.getYear(),nextMonthDate.getMonth()
                    ,1,visibleLastDayOfNextMonth);
            System.out.println("다음 달 \n "+visibleDaysListOfNextMonth);
            mergedResult.addAll(visibleDaysListOfLastMonth);
            mergedResult.addAll(visibleDaysListOfNextMonth);
            mergedResult.addAll(visibleDaysListOfCurrentMonth);

            //System.out.println(mergedResult);
            return mergedResult;
        }
    }


    public static class GetPlanByRangeOfDay extends AsyncTask<Integer[],Void,ArrayList<Plan>>{

        @Override
        protected ArrayList<Plan> doInBackground(Integer[]... integers) {
            Integer[] arr = integers[0];
                int year = arr[0];
                int month = arr[1];
                int left = arr[2];
                int right = arr[3];

                return (ArrayList<Plan>)(RootRepository.getCalendarPlanDAO().getPlanByRangeOfDay(year,month,left,right));
        }
    }

    public static class SelectAllPlan extends AsyncTask<Void,Void,ArrayList<Plan>>{

        @Override
        protected ArrayList<Plan> doInBackground(Void... voids) {
            return (ArrayList<Plan>)(RootRepository.getCalendarPlanDAO().selectAll());
        }
    }

    public static class DeleteAllPlan extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            RootRepository.getCalendarPlanDAO().deleteAll();
            return null;
        }
    }

    public static class DeletePlanByOneParentUID extends AsyncTask<Long,Void,Void> {

        @Override
        protected Void doInBackground(Long... longs) {

            long UID = longs[0];

            RootRepository.getCalendarPlanDAO().deletePlanByOneParentUID(UID);
            return null;
        }
    }

    public static class UpdateOnePlanCheckState extends  AsyncTask<Pair<Long,Boolean>, Void , Void >{

        @Override
        protected Void doInBackground(Pair<Long, Boolean>... pairs) {
            Pair<Long,Boolean> pair = pairs[0];
            RootRepository.getCalendarPlanDAO().updateOnePlanCheckState(pair.first,pair.second);
            return null;
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
