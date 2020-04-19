package com.example.evingPlanner.ui.singleDayDialog.movePlanDialog;

import android.app.Application;

import com.example.evingPlanner.R;
import com.example.evingPlanner.domain.Plan;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

import java.util.ArrayList;

public class PullPlanViewModel extends AbstractMovePlanViewModel{

    public PullPlanViewModel(Application application) {
        super(application);
    }

    private boolean isFirstPlan() {
        return planList.get(0).uid == plan.uid;
    }

    private LocalDateTime getPreviousPlanDate() {
        int position = getItemPosition();
        Plan prevPlan;

        if(position-1>=0) {
            prevPlan = planList.get(position-1);
        }else {
            prevPlan = planList.get(0);
        }

        return new LocalDateTime(prevPlan.year, prevPlan.month, prevPlan.day, 0, 0);
    }

    @Override
    public String getInfoMessage() {
        return getApplication().getString(R.string.pull_plan_msg);
    }

    /**
     * 제한요건
     * 1. 현재 시스템 날짜보다 이전 날짜로는 못 옮길 것
     */
    @Override
    public String getLimitDate() {
        LocalDateTime date;
        if(planList.get(0).uid == plan.uid) {
            date = LocalDateTime.now();
        }else {
            date = getPreviousPlanDate().plusDays(1);
        }

        return getDateFormatted(
                date.getYear(), date.getMonthOfYear(), date.getDayOfMonth());

    }

    @Override
    protected boolean isDateValidInBundleMode(LocalDateTime date) {

        return true;
    }

    @Override
    protected boolean isDateValidInSingleMode(LocalDateTime date) {

        if (getPreviousPlanDate().compareTo(date) >= 0) {
            return false;
        }

        return true;
    }

    @Override
    protected boolean isDateValid(boolean bundleMode, int year, int month, int day) {

        LocalDateTime dateToBeMoved= new LocalDateTime(year, month, day, 0, 0);

        // 같은 날짜로 옮기려는 경우 실패
        if(!dateToBeMoved.isAfter(planDate) && !dateToBeMoved.isBefore(planDate)) {
            return false;
        }

        // 밀어버리고 하는 경우 실패
        if(dateToBeMoved.isAfter(planDate)) {
            return false;
        }

        LocalDateTime now = new LocalDateTime();

        // 시스템 날짜 이전으로 옮기려는 경우 실패
        // 시스템 날짜로는 옮길 수 있으므로 1일을 빼서 비교한다.
        if(dateToBeMoved.isBefore(now.minusDays(1))) {
            return false;
        }

        // 첫 계획이라면, 시스템 날짜이전으로만 안 옮기면 된다.
        if(isFirstPlan()) {
            return true;
        }

        // 이전 계획보다 전의 날짜로 옮기려는 경우 실패
        if(getPreviousPlanDate().isAfter(dateToBeMoved)) {
            return false;
        }

        // 이전 계획날로 옮기려는 경우 실패
        if(!getPreviousPlanDate().isAfter(dateToBeMoved) && !getPreviousPlanDate().isBefore(dateToBeMoved)) {
            return false;
        }

        return bundleMode?isDateValidInBundleMode(dateToBeMoved):
                isDateValidInSingleMode(dateToBeMoved);
    }



}
