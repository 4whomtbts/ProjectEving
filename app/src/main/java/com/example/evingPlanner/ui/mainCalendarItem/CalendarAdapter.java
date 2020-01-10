package com.example.evingPlanner.ui.mainCalendarItem;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.evingPlanner.R;
import com.example.evingPlanner.TSLiveData;
import com.example.evingPlanner.custom.types.DayPlanList;
import com.example.evingPlanner.databinding.CalendarViewModelBinding;
import com.example.evingPlanner.domain.DayClass;
import com.example.evingPlanner.domain.Plan;
import com.example.evingPlanner.ui.singleDayDialog.DayDialogFragment;

import java.util.ArrayList;

public class CalendarAdapter extends RecyclerView.Adapter{
    private ArrayList<TSLiveData<DayClass>> dayList;
    private FragmentManager fm;
    private ConstraintLayout calendarItemWrapper;
    private Context context;
    private TextView DayText;
    private ViewGroup parent;
    private int selectedDay;
    private boolean isOnSwip = false;

    public CalendarAdapter(Context context, ArrayList<TSLiveData<DayClass>> list, FragmentManager fm){
        this.dayList = list;
        this.fm = fm;
        this.context = context;
    }

    public void setCalendarList(ArrayList<TSLiveData<DayClass>> dayClassList){

        dayList = dayClassList;
        notifyDataSetChanged();

    }

    @SuppressLint("ClickableViewAccessibility")
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {

        this.parent = parent;

        final CalendarViewModelBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.calender_item,parent,false);

        this.calendarItemWrapper = binding.calendarItemWrapper;
        this.DayText = binding.textDay;

        Display display = parent.getDisplay();
        Point size = new Point();
        display.getSize(size);

        GridLayoutManager.LayoutParams params = (GridLayoutManager.LayoutParams)
                binding.getRoot().getLayoutParams();
        params.height = size.y/6;

        binding.getRoot().setLayoutParams(params);

        return new CalendarViewHolder(binding);
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        CalendarViewHolder viewHolder = (CalendarViewHolder)holder;
        CalendarDayVM model = new CalendarDayVM(position);
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
        private int currentVisiblePlans;

        public CalendarViewHolder(@NonNull CalendarViewModelBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            setListeners();
            currentVisiblePlans=0;
        }

        @SuppressLint("ClickableViewAccessibility")
        private void setListeners(){
            this.binding.calendarItemWrapper.setOnTouchListener(new View.OnTouchListener() {


                @Override
                public boolean onTouch(View view, MotionEvent e) {

                    int action = e.getAction();
                    if (action == MotionEvent.ACTION_UP) {
                        if (!isOnSwip) {

                            DayDialogFragment dialog = new DayDialogFragment(fm,model.getGlobalCurrentYMD());
                            FragmentTransaction ft = fm.beginTransaction();
                            // TODO 축약
                            model.setGlobalSelectedYear(model.getYear());
                            model.setGlobalSelectedMonth(model.getMonth());
                            model.setGlobalSelectedDay(
                                    Integer.parseInt(model.getDay()));
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
            String text = model.getDay();
            binding.textDay.setText(text);
            binding.setModel(model);
            this.observe(position);

        }

        private View makeDayPreview(String title, boolean checked){
            View view = LayoutInflater.from(context).inflate(R.layout.day_preview_item, parent, false);
            TextView titleText = view.findViewById(R.id.day_preview_plan_title);
            titleText.setText(title);
            ImageView checkBoxImage= view.findViewById(R.id.day_preview_plan_checkBox);

            if(checked) checkBoxImage.setBackgroundResource(R.drawable.checked);
            else checkBoxImage.setBackgroundResource(R.drawable.unchecked);
            return view;
        }

        private void registerPlanPreviews(ArrayList<Plan> plans){

            int childCount = binding.planPreview.getChildCount();

            if(childCount==0){
                for(Plan plan : plans){
                    View dayPreview = makeDayPreview(plan.getTitle(), plan.isDone);
                    binding.planPreview.addView(dayPreview,currentVisiblePlans);
                }
            }else if(childCount!=plans.size()) {
                Plan plan = plans.get(plans.size() -1);
                View dayPreview = makeDayPreview(plan.getTitle(), plan.isDone);
                binding.planPreview.addView(dayPreview, currentVisiblePlans);
            }
        }


        public void observe(final int position){
            this.model.getLivePlanListAt(position).observeForever(new Observer<DayPlanList>() {

                @Override
                public void onChanged(DayPlanList plans) {
                            if(plans.size()!=0){
                                registerPlanPreviews(plans);
                                currentVisiblePlans++;
                            }
                }
            });
        }
    }
}
