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
import androidx.annotation.Nullable;

import com.elexlab.cybercontroller.R;
import com.elexlab.cybercontroller.services.SpeechRecognizer;

public class SpeechRecordView extends RelativeLayout {

    public interface  SpeechListener{
        void onRecognizeStart(int mode);
        void onRecognizingResults(int mode, String result);
        void onResults(int mode,String result);
        void onRecognizeEnd(int mode,String result);

    }
    private final static String TAG = SpeechRecordView.class.getSimpleName();
    private final static long TOUCH_INTERVAL_TIME = 300;
    public final static long AUTO_DISMISS_TIME = 30*1000;

    private int mode=0;
    public SpeechRecordView(Context context) {
        super(context);
        initView();
    }

    public SpeechRecordView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public SpeechRecordView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public SpeechRecordView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }



    private View rlMicrophone;
    private SpeechRecognizer speechRecognizer;
    private SpeechListener listener;

    public SpeechRecordView setListener(SpeechListener listener) {
        this.listener = listener;
        return this;
    }

    private void initView(){
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_speech_record,null);
        rlMicrophone = view.findViewById(R.id.rlMicrophone);
        addView(view);
        view.setOnTouchListener(onTouchListener);
        speechRecognizer = new SpeechRecognizer(getContext());

    }



    private Handler handler = new Handler();

    private Runnable  recognizer = new Runnable(){

        @Override
        public void run() {
            Log.d(TAG, "mode:"+mode);
            if(SpeechRecordView.this.listener != null){
                SpeechRecordView.this.listener.onRecognizeStart(mode);
            }
            speechRecognizer.startRec(new SpeechRecognizer.Listener() {
                @Override
                public void onRecognizingResults(String result) {
                    if(SpeechRecordView.this.listener != null){
                        SpeechRecordView.this.listener.onRecognizingResults(mode, result);
                    }
                }

                @Override
                public void onResults(String result) {
                    if(SpeechRecordView.this.listener != null){
                        SpeechRecordView.this.listener.onResults(mode,result);
                    }
                    speechRecognizer.refresh();

                }
            });
        }
    };

    private long lastTouchTime=0;

    private long holdStartTime = 0;
    private OnTouchListener onTouchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if(event.getAction()==MotionEvent.ACTION_DOWN) {
                holdStartTime = System.currentTimeMillis();
                if (System.currentTimeMillis() - lastTouchTime < TOUCH_INTERVAL_TIME) {
                    mode = 2;
                } else {
                    mode = 1;
                    handler.postDelayed(recognizer,TOUCH_INTERVAL_TIME);
                }
                lastTouchTime = System.currentTimeMillis();
            }else if(event.getAction()==MotionEvent.ACTION_UP){
                long holdTime = System.currentTimeMillis()-holdStartTime;
                if(holdTime > TOUCH_INTERVAL_TIME){
                    if(SpeechRecordView.this.listener != null){
                        SpeechRecordView.this.listener.onRecognizeEnd(mode, speechRecognizer.getCurrentContent());
                    }
                }
                mode = 0;

            }
            return true;
        }
    };

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if(speechRecognizer != null){
            speechRecognizer.destroy();
        }
    }
}
