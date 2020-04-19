package com.example.evingPlanner.ui.singleDayDialog.movePlanDialog;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.evingPlanner.R;
import com.example.evingPlanner.custom.YMD;
import com.example.evingPlanner.domain.Plan;
import com.example.evingPlanner.repository.CalendarRepository;
import com.example.evingPlanner.repository.PlanRepository;

import org.joda.time.Days;
import org.joda.time.LocalDateTime;

import java.util.ArrayList;
import java.util.Date;

import static com.google.common.base.Preconditions.checkNotNull;

public abstract class AbstractMovePlanViewModel extends AndroidViewModel {

    AbstractMovePlanViewModel(Application application) {
        super(application);
    }

    @NonNull
    protected Plan plan;
    @NonNull
    protected ArrayList<Plan> planList;
    protected LocalDateTime planDate;
    private int year;
    private int month;
    private int day;

    abstract public String getInfoMessage();
    abstract public String getLimitDate();
    abstract protected boolean isDateValidInBundleMode(final LocalDateTime date);
    abstract protected boolean isDateValidInSingleMode(final LocalDateTime date);
    abstract protected boolean isDateValid(final boolean bundleMode, int year, int month, int day);

    protected ArrayList<Plan> getUpdatedPlanList(boolean bundleMode, int diff) {

        ArrayList<Plan> newPlanList = new ArrayList<>();

        if(bundleMode) {
            int index = getItemPosition();

            while (index < planList.size()) {

                Plan currentPlan = planList.get(index);

                LocalDateTime date = new LocalDateTime(
                        currentPlan.year, currentPlan.month, currentPlan.day, 0, 0)
                        .plusDays(diff);


                int updatedYear = date.getYear();
                int updatedMonth = date.getMonthOfYear();
                int updatedDay = date.getDayOfMonth();
                Plan newPlan = currentPlan.setYear(updatedYear)
                        .setMonth(updatedMonth)
                        .setDay(updatedDay)
                        .setYMD(new YMD(updatedYear,  updatedMonth, updatedDay));

                newPlanList.add(newPlan);

                index++;
            }

        }else {
            int index = getItemPosition();

            LocalDateTime newDate = planDate.plusDays(diff);
            int updatedYear = newDate.getYear();
            int updatedMonth = newDate.getMonthOfYear();
            int updatedDay = newDate.getDayOfMonth();

            Plan newPlan = planList.get(index)
                    .setYear(updatedYear)
                    .setMonth(updatedMonth)
                    .setDay(updatedDay)
                    .setYMD(new YMD(updatedYear, updatedMonth, updatedDay));

            newPlanList.addAll(planList);
            newPlanList.set(index, newPlan);
        }

        return newPlanList;
    }

    protected final int getItemPosition() {

        for(int i=0; i < planList.size(); i++) {
            Plan currPlan = planList.get(i);
            if(plan.getYear() == currPlan.getYear()
                    && plan.getMonth() == currPlan.getMonth() &&
                    plan.getDay() == currPlan.getDay()) {
                return i;
            }
        }
        return -1;
    }

    protected final String getDateFormatted(int year, int month, int day) {
        return new StringBuilder()
                .append(getApplication().getResources().getString(R.string.limit_date_template_string))
                .append(year)
                .append("년 ")
                .append(month)
                .append("월 ")
                .append(day)
                .append("일").toString();
    }

    protected final int getDiffOfDay() {
        LocalDateTime targetPlanDate =
                new LocalDateTime(this.plan.year, this.plan.month, this.plan.day, 0, 0, 0);
        LocalDateTime requestedDate =
                new LocalDateTime(this.year, this.month, this.day, 0, 0, 0);

        return Days.daysBetween(targetPlanDate, requestedDate).getDays();
    }

     final boolean isValid(final boolean bundleMode, int year, int month, int day) {
        return isDateValid(bundleMode, year, month+1, day);
    }

     final boolean movePlan(boolean bundleMode, int year, int month, int day) {
         this.year = year;
         this.month = month + 1 ; // 0 월부터 시작하므로
         this.day = day;

        ArrayList<Plan> result = getUpdatedPlanList(bundleMode, getDiffOfDay());

        try {
            new PlanRepository.UpdatePlansByList().execute(result).get();
        }catch (Exception e) {
            return false;
        }

        CalendarRepository.refreshCalendar();
        return true;
    }

     void initViewModel(@NonNull Plan plan) {
        checkNotNull(plan, "plan couldn't be null");

        this.plan = plan;
        this.planDate = new LocalDateTime(plan.year, plan.month, plan.day, 0, 0);
        try {
            this.planList = new PlanRepository.GetAllPlanByPlanParentUID().execute(this.plan).get();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }



}
