package com.elexlab.cybercontroller;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.elexlab.cybercontroller.communication.BluetoothKeyboard;
import com.elexlab.cybercontroller.communication.TcpClient;
import com.elexlab.cybercontroller.pojo.CommandMessage;
import com.elexlab.cybercontroller.pojo.ScriptMessage;
import com.elexlab.cybercontroller.services.CommandAnalyzer;
import com.elexlab.cybercontroller.services.FaceAnalyzer;
import com.elexlab.cybercontroller.services.HideCamera;
import com.elexlab.cybercontroller.services.SpeechRecognizer;
import com.elexlab.cybercontroller.services.TextRecognizer;
import com.elexlab.cybercontroller.services.Translator;
import com.elexlab.cybercontroller.ui.activities.LoginActivity;
import com.elexlab.cybercontroller.ui.widget.InfoBoxView;
import com.elexlab.cybercontroller.ui.widget.SpeechRecordView;
import com.elexlab.cybercontroller.ui.widget.TouchboardView;
import com.elexlab.cybercontroller.ui.widget.TranslationView;
import com.elexlab.cybercontroller.utils.AssetsUtils;
import com.elexlab.cybercontroller.utils.DeviceUtil;
import com.elexlab.cybercontroller.utils.PermissionUtil;
import com.elexlab.cybercontroller.utils.SharedPreferencesUtil;
import com.google.gson.Gson;
import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialProber;
import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.mlsdk.common.LensEngine;
import com.huawei.hms.mlsdk.common.MLAnalyzer;
import com.huawei.hms.mlsdk.common.MLFrame;
import com.huawei.hms.mlsdk.gesture.MLGesture;
import com.huawei.hms.mlsdk.gesture.MLGestureAnalyzer;
import com.huawei.hms.mlsdk.gesture.MLGestureAnalyzerFactory;
import com.huawei.hms.mlsdk.gesture.MLGestureAnalyzerSetting;
import com.huawei.hms.mlsdk.handkeypoint.MLHandKeypointAnalyzer;
import com.huawei.hms.mlsdk.handkeypoint.MLHandKeypointAnalyzerFactory;
import com.huawei.hms.mlsdk.handkeypoint.MLHandKeypointAnalyzerSetting;
import com.huawei.hms.mlsdk.handkeypoint.MLHandKeypoints;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends Activity {
    private final static String TAG = MainActivity.class.getSimpleName();
    private static TcpClient tcpClient = new TcpClient();
    private Translator translator;
    private TranslationView tvTranslation;
    private InfoBoxView ivInfoBoxView;
    private SpeechRecordView speechRecordView;
    private TouchboardView touchboardView;
    private BluetoothKeyboard bluetoothKeyboard;
    private Handler handler = new Handler();
    public static void startMe(Activity activity){
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.login_transition, R.anim.login_transition);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PermissionUtil.checkAndRequestPermissions(this,new String[]{Manifest.permission.CAMERA,Manifest.permission.RECORD_AUDIO});

        setContentView(R.layout.activity_main);
        speechRecordView = findViewById(R.id.speechRecordView);
        tvTranslation = findViewById(R.id.tvTranslation);
        touchboardView = findViewById(R.id.touchboardView);
        ivInfoBoxView = findViewById(R.id.ivInfoBoxView);
        ivInfoBoxView.dismiss(0);

        translator = new Translator();

        initSetting();
        initTcpClient();
        initMicrophone();
        initTouchboard();

    }

    private void setImage(Bitmap bitmap){
        //test
        ImageView ivPreview = findViewById(R.id.ivPreview);
        ivPreview.setImageBitmap(bitmap);
    }

    private void recognizeTextFormImg(Bitmap bitmap){
        new TextRecognizer()
                .setCallback((String text)->{
                    translator.translate(text.toLowerCase(),(String result)->{
                        onTranslateSuccess(text.toLowerCase(),result);

                    });
                })
                .recognize(bitmap);
       // handler.post(()->{setImage(bitmap);});
    }

    private void initSetting(){
        findViewById(R.id.rlSettings).setOnClickListener((View view)->{
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            View settingView = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_settings, null,false);
            builder.setView(settingView);
            final Dialog dialog = builder.create();
            //dialog.setContentView(settingView);
            dialog.show();
            ViewGroup.LayoutParams layoutParams = settingView.getLayoutParams();
            layoutParams.width = (int) (DeviceUtil.getDeiveSize(MainActivity.this).widthPixels*0.8);
            layoutParams.height = (int) (DeviceUtil.getDeiveSize(MainActivity.this).heightPixels*0.8);
            settingView.setLayoutParams(layoutParams);

            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                }
            });
            EditText etHostIp = settingView.findViewById(R.id.etHostIp);
            EditText etHostPort = settingView.findViewById(R.id.etHostPort);
            etHostIp.setText(SharedPreferencesUtil.getPreference(MainActivity.this,"settings","hostIp","not set yet"));
            int port = SharedPreferencesUtil.getPreference(MainActivity.this,"settings","hostPort",2233);
            etHostPort.setText(String.valueOf(port));

            settingView.findViewById(R.id.btnSave).setOnClickListener((View v)->{
                Map<String,Object> preferences = new HashMap<String,Object>();

                preferences.put("hostIp",etHostIp.getText().toString());
                try{
                    preferences.put("hostPort",Integer.parseInt(etHostPort.getText().toString()));
                }catch (NumberFormatException e){
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this,"端口号必须是数字",Toast.LENGTH_SHORT).show();

                }
                SharedPreferencesUtil.setPreferences(MainActivity.this,"settings",preferences);
                Toast.makeText(MainActivity.this,"设置已更新,重启App生效",Toast.LENGTH_LONG).show();
                dialog.dismiss();

            });

            settingView.findViewById(R.id.btnCancel).setOnClickListener((View v)->{
                dialog.dismiss();
            });

        });
    }

    private void onTranslateSuccess(String source, String result){
        tvTranslation.setContent(source,result);
        DeviceUtil.acquireWakeLock(MainActivity.this,TranslationView.AUTO_DISMISS_TIME);
    }


    private void initTcpClient(){
        tcpClient.onReceive((int type, byte[] data)->{
            if(type==2){
                //andler.postDelayed(()->{setImage(data);},1000);
                Bitmap bitmap = BitmapFactory.decodeByteArray(data,0,data.length);
                recognizeTextFormImg(bitmap);
                return;
            }
            Log.d("!isInLoginActivity",""+!isInLoginActivity());
            String text = null;
            try {
                text = new String(data,"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return;
            }
            Log.d(TAG,text);
                CommandMessage commandMessage = new Gson().fromJson(text, CommandMessage.class);
                if(commandMessage.getCommand() == CommandMessage.CommandType.TRANS){
                    translator.translate(commandMessage.getMessage(), new OnSuccessListener<String>() {
                        @Override
                        public void onSuccess(String s) {
                            Log.d("OnSuccessListener:",s);
                            onTranslateSuccess(commandMessage.getMessage(),s);
                        }
                    });
                }else if(commandMessage.getCommand() == CommandMessage.CommandType.LOCK && !isInLoginActivity()){
                    LoginActivity.startMe(MainActivity.this,false);
                }
            }
        );

    }

    private void initMicrophone(){
        speechRecordView.setListener(new SpeechRecordView.SpeechListener() {
            @Override
            public void onRecognizingResults(int mode, String result) {
                ivInfoBoxView.setInfo(result);
            }

            @Override
            public void onResults(int mode, String result) {
                ivInfoBoxView.setInfo(result);

            }

            @Override
            public void onRecognizeStart(int mode) {
                Log.d(TAG,"onRecognizeStart");

                String title = "正在语音输入";
                if(mode==2){
                    title="正在语音命令";
                }
                ivInfoBoxView.setTitle(title);
                ivInfoBoxView.setInfo("");
                ivInfoBoxView.show(1000);
            }

            @Override
            public void onRecognizeEnd(int mode, String result) {
                Log.d(TAG,"onRecognizeEnd");
                if(mode == 1){
                    String script = AssetsUtils.loadCommandScripts(MainActivity.this,"text_input.py");
                    ScriptMessage scriptMessage = new ScriptMessage();
                    scriptMessage.setScript(script);
                    scriptMessage.setParams(result);
                    tcpClient.send(new Gson().toJson(scriptMessage));
                }else if(mode == 2){
                    List<ScriptMessage> scriptMessages = new CommandAnalyzer().analyzeCommand(result);
                    for(ScriptMessage scriptMessage:scriptMessages){
                        if(scriptMessage != null){
                            tcpClient.send(new Gson().toJson(scriptMessage));
                        }
                    }

                }



                handler.postDelayed(()->{ivInfoBoxView.dismiss(1000);},1000);
            }
        });
    }


    private void initTouchboard(){
        touchboardView.setTouchCallback(new TouchboardView.TouchCallback() {
            @Override
            public void onSwitchWindow(int direction) {
                String script = AssetsUtils.loadCommandScripts(MainActivity.this,"key_event.py");
                String direct = direction==0?"left_arrow":"right_arrow";
                String params = "ctrl,win,"+direct;
                ScriptMessage scriptMessage = new ScriptMessage();
                scriptMessage.setScript(script);
                scriptMessage.setParams(params);
                tcpClient.send(new Gson().toJson(scriptMessage));
            }

            @Override
            public void onWindowTab() {
                String script = AssetsUtils.loadCommandScripts(MainActivity.this,"key_event.py");
                String params = "win,tab";
                ScriptMessage scriptMessage = new ScriptMessage();
                scriptMessage.setScript(script);
                scriptMessage.setParams(params);
                tcpClient.send(new Gson().toJson(scriptMessage));
            }
        });
    }



    private Bitmap getImageFromAssetsFile(Context context, String fileName) {
        Bitmap image = null;
        AssetManager am = context.getResources().getAssets();
        try {
            InputStream is = am.open(fileName);
            image = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    private boolean isInLoginActivity()
    {
        ActivityManager am = (ActivityManager)getSystemService(ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        return cn.getClassName().contains(LoginActivity.class.getSimpleName());
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onBackPressed() {

    }
}