package com.elexlab.cybercontroller.ui.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.elexlab.cybercontroller.R;
import com.elexlab.cybercontroller.services.ClockService;

public class TranslationView extends RelativeLayout {
    private final static String TAG = TranslationView.class.getSimpleName();
    private final static long CLICK_INTERVAL_TIME = 300;
    public final static long AUTO_DISMISS_TIME = 30*1000;
    public TranslationView(Context context) {
        super(context);
        initView();
    }

    public TranslationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public TranslationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public TranslationView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }
    private TextView tvSource;
    private TextView tvResult;
    private long lastClickTime=0;
    private Handler handler = new Handler(){

        @Override
        public void handleMessage(@NonNull Message msg) {
            Log.d(TAG,"handleMessage:"+msg.what);
            switch (msg.what){
                case 1:{
                    dismiss(1000);
                    break;
                }
                default:break;
            }
        }
    };

    private OnClickListener viewOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if(System.currentTimeMillis()-lastClickTime<CLICK_INTERVAL_TIME){
                Log.d(TAG,"double click");
                startDismiss(0);
            }else{
                Log.d(TAG,"single click");
                startDismiss(AUTO_DISMISS_TIME);
            }
            lastClickTime = System.currentTimeMillis();
        }
    };
    private void initView(){
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_translation_layout,null);
        tvSource = view.findViewById(R.id.tvSource);
        tvResult = view.findViewById(R.id.tvResult);
        tvResult.setMovementMethod(ScrollingMovementMethod.getInstance());
        addView(view);
        view.setOnClickListener(viewOnClickListener);
        tvSource.setOnClickListener(viewOnClickListener);
        tvResult.setOnClickListener(viewOnClickListener);

        tvResult.setOnTouchListener((View v, MotionEvent event)->{
            startDismiss(AUTO_DISMISS_TIME);
            return false;
        });

    }

    public void setContent(String source,String result){
        float textSize = 20;
        float textLen = source.length();
        textSize = 100-0.5f*textLen;
        if(textSize<20){
            textSize=20;
        }
        tvSource.setTextSize(textSize);
        tvResult.setTextSize(textSize);

        tvSource.setText(source);
        tvResult.setText(result);
        tvResult.scrollTo(0,0);
        show(100);
        startDismiss(AUTO_DISMISS_TIME);
    }

    /**
     * 开启消失计时
     */
    private void startDismiss(long delayTime){
        handler.removeMessages(1);
        handler.sendEmptyMessageDelayed(1,delayTime);//dismiss after 30s
    }

    public void show(int animTime){
        if(this.getVisibility() == VISIBLE){
            return;
        }
        this.setVisibility(VISIBLE);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0,1);
        alphaAnimation.setDuration(animTime);
        this.setAnimation(alphaAnimation);
        alphaAnimation.start();
    }
    public void dismiss(int animTime){
        this.setVisibility(GONE);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1,0);
        alphaAnimation.setDuration(animTime);
        this.setAnimation(alphaAnimation);
        alphaAnimation.start();
    }
}
