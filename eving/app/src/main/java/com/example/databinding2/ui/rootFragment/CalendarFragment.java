package com.example.databinding2.ui.rootFragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.databinding2.R;
import com.example.databinding2.TSLiveData;
import com.example.databinding2.custom.CRecyclerView;
import com.example.databinding2.databinding.CalendarFragmentBinding;
import com.example.databinding2.domain.DayClass;
import com.example.databinding2.ui.mainCalendarItem.CalendarAdapter;

import java.util.ArrayList;
import java.util.GregorianCalendar;


public class CalendarFragment extends Fragment {
    private int check;
    private CRecyclerView recyclerView;
    private TextView yearMonth;
    private Button dayButton;
    private Fragment self = this;

    public static final float MIN_DIST = 300;
    public static final float MAXY_DIST = 200;
    private long prevTime = System.currentTimeMillis();
    private boolean wasDrag=false;
    private int _year;
    private int _month;
    private CalendarMonthVM vmodel;
    float x0;
    float y0;



    CalendarFragmentBinding binding;
    public CalendarFragment(){
        this.check = check;
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        this._year = gregorianCalendar.get(java.util.Calendar.YEAR);
        this._month = gregorianCalendar.get(java.util.Calendar.MONTH);


        x0=y0=0;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState){
        binding = DataBindingUtil.inflate(inflater,R.layout.calendar_fragment,container,false);
        View view = binding.getRoot();
        vmodel = ViewModelProviders.of(this).get(CalendarMonthVM.class);
        binding.setModel(vmodel);
        binding.setLifecycleOwner(this);
        yearMonth = binding.yearMonthText;
        recyclerView = binding.pagerCalendar;




        touchListener(view);
        observe();
        if(vmodel!=null){
            vmodel.initCalendar();
        }
        return view;
    }


    @SuppressLint("ClickableViewAccessibility")
    private void touchListener(View view) {



        yearMonth.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                return false;
            }
        });
        final CRecyclerView pager = binding.pagerCalendar;
        pager.setOnTouchListener(new View.OnTouchListener() {



            @Override
            public boolean onTouch(View view, MotionEvent event) {
                float x1 = event.getX();
                float y1 = event.getY();
                float diffX = Math.abs(x1 - x0);
                float dir = (x1 - x0) / diffX;
                float diffY = Math.abs(y1 - y0);
                float slope = 100;
                long currTime = System.currentTimeMillis();
                long timeDiff = currTime-prevTime;
                boolean isOnSwip = false;
                if(x0==0 && y0==0){
                    x0=event.getX();
                    y0=event.getY();
                }

                if (diffX != 0) {
                    slope = diffY / diffX;
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    Log.e("업발생" ,"플래그 상태 " +wasDrag);
                    return false;
                }
                if (event.getAction() == MotionEvent.ACTION_CANCEL) {
//                    Log.e("캔슬발생", "이전좌표 : ("+x0+","+y0+") 갱신좌표 : ("+x1+","+y1+")");
                    return false;

                }

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        Log.e("다운좌표갱신", "이전좌표 : ("+x0+","+y0+") 갱신좌표 : ("+x1+","+y1+")");

                        return true;

                    }

                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                        Log.e("이동", "이전좌표 : ("+x0+","+y0+") 갱신좌표 : ("+x1+","+y1+")");
                        Log.e("이동", "MIN_DIST : "+diffX+",  MAXY_DIST : "+diffY );

                        if(diffX > MIN_DIST && diffY < MAXY_DIST && slope < 5
                        && timeDiff > 200){

                            Log.e("이동완료", "MIN_DIST : "+diffX+",  MAXY_DIST : "+diffY );
                            Animation outThroughleft = AnimationUtils.loadAnimation(getActivity(),R.anim.anim_slide_out_left),
                                      inFromRight = AnimationUtils.loadAnimation(getActivity(),R.anim.anim_slide_in_right),
                                      outThroughRight = AnimationUtils.loadAnimation(getActivity(),R.anim.anim_slide_out_right),
                                      inFromLeft = AnimationUtils.loadAnimation(getActivity(),R.anim.anim_slide_in_left);

                            if(dir == -1){
                                view.startAnimation(outThroughRight);
                                vmodel.gotoPrevMonth();
                                view.startAnimation(inFromLeft);
                            }else{
                                view.startAnimation(outThroughleft);
                                vmodel.gotoNextMonth();
                                view.startAnimation(inFromRight);
                            }
                            vmodel.refreshCalendar();
                            binding.pagerCalendar.setVerticalScrollbarPosition(0);// 달력이 넘어가면 첫 날로 고정된다

                            prevTime=currTime;

                        }

                    x0=event.getX();
                    y0=event.getY();
                        return true;
                    }

                return true;
            }
        });

    }
    private void observe(){


        vmodel.getDaysArrayList().observe(this, new Observer<ArrayList<TSLiveData<DayClass>>>() {

            @Override
            public void onChanged(ArrayList<TSLiveData<DayClass>> dayClasses) {
                RecyclerView view= binding.pagerCalendar;
                CalendarAdapter adapter = new CalendarAdapter(dayClasses,getFragmentManager());
                    GridLayoutManager manager=  new GridLayoutManager(getActivity(),7);
                    view.setLayoutManager(manager);
                    view.setAdapter(adapter);

            }
        });
        vmodel.getLiveGlobalMonth().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {

                String currentDate =  vmodel.getGlobalCurrentCalendarYear()
                +"년 "+vmodel.getGlobalCurrentCalendarMonth()+"월 ";
                binding.yearMonthText.setText(currentDate);
            }
        });

    }

}
