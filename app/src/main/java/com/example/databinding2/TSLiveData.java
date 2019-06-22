package com.example.databinding2;

import androidx.databinding.Observable;
import androidx.lifecycle.MutableLiveData;

public class TSLiveData<T> extends MutableLiveData<T> {
    public TSLiveData(){}

    public TSLiveData(T value){
        setValue(value);
    }

    public void g(){

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
