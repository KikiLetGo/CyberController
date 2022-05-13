package com.elexlab.cybercontroller.services;

import android.graphics.Bitmap;
import android.util.Log;

import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.mlsdk.MLAnalyzerFactory;
import com.huawei.hms.mlsdk.common.MLFrame;
import com.huawei.hms.mlsdk.face.MLFace;
import com.huawei.hms.mlsdk.face.MLFaceAnalyzer;

import java.util.List;

public class FaceAnalyzer {
    private final static String TAG = FaceAnalyzer.class.getSimpleName();

    private MLFaceAnalyzer analyzer;

    public FaceAnalyzer() {
        init();
    }

    private void init(){

        // 方式二：使用默认参数配置，集成Lite SDK时可以使用此配置方式。默认参数为：检测关键点、检测人脸轮廓、检测特征点、精度偏好模式、不开启人脸追踪。
        analyzer = MLAnalyzerFactory.getInstance().getFaceAnalyzer();
    }

    public void detectFace(Bitmap bitmap){
        MLFrame frame = MLFrame.fromBitmap(bitmap);
        Task<List<MLFace>> task = analyzer.asyncAnalyseFrame(frame);
        task.addOnSuccessListener(new OnSuccessListener<List<MLFace>>() {
            @Override
            public void onSuccess(List<MLFace> faces) {
                // 检测成功。
                if(faces !=null && faces.size()>0){
                    Log.d(TAG,"有脸："+faces.size());
                }else{
                    Log.d(TAG,"无脸");

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                // 检测失败。
                e.printStackTrace();
            }
        });
    }
}
