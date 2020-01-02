package com.example.evingPlanner;

import androidx.lifecycle.MutableLiveData;

public class TSLiveData<T> extends MutableLiveData<T> {
    public TSLiveData(){}

    public TSLiveData(T value){
        setValue(value);
    }

    public void set(T value){
        super.setValue(value);

    }
    /*
    Observable.OnPropertyChangedCallback callback
            = new Observable.OnPropertyChangedCallback() {
        @Override
        public void onPropertyChanged(Observable sender, int propertyId) {

        }
    }
    */
}
