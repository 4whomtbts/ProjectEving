package com.example.evingPlanner.repository;

import android.os.AsyncTask;

import com.example.evingPlanner.TSLiveData;
import com.example.evingPlanner.custom.YMD;
import com.example.evingPlanner.custom.types.DayPlanList;
import com.example.evingPlanner.custom.types.LiveDayPlanList;
import com.example.evingPlanner.custom.types.MonthPlanList;
import com.example.evingPlanner.custom.types.PlanStorage;
import com.example.evingPlanner.domain.Plan;
import com.example.evingPlanner.util.CalendarUtil;

import java.util.ArrayList;
import java.util.List;

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
        CalendarRepository.refreshCalendar();
    }

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
            Plan plan = plans[0];
            RootRepository.getCalendarPlanDAO().insert(plan);
            return plan;
        }
        @Override
        protected void onPostExecute(Plan plans) { // doInBackground 에서 받아온 total 값 사용 장소 }
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

        }

    }

    public static class GetOnePlanByUID extends  AsyncTask<Long,Void,Plan> {

        @Override
        protected Plan doInBackground(Long... uids) {
            Long parnetUID = uids[0];
            Plan result = null;
            result = RootRepository.getCalendarPlanDAO().getPlanByUID(
                    parnetUID);
            return result;
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

    public static class GetAllPlanByPlanParentUID extends  AsyncTask<Plan,Void,ArrayList<Plan>>{

        @Override
        protected ArrayList<Plan> doInBackground(Plan... plans) {
            Plan plan = plans[0];
            ArrayList<Plan> result = new ArrayList<>();
            result = (ArrayList<Plan>)RootRepository.getCalendarPlanDAO().getPlanByParentUID(
                    plan.parentUID);
            return result;
        }
    }

    public static  class GetPlanByDay extends AsyncTask<YMD,Void,ArrayList<Plan>> {

        @Override
        protected ArrayList<Plan> doInBackground(YMD... dates) {


            ArrayList<Plan> result=new ArrayList<>();
            for(int i=0; i < dates.length; i++){
                result= (ArrayList<Plan>) RootRepository.getCalendarPlanDAO().getPlanByDay(
                        dates[i].getYear(),dates[i].getMonth(),dates[i].getDay()
                );

            }
            return result;
        }
        @Override
        protected void onPostExecute(ArrayList<Plan> plans) { // doInBackground 에서 받아온 total 값 사용 장소 }
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

    /**
     * Plan을 받아서 그것의 원본이 가지고 있는 progress 정보를 받는다
     */
    public static class GetProgressByPlan extends AsyncTask<Plan ,Void, Double>{


        @Override
        protected Double doInBackground(Plan... plans) {
            Plan plan = plans[0];
            return RootRepository.getCalendarPlanDAO().getProgressByUID(plan.parentUID);
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

    public static class DeleteAllChildrenPlan extends AsyncTask<Plan,Void,Void> {

        @Override
        protected Void doInBackground(Plan... plans) {
            Plan plan = plans[0];
            long uid = plan.parentUID;

            RootRepository.getCalendarPlanDAO().deleteChildrenPlanByParentUID(uid);
            return null;
        }
    }

    public static class DeletePlan extends AsyncTask<Plan, Void, Void> {

        @Override
        protected Void doInBackground(Plan... plans) {


            RootRepository.getCalendarPlanDAO().delete(plans);
            return null;
        }
    }

    // 파라미터로 넘어온 Plan을 업데이트하고 모든 children 을 동일하게 업데이트 한다.
    public static class UpdateParentAndAllChildrenOfPlan extends AsyncTask<Plan, Void, Void> {

        @Override
        protected Void doInBackground(Plan... plans) {
            Plan parentPlan = plans[0];
            long uid = parentPlan.uid;
            RootRepository.getCalendarPlanDAO().updatePlan(
                    parentPlan.uid,parentPlan.title,parentPlan.textPlan,
                    parentPlan.groupUID,
                    parentPlan.isDone);

            RootRepository.getCalendarPlanDAO().updatePlanByParentUID(uid,
                    parentPlan.groupUID,
                    parentPlan.getTitle(),parentPlan.textPlan);

            return null;
        }
    }

    public static class UpdatePlansByList extends AsyncTask<ArrayList<Plan>, Void, Void> {

        @Override
        protected Void doInBackground(ArrayList<Plan>... lists) {
            ArrayList<Plan> parentPlan = lists[0];

            for(Plan plan : parentPlan) {
                RootRepository.getCalendarPlanDAO().updatePlanDate(
                        plan.uid,plan.year, plan.month, plan.day);
            }

            return null;
        }
    }

    public static class UpdatePlansDateByUID extends AsyncTask<Plan, Void ,Void> {

        @Override
        protected Void doInBackground(Plan... plans) {

            for(Plan plan : plans) {
                //RootRepository.getCalendarPlanDAO().updatePlanDate(plan.uid,plan.year,plan.month,plan.day);
            }

            return null;
        }
    }

    public static class UpdateOnePlanByUID extends AsyncTask<Plan, Void ,Void> {

        @Override
        protected Void doInBackground(Plan... plans) {
            Plan plan = plans[0];
            RootRepository.getCalendarPlanDAO().updatePlan(plan.uid,plan.title,plan.textPlan,plan.groupUID ,plan.isDone);

            return null;
        }
    }

    public static class UpdateOnePlanCheckState extends  AsyncTask<Plan, Void , Void >{

        @Override
        protected Void doInBackground(Plan... plans) {
            Plan plan = plans[0];

            int diff=  PlanRepository.diffByDoneValue(plan.isDone);
            RootRepository.getCalendarPlanDAO().updateParentProgress(plan.parentUID,diff);
            RootRepository.getCalendarPlanDAO().updateOnePlanCheckState(plan.uid, plan.isDone);
            return null;
        }
    }

    public static class ReOrderingPlanCycleByParentUID extends AsyncTask<Plan, Void, Void> {

        @Override
        protected Void doInBackground(Plan... plans) {
            Plan plan = plans[0];
            long uid = plan.parentUID;

            List<Plan> currentList = RootRepository.getCalendarPlanDAO().getPlanByParentUID(uid);

            for(int i =0; i < currentList.size(); i++) {
                Plan currPlan = currentList.get(i);
                currPlan.setTotalCycle(currPlan.totalCycle - 1)
                        .setThisCycle(i+1);
                RootRepository.getCalendarPlanDAO().update(currPlan);
            }

            return null;
        }
    }

    private static int diffByDoneValue(boolean isDone) {
        return isDone?1:-1;
    }

}
