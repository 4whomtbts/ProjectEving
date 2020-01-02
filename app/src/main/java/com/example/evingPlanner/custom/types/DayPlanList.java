package com.example.evingPlanner.custom.types;

import com.example.evingPlanner.domain.Plan;

import java.util.ArrayList;

public class DayPlanList extends ArrayList<Plan> {


    public DayPlanList(){

    }
    public DayPlanList(ArrayList<Plan> list){
            this.addAll(list);
    }
    @Override
    public String toString() {
        StringBuffer _sb = new StringBuffer();
        for (Plan elem : this) {
            _sb.append(elem);
        }
        return _sb.toString();
    }
}

