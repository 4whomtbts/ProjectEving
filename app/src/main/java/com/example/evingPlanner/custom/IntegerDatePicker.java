package com.example.evingPlanner.custom;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.DatePicker;
import android.widget.NumberPicker;

import java.lang.reflect.Field;

public class IntegerDatePicker extends DatePicker {

    public IntegerDatePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        Field[] fields = DatePicker.class.getDeclaredFields();
        try {
            String[] s = new String[] {"01","02","03","04","05","06","07","08","09","10","11","12"};
            for (Field field : fields) {
                field.setAccessible(true);
                if (TextUtils.equals(field.getName(), "mMonthSpinner")) {
                    NumberPicker monthPicker = (NumberPicker) field.get(this);
                    monthPicker.setDisplayedValues(s);
                }
                if (TextUtils.equals(field.getName(), "mShortMonths")) {
                    field.set(this, s);
                }
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

    }
}
