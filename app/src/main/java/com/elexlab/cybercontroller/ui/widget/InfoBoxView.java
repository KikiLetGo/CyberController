package com.elexlab.cybercontroller.ui.widget;

import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.elexlab.cybercontroller.R;

public class InfoBoxView extends RelativeLayout {
    public InfoBoxView(Context context) {
        super(context);
        initView();
    }

    public InfoBoxView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public InfoBoxView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public InfoBoxView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }
    private TextView tvTitle;
    private TextView tvInfo;
    private void initView(){
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_info_box_layout,null);
        tvTitle = view.findViewById(R.id.tvTitle);
        tvInfo = view.findViewById(R.id.tvInfo);
        tvInfo.setMovementMethod(ScrollingMovementMethod.getInstance());
        addView(view);
    }

    public void setTitle(String content){
        this.tvTitle.setText(content);
    }

    public void setInfo(String content){
        this.tvInfo.setText(content);
    }

    public void show(int animTime){
        InfoBoxView.this.setVisibility(VISIBLE);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0,1);
        alphaAnimation.setDuration(animTime);
        this.setAnimation(alphaAnimation);
        alphaAnimation.start();
    }
    public void dismiss(int animTime){
        InfoBoxView.this.setVisibility(INVISIBLE);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1,0);
        alphaAnimation.setDuration(animTime);
        this.setAnimation(alphaAnimation);
        alphaAnimation.start();
    }
}
