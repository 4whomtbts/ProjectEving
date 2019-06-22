package com.example.databinding2.ui.adapter;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.databinding2.ui.presenter.CalendarFragment;

import java.util.ArrayList;
import java.util.List;

public class CalendarPageAdapter extends FragmentPagerAdapter   {

    private final List<Fragment> mFragmentList=  new ArrayList<>();
    public CalendarPageAdapter(FragmentManager fm) {
        super(fm);
    }

    public void addFragment(Fragment fragment, String title){
        mFragmentList.add(fragment);
    }
    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return 1;
    }


}
