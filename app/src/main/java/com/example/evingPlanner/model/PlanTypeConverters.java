package com.example.evingPlanner.model;

import androidx.room.TypeConverter;

import com.example.evingPlanner.custom.YMD;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlanTypeConverters {


    @TypeConverter
    public static String fromYMDToString(YMD ymd){
        StringBuilder sb = new StringBuilder();
        sb.append(ymd.getYear());
        sb.append('-');
        sb.append(ymd.getMonth());
        sb.append('-');
        sb.append(ymd.getDay());
        return sb.toString();
    }

    @TypeConverter
    public static YMD fromYMDToString(String value){
        String[] arr= null;
        arr  = value.split("-");


        YMD newYMD = new YMD(
                Integer.parseInt(arr[0]),
                Integer.parseInt(arr[1]),
                Integer.parseInt(arr[2])
        );
        return newYMD;
    }


    @TypeConverter
    public static String fromArrayList(ArrayList<Integer> list){
        StringBuffer converted = new StringBuffer();
        for( int elem : list) {
            converted.append(elem);
            converted.append(',');
        }
        return converted.toString();
    }

    @TypeConverter
    public static ArrayList<Integer> ArrayListToString(String strCycle){
        ArrayList<Integer> list = new ArrayList<>();
        int len = strCycle.length();
        int numberOfCycleNumber = len-1;

        for(int i=0; i < numberOfCycleNumber; i++){
            char cycleChar = strCycle.charAt(i);
            String cycleString = Character.toString(cycleChar);
            int cycleElem = Integer.parseInt(cycleString);
            list.add(cycleElem);
        }
        return list;
    }

    @TypeConverter
    public static String fromStringArray(String[] arr){
        StringBuffer converted = new StringBuffer();
        for(String elem : arr) {
            converted.append(elem);
            converted.append("@separator@");
        }
        return converted.toString();
    }

    @TypeConverter
    public static String[] toStringArray(String storedSuggestion){
        String[] resultArr = new String[10];
        resultArr = storedSuggestion.split("@@separator");
        return resultArr;
    }

    @TypeConverter
    public static String fromIntArrayToString(int[] arr){
        StringBuffer converted = new StringBuffer();
        for( int elem : arr) {
            converted.append(elem);
            converted.append(',');
        }
        return converted.toString();
    }

    @TypeConverter
    public static int[] toIntArray(String storedCycleArray){
        Pattern p = Pattern.compile("\\d+");
        Matcher m = p.matcher(storedCycleArray);
        ArrayList<Integer> tempList = new ArrayList<>();

        while(m.find()) {
            tempList.add(Integer.parseInt(m.group()));
        }

        int[] resultArray = new int[tempList.size()];
        for(int i=0; i< tempList.size(); i++) {
            resultArray[i] = tempList.get(i);
        }
        return resultArray;
    }

}
