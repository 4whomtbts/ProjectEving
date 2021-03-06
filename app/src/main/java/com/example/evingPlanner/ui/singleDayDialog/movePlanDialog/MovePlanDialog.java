package com.example.evingPlanner.ui.singleDayDialog.movePlanDialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.evingPlanner.R;
import com.example.evingPlanner.custom.IntegerDatePicker;
import com.example.evingPlanner.databinding.MovePlanBinding;
import com.example.evingPlanner.domain.Plan;

public class MovePlanDialog extends DialogFragment {

    public static final int PUSH_PLAN = 0;
    public static final int PULL_PLAN = 1;
    public boolean wasMoveSucceeded = false;
    private final Plan plan;
    private boolean bundleMode = false;
    private MovePlanDialogDismissListener dismissListener;
    private Class<? extends AbstractMovePlanViewModel> concreteViewModelClass;
    protected MovePlanBinding binding;
    protected AbstractMovePlanViewModel vmodel;

    public MovePlanDialog(int mode, Plan plan) {

        if(mode == PULL_PLAN) {
            concreteViewModelClass = PullPlanViewModel.class;
        }else {

            concreteViewModelClass = PushPlanViewModel.class;
        }

        this.plan = plan;
    }

    @Override
    public void onStart(){
        super.onStart();
        Dialog dialog = getDialog();

        Window window = getDialog().getWindow();
        Point size = new Point();

        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);

        if(dialog != null){
            int width = (int)(size.x * 0.80);
            int height = (int)(size.y * 0.80);
            dialog.getWindow().setLayout(width,height);
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams
                    .SOFT_INPUT_ADJUST_RESIZE);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        this.binding = DataBindingUtil.inflate(inflater, R.layout.move_plan_dialog, container, false);
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

    @Override
    public void onDismiss(DialogInterface dialogInterface) {
        super.onDismiss(dialogInterface);
        if(dismissListener != null) {
            dismissListener.handleDialogDismiss(this);
        }
    }

    public void dismissListener(MovePlanDialogDismissListener dismissListener) {
        this.dismissListener = dismissListener;
    }

    private void attachListeners() {
        this.binding.bundleMove.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked) {
                        bundleMode = true;
                    }
            }
        });

        this.binding.singleMove.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked) {
                        bundleMode = false;
                    }
            }
        });

        this.binding.moveConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                IntegerDatePicker picker = binding.datePicker;

                int year = picker.getYear();
                int month = picker.getMonth();
                int day = picker.getDayOfMonth();
                if(vmodel.isValid(bundleMode, year, month, day)) {

                    if(vmodel.movePlan(bundleMode,year, month, day)) {
                        Toast.makeText(getContext(),R.string.success_move_plan,Toast.LENGTH_LONG).show();
                        wasMoveSucceeded = true;
                        dismiss();
                        return;
                    }

                    Toast.makeText(getContext(),R.string.exception_move_plan,Toast.LENGTH_LONG).show();


                }else {
                    Toast.makeText(getContext(),R.string.invalid_move_plan_date,Toast.LENGTH_LONG).show();

                }

            }
        });
    }
}
