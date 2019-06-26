package com.example.databinding2.ui.adapter;

import android.annotation.SuppressLint;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.databinding2.R;
import com.example.databinding2.TSLiveData;
import com.example.databinding2.databinding.CalendarViewModelBinding;
import com.example.databinding2.domain.DayClass;
import com.example.databinding2.domain.Plan;
import com.example.databinding2.ui.presenter.DayDialogFragment;
import com.example.databinding2.ui.viewmodel.CalendarDayVM;

import java.util.ArrayList;

public class CalendarAdapter extends RecyclerView.Adapter{
    private ArrayList<TSLiveData<DayClass>> dayList;
    private FragmentManager fm;
    private ConstraintLayout calendarItemWrapper;
    private TextView DayText;
    private int selectedDay;
    private boolean isOnSwip = false;

    public CalendarAdapter(ArrayList<TSLiveData<DayClass>> list, FragmentManager fm){
        this.dayList = list;
        this.fm = fm;
    }

    public void setCalendarList(ArrayList<TSLiveData<DayClass>> dayClassList){

        dayList = dayClassList;
        notifyDataSetChanged();
    }

    @SuppressLint("ClickableViewAccessibility")
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        final CalendarViewModelBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.calender_item,parent,false);

        this.calendarItemWrapper = binding.calendarItemWrapper;
        this.DayText = binding.textDay;

        int newHeight = parent.getRootView().getMeasuredHeight();
        Display display = parent.getDisplay();
        Point size = new Point();
        display.getSize(size);

        GridLayoutManager.LayoutParams params = (GridLayoutManager.LayoutParams)
                binding.getRoot().getLayoutParams();
        params.height = size.y/6;
        binding.getRoot().setLayoutParams(params);

        CalendarViewHolder viewHolder = new CalendarViewHolder(binding);
        return new CalendarViewHolder(binding);
    }

    public ConstraintLayout getDayItemWrapper(){
        return this.calendarItemWrapper;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        CalendarViewHolder viewHolder = (CalendarViewHolder)holder;
        CalendarDayVM model = new CalendarDayVM();
        TSLiveData<DayClass> item = dayList.get(position);
        model.setCalendar(item.getValue());
        viewHolder.setViewModel(model,position);
    }


    @Override
    public int getItemCount() {
        if(dayList != null){
            return dayList.size();
        }
        return 0;
    }

    private class CalendarViewHolder extends RecyclerView.ViewHolder {
        private CalendarViewModelBinding binding;
        private CalendarDayVM model;
        private ConstraintLayout calendarItemWrapper;
        private TextView DayText;

        public CalendarViewHolder(@NonNull CalendarViewModelBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            setListeners();

        }

        @SuppressLint("ClickableViewAccessibility")
        private void setListeners(){
            this.binding.calendarItemWrapper.setOnTouchListener(new View.OnTouchListener() {


                @Override
                public boolean onTouch(View view, MotionEvent e) {

                    int action = e.getAction();
                    if (action == MotionEvent.ACTION_UP) {
                        if (!isOnSwip) {

                            DayDialogFragment dialog = new DayDialogFragment(fm);
                            FragmentTransaction ft = fm.beginTransaction();
                            model.setGlobalCurrentDay(model.getDay());
                            dialog.show(ft, "1234");
                        }
                        return true;
                    }

                    if(action == MotionEvent.ACTION_CANCEL){
                        return true;
                    }
                    return true;
                }
            });
        }

        private void setViewModel(CalendarDayVM model,int position) {
            this.model = model;
            binding.textDay.setText(model.getDay());
            binding.setModel(model);
            binding.executePendingBindings();
            this.observe(position);
        }

        /*TODO
            ObserveForever 사용 후 해체 루틴 작성
         */
        public void observe(final int position){

            this.model.getLiveCurrentMonthPlanListAt(position).observeForever(new Observer<ArrayList<Plan>>() {
                boolean isFirst=  true;

                @Override
                public void onChanged(ArrayList<Plan> plans) {

                    if(!isFirst){
                        Log.e("GetDay",model.getDay());
                        binding.textDay.setText(plans.get(0).getTextPlan());
                    }else{
                        isFirst=false;
                    }

                }
            });

            this.model.getLiveCurrentMonthPlanList().observeForever(new Observer<ArrayList<TSLiveData<ArrayList<Plan>>>>() {
                boolean isFirst=true;

                @Override
                public void onChanged(ArrayList<TSLiveData<ArrayList<Plan>>> tsLiveData) {
                    if(!isFirst){
                    }else{
                        isFirst=false;
                    }

                }
            });

            this.model.getDaysArrayList().observeForever(   new Observer<ArrayList<TSLiveData<DayClass>>>() {
                @Override
                public void onChanged(ArrayList<TSLiveData<DayClass>> tsLiveData) {

                }
            });

        }
    }
}
