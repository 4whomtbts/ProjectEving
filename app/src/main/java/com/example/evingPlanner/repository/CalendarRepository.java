package com.example.evingPlanner.repository;

import android.os.AsyncTask;
import android.util.Log;

import com.example.evingPlanner.TSLiveData;
import com.example.evingPlanner.custom.Pair;
import com.example.evingPlanner.custom.YMD;
import com.example.evingPlanner.custom.types.DayPlanList;
import com.example.evingPlanner.custom.types.MonthPlanList;
import com.example.evingPlanner.domain.DayClass;
import com.example.evingPlanner.domain.MonthClass;
import com.example.evingPlanner.domain.Plan;
import com.example.evingPlanner.util.CalendarUtil;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.evingPlanner.repository.PlanRepository.setCurrentMonthPlanList;
import static com.example.evingPlanner.util.CalendarUtil.convertDateToIndex;
import static com.example.evingPlanner.util.CalendarUtil.getFirstWeek;
import static com.example.evingPlanner.util.CalendarUtil.getLastDay;
import static com.example.evingPlanner.util.CalendarUtil.getStartDayOfMonth;

public class CalendarRepository {

    private static CalendarRepository Inst;

    private static TSLiveData<MonthClass> _currMonthObj;
//    private static TSLiveData<ArrayList<TSLiveData<MonthClass>>

    // global 에서 현재의 month 에 대한 각각의 일의 livedata

    private static TSLiveData<ArrayList<TSLiveData<DayClass>>> _daysOfCurrMonth;
    private static TSLiveData<Integer> _globalCurrentCalendarYear;
    private static TSLiveData<Integer> _globalCurrentCalendarMonth;
    private static TSLiveData<Integer> _globalCurrentCalendarDay;
    private static TSLiveData<Integer> _globalCurrentSelectedYear;
    private static TSLiveData<Integer> _globalCurrentSelectedMonth;
    private static TSLiveData<Integer> _globalCurrentSelectedDay;
    private static TSLiveData<HashMap<Pair<Integer,Integer>,ArrayList<DayClass>>> _backup;
    public static CalendarRepository get(){
        if(Inst == null){
            Inst = new CalendarRepository();

        }
        return Inst;
    }


    private CalendarRepository(){
        LocalDate now = LocalDate.now();
        int currentYear = now.getYear();
        int currentMonth = now.getMonthOfYear();
        int currentDay = now.getDayOfMonth();
        HashMap<Pair<Integer,Integer>,ArrayList<DayClass>> backup = new  HashMap<>();
        ArrayList<TSLiveData<DayClass>> list = new ArrayList<TSLiveData<DayClass>>();

        _currMonthObj  = new TSLiveData<>();
        _daysOfCurrMonth = new TSLiveData<>();

        _globalCurrentCalendarYear = new TSLiveData<>();
        _globalCurrentCalendarMonth = new TSLiveData<>();
        _globalCurrentCalendarDay = new TSLiveData<>();

        _globalCurrentSelectedYear = new TSLiveData<>();
        _globalCurrentSelectedMonth = new TSLiveData<>();
        _globalCurrentSelectedDay = new TSLiveData<>();

        _backup = new TSLiveData<>();
        _backup.setValue(backup);

        setGlobalCurrentSelectedYear(currentYear);
        setGlobalCurrentSelectedMonth(currentMonth);
        setGlobalCurrentSelectedDay(currentDay);
        setGlobalCurrentCalendarYear(currentYear);
        setGlobalCurrentCalendarMonth(currentMonth);
        setGlobalCurrentCalendarDay(currentDay);
        setCurrDaysArrayOfMonthObj(list);

    }

    private static void initVariables(){
    }


    public static class InsertDay extends AsyncTask<DayClass,Void,Void> {

        @Override
        protected Void doInBackground(DayClass... dayClasses) {
            int len = dayClasses.length;
            for(int i=0; i < len ;i++){
                RootRepository.getCalendarDayDAO().insertDay(dayClasses[i]);
            }

            return null;
        }
    }

    public static  class DeleteDays extends AsyncTask<DayClass,Void,Void> {

        @Override
        protected Void doInBackground(DayClass... dayClasses) {
            int len = dayClasses.length;
            for(int i=0; i < len ;i++){
                RootRepository.getCalendarDayDAO().deleteDay(dayClasses[i]);
            }
            return null;
        }
    }


    public static  class GetDayByDay extends AsyncTask<DayClass,Void,DayClass> {

        @Override
        protected DayClass doInBackground(DayClass... dayClasses) {
            DayClass day = dayClasses[0];
                RootRepository.getCalendarDayDAO().getDaysByDay(
                    day.year,day.month,day.day
                );
            return day;
        }
    }



    public static int getGlobalCurrentCalendarYear(){
        return _globalCurrentCalendarYear.getValue();
    }
    public static int getGlobalCurrentCalendarMonth(){
        return _globalCurrentCalendarMonth.getValue();
    }
    public static int getGlobalCurrentCalendarDay(){
        return _globalCurrentCalendarDay.getValue();
    }



    public static void setGlobalCurrentCalendarYear(int year){
        _globalCurrentCalendarYear.setValue(year);
    }
    public static void setGlobalCurrentCalendarMonth(int month){
        _globalCurrentCalendarMonth.setValue(month);
    }
    public static void setGlobalCurrentCalendarDay(int day){
        _globalCurrentCalendarDay.setValue(day);
    }

