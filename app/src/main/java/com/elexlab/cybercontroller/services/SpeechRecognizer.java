package com.elexlab.cybercontroller.services;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.huawei.hms.mlsdk.asr.MLAsrConstants;
import com.huawei.hms.mlsdk.asr.MLAsrListener;
import com.huawei.hms.mlsdk.asr.MLAsrRecognizer;

public class SpeechRecognizer {
    private final static String TAG = SpeechRecognizer.class.getSimpleName();
    public interface Listener{
        void onRecognizingResults(String result);
        void onResults(String result);
    }

    private Context context;
    private MLAsrRecognizer mSpeechRecognizer;
    private String currentContent="";
    private Listener listener;
    public SpeechRecognizer(Context context) {
        this.context = context;
        init();
    }


    public String getCurrentContent() {
        return currentContent;
    }

    private void init(){
        mSpeechRecognizer = MLAsrRecognizer.createAsrRecognizer(context);
        mSpeechRecognizer.setAsrListener(new SpeechRecognitionListener());
    }

    // 回调实现MLAsrListener接口，实现接口中的方法。
    protected class SpeechRecognitionListener implements MLAsrListener {
        @Override
        public void onStartListening() {
            // 录音器开始接收声音。
        }

        @Override
        public void onStartingOfSpeech() {
            // 用户开始讲话，即语音识别器检测到用户开始讲话。
        }

        @Override
        public void onVoiceDataReceived(byte[] data, float energy, Bundle bundle) {
            // 返回给用户原始的PCM音频流和音频能量，该接口并非运行在主线程中，返回结果需要在子线程中处理。
        }

        @Override
        public void onRecognizingResults(Bundle partialResults) {
            // 从MLAsrRecognizer接收到持续语音识别的文本，该接口并非运行在主线程中，返回结果需要在子线程中处理。
            String transResult = partialResults.getString(MLAsrRecognizer.RESULTS_RECOGNIZING);
            Log.d(TAG, "onRecognizingResults is " + transResult);
            currentContent = transResult;
            if(listener != null){
                listener.onRecognizingResults(transResult);
            }


        }

        @Override
        public void onResults(Bundle results) {
            // 语音识别的文本数据，该接口并非运行在主线程中，返回结果需要在子线程中处理。
            String transResult = results.getString(MLAsrRecognizer.RESULTS_RECOGNIZED);
            Log.d(TAG, "onResults is " + transResult);
            currentContent = transResult;
            if(listener != null){
                listener.onResults(transResult);
            }

        }

        @Override
        public void onError(int error, String errorMessage) {
            // 识别发生错误后调用该接口，该接口并非运行在主线程中，返回结果需要在子线程中处理。
        }

        @Override
        public void onState(int state, Bundle params) {
            // 通知应用状态发生改变，该接口并非运行在主线程中，返回结果需要在子线程中处理。
        }
    }

    public void startRec(Listener listener){
        this.listener = listener;
        // 新建Intent，用于配置语音识别参数。
        Intent mSpeechRecognizerIntent = new Intent(MLAsrConstants.ACTION_HMS_ASR_SPEECH);
        // 通过Intent进行语音识别参数设置。
        mSpeechRecognizerIntent
                // 设置识别语言为英语，若不设置，则默认识别英语。支持设置："zh-CN":中文；"en-US":英语；"fr-FR":法语；"es-ES":西班牙语；"de-DE":德语；"it-IT":意大利语；"ar": 阿拉伯语；"ru-RU":俄语；“th_TH”：泰语；“ms_MY”：马来语；“fil_PH”：菲律宾语。
                .putExtra(MLAsrConstants.LANGUAGE, "zh-CN")
                // 设置识别文本返回模式为边识别边出字，若不设置，默认为边识别边出字。支持设置：
                // MLAsrConstants.FEATURE_WORDFLUX：通过onRecognizingResults接口，识别同时返回文字；
                // MLAsrConstants.FEATURE_ALLINONE：识别完成后通过onResults接口返回文字。
                .putExtra(MLAsrConstants.FEATURE, MLAsrConstants.FEATURE_WORDFLUX)
                // 设置使用场景，MLAsrConstants.SCENES_SHOPPING：表示购物，仅支持中文，该场景对华为商品名识别进行了优化。
                .putExtra(MLAsrConstants.SCENES, MLAsrConstants.SCENES_SHOPPING);
        // 启动语音识别。
        mSpeechRecognizer.startRecognizing(mSpeechRecognizerIntent);

    }

    public void refresh(){
        this.currentContent = "";
    }

    public void destroy(){
        if (mSpeechRecognizer!= null) {
            mSpeechRecognizer.destroy();
        }
    }
}
