package com.elexlab.cybercontroller.services;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.camera2.CameraDevice;
import android.opengl.GLES11Ext;
import android.view.SurfaceView;

import java.io.IOException;

public class HideCamera {
    private Camera camera;
    private SurfaceTexture surfaceTexture;
    public HideCamera() {
        init();
    }

    public interface Callback{
        void onPictureTook(Bitmap bitmap);
    }

    private void init(){
        camera = Camera.open(findFontCameraId());
        Camera.Parameters parameters = camera.getParameters();
        parameters.setPictureFormat(PixelFormat.JPEG);

        parameters.setPreviewSize(320, 240);

        parameters.setPictureSize(1920, 1080);

        camera.setParameters(parameters);

        surfaceTexture = new SurfaceTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES);
        try {
            camera.setPreviewTexture(surfaceTexture);
        } catch (IOException e) {
            e.printStackTrace();
        }

        camera.startPreview();


    }

    public void takePicture(Callback callback){
        camera.takePicture(null,null, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(data,0,data.length);
                if(callback != null){
                    callback.onPictureTook(bitmap);
                }
            }
        });
    }

    public void closeCamera() {
        camera.stopPreview();
        camera.setPreviewCallbackWithBuffer(null);
        camera.release();
        camera = null;
        surfaceTexture.release();
    }

    private int findFontCameraId(){
        int numberOfCameras = Camera.getNumberOfCameras();// 获取摄像头个数
        //遍历摄像头信息
        for (int cameraId = 0; cameraId < numberOfCameras; cameraId++) {
            Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
            Camera.getCameraInfo(cameraId, cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {//前置摄像头
               return cameraId;
            }
        }
        return 0;
    }
}
