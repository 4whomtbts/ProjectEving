package com.example.databinding2.ui.singleDayDialog.dayPlan.planEditDialog;

import com.example.databinding2.domain.Plan;
import com.example.databinding2.ui.viewmodel.CalendarViewModel;

public class EditPlanVM extends CalendarViewModel {

    private Plan thisPlan;
    public EditPlanVM() {   }

    public void setThisPlan(Plan thisPlan){
        this.thisPlan = thisPlan;
    }

    public String getCycleState(){
        return this.thisPlan.getCycleState();
    }

    public String getTitle(){
        return this.thisPlan.getTitle();
    }
    public String getTextPlan(){
        return this.thisPlan.getTextPlan();
    }

}
