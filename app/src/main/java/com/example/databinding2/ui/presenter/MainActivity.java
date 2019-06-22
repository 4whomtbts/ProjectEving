package com.example.databinding2.ui.presenter;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.example.databinding2.custom.LockableViewPager;
import com.example.databinding2.repository.RootRepository;
import com.example.databinding2.ui.adapter.CalendarPageAdapter;
import com.example.databinding2.ui.viewmodel.CalendarListViewModel;
import com.example.databinding2.R;
import com.example.databinding2.databinding.CalendarListBinding;

public class MainActivity extends AppCompatActivity {
    CalendarPageAdapter cAdapter = new CalendarPageAdapter(getSupportFragmentManager());
     private CalendarListBinding binding;
     private int previousState, currentState;
     private CalendarListViewModel model;
     private LockableViewPager vPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        model = ViewModelProviders.of(this).get(CalendarListViewModel.class);
        binding.setModel(model);
        binding.setLifecycleOwner(this);

        RootRepository.get(getApplicationContext());
        vPager = binding.calendarViewPager;
        vPager.setSwipeable(false);

        setupViewPager(vPager);

        if(model != null){
            model.initCalendar();
        }

    }

    public void setupViewPager(ViewPager vp){
        cAdapter.addFragment(new CalendarFragment(),"이전");
        vp.setAdapter(cAdapter);
    }

/*
    */
}
