package com.example.evingPlanner.ui.main;

import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.example.evingPlanner.R;
import com.example.evingPlanner.custom.LockableViewPager;
import com.example.evingPlanner.databinding.CalendarListBinding;
import com.example.evingPlanner.repository.PlanRepository;
import com.example.evingPlanner.repository.RootRepository;
import com.example.evingPlanner.ui.rootFragment.CalendarFragment;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {
    rootViewPagerAdapter cAdapter = new rootViewPagerAdapter(getSupportFragmentManager());
     private CalendarListBinding binding;
     private MainVM model;
     private LockableViewPager vPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        model = ViewModelProviders.of(this).get(MainVM.class);
        binding.setModel(model);
        binding.setLifecycleOwner(this);

        vPager = binding.calendarViewPager;
        vPager.setSwipeable(false);

        setupViewPager(vPager);
        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(vPager);

        RootRepository.get(getApplicationContext());

        RootRepository.initGlobalSetting();
        showInfoMessage();
    }

    public void setupViewPager(ViewPager vp){
        cAdapter.addFragment(new CalendarFragment(),getString(R.string.calendar_tab));
        cAdapter.addFragment(new SettingFragment(), getString(R.string.app_info_tab));
        vp.setAdapter(cAdapter);
    }

    private void showInfoMessage() {
        SharedPreferences pref = getSharedPreferences("WELCOME_INFO", 0);

        if(pref.getInt("counter", 0) == 0) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setTitle("사용 전 안내사항")
                    .setPositiveButton(getResources().getString(R.string.confirm_button), null);

            View child = getLayoutInflater().inflate(R.layout.welcome_dialog, null);
            TextView welcomeMessage = child.findViewById(R.id.welcome_text);
            welcomeMessage.setText(getResources().getString(R.string.welcome_message));

            builder.setView(child);
            AlertDialog dialog = builder.create();
            dialog.show();

            SharedPreferences.Editor edit = pref.edit();
            edit.putInt("counter", 1);
            edit.commit();
        }
    }

}
