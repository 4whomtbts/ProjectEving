package com.example.databinding2.custom.types;

import com.example.databinding2.TSLiveData;
import com.example.databinding2.domain.Plan;

import java.util.ArrayList;

public class DayPlanList extends ArrayList<Plan> {


    @Override
    public String toString() {
        StringBuffer _sb = new StringBuffer();
        for (Plan elem : this) {
            _sb.append(elem);
        }
        return _sb.toString();
    }
}

