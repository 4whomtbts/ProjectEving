package com.example.evingPlanner.ui.main;

import android.content.SharedPreferences;
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
import com.example.evingPlanner.repository.RootRepository;
import com.example.evingPlanner.ui.rootFragment.CalendarFragment;
import com.google.android.material.tabs.TabLayout;
import com.kobakei.ratethisapp.RateThisApp;

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
        requestReviewConfig();
        requestReview();
        RootRepository.initGlobalSetting();
        showInfoMessage();
        requestReview();
    }

    public void setupViewPager(ViewPager vp){
        cAdapter.addFragment(new CalendarFragment(),getString(R.string.calendar_tab));
        cAdapter.addFragment(new SettingFragment(), getString(R.string.app_info_tab));
        vp.setAdapter(cAdapter);
    }

    private void requestReviewConfig() {
        RateThisApp.Config config = new RateThisApp.Config(7, 4);
        config.setTitle(R.string.review_request_title);
        config.setYesButtonText(R.string.rate_now);
        config.setNoButtonText(R.string.no_thanks);
        config.setCancelButtonText(R.string.remind_me_later);
        config.setMessage(R.string.review_request_message);
        RateThisApp.init(config);

    }
    private void requestReview() {
        RateThisApp.onCreate(this);
        // If the condition is satisfied, "Rate this app" dialog will be shown
        RateThisApp.showRateDialogIfNeeded(this);

        /*
        SharedPreferences reviewRequest = getSharedPreferences("REVIEW_REQUEST", 0);
        SharedPreferences firstUse = getSharedPreferences("FIRST_USE_DATE", 0);
        boolean requested = reviewRequest.getBoolean("REVIEW_REQUEST", false);
        String firstUseDate = firstUse.getString("FIRST_USE_DATE", null);

        if(!requested && (firstUseDate == null)) {
            SharedPreferences.Editor firstUseEdit = firstUse.edit();
            String startDate = DateTime.now().toString("yyyy-MM-dd");
            firstUseEdit.putString("FIRST_USE_DATE",startDate);
            firstUseEdit.apply();
        }

        if(!requested) {
            DateTime now = DateTime.now();
            DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
            DateTime firstUseDateTime = DateTime.parse(firstUseDate, formatter);

            if(Days.daysBetween(now, firstUseDateTime).getDays() > 1) {
                SharedPreferences.Editor reviewRequestEditor = reviewRequest.edit();
                reviewRequestEditor.putBoolean("REVIEW_REQUEST", true);
                reviewRequestEditor.apply();


            }
        }
         */


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
            edit.apply();

        }
    }

}
