package com.example.databinding2.domain;


import android.util.Log;

import com.example.databinding2.custom.YMD;

import net.danlew.android.joda.JodaTimeAndroid;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Instant;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class PlanCreator {


    private static String timeZone;
    public static YMD[] getTypicalMode(YMD date,String timeZone){
// 1 4 7 14 30 90 120
        DateTime requestedDate = new DateTime(date.getYear(),date.getMonth(),date.getDay(),9,0).withZone(
                DateTimeZone.forID(timeZone));
        DateTime baseDate = requestedDate;
        int[] op = {2,4,7,14,30,90,120};
        YMD[] result = new YMD[7];
        for(int i=0; i < 7; i++){
            DateTime newDate = baseDate.plusDays(op[i]-1);
            baseDate=requestedDate;

            YMD newYMD = new YMD(newDate.getYear(),newDate.getMonthOfYear(),newDate.getDayOfMonth());
            String test= newDate.getYear()+","+newDate.getMonthOfYear()+","+newDate.getDayOfMonth();
            Log.e("더해진 날짜",test);
            result[i] = newYMD;
        }
        for(int i=0; i < 7; i++){

        }
        return result;
    }


}
