package com.elexlab.cybercontroller.communication;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHidDevice;
import android.bluetooth.BluetoothHidDeviceAppQosSettings;
import android.bluetooth.BluetoothHidDeviceAppSdpSettings;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.Executor;

public class BluetoothClient extends BluetoothHidDevice.Callback{
    private static final String TAG = BluetoothClient.class.getSimpleName();

    public interface Listener{
        void onConnected();
        void onDisConnected();
    }

    private static BluetoothClient instance;
    public static BluetoothClient getInstance(){
        if (instance == null){
            Log.e(TAG,"BluetoothClient need bound a Context first");
        }
        return instance;
    }
    private BluetoothClient(){

    }

    public static void bindContext(Context context){
        if(instance != null){
            Log.e(TAG,"BluetoothClient already bind a Context");
            return;
        }
        instance = new BluetoothClient(context);
    }


    private BluetoothHidDevice btHid;
    private BluetoothDevice bluetoothDevice;
    private BluetoothDevice hostDevice;
    private BluetoothDevice mpluggedDevice;
    private BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
    private Context context;
    private ServiceListener serviceListener = new ServiceListener();
    private Listener listener;

    private BluetoothHidDeviceAppSdpSettings sdpRecord
            = new BluetoothHidDeviceAppSdpSettings("Pixel HID1",
                                                "Mobile BController",
                                                  "bla",
                                                    BluetoothHidDevice.SUBCLASS1_COMBO,
                                                    DescriptorCollection.MOUSE_KEYBOARD_COMBO
                                                  );

    private BluetoothHidDeviceAppQosSettings qosOut
            = new BluetoothHidDeviceAppQosSettings(BluetoothHidDeviceAppQosSettings.SERVICE_BEST_EFFORT,
                                                        800,
                                                        9,
                                                        0,
                                                        11250,
                                                        BluetoothHidDeviceAppQosSettings.MAX
                                                        );

    public void setListener(Listener listener){
        this.listener = listener;
    }

    public void connect(){
        btHid.connect(mpluggedDevice);
    }

    public void sendData(int id,byte[] data){
        btHid.sendReport(hostDevice, id, data);
    }
    public void active(){
        if(btHid == null){
            return;
        }
        btHid.registerApp(sdpRecord, null, qosOut, new Executor() {
            @Override
            public void execute(Runnable command) {
                command.run();
            }
        }, BluetoothClient.this);
    }
    public void stop(){
        btHid.unregisterApp();
    }

    public void destory(){
        btAdapter.closeProfileProxy(BluetoothProfile.HID_DEVICE, btHid);
    }


    private BluetoothClient(Context context){
        this.context = context;
        init();
    }

    private void init(){
        if(btHid != null){
            return;
        }
        btAdapter.getProfileProxy(context,serviceListener, BluetoothProfile.HID_DEVICE);
    }

    private class ServiceListener implements BluetoothProfile.ServiceListener{

        @Override
        public void onServiceConnected(int profile, BluetoothProfile proxy) {
            Log.i(TAG, "Connected to service");
            if (profile != BluetoothProfile.HID_DEVICE) {
                Log.wtf(TAG, "WTF:"+profile);
                return;
            }
            if (!(proxy instanceof BluetoothHidDevice)) {
                Log.wtf(TAG, "WTF? Proxy received but it's not BluetoothHidDevice");

                return;
            }
            btHid= (BluetoothHidDevice)proxy;
            active();

            //btAdapter.setScanMode(BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE, 300000);

            try {
                for(Method m:BluetoothAdapter.class.getMethods()){
                    if("setScanMode".equals(m.getName())&& m.getParameterCount()>1){
                        m.invoke(btAdapter,BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE, 300000);
                        break;
                    }

                }
//                Method method = BluetoothAdapter.class.getMethod("setScanMode", int.class, long.class);
//                method.invoke(btAdapter,BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE, 300000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(int profile) {
            Log.e(TAG, "Service disconnected!");
            if (profile == BluetoothProfile.HID_DEVICE)
                btHid = null;
        }
    }

    @Override
    public void onConnectionStateChanged(BluetoothDevice device, int state) {
        super.onConnectionStateChanged(device, state);
        Log.d(TAG,"Connection state:"+state);
        if (state == BluetoothProfile.STATE_CONNECTED) {
            if (device != null) {
                hostDevice = device;
                if(listener != null){
                    listener.onConnected();
                }


            } else {
                Log.e(TAG, "Device not connected");
            }
        } else {
            hostDevice = null;
            if(state == BluetoothProfile.STATE_DISCONNECTED)
            {
                if(listener != null){
                    listener.onDisConnected();
                }
            }

        }
    }

    @Override
    public void onAppStatusChanged(BluetoothDevice pluggedDevice, boolean registered) {
        super.onAppStatusChanged(pluggedDevice, registered);
        if(registered)
        {
            int[] states = new int[]{BluetoothProfile.STATE_CONNECTING,BluetoothProfile.STATE_CONNECTED,BluetoothProfile.STATE_DISCONNECTED,BluetoothProfile.STATE_DISCONNECTING};
            List<BluetoothDevice> pairedDevices = btHid.getDevicesMatchingConnectionStates(states);
            Log.d(TAG, "paired devices: "+pairedDevices);
            mpluggedDevice = pluggedDevice;
            if (pluggedDevice != null && btHid.getConnectionState(pluggedDevice) == BluetoothProfile.STATE_DISCONNECTED) {
                btHid.connect(pluggedDevice);
            } else {
                BluetoothDevice pairedDevice =  pairedDevices.get(0);

                int pairedDState = btHid.getConnectionState(pairedDevice);
                Log.d(TAG,"paired "+pairedDState);
                if (pairedDState == BluetoothProfile.STATE_DISCONNECTED) {
                    btHid.connect(pairedDevice);
                }

            }
        }
    }


}
