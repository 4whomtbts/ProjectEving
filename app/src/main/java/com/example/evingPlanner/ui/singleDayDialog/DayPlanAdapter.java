package com.example.evingPlanner.ui.singleDayDialog;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.evingPlanner.R;
import com.example.evingPlanner.databinding.DayPlanItemBinding;
import com.example.evingPlanner.databinding.MovePlanBinding;
import com.example.evingPlanner.domain.Plan;
import com.example.evingPlanner.ui.planDialogs.planEditDialog.clonePlan.EditClonePlanDialogFragment;
import com.example.evingPlanner.ui.planDialogs.planEditDialog.originalPlan.EditOrgPlanDialogFragment;
import com.example.evingPlanner.ui.singleDayDialog.movePlanDialog.MovePlanDialog;


import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.util.ArrayList;

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
        return new DayItemViewHolder(parent.getContext(), binding,  params.height);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        DayItemViewHolder vh = (DayItemViewHolder)holder;
        DayPlanVM model = new DayPlanVM(planList.get(position),position);
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
        Context context;
        DayPlanVM model;
        DayPlanItemBinding binding;
        int height;

        public DayItemViewHolder(Context context,  @NonNull DayPlanItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.context = context;
            setListeners();

        }
        public DayItemViewHolder(Context context, @NonNull DayPlanItemBinding binding, int height) {
            this(context, binding);
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
                  inflatedView.findViewById(R.id.pull_plan).setOnClickListener(pullPlanOnClickListener);
                  inflatedView.findViewById(R.id.pull_plan_button).setOnClickListener(pullPlanOnClickListener);
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

        private void moveClickListener(int moveType) {

            /*
            오늘을 기준으로 이전 계획은 밀고 당기기 불가능하게 했었는데
            막상 써보니깐 가능하게 하는게 더 좋아서 주석처리함.
            if(new DateTime(model.plan.year, model.plan.month, model.plan.day,0,0,0,0)
                .isBefore(new DateTime().minusDays(1))) {
                Toast.makeText(context, R.string.invalid_move_attempt, Toast.LENGTH_LONG).show();
                return;
            }
             */
            MovePlanDialog dialog = new MovePlanDialog(moveType, model.plan);
            FragmentTransaction ft = fragmentManager.beginTransaction();
            dialog.show(ft, Integer.toString(moveType));
        }


        //TODO 현재 날짜보다 이전 날짜의 계획일 경우에 무효화해야 함.
        private View.OnClickListener pushPlanOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveClickListener(MovePlanDialog.PUSH_PLAN);
            }
        };

        private View.OnClickListener pullPlanOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveClickListener(MovePlanDialog.PULL_PLAN);
            }
        };
    }
}
