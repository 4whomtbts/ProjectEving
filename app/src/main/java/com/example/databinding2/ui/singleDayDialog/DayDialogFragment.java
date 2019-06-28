package com.example.databinding2.ui.singleDayDialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.example.databinding2.TSLiveData;
import com.example.databinding2.databinding.DayDialogBinding;
import com.example.databinding2.domain.Plan;
import com.example.databinding2.ui.planCreateDialog.MakePlanDialogFragment;
import com.example.databinding2.ui.singleDayDialog.dayPlan.DayPlanAdapter;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;


public class DayDialogFragment extends DialogFragment {

    private CalendarDayDetailVM vmodel;
    private DayDialogBinding binding;
    private TextView DayText;
    private TextInputEditText DayContentInput;
    private Button DayContentConfirm;
    private FragmentManager fragmentManager;
    public DayDialogFragment(FragmentManager fragmentManager){
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
        this.DayContentInput = this.binding.dayContentInput;
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
                DialogFragment makePlanDialog = new MakePlanDialogFragment(fragmentManager);

                FragmentTransaction ft = fragmentManager.beginTransaction();
                makePlanDialog.show(ft,"1234");

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
        this.vmodel.getLivePlanList().observe(this, new Observer<ArrayList<TSLiveData<Plan>>>() {
            @Override
            public void onChanged(ArrayList<TSLiveData<Plan>> tsLiveData) {

            }

        });
        vmodel.getLiveCurrentMonthPlanListAt(vmodel.getListIndexDayAt()).observe(this, new Observer<ArrayList<Plan>>() {
            @Override
            public void onChanged(ArrayList<Plan> plans) {
                    RecyclerView view = binding.planRecyclerView;
                    DayPlanAdapter adapter = (DayPlanAdapter)view.getAdapter();
                    ArrayList<Plan> instList = new ArrayList<>();

                    for(int i=0; i < plans.size(); i++){
                        instList.add(plans.get(i));
                    }

                    if(adapter!=null){
                        adapter.setPlanList(instList);
                    }else{
                        LinearLayoutManager manager = new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false);

                        adapter = new DayPlanAdapter(instList,getFragmentManager());
                        adapter.setPlanList(instList);
                        view.setLayoutManager(manager);
                        view.setAdapter(adapter);

                    }
            }
        });
    }

}
