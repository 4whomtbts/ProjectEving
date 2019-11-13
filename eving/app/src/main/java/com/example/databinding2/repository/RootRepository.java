package com.example.databinding2.repository;

import android.content.Context;

import androidx.room.Room;

import com.example.databinding2.domain.planTypes.PlanType;
import com.example.databinding2.model.CalendarContentDatabase;
import com.example.databinding2.model.CalendarDayDAO;
import com.example.databinding2.model.CalendarPlanDAO;
import com.example.databinding2.model.PlanTypeDAO;
import com.example.databinding2.model.PlanDatabase;
import com.example.databinding2.model.PlanTypeDatabase;

import java.util.ArrayList;

import static com.example.databinding2.util.Constants.TIMEZONE_SEOUL;

public class RootRepository {

    private static RootRepository repo;
    private static CalendarRepository RepoCalendar;
    private static PlanRepository RepoPlan;
    private static CalendarContentDatabase appDB;
    private static PlanTypeRepository RepoPlanType;
    private static SingleDayDialogRepository RepoSingle;
    private static PlanDatabase planDB;
    private static PlanTypeDatabase planTypeDB;
    public static Context context; // TODO  static 접근 위험
    private static String timeZone;

    public RootRepository(Context context){
        this.context  = context;
        timeZone = TIMEZONE_SEOUL;

        getCalendarContentDB();
        getCalendarRepository();
        getCalendarDayDAO();
        getPlanRepostiory();
        getPlanTypeRepository();
        getPlanTypeDatabase();
        getSingleDayDialogRepository();


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

    public static void initGlobalSetting(){

        ArrayList<PlanType> existsData = new ArrayList<>();
        try {

            existsData = new PlanTypeRepository.SelectAll().execute().get();

        }catch(Exception e){

        }

        if(existsData.size() == 0){

            new PlanTypeRepository.InsertPlanTypes().execute(PlanType.getDefaultPlanTypes());
        }


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

    public static PlanTypeRepository getPlanTypeRepository(){
        if(RepoPlanType == null){
            RepoPlanType = PlanTypeRepository.get();
        }
        return RepoPlanType;
    }

    public synchronized static SingleDayDialogRepository getSingleDayDialogRepository(){
        if(RepoSingle == null){
            RepoSingle = SingleDayDialogRepository.get();
        }
        return RepoSingle;
    }


    public static CalendarContentDatabase getCalendarContentDB(){
        if(appDB==null){
            appDB = Room.databaseBuilder(RootRepository.context, CalendarContentDatabase.class,"calendar_content_db")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return appDB;
    }

    public static PlanDatabase getPlanDatabase(){
        if(planDB == null){
            planDB = Room.databaseBuilder(RootRepository.context,PlanDatabase.class,"plan_db")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return planDB;
    }

    public static PlanTypeDatabase getPlanTypeDatabase(){

        if(planTypeDB == null){
            planTypeDB = Room.databaseBuilder(RootRepository.context,PlanTypeDatabase.class,"plan_type_db")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return planTypeDB;
    }




    public static CalendarDayDAO getCalendarDayDAO(){

        return RootRepository.getCalendarContentDB().getDayDAO();
    }

    public static CalendarPlanDAO getCalendarPlanDAO(){

        return RootRepository.getPlanDatabase().getPlanDAO();
    }

    public static PlanTypeDAO getCalendarPlanTypeDAO(){
        return RootRepository.getPlanTypeDatabase().getPlanTypeDAO();
    }


}
