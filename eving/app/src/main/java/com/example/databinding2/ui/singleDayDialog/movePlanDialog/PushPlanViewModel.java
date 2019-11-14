package com.example.databinding2.ui.singleDayDialog.movePlanDialog;

import androidx.databinding.ObservableInt;

import com.example.databinding2.R;
import com.example.databinding2.domain.Plan;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

import java.util.ArrayList;
import java.util.Date;

public class PushPlanViewModel extends AbstractMovePlanViewModel{

    public PushPlanViewModel() {

    }

    @Override
    public String getInfoMessage() {
        return new ObservableInt(R.string.push_plan_msg).toString();
    }

    private boolean isLastPlan() {
        return planList.get(planList.size()-1) == plan;
    }

    //TODO refactoring and debugger, list.get 작동안해서 임시방편으로 사용
    // plan 의 인덱스를 받아
    private int getItemPosition() {

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

    private LocalDateTime getNextPlanDate() {
        int position = getItemPosition();
        Plan nextPlan = planList.get(position+1);

        // 전 날 까지만 되옴므로 minusDays(1);
         return new LocalDateTime(nextPlan.year, nextPlan.month, nextPlan.day, 0, 0);
    }

    @Override
    public String getLimitDate() {

        if(planList.get(planList.size()-1) == plan) {
            return "";
        }


        LocalDateTime date = getNextPlanDate().minusDays(1);

        return getDateFormatted(
                date.getYear(), date.getMonthOfYear(), date.getDayOfMonth());
    }

    @Override
    protected boolean isDateValid(int year, int month, int day) {

        LocalDateTime date = new LocalDateTime(year, month, day, 0, 0);

        if(isLastPlan()) {
            return true;
        }

        if(getNextPlanDate().compareTo(date) < 0) {
            return false;
        }

        if(planDate.compareTo(date) >= 0) {
            return false;
        }

        return true;
    }

    @Override
    public ArrayList<Plan> getUpdatedPlanList(boolean bundleMode, int diff) {

        ArrayList<Plan> newPlanList = new ArrayList<>();

        if(bundleMode) {
            int index = getItemPosition();

            while (index < planList.size()) {

                Plan currentPlan = planList.get(index);

                LocalDateTime date = new LocalDateTime(
                        currentPlan.year, currentPlan.month, currentPlan.day, 0, 0)
                        .plusDays(diff);


                Plan newPlan = currentPlan.setYear(date.getYear())
                        .setMonth(date.getMonthOfYear())
                        .setDay(date.getDayOfMonth());

                newPlanList.add(newPlan);

                index++;

            }
        }else {
            int index = getItemPosition();
            LocalDateTime newDate = planDate.plusDays(diff);
            Plan newPlan = planList.get(index)
                                   .setYear(newDate.getYear())
                                   .setMonth(newDate.getMonthOfYear())
                                   .setDay(newDate.getDayOfMonth());

            newPlanList.addAll(planList);
            newPlanList.set(index, newPlan);
        }

        return newPlanList;
    }


}
