package com.example.databinding2.ui.singleDayDialog.movePlanDialog;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.DatePicker;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.example.databinding2.R;
import com.example.databinding2.custom.YMD;
import com.example.databinding2.databinding.MovePlanBinding;
import com.example.databinding2.domain.Plan;

public class MovePlanDialog extends DialogFragment {

    public static final int PUSH_PLAN = 0;
    public static final int PULL_PLAN = 1;
    private final int mode;
    private final Plan plan;
    private boolean bundleMode = false;
    protected MovePlanBinding binding;
    protected AbstractMovePlanViewModel vmodel;


    public MovePlanDialog(int mode, Plan plan) {
        this.mode = mode;
        this.plan = plan;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        this.binding = DataBindingUtil.inflate(inflater, R.layout.move_plan_dialog, container, false);

        Class<? extends AbstractMovePlanViewModel> concreteViewModelClass = PushPlanViewModel.class;

        if(mode == PULL_PLAN) {

        }else {
            concreteViewModelClass = PushPlanViewModel.class;
        }
        this.vmodel = ViewModelProviders.of(this).get(concreteViewModelClass);

        this.binding.setModel(vmodel);
        this.binding.setLifecycleOwner(this);

        vmodel.initViewModel(plan);
        this.binding.movePlanInfoMsg.setText(vmodel.getInfoMessage());
        this.binding.limitText.setText(vmodel.getLimitDate());
        View view = this.binding.getRoot();
        attachListeners();
        return view;
    }

    private void attachListeners() {
        this.binding.bundleMove.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked && binding.bundleMove.isChecked()) {
                    binding.singleMove.setChecked(false);
                }
                bundleMode = isChecked;
            }
        });

        this.binding.singleMove.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked && binding.bundleMove.isChecked()) {
                    binding.bundleMove.setChecked(false);
                }
            }
        });

        this.binding.moveConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePicker picker = binding.datePicker;

                int year = picker.getYear();
                int month = picker.getMonth();
                int day = picker.getDayOfMonth();
                if(vmodel.isValid(year, month, day)) {
                    vmodel.movePlan(bundleMode,year, month, day);
                }
            }
        });
    }


}
