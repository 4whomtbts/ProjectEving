package com.example.evingPlanner.repository;

import android.content.Context;

import androidx.room.Room;

import com.example.evingPlanner.domain.Day;
import com.example.evingPlanner.domain.DayVocaJoin;
import com.example.evingPlanner.domain.planTypes.PlanType;
import com.example.evingPlanner.model.CalendarContentDatabase;
import com.example.evingPlanner.model.CalendarDayDAO;
import com.example.evingPlanner.model.CalendarPlanDAO;
import com.example.evingPlanner.model.CategoryDAO;
import com.example.evingPlanner.model.CategoryDatabase;
import com.example.evingPlanner.model.DayDAO;
import com.example.evingPlanner.model.DayDatabase;
import com.example.evingPlanner.model.DayVocaJoinDAO;
import com.example.evingPlanner.model.DayVocaJoinDatabase;
import com.example.evingPlanner.model.PlanTypeDAO;
import com.example.evingPlanner.model.PlanDatabase;
import com.example.evingPlanner.model.PlanTypeDatabase;
import com.example.evingPlanner.model.VocabularyDAO;
import com.example.evingPlanner.model.VocabularyDatabase;

import java.util.ArrayList;

import static com.example.evingPlanner.util.Constants.TIMEZONE_SEOUL;

public class RootRepository {

    private static RootRepository repo;
    private static CalendarRepository RepoCalendar;
    private static PlanRepository RepoPlan;
    private static CalendarContentDatabase appDB;
    private static PlanTypeRepository RepoPlanType;
    private static SingleDayDialogRepository RepoSingle;
    private static VocabularyRepository RepoVoca;
    private static PlanDatabase planDB;
    private static PlanTypeDatabase planTypeDB;
    private static CategoryDatabase categoryDB;
    private static VocabularyDatabase vocaDB;
    private static DayDatabase dayDB;
    private static DayVocaJoinDatabase dayVocaJoinDB;

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

    public static VocabularyRepository getVocabularyRepository() {
        if (RepoVoca == null) {
            RepoVoca = VocabularyRepository.get();
        }
        return RepoVoca;
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
                    .build();
        }
        return appDB;
    }

    public static PlanDatabase getPlanDatabase(){
        if(planDB == null){
            planDB = Room.databaseBuilder(RootRepository.context,PlanDatabase.class,"plan_db")
                    .addMigrations(PlanDatabase.MIGRATION_1_2)
                    .build();
        }
        return planDB;
    }

    public static PlanTypeDatabase getPlanTypeDatabase(){

        if(planTypeDB == null){
            planTypeDB = Room.databaseBuilder(RootRepository.context,PlanTypeDatabase.class,"plan_type_db")
                    .build();
        }
        return planTypeDB;
    }

    public static VocabularyDatabase getVocaTypeDatabase(){

        if(vocaDB == null){
            vocaDB = Room.databaseBuilder(RootRepository.context,VocabularyDatabase.class,"voca_db")
                    .build();
        }
        return vocaDB;
    }

    public static CategoryDatabase getCategoryDatabase() {

        if(categoryDB == null) {
            categoryDB = Room.databaseBuilder(RootRepository.context, CategoryDatabase.class, "category_db")
                    .build();
        }
        return categoryDB;
    }

    public static DayDatabase getDayDatabase() {

        if (dayDB == null) {
            dayDB = Room.databaseBuilder(RootRepository.context, DayDatabase.class, "day_db")
                    .build();
        }
        return dayDB;
    }

    public static DayVocaJoinDatabase getDayvocaJoinDatabase() {

        if (dayVocaJoinDB == null) {
            dayVocaJoinDB = Room.databaseBuilder(RootRepository.context, DayVocaJoinDatabase.class, "day_voca_join_db")
                    .build();
        }
        return dayVocaJoinDB;
    }

    public static VocabularyDAO  getVocabularyDAO() {
        return RootRepository.getVocaTypeDatabase().getVocaDAO();
    }

    public static DayDAO getDayDAO() {
        return RootRepository.getDayDatabase().getDayDAO();
    }

    public static DayVocaJoinDAO getDayVocaJoinDAO() {
        return RootRepository.getDayvocaJoinDatabase().getDayVocaJoinDAO();
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

    public static CategoryDAO getCategoryDAO() {
        return RootRepository.getCategoryDatabase().getCategoryDAO();
    }

}
