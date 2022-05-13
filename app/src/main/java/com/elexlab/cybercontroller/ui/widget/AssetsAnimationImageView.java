package com.elexlab.cybercontroller.ui.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.elexlab.cybercontroller.R;
import com.elexlab.cybercontroller.utils.AssetsUtils;

import java.util.List;

public class AssetsAnimationImageView extends ImageView {
    private final static String TAG = AssetsAnimationImageView.class.getSimpleName();
    public AssetsAnimationImageView(Context context) {
        super(context);
    }

    public AssetsAnimationImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AssetsAnimationImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public AssetsAnimationImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private Handler handler = new Handler();
    private String path;
    private List<String> animImgs;
    private int index=0;
    private int step=1;
    private int deltaTime = 100;
    public void setImages(String path){
        this.path = path;
        animImgs = AssetsUtils.listFiles(getContext(),path);

    }
    public void setDeltaTime(int deltaTime){
        this.deltaTime = deltaTime;
    }

    private Runnable anim = new Runnable() {
        @Override
        public void run() {

            Bitmap bitmap = AssetsUtils.getImageFromAssetsFile(getContext(),path+"/"+animImgs.get(index));
            setImageBitmap(bitmap);

            if((index==animImgs.size()-1&&step>0) || (index==0&&step<0)){
                step=-step;
            }
            index+=step;

            handler.postDelayed(this,deltaTime);
        }
    };

    public void start(){
        if(animImgs == null || animImgs.size()<=0){
            Log.e(TAG,"no animImgs setted!");
            return;
        }
        handler.post(anim);
    }
    public void stopAnim(){
        handler.removeCallbacks(anim);
    }
}
