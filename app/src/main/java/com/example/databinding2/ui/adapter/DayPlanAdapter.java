package com.example.databinding2.ui.adapter;

import android.graphics.Point;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.databinding2.R;
import com.example.databinding2.TSLiveData;
import com.example.databinding2.databinding.DayPlanItemBinding;
import com.example.databinding2.domain.Plan;
import com.example.databinding2.ui.viewmodel.DayPlanVM;

import java.util.ArrayList;

public class DayPlanAdapter extends RecyclerView.Adapter {

    private ArrayList<Plan> planList;
    private FragmentManager fragmentManager;

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


        return new DayItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        DayItemViewHolder vh = (DayItemViewHolder)holder;
        DayPlanVM model = new DayPlanVM(position);
        vh.binding.textPlanTextView.setText(this.planList.get(position).getTextPlan());
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

        public DayItemViewHolder(@NonNull DayPlanItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }
        private void setViewModel(DayPlanVM model){
            this.model  = model;
            binding.setModel(model);
            binding.executePendingBindings();

        }
    }
}
