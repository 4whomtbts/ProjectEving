package com.example.databinding2.util;

import com.example.databinding2.repository.CalendarRepository;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static java.util.Calendar.YEAR;

public class CalendarUtil {

    public static int DAY_IN_MONTH = 42;
    public static GregorianCalendar calendar=  new GregorianCalendar();


    public static int getFirstWeek(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(year,month-1,1);
        return cal.get(Calendar.DAY_OF_WEEK);

    }

    public static int getWeek(int year, int month,int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(year,month,day);
        return cal.get(Calendar.DAY_OF_WEEK);

    }

    public static int getLastDay(int year, int month){

        Calendar cal = Calendar.getInstance();
        cal.set(year,month,0);
        return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    public static int getCurrYear(){
        return CalendarUtil.calendar.get(Calendar.YEAR);
    }
    public static int getCurrMonth(){
        return CalendarUtil.calendar.get(Calendar.MONTH);
    }
    public static int getCurrDay(){
        return CalendarUtil.calendar.get(Calendar.DATE);
    }

}
