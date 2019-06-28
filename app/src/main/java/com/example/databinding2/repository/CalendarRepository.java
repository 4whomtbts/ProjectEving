package com.example.databinding2.repository;

import android.os.AsyncTask;

import com.example.databinding2.TSLiveData;
import com.example.databinding2.custom.Pair;
import com.example.databinding2.custom.YMD;
import com.example.databinding2.domain.DayClass;
import com.example.databinding2.domain.MonthClass;
import com.example.databinding2.util.CalendarUtil;

import java.util.ArrayList;
import java.util.HashMap;

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
        setGlobalCurrentSelectedYear(CalendarUtil.getCurrYear());
        setGlobalCurrentSelectedMonth(CalendarUtil.getCurrMonth());
        setGlobalCurrentSelectedDay(CalendarUtil.getCurrDay());
        setGlobalCurrentCalendarYear(CalendarUtil.getCurrYear());
        setGlobalCurrentCalendarMonth(CalendarUtil.getCurrMonth());
        setGlobalCurrentCalendarDay(CalendarUtil.getCurrDay());
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
    public static TSLiveData<Integer> getLiveGlobalYear(){
        return _globalCurrentCalendarYear;
    }
    public static TSLiveData<Integer> getLiveGlobalMonth(){
        return _globalCurrentCalendarMonth;
    }
    public static TSLiveData<ArrayList<TSLiveData<DayClass>>> getLiveGlobalDaysList(){
        return _daysOfCurrMonth;
    }
    public TSLiveData<DayClass> liveGetDayAt(int position){
        return _daysOfCurrMonth.getValue().get(position);
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
    public ArrayList<TSLiveData<DayClass>> getCurrDaysArrayReference(){
        return _daysOfCurrMonth.getValue();
    }

    public ArrayList<DayClass> getStoredMonthData(int month){
        return _backup.getValue().get(month);
    }





}
