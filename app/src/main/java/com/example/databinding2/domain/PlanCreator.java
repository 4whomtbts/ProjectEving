package com.example.databinding2.domain;


import android.util.Log;

import com.example.databinding2.custom.YMD;
import com.example.databinding2.repository.EnvRepository;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

public class PlanCreator {

    private static int[] TYPICAL_MODE = {2,5,8,15,31,91,121};
    private static int[] DENSE_MODE = {2,5,8,15,22,29,41,61,91,121};

    private static int[][] modes = {
            TYPICAL_MODE,DENSE_MODE
    };

    public static YMD[] genericMakePlanByMode(YMD date, int[] mode){
        DateTime requestedDate = new DateTime(date.getYear(),date.getMonth(),date.getDay(),9,0).withZone(
                DateTimeZone.forID(EnvRepository.getTimeZone()));

        DateTime baseDate = requestedDate;

        YMD[] result = new YMD[mode.length];

        for(int i=0; i < mode.length; i++){
            DateTime newDate = baseDate.plusDays(mode[i]-1);
            baseDate=requestedDate;

            YMD newYMD = new YMD(newDate.getYear(),newDate.getMonthOfYear(),newDate.getDayOfMonth());
            Log.e("","계획될 날짜 : "+newDate.getYear()+", "+newDate.getMonthOfYear()+", "+newDate.getDayOfMonth());
            result[i] = newYMD;
        }

        return result;
    }

    public static YMD[] getTypicalMode(YMD date){
    // 1 4 7 14 30 90 120
        return genericMakePlanByMode(date,TYPICAL_MODE);
    }

    // 1 4 7 14 21 28 40 60 90 120
    public static YMD[] getDenseMode(YMD date){
        return genericMakePlanByMode(date,DENSE_MODE);
    }


}
