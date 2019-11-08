package com.example.databinding2.ui.rootFragment;

import android.util.Log;

import com.example.databinding2.TSLiveData;
import com.example.databinding2.custom.Pair;
import com.example.databinding2.custom.YMD;
import com.example.databinding2.custom.types.MonthPlanList;
import com.example.databinding2.domain.DayClass;
import com.example.databinding2.domain.MonthClass;
import com.example.databinding2.domain.Plan;
import com.example.databinding2.repository.CalendarRepository;
import com.example.databinding2.repository.PlanRepository;
import com.example.databinding2.ui.viewmodel.CalendarViewModel;
import com.example.databinding2.util.CalendarUtil;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.databinding2.util.CalendarUtil.convertDateToIndex;
import static com.example.databinding2.util.CalendarUtil.getFirstWeek;
import static com.example.databinding2.util.CalendarUtil.getLastDay;

public class CalendarMonthVM extends CalendarViewModel {
    private CalendarRepository repo = CalendarRepository.get();
    public TSLiveData<MonthClass> mMonthClass;
    public TSLiveData<ArrayList<DayClass>> mArrDays;
    public TSLiveData<Integer> mYearData;
    public TSLiveData<Integer> mMonth;


    private TSLiveData<HashMap<Pair<Integer, Integer>, ArrayList<DayClass>>> TempStore;

    public CalendarMonthVM() {
        generateDaysListByDate(getGlobalCurrentCalendarYear(), getGlobalCurrentCalendarMonth());
    }


    private void setListOfDays(ArrayList<TSLiveData<DayClass>> list) {
        repo.setCurrDaysArrayOfMonthObj(list);
    }

    public void refreshPlanListWhenMonthChanged(int year, int month) {


    }

    void gotoPrevMonth() {
        int month = getGlobalCurrentCalendarMonth();
        int year = getGlobalCurrentCalendarYear();
        if (month == 1) {
            setGlobalCurrentYear(year - 1);
            setGlobalCurrentMonth(12);
        } else {
            setGlobalCurrentMonth(month - 1);
        }
    }

    public void gotoNextMonth() {
        int year = getGlobalCurrentCalendarYear();
        int month = getGlobalCurrentCalendarMonth();
        if (month == 12) {
            setGlobalCurrentYear(year + 1);
        } else {
            setGlobalCurrentMonth(month + 1);
        }

        Pair<Integer, Integer> yearMonthPair = new Pair(getGlobalCurrentCalendarYear(),
                getGlobalCurrentCalendarMonth());
        /*TODO
            기조회 데이터 백업할 것인지 안 할 것인지 정하기
            repo.setDataToBackUp(yearMonthPair,repo.getCurrDaysArrayReference());
         */

    }


    public MonthClass getMonthObject() {
        return this.mMonthClass.getValue();
    }

    private void generateDaysListByDate(int year, int month) {
        ArrayList<TSLiveData<DayClass>> list = new ArrayList<>();

        int daysOfweek = getFirstWeek(year, month);
        int firstWeek = daysOfweek == 1 ? (7) : (daysOfweek);
        int lastDayOfLastMonth = getLastDay(year, month - 1);
        int lastDayOfThisMonth = getLastDay(year, month);
        int totalDaysInCalender = 42;
        int startDay = firstWeek == 7 ? (lastDayOfLastMonth - 6) : (lastDayOfLastMonth - (firstWeek - 2));

        Log.e("달력생성",
                "!현재 달 : " + year + ",  현재 달 : " + month +
                        ",  lastDayOfLastMonth : " + lastDayOfLastMonth + ",  " +
                        "lastDayOfThisMonth : " + lastDayOfThisMonth + ",  " +
                        "totalDaysInCalendar : " + totalDaysInCalender +
                        ",  startDay " + startDay);


        TSLiveData<DayClass> day;
        for (int i = startDay; i <= lastDayOfLastMonth; i++) {
            day = new TSLiveData<>();
            day.setValue(new DayClass());

            day.getValue().setDay(i);

            day.getValue().setMonth(month - 1); // TODO 연도 바뀜 처리
            list.add(day);
        }

        for (int i = 1; i <= lastDayOfThisMonth; i++) {
            day = new TSLiveData<>();
            day.setValue(new DayClass());
            day.getValue().setMonth(month);
            day.getValue().setDay(i);
            list.add(day);
        }

        for (int i = 1;i <= CalendarUtil.getLastVisibleDayOfNextMonth(year, month); i++) {
            TSLiveData<DayClass> nextMonthDay = new TSLiveData<>();
            nextMonthDay.setValue(new DayClass());
            nextMonthDay.getValue().setMonth(month + 1);
            nextMonthDay.getValue().setDay(i);
            list.add(nextMonthDay);
        }

        setListOfDays(list);

    }

    public void initCalendar() {
        generateDaysListByDate(getGlobalCurrentCalendarYear(),
                getGlobalCurrentCalendarMonth());
    }

    public void refreshCalendar() {
        generateDaysListByDate(getGlobalCurrentCalendarYear(),
                getGlobalCurrentCalendarMonth());
        ArrayList<Plan> result = null;
        ArrayList<Plan> fullResult = null;

        try {
            result = new PlanRepository.GetVisiblePlanMonthAt().execute(new YMD(getGlobalCurrentCalendarYear(),
                    getGlobalCurrentCalendarMonth())).get();
            fullResult = new PlanRepository.SelectAllPlan().execute().get();
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
        }

        System.out.println(fullResult);


        setCurrentMonthPlanList(list);

    }
}