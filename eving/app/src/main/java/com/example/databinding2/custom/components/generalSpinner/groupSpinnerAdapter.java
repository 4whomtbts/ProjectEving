package com.example.databinding2.custom.components.generalSpinner;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.example.databinding2.R;

import java.util.ArrayList;

public class groupSpinnerAdapter extends ArrayAdapter {
    ArrayList<String> spinnerItemList;
    public groupSpinnerAdapter(Context context, ArrayList<String> list) {
        super(context, R.layout.group_spinner_item,list);
        spinnerItemList = new ArrayList<>();
        spinnerItemList.add("전공");
        spinnerItemList.add("교양");
        spinnerItemList.add("수학");
        spinnerItemList.add("경제학");
        spinnerItemList.add("법학");
    }

}
