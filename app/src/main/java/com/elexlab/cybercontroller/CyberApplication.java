package com.elexlab.cybercontroller;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.elexlab.cybercontroller.communication.BluetoothClient;
import com.huawei.hms.mlsdk.common.MLApplication;

public class CyberApplication extends Application {
    private static Context context;

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.context = this;
        MLApplication.getInstance().setApiKey("替换成你自己的ApiKey");

        BluetoothClient.bindContext(this);

        ScreenStatusReceiver mScreenStatusReceiver = new ScreenStatusReceiver();

        IntentFilter intentFilter = new IntentFilter();

        intentFilter.addAction(Intent.ACTION_SCREEN_ON);

        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);

        registerReceiver(mScreenStatusReceiver, intentFilter);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    private class ScreenStatusReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if ( "android.intent.action.SCREEN_ON".equals(intent.getAction())) {

                BluetoothClient.getInstance().active();
            }
        }
    }
}