    public static void setGlobalCurrentSelectedYear(int year){
        _globalCurrentSelectedYear.setValue(year);
    }
    public static void setGlobalCurrentSelectedMonth(int month){
        _globalCurrentSelectedMonth.setValue(month);
    }
    public static void setGlobalCurrentSelectedDay(int day){
        _globalCurrentSelectedDay.setValue(day);
    }
    public static int getGlobalCurrentSelectedYear(){
        return _globalCurrentSelectedYear.getValue();
    }
    public static int getGlobalCurrentSelectedMonth(){
        return _globalCurrentSelectedMonth.getValue();
    }
    public static int getGlobalCurrentSelectedDay(){
        return _globalCurrentSelectedDay.getValue();
    }

    public void setCurrDaysArrayOfMonthObj(ArrayList<TSLiveData<DayClass>> list){
        this._daysOfCurrMonth.setValue(list);
    }

    public static TSLiveData<Integer> getLiveGlobalMonth(){
        return _globalCurrentCalendarMonth;
    }
    public static TSLiveData<ArrayList<TSLiveData<DayClass>>> getLiveGlobalDaysList(){
        return _daysOfCurrMonth;
    }

    /*TODO
       나중에, YMD 를 변경하여 이용시 LiveData 와 연동이 안 되기 때문에
       오류 시 체크 / 조만간 LiveData 와 연동작업 해야함
    */
    public static YMD getGlobalCurrentYMD(){
        return new YMD(getGlobalCurrentCalendarYear(),
                getGlobalCurrentCalendarMonth(),
                getGlobalCurrentCalendarDay());
    }
    public static YMD getGlobalSelectedYMD(){
        return new YMD(getGlobalCurrentSelectedYear(),
                getGlobalCurrentSelectedMonth(),
                getGlobalCurrentSelectedDay());
    }

    private static void setListOfDays(ArrayList<TSLiveData<DayClass>> list) {
        CalendarRepository.get().setCurrDaysArrayOfMonthObj(list);
    }

    public static void generateDaysListByDate(int year, int month) {
        ArrayList<TSLiveData<DayClass>> list = new ArrayList<>();

        int lastDayOfLastMonth = getLastDay(year, month - 1);
        int lastDayOfThisMonth = getLastDay(year, month);
        int totalDaysInCalender = 42;
        int startDay = getStartDayOfMonth(year, month);

        Log.e("달력생성",
                "!현재 달 : " + year + ",  현재 달 : " + month +
                        ",  lastDayOfLastMonth : " + lastDayOfLastMonth + ",  " +
                        "lastDayOfThisMonth : " + lastDayOfThisMonth + ",  " +
                        "totalDaysInCalendar : " + totalDaysInCalender +
                        ",  startDay " + startDay);

        TSLiveData<DayClass> day;
        for (int i = startDay; i <= lastDayOfLastMonth; i++) {
            day = new TSLiveData<>();
            YMD prevMonthYMD = new YMD(year, month, i).prevMonth();
            day.setValue(new DayClass());
            day.getValue().setYear(prevMonthYMD.getYear());
            day.getValue().setMonth(prevMonthYMD.getMonth()); // TODO 연도 바뀜 처리
            day.getValue().setDay(i);

            list.add(day);
        }

        for (int i = 1; i <= lastDayOfThisMonth; i++) {
            day = new TSLiveData<>();
            YMD currentMonthYMD = new YMD(year, month, i);
            day.setValue(new DayClass());
            day.getValue().setYear(currentMonthYMD.getYear());
            day.getValue().setMonth(currentMonthYMD.getMonth());
            day.getValue().setDay(i);
            list.add(day);
        }

        for (int i = 1;i <= CalendarUtil.getLastVisibleDayOfNextMonth(year, month); i++) {
            day = new TSLiveData<>();
            YMD nextMonthYMD = new YMD(year, month, i).nextMonth();
            day.setValue(new DayClass());
            day.getValue().setYear(nextMonthYMD.getYear());
            day.getValue().setMonth(nextMonthYMD.getMonth());
            day.getValue().setDay(i);
            list.add(day);
        }

        setListOfDays(list);
        setCurrentMonthPlanList(new MonthPlanList());
    }

    public static void refreshCalendar() {
        generateDaysListByDate(getGlobalCurrentCalendarYear(),
                getGlobalCurrentCalendarMonth());
        ArrayList<Plan> result = new ArrayList<>();

        try {
            result = new PlanRepository.GetVisiblePlanMonthAt().execute(new YMD(getGlobalCurrentCalendarYear(),
                    getGlobalCurrentCalendarMonth())).get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        MonthPlanList list = new MonthPlanList();

        for (Plan plan : result) {

            int absIndex = convertDateToIndex(
                    getGlobalCurrentCalendarYear(), getGlobalCurrentCalendarMonth(),
                    plan.getYear(),
                    plan.getMonth(), plan.getDay());


            list.get(absIndex).getValue().add(plan);

            ArrayList<Plan> newList = new ArrayList<Plan>();
            newList.add(plan);
            PlanRepository.getCurrentMonthPlanListAt(absIndex).setValue(
                    new DayPlanList(newList));

        }

        PlanRepository.setCurrentMonthPlanList(list);

    }

}
