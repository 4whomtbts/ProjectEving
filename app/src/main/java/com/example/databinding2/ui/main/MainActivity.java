package com.example.databinding2.ui.main;

import android.os.Bundle;
import android.provider.DocumentsContract;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.example.databinding2.R;
import com.example.databinding2.custom.LockableViewPager;
import com.example.databinding2.databinding.CalendarListBinding;
import com.example.databinding2.repository.EnvRepository;
import com.example.databinding2.repository.PlanRepository;
import com.example.databinding2.repository.RootRepository;
import com.example.databinding2.ui.rootFragment.CalendarFragment;

import net.danlew.android.joda.JodaTimeAndroid;

public class MainActivity extends AppCompatActivity {
    rootViewPagerAdapter cAdapter = new rootViewPagerAdapter(getSupportFragmentManager());
     private CalendarListBinding binding;
     private MainVM model;
     private LockableViewPager vPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        RootRepository.get(getApplicationContext());
        new PlanRepository.DeleteAllPlan().execute();

        RootRepository.initGlobalSetting();

        super.onCreate(savedInstanceState);
        JodaTimeAndroid.init(this);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        model = ViewModelProviders.of(this).get(MainVM.class);
        binding.setModel(model);
        binding.setLifecycleOwner(this);

        vPager = binding.calendarViewPager;
        vPager.setSwipeable(false);

        setupViewPager(vPager);

        globalInit();

    }

    public void setupViewPager(ViewPager vp){
        cAdapter.addFragment(new CalendarFragment(),"이전");
        vp.setAdapter(cAdapter);
    }

    private void globalInit(){
        EnvRepository.setTimeZone();

    }

}
