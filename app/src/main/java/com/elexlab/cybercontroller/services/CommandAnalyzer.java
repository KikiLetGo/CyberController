package com.elexlab.cybercontroller.services;

import android.text.TextUtils;
import android.util.Log;

import com.elexlab.cybercontroller.CyberApplication;
import com.elexlab.cybercontroller.MainActivity;
import com.elexlab.cybercontroller.pojo.ScriptMessage;
import com.elexlab.cybercontroller.utils.AssetsUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 图一乐，不知道TNT的意图识别是怎么实现的，这里用一些正则的手段硬编码
 */
public class CommandAnalyzer {
    private final static String TAG = CommandAnalyzer.class.getSimpleName();
    private static Map<String,String> colorParmas = new HashMap<>();
    static {
        /*
         *只录入了部分颜色，实现原理是模拟按键，然后用快捷键切换颜色
         */
        colorParmas.put("红色","alt,h,f,c,down_arrow,down_arrow,down_arrow,down_arrow,down_arrow,down_arrow,right_arrow,enter");
        colorParmas.put("橙色","alt,h,f,c,down_arrow,down_arrow,down_arrow,down_arrow,down_arrow,down_arrow,right_arrow,right_arrow,enter");
        colorParmas.put("黄色","alt,h,f,c,down_arrow,down_arrow,down_arrow,down_arrow,down_arrow,down_arrow,right_arrow,right_arrow,right_arrow,enter");
        colorParmas.put("绿色","alt,h,f,c,down_arrow,down_arrow,down_arrow,down_arrow,down_arrow,down_arrow,right_arrow,right_arrow,right_arrow,right_arrow,right_arrow,enter");
        colorParmas.put("蓝色","alt,h,f,c,down_arrow,down_arrow,down_arrow,down_arrow,down_arrow,down_arrow,right_arrow,right_arrow,right_arrow,right_arrow,right_arrow,right_arrow,enter");

        colorParmas.put("白色","alt,h,f,c,down_arrow,enter");
        colorParmas.put("黑色","alt,h,f,c,right_arrow,enter");

    }
    public List<ScriptMessage> analyzeCommand(String command){
        if("求和".equals(command)){
            String script = AssetsUtils.loadCommandScripts(CyberApplication.getContext(),"key_event.py");
            String params = "alt,h,u,s";
            ScriptMessage scriptMessage = new ScriptMessage();
            scriptMessage.setScript(script);
            scriptMessage.setParams(params);
            return Arrays.asList(scriptMessage);

        }else{
            return analyzePPTCommand(command);
        }
    }

