package com.example.databinding2.ui.singleDayDialog;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.text.Layout;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.databinding2.R;
import com.example.databinding2.custom.types.DayPlanList;
import com.example.databinding2.databinding.DayPlanItemBinding;
import com.example.databinding2.databinding.MovePlanBinding;
import com.example.databinding2.domain.Plan;
import com.example.databinding2.repository.CalendarRepository;
import com.example.databinding2.repository.PlanRepository;
import com.example.databinding2.ui.planDialogs.planEditDialog.clonePlan.EditClonePlanDialogFragment;
import com.example.databinding2.ui.planDialogs.planEditDialog.originalPlan.EditOrgPlanDialogFragment;
import com.example.databinding2.ui.singleDayDialog.movePlanDialog.MovePlanDialog;

import java.util.ArrayList;

import javax.crypto.spec.DESedeKeySpec;

public class DayPlanAdapter extends RecyclerView.Adapter {

    private ArrayList<Plan> planList;
    private FragmentManager fragmentManager;
    private RecyclerView.Adapter parentAdapter = this;

    public DayPlanAdapter(ArrayList<Plan> list, FragmentManager fm) {

        this.planList = list;
        this.fragmentManager = fm;
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

        private void showAllView() {
            this.binding.isDoneCheckBox.setVisibility(View.VISIBLE);
            this.binding.groupTextView.setVisibility(View.VISIBLE);
            this.binding.cycleTextView.setVisibility(View.VISIBLE);
            this.binding.planTitle.setVisibility(View.VISIBLE);
        }

        private void hideAllView() {
            this.binding.isDoneCheckBox.setVisibility(View.GONE);
            this.binding.groupTextView.setVisibility(View.GONE);
            this.binding.cycleTextView.setVisibility(View.GONE);
            this.binding.planTitle.setVisibility(View.GONE);
        }

        @SuppressLint("ClickableViewAccessibility")
        private void setListeners(){

            this.binding.planWrapper.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {

                    if(optionView != null) {
                        return;
                    }

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
                }
            });


          this.binding.isDoneCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
              @Override
              public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                          model.setIsDone(isChecked);

              }
          });

          this.binding.planWrapper.setOnLongClickListener(new View.OnLongClickListener() {
              @Override
              public boolean onLongClick(View v) {

                  Display display = v.getDisplay();
                  Point size = new Point();
                  display.getSize(size);
                  View inflatedView = LayoutInflater.from(v.getContext()).inflate(R.layout.plan_long_click_option,binding.planWrapper,false);
                  optionView = inflatedView;
                  binding.planWrapper.addView(inflatedView);

                  inflatedView.findViewById(R.id.cancel_option).setOnClickListener(cancelOptionOnClickListener);
                  inflatedView.findViewById(R.id.cancel_option_button).setOnClickListener(cancelOptionOnClickListener);
                  inflatedView.findViewById(R.id.delete_plan).setOnClickListener(removePlanOnClickListener);
                  inflatedView.findViewById(R.id.delete_plan_button).setOnClickListener(removePlanOnClickListener);
                  inflatedView.findViewById(R.id.push_plan).setOnClickListener(pushPlanOnClickListener);
                  inflatedView.findViewById(R.id.push_plan_button).setOnClickListener(pushPlanOnClickListener);
                  hideAllView();

                  return true;
              }
          });
        }

        private View optionView;

        private View.OnClickListener cancelOptionOnClickListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                binding.planWrapper.removeView(optionView);
                showAllView();

            }
        };

        private DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:

                        int position = -1;
                        for(int i=0; i < planList.size(); i++) {
                            if(planList.get(i).uid == model.plan.uid) {
                                position = i;
                                break;
                            }
                        }

                        if(position < 0) {
                            return;
                        }

                        model.deleteCurrentPlan();
                        planList.remove(position);
                        binding.planWrapper.removeView(optionView);
                        showAllView();
                        hideAllView();
                        parentAdapter.notifyItemRemoved(position);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:

                        binding.planWrapper.removeView(optionView);
                        showAllView();
                        break;
                }
            }
        };


        private AlertDialog.Builder alertBuilder;

        private void attachAlert(int messageId) {
            alertBuilder.setMessage(messageId).setPositiveButton("예", dialogClickListener)
                    .setNegativeButton("아니요", dialogClickListener).show();

        }

        private View.OnClickListener removePlanOnClickListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(alertBuilder == null) {
                    alertBuilder = new AlertDialog.Builder(v.getContext());
                }

                if(model.plan.isParentPlan()) {
                    attachAlert(R.string.parent_plan_delete_ask_msg);
                }else {
                    attachAlert(R.string.child_plan_delete_ask_msg);
                }
            }
        };

        private View.OnClickListener pushPlanOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MovePlanDialog dialog = new MovePlanDialog(MovePlanDialog.PUSH_PLAN, model.plan);
                FragmentTransaction ft = fragmentManager.beginTransaction();
                dialog.show(ft, "MOVE_PLAN_DIALOG");
            }
        };
    }
}
