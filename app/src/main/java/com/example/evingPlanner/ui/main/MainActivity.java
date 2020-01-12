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
import com.example.evingPlanner.domain.Category;
import com.example.evingPlanner.domain.planTypes.PlanType;
import com.example.evingPlanner.repository.CategoryRepository;
import com.example.evingPlanner.repository.PlanTypeRepository;
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
        databaseTask();
        requestReview();
        RootRepository.initGlobalSetting();
        showInfoMessage();
        showMessageByGivenSharedPreference("1.2.0", R.string.v1_2_0_title, R.string.v1_2_0_content);
    }

    public void setupViewPager(ViewPager vp){
        cAdapter.addFragment(new CalendarFragment(),getString(R.string.calendar_tab));
        cAdapter.addFragment(new InfoFragment(), getString(R.string.app_info_tab));
        vp.setAdapter(cAdapter);
    }

    public void databaseTask() {
        //TODO 기본 Plan 등록하는 과정임, 기존의 데이터가 싱크되어있으면 분기처리 해줘야함
        final String DEFAULT_PLAN_TYPE_REGISTERED = "DEFAULT_PLAN_TYPE_REGISTERED";
        SharedPreferences pref = getSharedPreferences(DEFAULT_PLAN_TYPE_REGISTERED, 0);

        if(!pref.getBoolean(DEFAULT_PLAN_TYPE_REGISTERED, false)) {
            PlanType[] planTypeArray = PlanType.getDefaultPlanTypes();
            new PlanTypeRepository.InsertPlanTypes().execute(planTypeArray[0], planTypeArray[1], planTypeArray[2]);
            SharedPreferences.Editor edit = pref.edit();
            edit.putBoolean(DEFAULT_PLAN_TYPE_REGISTERED, true);
            edit.apply();
        }

        final String DEFAULT_CATEGORY_REGISTERED = "DEFAULT_CATEGORY_REGISTERED";
        SharedPreferences categoryPref = getSharedPreferences(DEFAULT_CATEGORY_REGISTERED, 0);

        if( categoryPref.getBoolean(DEFAULT_CATEGORY_REGISTERED, false)) {
            Category defaultCategory = new Category();
            defaultCategory.setCategoryName(getString(R.string.no_category));
            new CategoryRepository.InsertOneCategory().execute(new Category());
            SharedPreferences.Editor edit = categoryPref.edit();
            edit.putBoolean(DEFAULT_CATEGORY_REGISTERED, true);
            edit.apply();
        }
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
        RateThisApp.showRateDialogIfNeeded(this);
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

    // 메세지를 단 한 번만 보여준다.
    private void showMessageByGivenSharedPreference(String key, int titleRes, int messageRes) {
        String FLAG = "FLAG";
        String title = getResources().getString(titleRes);
        String message = getResources().getString(messageRes);
        String PREF_KEY = key + '_' + FLAG;

        SharedPreferences pref = getSharedPreferences(key, 0);

        if(!pref.getBoolean(PREF_KEY, false)) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setTitle(title)
                    .setPositiveButton(getResources().getString(R.string.confirm_button), null);

            View child = getLayoutInflater().inflate(R.layout.welcome_dialog, null);
            TextView welcomeMessage = child.findViewById(R.id.welcome_text);
            welcomeMessage.setText(message);

            builder.setView(child);
            AlertDialog dialog = builder.create();
            dialog.show();

            SharedPreferences.Editor edit = pref.edit();
            edit.putBoolean(PREF_KEY, true);
            edit.apply();

        }
    }
}
