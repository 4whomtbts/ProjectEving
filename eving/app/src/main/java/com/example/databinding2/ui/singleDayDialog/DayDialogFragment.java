package com.example.databinding2.ui.singleDayDialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.databinding2.R;
import com.example.databinding2.custom.YMD;
import com.example.databinding2.databinding.DayDialogBinding;
import com.example.databinding2.domain.Plan;
import com.example.databinding2.ui.planDialogs.planCreateDialog.MakePlanDialogFragment;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;


public class DayDialogFragment extends DialogFragment {

    private CalendarDayDetailVM vmodel;
    private DayDialogBinding binding;
    private TextView DayText;
    private TextInputEditText DayContentInput;
    private ImageButton DayContentConfirm;
    private FragmentManager fragmentManager;

    public DayDialogFragment(FragmentManager fragmentManager, YMD date){
        this.fragmentManager = fragmentManager;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         if (getArguments() != null) {
         }

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        this.binding = DataBindingUtil.inflate(inflater,R.layout.day_dialog,container,false);

        vmodel = ViewModelProviders.of(this).get(CalendarDayDetailVM.class);

        this.binding.setModel(vmodel);
        this.binding.setLifecycleOwner(this);

        this.DayText = this.binding.dayText;
        this.DayContentConfirm = this.binding.dayContentConfirm;
        DayText.setText(
                String.valueOf(vmodel.getGlobalCurrentCalendarDay()));
        View view = this.binding.getRoot();

        attachListeners();

        return view;
    }


    private void attachListeners(){
        this.DayContentConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment makePlanDialog = new MakePlanDialogFragment(fragmentManager,false);
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
                    DayPlanAdapter adapter = new DayPlanAdapter(plans,getFragmentManager());
                    LinearLayoutManager manager = new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false);
                    view.setLayoutManager(manager);
                    view.setAdapter(adapter);

            }
        });
    }

}
