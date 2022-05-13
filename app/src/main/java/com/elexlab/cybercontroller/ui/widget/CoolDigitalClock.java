package com.elexlab.cybercontroller.ui.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.elexlab.cybercontroller.R;
import com.elexlab.cybercontroller.services.ClockService;

import java.util.Date;

/**
 * Created by BruceYoung on 16/2/3.
 */
public class CoolDigitalClock extends RelativeLayout {

    private Context mContext;
    private ClockService clockService;
    public CoolDigitalClock(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    public CoolDigitalClock(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
    }

    public CoolDigitalClock(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }


    private TextView tvBreaker;
    private SevenPartDigitalTube timeHourHigh;
    private SevenPartDigitalTube timeHourLow;
    private SevenPartDigitalTube timeMinHigh;
    private SevenPartDigitalTube timeMinLow;

    private Handler handler = new Handler();


    public void destroy(){
        clockService.destroy();
    }
    private void initView(){
        View view = LayoutInflater.from(mContext).inflate(R.layout.cool_digital_clock_layout,null);
        tvBreaker = (TextView) view.findViewById(R.id.tvBreaker);
        timeHourHigh = (SevenPartDigitalTube) view.findViewById(R.id.timeHourHigh);
        timeHourLow = (SevenPartDigitalTube) view.findViewById(R.id.timeHourLow);
        timeMinHigh = (SevenPartDigitalTube) view.findViewById(R.id.timeMinHigh);
        timeMinLow = (SevenPartDigitalTube) view.findViewById(R.id.timeMinLow);
        tvBreaker.setTextColor(0xffffffff);
        addView(view);
        setTimeToView();

        clockService = new ClockService(mContext);
        clockService.registClockObserver(new ClockService.ClockObserverInterf() {
            @Override
            public boolean dida(ClockService.Dida dida) {
                if(dida!=null&&dida.isNewHour()||dida.isNewMin()){
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            setTimeToView();
                        }
                    });
                }
                return true;
            }
        });

        didaAnim();

    }

    private void didaAnim(){
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f,0.0f);
        alphaAnimation.setDuration(500);
        alphaAnimation.setRepeatCount(Animation.INFINITE);
        alphaAnimation.setRepeatMode(Animation.REVERSE);
        tvBreaker.setAnimation(alphaAnimation);
        alphaAnimation.start();

    }

    private void setTimeToView(){
        Date date = new Date();
        int hour = date.getHours();
        int min = date.getMinutes();

        int timeHourHighValue = hour / 10;
        int timeHourLowValue = hour % 10;
        int timeMinHighValue = min / 10;
        int timeMinLowValue = min % 10;

        if(timeHourHigh.getValue()!=timeHourHighValue){
            timeHourHigh.setValue(timeHourHighValue);
        }
        if(timeHourLowValue!=timeHourLow.getValue()){
            timeHourLow.setValue(timeHourLowValue);
        }

        if(timeMinHighValue!=timeMinHigh.getValue()){
            timeMinHigh.setValue(timeMinHighValue);
        }
        if(timeMinLowValue!=timeMinLow.getValue()){
            timeMinLow.setValue(min % 10);
        }
    }
}
