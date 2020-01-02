package com.example.evingPlanner.custom.types;

import java.util.ArrayList;

import static com.example.evingPlanner.util.CalendarUtil.DAY_IN_MONTH;

public class MonthPlanList extends ArrayList<LiveDayPlanList> {

    public MonthPlanList() {
        super(DAY_IN_MONTH);
        for(int i=0; i < DAY_IN_MONTH; i++){
            LiveDayPlanList list = new LiveDayPlanList();
            this.add(list);
        }
    }

    @Override
    public String toString(){
            StringBuffer _sb = new StringBuffer();
            for (LiveDayPlanList elem : this) {
                _sb.append(elem);
            }
            return _sb.toString();
    }
}
