package com.example.evingPlanner;

import android.widget.TextView;

import androidx.databinding.BindingAdapter;

public class TextBindingAdapter {
    @BindingAdapter({"setTextAsPresident"})
    public static void setTextAsPresident(TextView view, String str){
        view.setText(str);
    }
    @BindingAdapter({"setTextByButtonClick"})
    public static void setTextByButtonClick(TextView  view, String str){
        view.setText(str);
    }
}
