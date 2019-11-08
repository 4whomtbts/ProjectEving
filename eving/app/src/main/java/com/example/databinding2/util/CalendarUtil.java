package com.example.databinding2.util;

import com.example.databinding2.custom.YMD;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class CalendarUtil {

    public static int DAY_IN_MONTH = 42;
    public static GregorianCalendar calendar = new GregorianCalendar();


    public static int getLastDayOfLastMonth(int year, int month) {
        if (month == 1) {
            return getLastDay(year - 1, 12);
        }
        return getLastDay(year, month - 1);
    }

    public static int getLastDayOfLastMonth(YMD ymd) {
        return getLastDayOfLastMonth(ymd.getYear(), ymd.getMonth());
    }

    public static int getFirstWeek(int year, int month) {
        Calendar cal = Calendar.getInstance();
        if (month != 1) {
            cal.set(year, month - 1, 1);
        } else {
            cal.set(year - 1, 12, 1);
        }
        return cal.get(Calendar.DAY_OF_WEEK);
    }

    public static int getFirstWeek(YMD ymd) {
        return getFirstWeek(ymd.getYear(), ymd.getMonth());
    }


    public static int getLastDay(int year, int month) {

        Calendar cal = Calendar.getInstance();
        cal.set(year, month, 0);
        return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    public static int getLastDay(YMD ymd) {
        return getLastDay(ymd.getYear(), ymd.getMonth());
    }

    public static int getWeek(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day);
        return cal.get(Calendar.DAY_OF_WEEK);

    }

    public static int getLastDayOfMonth(int year, int month) {
        return getLastDay(year, month);
    }

    public static int getLastVisibleDayOfNextMonth(int year, int month) {

        if (getFirstWeek(year, month) == 1) {
            return DAY_IN_MONTH - (7 + getLastDay(year, month));
        }

        return DAY_IN_MONTH - (getFirstWeek(year, month) - 1 + getLastDayOfMonth(year, month));
    }

    public static int getLastVisibleDayOfNextMonth(YMD ymd) {
        return getLastVisibleDayOfNextMonth(ymd.getYear(), ymd.getMonth());
    }


    // 논리적인 달력에서 포함되는 날인가?
    public static boolean isInRangeOfMonth(int year, int month, int day) {
        //  LocalDate endOfMonth = new LocalDate(year,month,0).dayOfMonth().withMaximumValue();
        if (day > getLastDay(year, month)) {
            return false;
        }
        return true;
    }

    public static int getFirstDayOfLastMonth(int year, int month) {
        if (getFirstWeek(year, month) == 1) {
            return getLastDayOfLastMonth(year, month) - 7 + 1;
        } else {
            return getLastDayOfLastMonth(year, month) - getFirstWeek(year, month) + 2;
        }
    }

    public static int getFirstDayOfLastMonth(YMD ymd) {
        return getFirstDayOfLastMonth(ymd.getYear(), ymd.getMonth());
    }

    public static int getTotalVisibleDayOfLastMonth(int year, int month) {
        if (getFirstWeek(year, month) == 1) {
            return 7;
        } else {
            return DAY_IN_MONTH - (getLastVisibleDayOfNextMonth(year, month) + getLastDay(year, month));
        }
    }

    public static int getTotalVisibleDayOfLastMonth(YMD ymd) {
        return getTotalVisibleDayOfLastMonth(ymd.getYear(), ymd.getMonth());
    }

    // 캘린더 view 내에 포함되는 날인가?

    public static boolean isInRangeOfMonthInCalendar(int year,
                                                     int calendarMonth,
                                                     int askedYear,
                                                     int askedMonth,
                                                     int day) {

        if (Math.abs(year - askedYear) > 1) {
            return false;
        }


        if (year != askedYear && (Math.abs(calendarMonth - askedMonth) != 11)) {
            return false;
        }


        if (    year == askedYear &&
                calendarMonth == askedMonth && day <= getLastDay(askedYear,askedMonth)) {
            return true;

        } else {


            if ((year == askedYear && calendarMonth + 1 == askedMonth) ||
                    (year + 1 == askedYear && askedMonth == 1)) {

                int lastDay = getLastVisibleDayOfNextMonth(year, calendarMonth);
                if (lastDay >= day) {
                    return true;
                }
                return false;
            }

            if ((year == askedYear && calendarMonth - 1 == askedMonth) ||
                    (year - 1 == askedYear && askedMonth == 12)) {
                int firstDay = getFirstDayOfLastMonth(year, calendarMonth);
                if (firstDay <= day && day <= getLastDay(askedYear, askedMonth)) {
                    return true;
                }
                return false;
            }
        }
        return false;
    }


    /**
     * @param currMonth  현재 view의 월
     * @param askedMonth 질의 월
     */
    public static int convertDateToIndex1(int year, int currMonth,int askedMonth, int day) {

        if (currMonth - 1 == askedMonth || currMonth == 1 && askedMonth == 12) {
            int zeroIndex = getFirstDayOfLastMonth(year, currMonth);
            return day - zeroIndex;
        } else if (currMonth == askedMonth) {
            int lastIndexOfCurrMonth = getLastDayOfLastMonth(year, currMonth)
                    - getFirstDayOfLastMonth(year, currMonth);
            return lastIndexOfCurrMonth + day;
        } else if (currMonth + 1 == askedMonth || currMonth == 12 && askedMonth == 1) {
            return getTotalVisibleDayOfLastMonth(year, currMonth) +
                    getLastDay(year, currMonth) + day - 1;


        } else {
            return -1;
        }
    }


    public static int convertDateToIndex(int year, int currMonth, int askedYear, int askedMonth, int day) {

        if ( (year == askedYear && askedMonth + 1 == currMonth) ||
                (year == askedYear+1 && askedMonth == 12)){
            return day - getFirstDayOfLastMonth(year,currMonth);
        }else if(year == askedYear && askedMonth == currMonth ){

            int firstWeek = getFirstWeek(year,currMonth);
            int startIndexOfCurrentMonthDay = firstWeek==1?(7):(firstWeek-1);
            return startIndexOfCurrentMonthDay + (day-1);

        }else if((year == askedYear && askedMonth - 1 == currMonth) ||
                (year == askedYear -1 && askedMonth == 1)) {

            if(day > getLastVisibleDayOfNextMonth(year,currMonth)){
                return -1;
            }
            int indexBeforeNextMonth =
                    getFirstWeek(year,currMonth) + getTotalDayOfMonth(year,currMonth)-2 ;
            return indexBeforeNextMonth + day;
        }else{
            return -1;
        }

    }

    public static int getTotalDayOfMonth(int year, int month){
        return new GregorianCalendar(year,month-1,1).getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    public static int getCurrYear() {
        return CalendarUtil.calendar.get(Calendar.YEAR);
    }

    public static int getCurrMonth() {
        return CalendarUtil.calendar.get(Calendar.MONTH);
    }

    public static int getCurrDay() {
        return CalendarUtil.calendar.get(Calendar.DATE);
    }

}
