package com.elexlab.cybercontroller.services;

import android.graphics.Bitmap;
import android.util.Log;

import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.mlsdk.MLAnalyzerFactory;
import com.huawei.hms.mlsdk.common.MLFrame;
import com.huawei.hms.mlsdk.text.MLLocalTextSetting;
import com.huawei.hms.mlsdk.text.MLText;
import com.huawei.hms.mlsdk.text.MLTextAnalyzer;

public class TextRecognizer {
    private final static String TAG = TextRecognizer.class.getSimpleName();
    private MLTextAnalyzer analyzer;

    public interface Callback{
        void onRecognized(String text);
    }
    private Callback callback;

    public TextRecognizer setCallback(Callback callback) {
        this.callback = callback;
        return this;
    }

    public TextRecognizer() {
        init();
    }

    private void init(){
        analyzer = MLAnalyzerFactory.getInstance().getRemoteTextAnalyzer();
    }
    public void recognize(Bitmap bitmap){
        MLFrame frame = MLFrame.fromBitmap(bitmap);
        Task<MLText> task = analyzer.asyncAnalyseFrame(frame);
        task.addOnSuccessListener(new OnSuccessListener<MLText>() {
            @Override
            public void onSuccess(MLText text) {
                // 识别成功处理。
                Log.d(TAG,text.getStringValue());
                if(callback != null){
                    callback.onRecognized(text.getStringValue());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                // 识别失败处理。
                e.printStackTrace();
                Log.e(TAG,"error");

            }
        });
    }
}
