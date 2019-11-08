package com.example.databinding2.ui.planDialogs.clonePreview;

import android.graphics.Point;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.databinding2.R;
import com.example.databinding2.custom.types.YMDList;
import com.example.databinding2.databinding.ClonePlanPreViewBinding;
import com.example.databinding2.ui.planDialogs.planCreateDialog.MakePlanVM;
import com.example.databinding2.ui.viewmodel.PlanMakeViewModel;

public class ClonePreviewAdapter extends RecyclerView.Adapter {

    private YMDList dateList;
    private DialogFragment fragment;
    public ClonePreviewAdapter(YMDList list, DialogFragment fm) {
        this.dateList = list;
        this.fragment= fm;
    }

    public void refreshPreViewAdapter(YMDList dateList){
        this.dateList = dateList;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final ClonePlanPreViewBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.clone_plan_preview_item,parent,false);

        Display display = parent.getDisplay();
        Point size = new Point();
        display.getSize(size);

        ViewGroup.LayoutParams params = binding.getRoot().getLayoutParams();

        params.width = size.x;
        params.height = size.y / 20;
        binding.getRoot().setLayoutParams(params);
        return new ClonePreViewHolder(binding,fragment);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ClonePreViewHolder vh =  (ClonePreViewHolder)holder;
        ClonePreViewVM model = new ClonePreViewVM(position);
        vh.setViewModel(model);
    }

    @Override
    public int getItemCount() {

        if(dateList == null){
            return 0;
        }
        return dateList.size();
    }

    protected class ClonePreViewHolder extends RecyclerView.ViewHolder {
        ClonePreViewVM model;
        ClonePlanPreViewBinding binding;
        DialogFragment fragment;

        public ClonePreViewHolder(@NonNull ClonePlanPreViewBinding binding, DialogFragment fragment) {
            super(binding.getRoot());
            this.binding = binding;
            this.fragment= fragment;
        }

        private void setViewModel(ClonePreViewVM model){
            this.model  = model;
            binding.setModel(model);
            binding.executePendingBindings();
            this.binding.cloneDateText.setText(model.getDate());
            this.binding.cycleInfoText.setText(model.getCycleInfo());

            if(model.isMyCheckBoxChecked()){
                this.binding.checkBox.setChecked(true);
            }else{
                this.binding.checkBox.setChecked(false);
            }
            setListeners();
        }

        private void setListeners(){
            this.binding.checkBox.setOnClickListener(new CheckBox.OnClickListener() {

                @Override
                public void onClick(View view) {

                    if(model.isMyCheckBoxChecked()){
                        binding.checkBox.setChecked(false);
                        model.noticeCheckBoxCheckUnDone();
                    }else{
                        binding.checkBox.setChecked(true);
                        model.noticeCheckBoxIsChecked();
                    }


                }
            });

        }


     }
}
