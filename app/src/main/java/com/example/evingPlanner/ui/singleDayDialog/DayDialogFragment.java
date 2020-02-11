package com.example.evingPlanner.ui.singleDayDialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.core.view.ViewCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.evingPlanner.R;
import com.example.evingPlanner.custom.YMD;
import com.example.evingPlanner.databinding.DayDialogBinding;
import com.example.evingPlanner.domain.Plan;
import com.example.evingPlanner.ui.planDialogs.planCreateDialog.MakePlanDialogFragment;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;


public class DayDialogFragment extends DialogFragment {

    private CalendarDayDetailVM vmodel;
    private DayDialogBinding binding;
    private TextView DayText;
    private TextView newPlanText;
    private FragmentManager fragmentManager;

    public DayDialogFragment(FragmentManager fragmentManager){
        this.fragmentManager = fragmentManager;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        this.binding = DataBindingUtil.inflate(inflater,R.layout.day_dialog,container,false);

        vmodel = ViewModelProviders.of(this).get(CalendarDayDetailVM.class);

        this.binding.setModel(vmodel);
        this.binding.setLifecycleOwner(this);

        this.DayText = this.binding.dayText;
        this.newPlanText = this.binding.newPlanText;
        DayText.setText(String.valueOf(vmodel.getGlobalCurrentCalendarDay()));
        View view = this.binding.getRoot();

        attachListeners();

        return view;
    }

    private void attachListeners(){
        this.newPlanText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment makePlanDialog = new MakePlanDialogFragment();
                FragmentTransaction ft = fragmentManager.beginTransaction();
                makePlanDialog.show(ft,"1234");
                dismiss();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);

        }

        this.DayText = binding.dayText;

        String result = "현재 일 : "+vmodel.getGlobalSelectedDay();
        this.DayText.setText(result);
        observe();

    }

    public void observe(){
        this.vmodel.getLiveCurrentDayPlanList().observe(this, new Observer<ArrayList<Plan>>() {
            @Override
            public void onChanged(ArrayList<Plan> plans) {
                    RecyclerView view = binding.planRecyclerView;
                    RecyclerView.LayoutManager layoutManager =
                            new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                    view.setLayoutManager(layoutManager);
                    DayPlanAdapter adapter = new DayPlanAdapter(plans, getFragmentManager());
                    view.setAdapter(adapter);

            }
        });
    }
}
