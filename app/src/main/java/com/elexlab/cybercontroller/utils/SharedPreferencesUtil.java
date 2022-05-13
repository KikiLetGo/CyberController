package com.elexlab.cybercontroller.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.Map;

public class SharedPreferencesUtil {
    private final static String TAG = SharedPreferencesUtil.class.getSimpleName();
    public static void setPreferences(Context context, String preference, Map<String,Object> keyValues){
        SharedPreferences sharedPreferences = context.getSharedPreferences(preference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑
        for(String key:keyValues.keySet()){
            Object value = keyValues.get(key);
            if(value instanceof String){
                editor.putString(key, (String) value);
            }else if(value instanceof Integer){
                editor.putInt(key, (Integer) value);
            }else if(value instanceof Long){
                editor.putLong(key, (Long) value);
            }else if(value instanceof Boolean){
                editor.putBoolean(key, (Boolean) value);
            }else if(value instanceof Float){
                editor.putFloat(key, (Float) value);
            }
        }
        editor.commit();
    }

    public static <T> void setPreference(Context context, String preference, String key, T value){
        SharedPreferences sharedPreferences = context.getSharedPreferences(preference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑
        if(value instanceof String){
            editor.putString(key, (String) value);
        }else if(value instanceof Integer){
            editor.putInt(key, (Integer) value);
        }else if(value instanceof Long){
            editor.putLong(key, (Long) value);
        }else if(value instanceof Boolean){
            editor.putBoolean(key, (Boolean) value);
        }else if(value instanceof Float){
            editor.putFloat(key, (Float) value);
        }
        editor.commit();
    }

    public static <T> T getPreference(Context context,String preference,String key,T defValue){
        SharedPreferences sharedPreferences = context.getSharedPreferences(preference, Context.MODE_PRIVATE);
        Object value = null;
        if(defValue instanceof String){
            value = sharedPreferences.getString(key,(String)defValue);
        }else if(defValue instanceof Integer){
            value = sharedPreferences.getInt(key,(Integer)defValue);

        }else if(defValue instanceof Long){
            value = sharedPreferences.getLong(key,(Long)defValue);

        }else if(defValue instanceof Boolean){
            value = sharedPreferences.getBoolean(key,(Boolean)defValue);

        }else if(defValue instanceof Float){
            value = sharedPreferences.getFloat(key,(Float)defValue);

        }
        return (T) value;
    }
}
