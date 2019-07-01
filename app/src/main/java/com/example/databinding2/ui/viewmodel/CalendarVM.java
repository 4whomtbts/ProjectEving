package com.example.databinding2.ui.viewmodel;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.example.databinding2.TSLiveData;

public class CalendarVM extends ViewModelProvider.NewInstanceFactory {


    private static TSLiveData<Integer> _sharedCurrentYear;
    private static TSLiveData<Integer> _sharedCurrentMonth;



}
