package com.example.databinding2.custom.types;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

public class CustmScrollView extends ScrollView {

    private float prevX = 0;
    private float prevY = 0;
    private float currX = 0;
    private float currY = 0;
    private float slope = 0;
    private boolean isSwipe = false;
    public CustmScrollView(Context context) {
        super(context);
    }

    public CustmScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustmScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public boolean onTouchEvent(MotionEvent e){
        Log.e("스크롤뷰 이벤트 ","");

        int action = e.getAction();
        float slope = Math.abs((currY-prevY)/(currX-prevX));


        if(action == MotionEvent.ACTION_DOWN){

            if(isSwipe){
                return false;
            }
            prevX = e.getX();
            prevY = e.getY();
            return true;
        }

        if(action == MotionEvent.ACTION_MOVE){

            Log.e("스크롤뷰 슬로프 : ",Float.toString(slope));
            if(isSwipe){
                return true;
            }
            currX = prevX;
            currY = prevY;
            if(slope < 100){
                isSwipe= true;
                return false;
            }
            return true;
        }
        return true;
    }
}
