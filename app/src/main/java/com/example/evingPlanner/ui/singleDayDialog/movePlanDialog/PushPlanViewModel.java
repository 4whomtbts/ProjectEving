package com.example.evingPlanner.ui.singleDayDialog.movePlanDialog;

import android.app.Application;
import com.example.evingPlanner.R;
import com.example.evingPlanner.domain.Plan;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

import java.util.ArrayList;

public class PushPlanViewModel extends AbstractMovePlanViewModel{

    public PushPlanViewModel(Application application) {
        super(application);

    }

    private boolean isLastPlan() {
        final Plan lastPlan = planList.get(planList.size()-1);
        return lastPlan.uid == plan.uid;
    }

    private LocalDateTime getNextPlanDate() {
        int position = getItemPosition();
        Plan nextPlan = planList.get(position+1);

        // 전 날 까지만 되옴므로 minusDays(1);
         return new LocalDateTime(nextPlan.year, nextPlan.month, nextPlan.day, 0, 0);
    }

    @Override
    public String getInfoMessage() {
        return getApplication().getString(R.string.push_plan_msg);
    }

    @Override
    public String getLimitDate() {

        if(isLastPlan()) {
            return getApplication().getString(R.string.limit_date_free);
        }

        LocalDateTime date = getNextPlanDate().minusDays(1);

        return getDateFormatted(
                date.getYear(), date.getMonthOfYear(), date.getDayOfMonth());
    }

    @Override
    protected boolean isDateValidInBundleMode(final LocalDateTime dateToBeMoved) {
        return true;
    }

    @Override
    protected boolean isDateValidInSingleMode(final LocalDateTime dateToBeMoved) {

        if(getNextPlanDate().compareTo(dateToBeMoved) <= 0) {
            return false;
        }
        return true;
    }

    @Override
    protected boolean isDateValid(final boolean bundleMode, int year, int month, int day) {

        LocalDateTime requestedDate = new LocalDateTime(year, month, day, 0, 0, 0, 0);
        LocalDateTime planDate = new LocalDateTime(plan.year, plan.month, plan.day, 0, 0,0, 0);

        if(!requestedDate.isBefore(planDate) && !requestedDate.isAfter(planDate)) {
            return false;
        }

        if(bundleMode) {
            // 밀어버리는 기능에서 당기는 날짜를 입력할 경우 종료시킴
            return !planDate.isAfter(requestedDate);
        }

        LocalDateTime date = new LocalDateTime(year, month, day, 0, 0);

        if(date.compareTo(planDate) <= 0) {
            return false;
        }

        if(isLastPlan()) {
            return true;
        }

        return isDateValidInSingleMode(date);
    }



}
