package com.elexlab.cybercontroller.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

public class TouchboardView extends View {
    private final static String TAG = TouchboardView.class.getSimpleName();
    public interface TouchCallback{
        void onSwitchWindow(int direction);
        void onWindowTab();


    }
    private TouchCallback touchCallback;

    public TouchboardView setTouchCallback(TouchCallback touchCallback) {
        this.touchCallback = touchCallback;
        return this;
    }

    public TouchboardView(Context context) {
        super(context);
        init();
    }

    public TouchboardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TouchboardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public TouchboardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private float xPoint;
    private float yPoint;

    private void init(){
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d(TAG,"PointerCount:"+event.getPointerCount());
                Log.d(TAG,"Action:"+event.getAction());
                if(MotionEvent.ACTION_DOWN == event.getAction()){
                    xPoint = event.getX(0);
                    yPoint = event.getY(0);
                } else if(MotionEvent.ACTION_MOVE == event.getAction()){

                }else if(MotionEvent.ACTION_POINTER_2_UP == event.getAction()
                        || MotionEvent.ACTION_POINTER_UP == event.getAction()){//tow finger up
                    float endXPoint =  event.getX(0);
                    float endYPoint =  event.getY(0);

                    if(endXPoint-xPoint>100){
                        Log.d(TAG,"need left");
                        if(touchCallback != null){
                            touchCallback.onSwitchWindow(0);
                        }
                    }else if(xPoint-endXPoint>100){
                        Log.d(TAG,"need right");
                        if(touchCallback != null){
                            touchCallback.onSwitchWindow(1);
                        }

                    }else if(Math.abs(yPoint-endYPoint)>100){
                        Log.d(TAG,"need win tab");
                        if(touchCallback != null){
                            touchCallback.onWindowTab();
                        }
                    }


                }

                return true;
            }
        });
    }
}
