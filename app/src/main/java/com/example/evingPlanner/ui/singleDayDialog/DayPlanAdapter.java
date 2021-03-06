package com.example.evingPlanner.ui.singleDayDialog;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.evingPlanner.R;
import com.example.evingPlanner.databinding.DayPlanItemBinding;
import com.example.evingPlanner.domain.Category;
import com.example.evingPlanner.domain.Plan;
import com.example.evingPlanner.repository.CategoryRepository;
import com.example.evingPlanner.ui.planDialogs.planEditDialog.clonePlan.EditClonePlanDialogFragment;
import com.example.evingPlanner.ui.planDialogs.planEditDialog.originalPlan.EditOrgPlanDialogFragment;
import com.example.evingPlanner.ui.singleDayDialog.movePlanDialog.MovePlanDialog;
import com.example.evingPlanner.ui.singleDayDialog.movePlanDialog.MovePlanDialogDismissListener;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;

public class DayPlanAdapter extends RecyclerView.Adapter {

    private ArrayList<Plan> planList;
    private FragmentManager fragmentManager;
    private RecyclerView.Adapter parentAdapter = this;
    private FirebaseAnalytics mFirebaseAnalytics;

    public DayPlanAdapter(ArrayList<Plan> list, FragmentManager fm) {
        this.planList = list;
        this.fragmentManager = fm;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final DayPlanItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.day_plan_item,parent,false);
        return new DayItemViewHolder(parent.getContext(), binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        DayItemViewHolder vh = (DayItemViewHolder)holder;
        DayPlanVM model = new DayPlanVM(planList.get(position), position);
        vh.setViewModel(model);
    }

    @Override
    public int getItemCount() {
        if(this.planList != null){
            return this.planList.size();
        }
        return 0;
    }



    public class DayItemViewHolder extends RecyclerView.ViewHolder {
        Context context;
        DayPlanVM model;
        DayPlanItemBinding binding;

        public DayItemViewHolder(Context context,  @NonNull DayPlanItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.context = context;
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
            binding.dayPlanItemOptionButton.setFocusable(false);
            binding.dayPlanItemOptionButton.setFocusableInTouchMode(false);
            setListeners();

        }

        private void setViewModel(DayPlanVM model){
            this.model  = model;
            Category category = new Category();
            try {
                category = new CategoryRepository.SelectOneByUid()
                        .execute(model.getGroupUiD())
                        .get();

            }catch (Exception e) {
                e.printStackTrace();
            }


            binding.setModel(model);
            binding.executePendingBindings();
            this.binding.isDoneCheckBox.setChecked(model.isDone());
            this.binding.groupTextView.setText(category.categoryName);
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
                  Bundle bundle = new Bundle();
                  bundle.putString("plan_complete", "제목 [ "+model.plan.getTitle()+" ], 내용 [ "+model.plan.getTextPlan()+" ], 그룹 [ "+model.plan.group+" ]");
                  mFirebaseAnalytics.logEvent("plan_complete", bundle);
                  model.setIsDone(isChecked);

              }
          });

          this.binding.dayPlanItemOptionButton.setOnClickListener(new View.OnClickListener() {

              @Override
              public void onClick(View v) {
                  PopupMenu popup = new PopupMenu(context, v);
                  popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                      @Override
                      public boolean onMenuItemClick(MenuItem item) {
                          switch(item.getItemId()) {
                              case R.id.day_plan_item_delete:
                                  if(alertBuilder == null) {
                                      alertBuilder = new AlertDialog.Builder(context, R.style.plan_remove_reask_dialog);
                                  }

                                  if(model.plan.isParentPlan()) {
                                      attachAlert(R.string.parent_plan_delete_ask_msg);
                                  }else {
                                      attachAlert(R.string.child_plan_delete_ask_msg);
                                  }
                                  break;

                              case R.id.day_plan_item_pull:
                                  moveClickListener(MovePlanDialog.PULL_PLAN);
                                  break;

                              case R.id.day_plan_item_push:
                                  moveClickListener(MovePlanDialog.PUSH_PLAN);
                                  break;

                          }
                          return true;
                      }
                  });
                  MenuInflater inflater = popup.getMenuInflater();
                  inflater.inflate(R.menu.day_plan_item_option_menu, popup.getMenu());
                  popup.show();
              }
          });

        }

        private View optionView;

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


        private AlertDialog.Builder alertBuilder = null;

        private void attachAlert(final int messageId) {
            AlertDialog dialog = alertBuilder.setMessage(messageId)
                        .setPositiveButton(context.getResources().getString(R.string.yes), dialogClickListener)
                        .setNegativeButton(context.getResources().getString(R.string.no), dialogClickListener)
                        .show();
        }


        private void moveClickListener(final int moveType) {

            /*
            오늘을 기준으로 이전 계획은 밀고 당기기 불가능하게 했었는데
            막상 써보니깐 가능하게 하는게 더 좋아서 주석처리함.
            if(new DateTime(model.plan.year, model.plan.month, model.plan.day,0,0,0,0)
                .isBefore(new DateTime().minusDays(1))) {
                Toast.makeText(context, R.string.invalid_move_attempt, Toast.LENGTH_LONG).show();
                return;
            }
             */
            final MovePlanDialog dialog = new MovePlanDialog(moveType, model.plan);

             MovePlanDialogDismissListener dismissListener = new MovePlanDialogDismissListener() {
                @Override
                public void handleDialogDismiss(MovePlanDialog movePlanDialog) {
                    if (movePlanDialog.wasMoveSucceeded) {
                        int index = -1;
                        for (int i=0; i < planList.size(); i++) {
                            Plan plan = planList.get(i);
                            if (plan.uid == model.plan.uid) {
                                index = i;
                                break;
                            }
                        }

                        planList.remove(index);
                        notifyItemRemoved(index);
                    }
                }
            };
            dialog.dismissListener(dismissListener);
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


