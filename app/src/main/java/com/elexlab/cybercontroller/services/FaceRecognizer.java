package com.elexlab.cybercontroller.services;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.elexlab.cybercontroller.ui.activities.LoginActivity;
import com.elexlab.cybercontroller.utils.AssetsUtils;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.mlsdk.common.MLFrame;
import com.huawei.hms.mlsdk.faceverify.MLFaceTemplateResult;
import com.huawei.hms.mlsdk.faceverify.MLFaceVerificationAnalyzer;
import com.huawei.hms.mlsdk.faceverify.MLFaceVerificationAnalyzerFactory;
import com.huawei.hms.mlsdk.faceverify.MLFaceVerificationResult;
import com.huawei.hms.mlsdk.livenessdetection.MLLivenessCapture;
import com.huawei.hms.mlsdk.livenessdetection.MLLivenessCaptureResult;

import java.util.List;

public class FaceRecognizer {

    public interface Callback{
        void onSuccess(Bitmap pic);
        void onFailure();
    }
    private final static String TAG = FaceRecognizer.class.getSimpleName();
    private static final String[] PERMISSIONS = {Manifest.permission.CAMERA};

    private static final int RC_CAMERA_AND_EXTERNAL_STORAGE = 0x01 << 8;

    private Activity activity;
    private MLFaceVerificationAnalyzer analyzer;


    public FaceRecognizer(Activity activity) {
        this.activity = activity;
        initAnalyzer();
    }

    private void initAnalyzer(){
        analyzer = MLFaceVerificationAnalyzerFactory.getInstance().getFaceVerificationAnalyzer();
    }

    private void setTempFace(Bitmap templateBitmap){
        // 通过bitmap创建MLFrame
        MLFrame templateFrame = MLFrame.fromBitmap(templateBitmap);
        List<MLFaceTemplateResult> results = analyzer.setTemplateFace(templateFrame);
    }

    public void compareFace(Bitmap compareBitmap, Bitmap templateBitmap, Callback callback){
        setTempFace(templateBitmap);
        Task<List<MLFaceVerificationResult>> task
                = analyzer.asyncAnalyseFrame(MLFrame.fromBitmap(compareBitmap));
        task.addOnSuccessListener(new OnSuccessListener<List<MLFaceVerificationResult>>() {
            @Override
            public void onSuccess(List<MLFaceVerificationResult> mlFaceVerificationResults) {
                for (MLFaceVerificationResult template : mlFaceVerificationResults) {
                    Rect location = template.getFaceInfo().getFaceRect();
                    float similarity = template.getSimilarity();
                    Log.d(TAG,"face similarity:"+similarity);
                    if(similarity>0.8){
                        if(callback !=null){
                            callback.onSuccess(null);
                        }
                    }else{
                        if(callback !=null){
                            callback.onFailure();
                        }
                    }

                }
            }
        });
    }




}
