package com.example.databinding2.repository;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.databinding2.TSLiveData;
import com.example.databinding2.custom.Pair;
import com.example.databinding2.custom.YMD;
import com.example.databinding2.domain.DayClass;
import com.example.databinding2.domain.MonthClass;
import com.example.databinding2.domain.Plan;
import com.example.databinding2.ui.adapter.DayPlanAdapter;
import com.example.databinding2.util.CalendarUtil;

import java.lang.reflect.Array;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CalendarRepository {

    private static CalendarRepository Inst;

    private static TSLiveData<MonthClass> _currMonthObj;
//    private static TSLiveData<ArrayList<TSLiveData<MonthClass>>

    // global 에서 현재의 month 에 대한 각각의 일의 livedata

    private static TSLiveData<ArrayList<TSLiveData<DayClass>>> _daysOfCurrMonth;
    private static TSLiveData<Integer> _globalCurrentCalendarYear;
    private static TSLiveData<Integer> _globalCurrentCalendarMonth;
    private static TSLiveData<Integer> _globalCurrentCalendarDay;
    private static TSLiveData<HashMap<Pair<Integer,Integer>,ArrayList<DayClass>>> _backup;
    private static TSLiveData<ArrayList<TSLiveData<Plan>>> _currentPlanList;

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
        _currentPlanList = new TSLiveData<>();
        _daysOfCurrMonth = new TSLiveData<>();
        _globalCurrentCalendarYear = new TSLiveData<>();
        _globalCurrentCalendarMonth = new TSLiveData<>();
        _globalCurrentCalendarDay = new TSLiveData<>();
        _backup = new TSLiveData<>();
        _backup.setValue(backup);

        setGlobalCurrentCalendarYear(CalendarUtil.getCurrYear());
        setGlobalCurrentCalendarMonth(CalendarUtil.getCurrMonth());
        setCurrDaysArrayOfMonthObj(list);
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




    public void setGlobalCurrentCalendarYear(int year){
        _globalCurrentCalendarYear.setValue(year);
    }
    public void setGlobalCurrentCalendarMonth(int month){
        _globalCurrentCalendarMonth.setValue(month);
    }
    public void setGlobalCurrentCalendarDay(int day){
        _globalCurrentCalendarDay.setValue(day);
    }
    public void setDataToBackUp(Pair<Integer,Integer> yearMonthPair, ArrayList<DayClass> dayListOfMonth){
        this._backup.getValue().put(yearMonthPair,dayListOfMonth);
    }
    public void setCurrMonthObj(){

    }
    public void setCurrDaysArrayOfMonthObj(ArrayList<TSLiveData<DayClass>> list){
        this._daysOfCurrMonth.setValue(list);
    }
    public static TSLiveData<Integer> liveGetGlobalCalendarYear(){
        return _globalCurrentCalendarYear;
    }
    public static TSLiveData<Integer> liveGetGlobalCalendarMonth(){
        return _globalCurrentCalendarMonth;
    }
    public static TSLiveData<ArrayList<TSLiveData<DayClass>>> liveGetGlobalDaysArray(){
        return _daysOfCurrMonth;
    }
    public TSLiveData<DayClass> liveGetDayAt(int position){
        return _daysOfCurrMonth.getValue().get(position);
    }
    public static TSLiveData<ArrayList<TSLiveData<Plan>>> getLiveCurrentDayPlanList(){
        return _currentPlanList;
    }
    public static void setLiveCurrentDayPlanList(TSLiveData<ArrayList<TSLiveData<Plan>>> plan){
        _currentPlanList = plan;
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
    public ArrayList<TSLiveData<DayClass>> getCurrDaysArrayReference(){
        return _daysOfCurrMonth.getValue();
    }
    /*
    public ArrayList<DayClass>> getCurrDaysArrayCopy(){
        return _daysOfCurrMonth.getValue()
    }
    */
    public ArrayList<DayClass> getStoredMonthData(int month){
        return _backup.getValue().get(month);
    }



}
