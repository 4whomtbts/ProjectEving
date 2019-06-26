package com.example.databinding2.ui.viewmodel;

import android.app.admin.DelegatedAdminReceiver;
import android.util.Log;
import android.widget.ArrayAdapter;

import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import com.example.databinding2.TSLiveData;
import com.example.databinding2.custom.Pair;
import com.example.databinding2.domain.DayClass;
import com.example.databinding2.domain.MonthClass;
import com.example.databinding2.repository.CalendarRepository;
import com.example.databinding2.util.CalendarUtil;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;

import static com.example.databinding2.util.CalendarUtil.getFirstWeek;
import static com.example.databinding2.util.CalendarUtil.getLastDay;

public class CalendarMonthVM extends CalendarViewModel {
    private CalendarRepository repo = CalendarRepository.get();
    public TSLiveData<MonthClass> mMonthClass;
    public TSLiveData<ArrayList<DayClass>> mArrDays;
    public TSLiveData<Integer> mYearData;
    public TSLiveData<Integer> mMonth;

    public class TempBackUp extends HashMap<Pair<Integer,Integer>,ArrayList<DayClass>>{}


    private TSLiveData<HashMap<Pair<Integer,Integer>,ArrayList<DayClass>>> TempStore;
    CalendarMonthVM(){
        generateDaysListByDate(repo.getGlobalCurrentCalendarYear(),repo.getGlobalCurrentCalendarMonth());
    }
    private ArrayList<TSLiveData<DayClass>> getArrDaysRef(){
        return repo.getCurrDaysArrayReference();
    }
    private void setTempStore(Pair<Integer,Integer> yearMonthPair, ArrayList<DayClass> dayListOfMonth){
        repo.setDataToBackUp(yearMonthPair,dayListOfMonth);
    }
    private ArrayList<DayClass> getTempStoreAtMonth(int month){
        return repo.getStoredMonthData(month);
}

    private void setListOfDays(ArrayList<TSLiveData<DayClass>> list){
        repo.setCurrDaysArrayOfMonthObj(list) ;
    }


    public void gotoPrevMonth(){
        int month = repo.getGlobalCurrentCalendarMonth();
        if(month==1){

        }else{
            repo.setGlobalCurrentCalendarMonth(month-1);
        }
    }

    public void gotoNextMonth(){
        int month = repo.getGlobalCurrentCalendarMonth();
        int year = repo.getGlobalCurrentCalendarYear();
        if(month==12){
            repo.setGlobalCurrentCalendarYear(year+1);
        }else{
            repo.setGlobalCurrentCalendarMonth(month+1);
        }

        Pair<Integer,Integer> yearMonthPair = new Pair(repo.getGlobalCurrentCalendarYear(),
                repo.getGlobalCurrentCalendarMonth());
        /*TODO
            기조회 데이터 백업할 것인지 안 할 것인지 정하기
            repo.setDataToBackUp(yearMonthPair,repo.getCurrDaysArrayReference());
         */

    }



    public MonthClass getMonthObject(){
        return this.mMonthClass.getValue();
    }

    private void generateDaysListByDate(int year, int month){
        ArrayList<TSLiveData<DayClass>> list = new ArrayList<>();

        int firstWeek = getFirstWeek(year,month);
        int lastDayOfLastMonth = getLastDay(year,month-1);
        int lastDayOfThisMonth = getLastDay(year,month);
        int totalDaysInCalender =
                (firstWeek-1)+lastDayOfLastMonth;



        int startDay = lastDayOfLastMonth-firstWeek+1;

        Log.e("달력생성",
                "현재 달 : "+year+",  현재 달 : "+month+
                ",  lastDayOfLastMonth : "+lastDayOfLastMonth+",  " +
                        "lastDayOfThisMonth : "+lastDayOfThisMonth+",  " +
                        "totalDaysInCalendar : "+totalDaysInCalender+
                ",  startDay "+startDay);

        if(startDay == lastDayOfLastMonth){
            for(int i=1; i <= totalDaysInCalender; i++){

                //TSLiveData<DayClass> day = new DayClass(i);
                TSLiveData<DayClass> day = new TSLiveData<>();
                day.getValue().setDay(i);
                day.setValue(new DayClass());
                list.add(day);
            }
        }else {
            startDay++;
            TSLiveData<DayClass> day;
            for (int i = startDay; i <= lastDayOfLastMonth; i++) {
                day = new TSLiveData<>();
                day.setValue(new DayClass());
                day.getValue().setDay(i);
                list.add(day);
            }

            for (int i = 1; i <= lastDayOfThisMonth; i++) {
                day = new TSLiveData<>();
                day.setValue(new DayClass());
                day.getValue().setDay(i);
                list.add(day);
            }
        }
        setListOfDays(list);
    }
    public void initCalendar(){
        generateDaysListByDate(repo.getGlobalCurrentCalendarYear(),
                repo.getGlobalCurrentCalendarMonth());
    }







}
