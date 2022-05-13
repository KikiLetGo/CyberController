package com.elexlab.cybercontroller.services;

import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.mlsdk.common.MLApplication;
import com.huawei.hms.mlsdk.common.MLException;
import com.huawei.hms.mlsdk.translate.MLTranslatorFactory;
import com.huawei.hms.mlsdk.translate.cloud.MLRemoteTranslateSetting;
import com.huawei.hms.mlsdk.translate.cloud.MLRemoteTranslator;

public class Translator {
    private MLRemoteTranslator mlRemoteTranslator;
    public Translator(){
        init();
    }
    private void init(){
        // 使用自定义的参数配置创建文本翻译器。
        MLRemoteTranslateSetting setting = new MLRemoteTranslateSetting
                .Factory()
                // 设置源语言的编码，使用ISO 639-1标准（中文繁体使用BCP-47标准）。此设置为可选项，如果不设置，将自动检测语种进行翻译。
                .setSourceLangCode("en")
                // 设置目标语言的编码，使用ISO 639-1标准（中文繁体使用BCP-47标准）。
                .setTargetLangCode("zh")
                .create();
        mlRemoteTranslator = MLTranslatorFactory.getInstance().getRemoteTranslator(setting);
    }

    public void translate(String sourceText,OnSuccessListener<String> onSuccessListener){
        final Task<String> task = mlRemoteTranslator.asyncTranslate(sourceText);
        task.addOnSuccessListener(new OnSuccessListener<String>() {
            @Override
            public void onSuccess(String text) {
                // 识别成功的处理逻辑。
                if(onSuccessListener != null){
                    onSuccessListener.onSuccess(text);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                e.printStackTrace();
                // 识别失败的处理逻辑。
                try {
                    MLException mlException = (MLException)e;
                    // 获取错误码，开发者可以对错误码进行处理，根据错误码进行差异化的页面提示。
                    int errorCode = mlException.getErrCode();
                    // 获取报错信息，开发者可以结合错误码，快速定位问题。
                    String errorMessage = mlException.getMessage();
                } catch (Exception error) {
                    // 转换错误处理。
                }
            }
        });
    }
}
