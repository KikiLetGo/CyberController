package com.elexlab.cybercontroller.communication;

import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import com.elexlab.cybercontroller.CyberApplication;
import com.elexlab.cybercontroller.communication.tcp.PackageUtil;
import com.elexlab.cybercontroller.utils.SharedPreferencesUtil;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Arrays;

public class TcpClient {

    private static final String TAG = TcpClient.class.getSimpleName();
    private static final int PACKAGE_HEAD_LENGTH = 8;

    public interface ReceiveListener{
        void onReceive(int type, byte[] data);
    }
    private ReceiveListener receiveListener;
    private String hostip = "192.168.3.28";
    private int port = 2233;
    private  Socket socketClient;
    private BufferedReader reader;
    private BufferedWriter writer;
    private Handler handler;
    private Thread receiveThread;

    public TcpClient(){
        HandlerThread tcpThread = new HandlerThread("tcpThread");
        tcpThread.start();
        handler = new Handler(tcpThread.getLooper());
        hostip = SharedPreferencesUtil.getPreference(CyberApplication.getContext(),"settings","hostIp",hostip);
        port = SharedPreferencesUtil.getPreference(CyberApplication.getContext(),"settings","hostPort",port);

        tryConnect();
    }

    private void tryConnect(){

        handler.post(()->{
            while(true){
                if(connect()){
                    startReceive();
                    break;
                }
            }

        });

    }

    private boolean connect(){
        closeAll();
        try{
            socketClient = new Socket();
            SocketAddress socAddress = new InetSocketAddress(hostip, port);
            socketClient.connect(socAddress,5000);
            Log.i(TAG,"socket 连接成功");
            writer = new BufferedWriter(new OutputStreamWriter(socketClient.getOutputStream()));

        }catch (UnknownHostException e){
            e.printStackTrace();
            return false;
        }catch (IOException e) {
            e.printStackTrace();
            return false;

        }
        return true;

    }
    public void send(String msg){
        Log.d(TAG,"send msg:"+msg);
        byte[] data = PackageUtil.pack(msg.getBytes(),1);
        send(data);
    }

    public void send(byte[] data){
        Log.d(TAG,"send data len:"+data.length);
        handler.post(()->{
            try {
                OutputStream outputStream = socketClient.getOutputStream();
                outputStream.write(data);
            } catch (SocketException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {

            }
        });
    }

    public void onReceive(ReceiveListener receiveListener){
        this.receiveListener = receiveListener;
    }

    public void destroy(){
        if(receiveThread!=null){
            receiveThread.interrupt();
        }
        closeAll();
    }

    private void closeAll(){
        if(socketClient == null || socketClient.isClosed() || !socketClient.isConnected()){
            return;
        }

        try {
            socketClient.shutdownInput();
            socketClient.shutdownOutput();
            socketClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startReceive(){
        if(receiveThread != null){
            receiveThread.interrupt();
        }
        receiveThread = new Thread(new Receiver());
        receiveThread.start();
    }
    private class Receiver implements Runnable{

        @Override
        public void run() {
            while (true){
                if(receiveThread.isInterrupted()){
                    break;
                }
                try {
                    InputStream inputStream = socketClient.getInputStream();
                    boolean badPackage = false;//a flag control the program blow
                    int countHeadBytesLength = 0;
                    byte[] headData = new byte[PACKAGE_HEAD_LENGTH];
                    while (countHeadBytesLength < PACKAGE_HEAD_LENGTH) {
                        int result = inputStream.read(headData, countHeadBytesLength, PACKAGE_HEAD_LENGTH - countHeadBytesLength);
                        if (result == -1) {
                            //it's a bad package when we read a package head
                            //but it not enough for 8 byte
                            badPackage = true;
                            break;
                        }
                        countHeadBytesLength += result;
                    }
                    if(badPackage){
                        Log.e(TAG,"badPackage!");
                        continue;
                    }
                    Log.d(TAG,"headData:"+ Arrays.toString(headData));

                    int packageType = getTypeFromHeadByte(headData);
                    int packageBodyBytesLength = getLengthFromHeadByte(headData);
                    Log.d(TAG,"new data received type:"+packageType+" body len:"+packageBodyBytesLength);

                    int countBodyBytesLength = 0;
                    byte[] bodyData = new byte[packageBodyBytesLength];
                    while (countBodyBytesLength < packageBodyBytesLength) {
                        int result = inputStream.read(bodyData, countBodyBytesLength, packageBodyBytesLength - countBodyBytesLength);
                        if (result == -1) {
                            //we follow the strict protocol
                            badPackage = true;
                            break;
                        }
                        countBodyBytesLength += result;
                    }
                    if(badPackage){
                        Log.e(TAG,"badPackage!");
                        continue;
                    }

                    if(receiveListener != null){
                        receiveListener.onReceive(packageType, bodyData);
                    }
                    bodyData=null;

                } catch (IOException e) {
                    e.printStackTrace();
                    if(receiveThread.isInterrupted()){
                        break;
                    }
                    tryConnect();
                    break;
                }

            }
        }
    }

    private int getLengthFromHeadByte(byte[] b) throws IOException{
        if(b.length!=8){
            throw new EOFException();
        }
        int ch1 = b[4] & 0x00FF;
        int ch2 = b[5] & 0x00FF;
        int ch3 = b[6] & 0x00FF;
        int ch4 = b[7] & 0x00FF;

        return ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0));
    }

    private int getTypeFromHeadByte(byte[] b) throws IOException{
        if(b.length!=8){
            throw new EOFException();
        }
        int ch1 = b[0] & 0x00FF;
        int ch2 = b[1] & 0x00FF;
        int ch3 = b[2] & 0x00FF;
        int ch4 = b[3] & 0x00FF;

        return ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0));
    }


}
