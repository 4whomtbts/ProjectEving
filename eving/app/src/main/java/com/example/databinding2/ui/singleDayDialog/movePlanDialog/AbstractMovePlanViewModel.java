package com.example.databinding2.ui.singleDayDialog.movePlanDialog;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;

import com.example.databinding2.domain.Plan;
import com.example.databinding2.repository.CalendarRepository;
import com.example.databinding2.repository.PlanRepository;

import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

import java.util.ArrayList;
import java.util.Date;

public abstract class AbstractMovePlanViewModel extends AndroidViewModel {

    AbstractMovePlanViewModel(Application application) {
        super(application);
    }

    private String date;
    @NonNull
    protected Plan plan;
    @NonNull
    protected ArrayList<Plan> planList;
    protected LocalDateTime planDate;
    private int year;
    private int month;
    private int day;
    private int diff;

    abstract protected ArrayList<Plan> getUpdatedPlanList(boolean bundleMode, int diff);

    abstract public String getInfoMessage();

    abstract public String getLimitDate();

    abstract protected boolean isDateValid(final boolean bundleMode, int year, int month, int day);

    protected String getDateFormatted(int year, int month, int day) {
        return new StringBuilder()
                .append(year)
                .append("년 ")
                .append(month)
                .append("월 ")
                .append(day)
                .append("일").toString();
    }

    protected final int getDiffOfDay() {
        LocalDate targetPlanDate = LocalDate.fromDateFields(new Date(this.plan.year, this.plan.month, this.plan.day));
        LocalDate requestedDate = LocalDate.fromDateFields(new Date(this.year, this.month, this.day));
        return Days.daysBetween(targetPlanDate, requestedDate).getDays();
    }

     final boolean isValid(final boolean bundleMode, int year, int month, int day) {
        this.year = year;
        this.month = month + 1 ; // 0 월부터 시작하므로
        this.day = day;
        return isDateValid(bundleMode, this.year, this.month, this.day);
    }

     final boolean movePlan(boolean bundleMode, int year, int month, int day) {

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
        this.plan = plan;
        this.planDate = new LocalDateTime(plan.year, plan.month, plan.day, 0, 0);
        try {
            this.planList = new PlanRepository.GetAllPlanByPlanParentUID().execute(this.plan).get();
        }catch (Exception e) {

        }
    }



}
