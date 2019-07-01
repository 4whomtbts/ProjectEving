package com.example.databinding2.ui.mainCalendarItem;

import android.annotation.SuppressLint;
import android.graphics.Point;
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
import com.example.databinding2.custom.types.DayPlanList;
import com.example.databinding2.databinding.CalendarViewModelBinding;
import com.example.databinding2.domain.DayClass;
import com.example.databinding2.domain.Plan;
import com.example.databinding2.ui.singleDayDialog.DayDialogFragment;

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
        private ConstraintLayout calendarItemWrapper;
        private TextView DayText;
        private boolean isFirst;
        private int currentVisiblePlans;

        public CalendarViewHolder(@NonNull CalendarViewModelBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            setListeners();
            currentVisiblePlans=0;
            isFirst=true;
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
            String text = model.getMonth()+"월"+model.getDay()+"일";
            binding.textDay.setText(text);
            binding.setModel(model);
            binding.executePendingBindings();
            this.observe(position);

        }

        /*TODO
            ObserveForever 사용 후 해체 루틴 작성
         */

        private TextView makeNewTextView(){
            TextView newPlanTextView = new TextView(binding.getRoot().getContext());
            newPlanTextView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));

            return newPlanTextView;
        }
        private void registerPlanPreviews(ArrayList<Plan> plans){

            int childCount = binding.planPreview.getChildCount();

            if(childCount==0){
                for(Plan plan : plans){
                    TextView newPlanTextView = makeNewTextView();
                    newPlanTextView.setText(plan.getTextPlan());
                    binding.planPreview.addView(newPlanTextView,currentVisiblePlans);
                }
            }else if(childCount!=plans.size()) {
                TextView newPlanTextView = makeNewTextView();
                newPlanTextView.setText(plans.get(plans.size() - 1).getTextPlan());
                binding.planPreview.addView(newPlanTextView, currentVisiblePlans);
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
