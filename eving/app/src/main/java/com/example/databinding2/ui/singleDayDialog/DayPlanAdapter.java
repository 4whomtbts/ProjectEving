package com.example.databinding2.ui.singleDayDialog;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Point;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.databinding2.R;
import com.example.databinding2.databinding.DayPlanItemBinding;
import com.example.databinding2.domain.Plan;
import com.example.databinding2.ui.planDialogs.planEditDialog.clonePlan.EditClonePlanDialogFragment;
import com.example.databinding2.ui.planDialogs.planEditDialog.originalPlan.EditOrgPlanDialogFragment;

import java.util.ArrayList;

public class DayPlanAdapter extends RecyclerView.Adapter {

    private ArrayList<Plan> planList;
    private FragmentManager fragmentManager;
    private boolean prevChecked = false;

    // 데이터 초기화하는 부분에 데이터베이스의 체크카운트를 올리지 않기 위한 플래
    private boolean checkInitiated = false;

    public DayPlanAdapter(ArrayList<Plan> list, FragmentManager fm) {

        this.planList = list;
        this.fragmentManager = fm;
    }

    public void setPlanList(ArrayList<Plan> planList){

        this.planList = planList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final DayPlanItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.day_plan_item,parent,false);

        Display display = parent.getDisplay();
        Point size = new Point();
        display.getSize(size);

        ViewGroup.LayoutParams params = binding.getRoot().getLayoutParams();

        params.width=size.x;
        params.height =size.y/10;
        binding.getRoot().setLayoutParams(params);
        return new DayItemViewHolder(binding,  params.height);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        DayItemViewHolder vh = (DayItemViewHolder)holder;
        DayPlanVM model = new DayPlanVM(planList.get(position),position);
        //vh.binding.textPlanTextView.setText(this.planList.get(position).getTextPlan());
        vh.setViewModel(model);
    }

    @Override
    public int getItemCount() {
        if(this.planList != null){
            return this.planList.size();
        }
        return 0;
    }
    public class DayItemViewHolder extends RecyclerView.ViewHolder{
        DayPlanVM model;
        DayPlanItemBinding binding;
        int height;

        public DayItemViewHolder(@NonNull DayPlanItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            setListeners();

        }
        public DayItemViewHolder(@NonNull DayPlanItemBinding binding, int height) {
            this(binding);
            this.height=height;
        }
        private void setViewModel(DayPlanVM model){
            this.model  = model;
            binding.setModel(model);
            binding.executePendingBindings();
            this.binding.isDoneCheckBox.setChecked(model.isDone());
            this.binding.groupTextView.setText(model.getGroup());
            this.binding.cycleTextView.setText(model.getCycleInfo());
            this.binding.planTitle.setText(model.getTitle());
        }

        @SuppressLint("ClickableViewAccessibility")
        private void setListeners(){

            this.binding.planWrapper.setOnTouchListener(new View.OnTouchListener(){

                @Override
                public boolean onTouch(View view, final MotionEvent motionEvent) {
                    DialogFragment editPlanDialog = null;


                    if(model.isParent()){
                        editPlanDialog = new EditOrgPlanDialogFragment(fragmentManager,model.getPlan());

                        FragmentTransaction ft = fragmentManager.beginTransaction();
                        editPlanDialog.show(ft,"1234");
                    }else{
                        editPlanDialog = new EditClonePlanDialogFragment(fragmentManager,model.getPlan());

                        FragmentTransaction ft = fragmentManager.beginTransaction();
                        editPlanDialog.show(ft,"1234");
                    }
                    fragmentManager.executePendingTransactions();
                    editPlanDialog.getDialog().setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            model.refreshModel();
                            setViewModel(model);
                        }
                    });


                    return false;
                }
            });


          this.binding.isDoneCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
              @Override
              public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                          model.setIsDone(isChecked);

              }
          });
        }

    }
}
