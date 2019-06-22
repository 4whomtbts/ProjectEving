package com.example.databinding2.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class CRecyclerView extends RecyclerView {
    private boolean isSwipe = false;
    private float prevX = 0;
    private float prevY = 0;
    private float currX = 0;
    private float currY =0 ;


    public CRecyclerView(@NonNull Context context) {
        super(context);
    }

    public CRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        int action =e.getAction();
        currX = e.getX();
        currY = e.getY();
        if(prevX == 0 && prevY==0){
            prevX = currX;
            prevY = currY;
        }
        float diffX = Math.abs(prevX-currX );
        float diffY = Math.abs(prevY-currY );



        if(action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP){
            isSwipe=false;
            return false;
        }
        switch (action) {
            case MotionEvent.ACTION_MOVE: {


                if (isSwipe) {

                    // We're currently scrolling, so yes, intercept the
                    // touch event!
                    return true;
                }

                // If the user has dragged her finger horizontally more than
                // the touch slop, start the scroll

                // left as an exercise for the reader

                // Touch slop should be calculated using ViewConfiguration
                // constants.
                if (diffX   > 5) {

                    // Start scrolling!
                    isSwipe = true;
                    return true;
                }
                break;
            }


        }

        // In general, we don't want to intercept touch events. They should be
        // handled by the child view.
        prevX = currX;
        prevY = currY;
        isSwipe=false;
        return false;

    }
/*

    @Override
    public boolean dispatchTouchEvent(MotionEvent e){
    int action =e.getAction();
    Log.e("dispatchTouchEvent", Integer.toString(e.getAction()));

        if(action==MotionEvent.ACTION_MOVE
            || action==MotionEvent.ACTION_DOWN){


            return true;
        }
        if(e.getAction()==MotionEvent.ACTION_UP){
            return true;
        }
    return true;
}
    }

        */
}
