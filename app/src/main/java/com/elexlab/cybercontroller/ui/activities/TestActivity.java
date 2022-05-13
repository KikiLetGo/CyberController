package com.elexlab.cybercontroller.ui.activities;

import android.Manifest;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.elexlab.cybercontroller.R;
import com.elexlab.cybercontroller.communication.BluetoothKeyboard;
import com.elexlab.cybercontroller.services.Translator;
import com.elexlab.cybercontroller.utils.PermissionUtil;

public class TestActivity extends Activity {
    private BluetoothKeyboard bluetoothKeyboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PermissionUtil.checkAndRequestPermissions(this,new String[]{Manifest.permission.CAMERA,Manifest.permission.RECORD_AUDIO});

        setContentView(R.layout.activity_test);
        View btTest = findViewById(R.id.btTest);
        bluetoothKeyboard = new BluetoothKeyboard(this);

        btTest.setOnClickListener((View view)->{
            unLock();

        });

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
}
