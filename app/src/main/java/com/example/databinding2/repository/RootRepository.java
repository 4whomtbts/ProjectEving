package com.example.databinding2.repository;

import android.content.Context;

import androidx.room.Room;

import com.example.databinding2.model.CalendarContentDatabase;
import com.example.databinding2.model.CalendarDayDAO;
import com.example.databinding2.model.CalendarPlanDAO;
import com.example.databinding2.model.PlanDatabase;

import static com.example.databinding2.util.Constants.TIMEZONE_SEOUL;

public class RootRepository {

    private static RootRepository repo;
    private static CalendarRepository RepoCalendar;
    private static PlanRepository RepoPlan;
    private static CalendarContentDatabase appDB;
    private static PlanDatabase planDB;
    public static Context context; // TODO  static 접근 위험
    private static String timeZone;

    public RootRepository(Context context){
        this.context  = context;
        timeZone = TIMEZONE_SEOUL;

        getCalendarContentDB();
        getCalendarRepository();
        getCalendarDayDAO();
        getPlanRepostiory();
    }
    public RootRepository(Context context,String zone){
        this(context);
        timeZone = zone;

    }




    public static RootRepository get(){
        return repo;
    }

    public static RootRepository get(Context context){
        if(repo==null){
            repo = new RootRepository(context);
        }
        return repo;
    }

    public static CalendarRepository getCalendarRepository(){
        if(RepoCalendar == null){
            RepoCalendar = CalendarRepository.get();
        }
        return RepoCalendar;
    }

    public static PlanRepository getPlanRepostiory(){
        if(RepoPlan == null){
            RepoPlan = PlanRepository.get();
        }
        return RepoPlan;
    }


    public static CalendarContentDatabase getCalendarContentDB(){
        if(appDB==null){
            appDB = Room.databaseBuilder(RootRepository.context, CalendarContentDatabase.class,"calendar_content_db")
                    .build();
        }
        return appDB;
    }

    public static PlanDatabase getPlanDatabase(){
        if(planDB == null){
            planDB = Room.databaseBuilder(RootRepository.context,PlanDatabase.class,"plan_db")
                    .build();
        }
        return planDB;
    }



    public static CalendarDayDAO getCalendarDayDAO(){

        return RootRepository.getCalendarContentDB().getDayDAO();
    }

    public static CalendarPlanDAO getCalendarPlanDAO(){

        return RootRepository.getPlanDatabase().getPlanDAO();
    }

}
