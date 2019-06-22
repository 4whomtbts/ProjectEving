package com.example.databinding2.repository;

import android.content.Context;
import android.provider.DocumentsContract;

import androidx.room.Room;

import com.example.databinding2.R;
import com.example.databinding2.model.CalendarContentDatabase;
import com.example.databinding2.model.CalendarDayDAO;

public class RootRepository {

    private static RootRepository repo;
    private static CalendarRepository RepoCalendar;
    private static CalendarContentDatabase appDB;
    public static Context context;

    public RootRepository(Context context){
        this.context  = context;
        getCalendarContentDB();
        getCalendarRepository();
        getCalendarDayDAO();
    }


    public static RootRepository get(Context context){
        if(repo==null){
            repo = new RootRepository(context);
        }
        return repo;
    }

    public CalendarRepository getCalendarRepository(){
        if(RepoCalendar == null){
            RepoCalendar = CalendarRepository.get();
        }
        return RepoCalendar;
    }

    public static CalendarContentDatabase getCalendarContentDB(){
        if(appDB==null){
            appDB = Room.databaseBuilder(RootRepository.context, CalendarContentDatabase.class,"calendar_content_db")
                    .build();
        }
        return appDB;
    }

    public static CalendarDayDAO getCalendarDayDAO(){

        return RootRepository.getCalendarContentDB().getDayDAO();
    }



}
