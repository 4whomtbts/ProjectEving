package com.example.databinding2.repository;

import static com.example.databinding2.util.Constants.TIMEZONE_SEOUL;

public class EnvRepository {

    private static String timeZone;
    private static EnvRepository repo;

    public EnvRepository getInst(){
        if(repo == null){
            repo = new EnvRepository();
        }
        return repo;
    }

    public static void setTimeZone(String timeZone){
        if(timeZone==null){
            EnvRepository.timeZone = TIMEZONE_SEOUL;
        }else{
            EnvRepository.timeZone = timeZone;
        }
    }
    public static void setTimeZone(){
        setTimeZone(null);
    }

    public static String getTimeZone(){
        return EnvRepository.timeZone;
    }

}
