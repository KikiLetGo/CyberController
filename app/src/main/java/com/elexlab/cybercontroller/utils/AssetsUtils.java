package com.elexlab.cybercontroller.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Arrays;
import java.util.List;

public class AssetsUtils {
    public static Bitmap getImageFromAssetsFile(Context context, String fileName) {
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

    public static List<String> listFiles(Context context,String dir){
        AssetManager am = context.getResources().getAssets();
        try {
            return Arrays.asList(am.list(dir));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String loadCommandScripts(Context context, String fileName) {
        Bitmap image = null;
        AssetManager am = context.getResources().getAssets();
        BufferedReader bufferedReader = null;
        String content = "";

        try {
            InputStreamReader inputStreamReader = new InputStreamReader(am.open("command_scripts"+ File.separator +fileName));
            bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            while((line=bufferedReader.readLine())!=null){
                content+=line+"\n";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(bufferedReader != null){
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return content;
    }
}
