package com.elexlab.cybercontroller.ui.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.elexlab.cybercontroller.MainActivity;
import com.elexlab.cybercontroller.R;
import com.elexlab.cybercontroller.communication.BluetoothClient;
import com.elexlab.cybercontroller.communication.BluetoothKeyboard;
import com.elexlab.cybercontroller.services.FaceRecognizer;
import com.elexlab.cybercontroller.ui.widget.AssetsAnimationImageView;
import com.elexlab.cybercontroller.ui.widget.CoolDigitalClock;
import com.elexlab.cybercontroller.utils.AssetsUtils;
import com.elexlab.cybercontroller.utils.DeviceUtil;
import com.elexlab.cybercontroller.utils.PermissionUtil;
import com.huawei.hms.mlsdk.livenessdetection.MLLivenessCaptureResult;
import com.huawei.hms.mlsdk.livenessdetection.MLLivenessDetectView;
import com.huawei.hms.mlsdk.livenessdetection.OnMLLivenessDetectCallback;

import java.sql.Time;

import static com.huawei.hms.mlsdk.livenessdetection.MLLivenessDetectView.DETECT_MASK;

public class LoginActivity extends Activity {

    private BluetoothKeyboard bluetoothKeyboard;
    private MLLivenessDetectView mlLivenessDetectView;
    private FrameLayout mPreviewContainer;
    private View rlScanFace;
    private TextView tvInfo;
    private View llMain;
    private View llSleep;
    private CoolDigitalClock digitalClock;
    FaceRecognizer faceRecognizer;
    AssetsAnimationImageView tvImageView;

    public static void startMe(Activity activity, boolean directUnlock){
        Intent intent = new Intent(activity, LoginActivity.class);
        intent.putExtra("directUnlock",directUnlock);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.login_transition, R.anim.login_transition);
        DeviceUtil.acquireWakeLock(activity,1000);
    }


    private Handler handler = new Handler();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PermissionUtil.checkAndRequestPermissions(this,new String[]{Manifest.permission.CAMERA});
        setContentView(R.layout.activity_login);
        mPreviewContainer = findViewById(R.id.surface_layout);
        rlScanFace = findViewById(R.id.rlScanFace);
        tvInfo = findViewById(R.id.tvInfo);
        llMain = findViewById(R.id.llMain);
        digitalClock = findViewById(R.id.digitalClock);
        llSleep = findViewById(R.id.llSleep);

        bluetoothKeyboard = new BluetoothKeyboard(this);
        faceRecognizer = new FaceRecognizer(this);


        //initLiveDetector(savedInstanceState);

        tvImageView = findViewById(R.id.tvImageView);

        tvImageView.setImages("tv");
        tvImageView.setDeltaTime(50);
        boolean directUnlock = getIntent().getBooleanExtra("directUnlock",false);

        llMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startMe(LoginActivity.this,true);
            }
        });
        if(directUnlock){
            startDetector(savedInstanceState);
        }

        BluetoothClient.getInstance().active();

    }

    private void startDetector(Bundle savedInstanceState){
        digitalClock.setVisibility(View.INVISIBLE);
        rlScanFace.setVisibility(View.VISIBLE);
        llSleep.setVisibility(View.INVISIBLE);
        tvImageView.start();

        //Obtain MLLivenessDetectView
        DisplayMetrics outMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        int widthPixels = outMetrics.widthPixels;

        mlLivenessDetectView = new MLLivenessDetectView.Builder()
                .setContext(this)
                //.setOptions(DETECT_MASK)
                // set Rect of face frame relative to surface in layout
                .setFaceFrameRect(new Rect(0, 0, widthPixels,dip2px(this,360) ))
                .setDetectCallback(new OnMLLivenessDetectCallback() {
                    @Override
                    public void onCompleted(MLLivenessCaptureResult result) {
                        if(result.isLive()){
                            compareFace(result.getBitmap());
                        }else{
                            onRecFailure();
                        }
                    }

                    @Override
                    public void onError(int error) {
                    }

                    public void onInfo(int infoCode, Bundle bundle) {

                    }

                    @Override
                    public void onStateChange(int state, Bundle bundle) {

                    }
                }).build();

        mPreviewContainer.addView(mlLivenessDetectView);
        mlLivenessDetectView.onCreate(savedInstanceState);
    }



    private void compareFace(Bitmap compareBitmap){
        Bitmap templateBitmap = AssetsUtils.getImageFromAssetsFile(this,"admin.jpg");
        faceRecognizer.compareFace(compareBitmap, templateBitmap, new FaceRecognizer.Callback() {
            @Override
            public void onSuccess(Bitmap pic) {
                unLock();
                onRecSuccess();

            }

            @Override
            public void onFailure() {
                onRecFailure();

            }
        });
    }

    private void onRecSuccess(){
        rlScanFace.setVisibility(View.GONE);
        tvImageView.stopAnim();
        tvImageView.setImageBitmap(AssetsUtils.getImageFromAssetsFile(this,"tv_happy.png"));
        tvInfo.setVisibility(View.VISIBLE);
        tvInfo.setText("欢迎回来~");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
                LoginActivity.this.overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            }
        },1000);

    }

    private void onRecFailure(){
        rlScanFace.setVisibility(View.GONE);
        tvInfo.setVisibility(View.VISIBLE);
        tvInfo.setText("你不对劲！");
    }

    private void unLock(){
        bluetoothKeyboard.sendKey("ESC");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                bluetoothKeyboard.sendKey("1");
                bluetoothKeyboard.sendKey("q");
                bluetoothKeyboard.sendKey("a");
                bluetoothKeyboard.sendKey("z");
                bluetoothKeyboard.sendKey("@");
                bluetoothKeyboard.sendKey("W");
                bluetoothKeyboard.sendKey("S");
                bluetoothKeyboard.sendKey("X");
                bluetoothKeyboard.sendKey("Enter");
            }
        },500);

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mlLivenessDetectView != null){
            mlLivenessDetectView.onDestroy();
        }
        digitalClock.destroy();

    }

    public static int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }



    @Override
    protected void onPause() {
        super.onPause();
        if(mlLivenessDetectView != null){
            mlLivenessDetectView.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mlLivenessDetectView != null){
            mlLivenessDetectView.onResume();
        }
    }


}
