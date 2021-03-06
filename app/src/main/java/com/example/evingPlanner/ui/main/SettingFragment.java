package com.example.evingPlanner.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.evingPlanner.R;
import com.example.evingPlanner.databinding.SettingFragmentBinding;

public final class SettingFragment extends Fragment {

    private SettingFragmentBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.setting_fragment, container, false);
        View view = binding.getRoot();
        binding.setLifecycleOwner(this);
        return view;
    }

}