    private List<ScriptMessage> analyzePPTCommand(String command){
        String[] fontInfo =findFontSize(command);
        String fontSize = fontInfo[0];
        String fontSizeStr = fontInfo[1];

        String color =findColor(command);
        String italics =findItalics(command);
        String bold =findBold(command);
        String underline =findUnderline(command);

        String fontCommand = command.replace(color==null?"":color,"")
                .replace(italics==null?"":italics,"")
                .replace(bold==null?"":bold,"")
                .replace(underline==null?"":underline,"")
                .replace(fontSizeStr==null?"":fontSizeStr,"");
        Log.d(TAG,"fontCommand:"+fontCommand);

        String font = findFont(fontCommand);

        List<ScriptMessage> scriptMessages = new ArrayList<>();


        Log.d(TAG,"****************PPT 命令消息******************");



        if(color != null){
            Log.d(TAG,"color:"+color);
            String script = AssetsUtils.loadCommandScripts(CyberApplication.getContext(),"key_click.py");
            String params = colorParmas.get(color);
            ScriptMessage scriptMessage = new ScriptMessage();
            scriptMessage.setScript(script);
            scriptMessage.setParams(params);
            scriptMessages.add(scriptMessage);
        }
        if(italics != null){
            Log.d(TAG,"italics:"+italics);
            String script = AssetsUtils.loadCommandScripts(CyberApplication.getContext(),"key_event.py");
            String params = "ctrl,i";
            ScriptMessage scriptMessage = new ScriptMessage();
            scriptMessage.setScript(script);
            scriptMessage.setParams(params);
            scriptMessages.add(scriptMessage);
        }
        if(bold != null){
            Log.d(TAG,"bold:"+bold);
            String script = AssetsUtils.loadCommandScripts(CyberApplication.getContext(),"key_event.py");
            String params = "ctrl,b";
            ScriptMessage scriptMessage = new ScriptMessage();
            scriptMessage.setScript(script);
            scriptMessage.setParams(params);
            scriptMessages.add(scriptMessage);
        }
        if(underline != null){
            Log.d(TAG,"underline:"+underline);
            String script = AssetsUtils.loadCommandScripts(CyberApplication.getContext(),"key_event.py");
            String params = "ctrl,u";
            ScriptMessage scriptMessage = new ScriptMessage();
            scriptMessage.setScript(script);
            scriptMessage.setParams(params);
            scriptMessages.add(scriptMessage);
        }

        if(fontSize != null){
            Log.d(TAG,"fontSize:"+fontSize);

            //先选中字号
            String script1 = AssetsUtils.loadCommandScripts(CyberApplication.getContext(),"key_click.py");
            String params1 = "alt,h,f,s";
            ScriptMessage scriptMessage1 = new ScriptMessage();
            scriptMessage1.setScript(script1);
            scriptMessage1.setParams(params1);
            scriptMessages.add(scriptMessage1);

            //再输入
            String script = AssetsUtils.loadCommandScripts(CyberApplication.getContext(),"text_input.py");
            String params = fontSize;
            ScriptMessage scriptMessage = new ScriptMessage();
            scriptMessage.setScript(script);
            scriptMessage.setParams(params);
            scriptMessages.add(scriptMessage);

            //最后回车
            String script3 = AssetsUtils.loadCommandScripts(CyberApplication.getContext(),"key_click.py");
            String params3 = "enter";
            ScriptMessage scriptMessage3 = new ScriptMessage();
            scriptMessage3.setScript(script3);
            scriptMessage3.setParams(params3);
            scriptMessages.add(scriptMessage3);
        }

        if(!TextUtils.isEmpty(font)){
            Log.d(TAG,"font:"+font);
            //先选中字体
            String script1 = AssetsUtils.loadCommandScripts(CyberApplication.getContext(),"key_click.py");
            String params1 = "alt,h,f,f";
            ScriptMessage scriptMessage1 = new ScriptMessage();
            scriptMessage1.setScript(script1);
            scriptMessage1.setParams(params1);
            scriptMessages.add(scriptMessage1);

            //再输入
            String script = AssetsUtils.loadCommandScripts(CyberApplication.getContext(),"text_input.py");
            String params = font;
            ScriptMessage scriptMessage = new ScriptMessage();
            scriptMessage.setScript(script);
            scriptMessage.setParams(params);
            scriptMessages.add(scriptMessage);

            //最后回车
            String script3 = AssetsUtils.loadCommandScripts(CyberApplication.getContext(),"key_click.py");
            String params3 = "enter";
            ScriptMessage scriptMessage3 = new ScriptMessage();
            scriptMessage3.setScript(script3);
            scriptMessage3.setParams(params3);
            scriptMessages.add(scriptMessage3);
        }
        Log.d(TAG,"***********************************");



        return scriptMessages;
    }

    private String[] findFontSize(String command){
        Matcher matcher = Pattern.compile("\\d+(号|好|后)").matcher(command);

        String fontSizeStr = null;
        String fontSize = null;
        if (matcher.find( )) {
            fontSizeStr = matcher.group();
            fontSize = fontSizeStr.replaceAll("(号|好|后)","");
        }
        return new String[]{fontSize,fontSizeStr};
    }

    private String findColor(String command){
        Matcher matcher = Pattern.compile(".{1}色").matcher(command);
        String color = null;
        if (matcher.find( )) {
            color = matcher.group();
        }
        return color;
    }

    private String findItalics(String command){
        Matcher matcher = Pattern.compile("斜体").matcher(command);
        String italics = null;
        if (matcher.find( )) {
            italics = matcher.group();
        }
        return italics;
    }

    private String findBold(String command){
        Matcher matcher = Pattern.compile("加粗|加出").matcher(command);
        String bold = null;
        if (matcher.find( )) {
            bold = matcher.group();
        }
        return bold;
    }

    private String findUnderline(String command){
        Matcher matcher = Pattern.compile("下.{1}线").matcher(command);
        String underline = null;
        if (matcher.find( )) {
            underline = matcher.group();
        }
        return underline;
    }

    private String findFont(String fontCommand){
        if(TextUtils.isEmpty(fontCommand)){
            return null;
        }
        for(String font:fonts){
            if(font.contains(fontCommand)){
                return font;
            }
        }
        return null;
    }

    /*
     * 只cover了常见字体，更完整的做法是让电脑把所有字体下发过来，不想弄了。。。
     */
    private static String[] fonts = new String[]{
            //方正字体
            "方正榜书楷简体",
            "方正粗圆简体",
            "方正大黑简体",
            "方正大雅宋简体",
            "方正工业黑简体",
            "方正姚体",
            "方正兰亭黑简体",
            //widows自带
            "宋体",
            "仿宋",
            "新宋体",
            "幼圆",
            "楷体",
            "隶书",
            "黑体",
            "微软雅黑",
            //华文字体
            "华文琥珀",
            "华文新魏",
            "华文行楷",
            "华文隶书",
            "华文彩云",
            "华文宋体",
            "华文细黑",
            "华文楷体",
            "华文中宋",
            "华文仿宋"

    };
}
