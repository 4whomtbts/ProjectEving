package com.example.evingPlanner.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.evingPlanner.custom.YMD;
import com.example.evingPlanner.domain.Plan;

import java.util.List;

@Dao
public interface CalendarPlanDAO {
    @Insert
    public void insert(Plan... plan);

    @Delete
    public void delete(Plan... plan);

    @Update
    public void update(Plan... plan);

    @Query("DELETE FROM table_plans")
    public void deleteAll();

    /*
    @Query("SELECT *, progress = " +
            "(SELECT uid, progress FROM table_plans AS parent WHERE parent.uid = current_plan.uid) " +
            "FROM table_plans current_plan")
    public List<Plan> selectAll();
    */

    @Query("SELECT progress FROM table_plans WHERE uid = :parentUID")
    public double getProgressByUID(long parentUID);

    @Query("SELECT * FROM table_plans")
    public List<Plan> selectAll();

    @Query("SELECT number_of_done_child FROM table_plans")
    public double getChildDoneCount();

    @Query("SELECT isDone FROM table_plans WHERE uid = :uid")
    public boolean isDone(long uid);

    @Query("SELECT * FROM table_plans WHERE year = :year AND month = :month AND day = :day")
    //public ArrayList<TSLiveData<Plan>> getPlanByDay(int year, int month, int day);
    public List<Plan> getPlanByDay(int year, int month, int day);

    @Query("SELECT * FROM table_plans WHERE year = :year AND month = :month")
    public List<Plan> getPlanByMonthDefault(int year, int month);

    @Query("SELECT * FROM table_plans WHERE parentUID = :parentUID ORDER BY thisCycle ASC")
    public List<Plan> getPlanByParentUID(long parentUID);

    @Query("SELECT * FROM table_plans WHERE uid = :uid")
    public Plan getPlanByUID(long uid);

    @Query("SELECT * FROM table_plans WHERE year = :year AND month = :month ORDER BY day ASC")
    public List<Plan> getPlanByMonth(int year, int month);

    @Query("SELECT * FROM table_plans WHERE year = :year AND month = :month AND day  BETWEEN :left AND :right")
    public List<Plan> getPlanByRangeOfDay(int year, int month, int left, int right);

    @Query("DELETE FROM table_plans WHERE parentUID = :parentUID")
    public void deleteChildrenPlanByParentUID(Long parentUID);

    @Query("UPDATE table_plans SET isDone = :isDone WHERE uid = :uid ")
    public void updateOnePlanCheckState(Long uid , boolean isDone);

    /*
    @Query("UPDATE table_plans SET number_of_done_child = number_of_done_child + :diff, progress = (number_of_done_child/totalCycle)*100 " +
            "WHERE parentUID = :uid")
    public void updateParentProgress(Long uid , int diff);

     */
    @Query("UPDATE table_plans SET number_of_done_child = number_of_done_child + :diff WHERE parentUID = :uid")
    public void updateParentProgress(Long uid , int diff);

    @Query("UPDATE table_plans SET title = :title ,textPlan = :textPlan , isDone = :isDone WHERE uid = :uid")
    public void updateOneUserInputDatas(Long uid,String title, String textPlan, boolean isDone);

    @Query("UPDATE table_plans SET title = :title ,textPlan = :textPlan , isDone = :isDone, group_uid = :groupUid WHERE uid = :uid")
    public void updatePlan(Long uid,String title, String textPlan, long groupUid, boolean isDone);

    @Query("UPDATE table_plans SET year = :year, month = :month, day = :day WHERE uid = :uid")
    public void updatePlanDate(long uid, int year, int month, int day);

    @Query("UPDATE table_plans SET title = :title ,textPlan = :textPlan, group_uid = :groupUid WHERE parentUID = :parentUID")
    public void updatePlanByParentUID(Long parentUID, Long groupUid, String title, String textPlan);


}
