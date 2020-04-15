package com.example.evingPlanner.ui.main;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
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

import net.danlew.android.joda.JodaTimeAndroid;

import org.joda.time.DateTimeZone;

import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {
    rootViewPagerAdapter cAdapter = new rootViewPagerAdapter(getSupportFragmentManager());
    private CalendarListBinding binding;
    private MainVM model;
    private LockableViewPager vPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        JodaTimeAndroid.init(this);
        DateTimeZone.setDefault(DateTimeZone.forTimeZone(TimeZone.getDefault()));
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        model = ViewModelProviders.of(this).get(MainVM.class);
        binding.setModel(model);
        binding.setLifecycleOwner(this);

        vPager = binding.calendarViewPager;
        vPager.setSwipeable(false);

        setupViewPager(vPager);
        tabLayout = findViewById(R.id.tabs);

        tabLayout.setupWithViewPager(vPager);
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.white));
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_calendar_icon);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_info_icon);
        tabLayout.getTabAt(0).getIcon().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(1).getIcon().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.gray_icon), PorterDuff.Mode.SRC_IN);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.gray_icon), PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                onTabSelected(tab);
            }
        });
        RootRepository.get(getApplicationContext());
        requestReviewConfig();
        databaseTask();
        requestReview();
        RootRepository.initGlobalSetting();
        //showInfoMessage();
        showMessageByGivenSharedPreference("1.2.6", R.string.v1_2_6_title, R.string.v1_2_6_content);
    }

    public void setupViewPager(ViewPager vp) {
        cAdapter.addFragment(new CalendarFragment(), null);
        cAdapter.addFragment(new SettingFragment(), null);

        vp.setAdapter(cAdapter);
    }

    public void databaseTask() {
        //TODO 기본 Plan 등록하는 과정임, 기존의 데이터가 싱크되어있으면 분기처리 해줘야함
        final String DEFAULT_PLAN_TYPE_REGISTERED = "DEFAULT_PLAN_TYPE_REGISTERED";
        SharedPreferences pref = getSharedPreferences(DEFAULT_PLAN_TYPE_REGISTERED, 0);

        if (!pref.getBoolean(DEFAULT_PLAN_TYPE_REGISTERED, false)) {
            PlanType[] planTypeArray = PlanType.getDefaultPlanTypes();
            new PlanTypeRepository.InsertPlanTypes().execute(planTypeArray[0], planTypeArray[1], planTypeArray[2]);
            SharedPreferences.Editor edit = pref.edit();
            edit.putBoolean(DEFAULT_PLAN_TYPE_REGISTERED, true);
            edit.apply();
        }

        final String DEFAULT_CATEGORY_REGISTERED = "DEFAULT_CATEGORY_REGISTERED";
        SharedPreferences categoryPref = getSharedPreferences(DEFAULT_CATEGORY_REGISTERED, 0);

        if (!categoryPref.getBoolean(DEFAULT_CATEGORY_REGISTERED, false)) {
            Category defaultCategory = new Category();
            // 기본 계획임을 구별하기 위해서, uid 를 0으로 설정함.
            defaultCategory.setUid(0);
            defaultCategory.setCategoryName(getResources().getString(R.string.no_category));
            new CategoryRepository.InsertOneCategory().execute(defaultCategory);
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
        RateThisApp.setCallback(new RateThisApp.Callback() {
            @Override
            public void onYesClicked() {
                RateThisApp.stopRateDialog(getApplicationContext());
            }

            @Override
            public void onNoClicked() {
                RateThisApp.stopRateDialog(getApplicationContext());
            }

            @Override
            public void onCancelClicked() {

            }
        });
    }

    private void showInfoMessage() {
        /*
        SharedPreferences pref = getSharedPreferences("WELCOME_INFO", 0);

        if (pref.getInt("counter", 0) == 0) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.plan_remove_reask_dialog)
                    .setTitle("사용 전 안내사항")
                    .setPositiveButton(getResources().getString(R.string.confirm_button), null);

            View child = getLayoutInflater().inflate(R.layout.version_release_dialog, null);
            TextView welcomeMessage = child.findViewById(R.id.welcome_text);
            welcomeMessage.setText(getResources().getString(R.string.welcome_message));

            builder.setView(child);
            AlertDialog dialog = builder.create();
            dialog.show();

            SharedPreferences.Editor edit = pref.edit();
            edit.putInt("counter", 1);
            edit.apply();
        }
        */
    }

    private void showImage() {
        /*
        String FLAG = "FLAG";
        String title = "1.2.1 버전 업데이트";
        SharedPreferences pref = getSharedPreferences("1.3.1", 0);

        if(!pref.getBoolean("1.2.1_key", false)) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.plan_remove_reask_dialog)
                    .setTitle(title)
                    .setPositiveButton(getResources().getString(R.string.confirm_button), null);

            View child = getLayoutInflater().inflate(R.layout.joke, null);
            builder.setView(child);
            AlertDialog dialog = builder.create();
            dialog.show();

            SharedPreferences.Editor edit = pref.edit();
            edit.putBoolean("1.2.1_key", true);
            edit.apply();
        }
         */
    }

    // 메세지를 단 한 번만 보여준다.
    private void showMessageByGivenSharedPreference(String key, int titleRes, int messageRes) {
        String FLAG = "FLAG";
        String title = getResources().getString(titleRes);
        String message = getResources().getString(messageRes);
        String PREF_KEY = key + '_' + FLAG;

        SharedPreferences pref = getSharedPreferences(key, 0);

        if (!pref.getBoolean(PREF_KEY, false)) {

            VersionReleaseDialog dialog = new VersionReleaseDialog(title, message);
            dialog.show(getSupportFragmentManager(), "VERSION_RELEASE_DIALOG");

            SharedPreferences.Editor edit = pref.edit();
            edit.putBoolean(PREF_KEY, true);
            edit.apply();
        }
    }
}

