package com.example.geyiyang.eric_x_music.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.geyiyang.eric_x_music.App.App;

import static com.example.geyiyang.eric_x_music.Constant.Constant.SP_NAME;

/**
 * Created by geyiyang on 2017/9/28.
 * 将当前list中播放音乐的position放入sharedPreference，背景播放重新开启app时可以取出
 */

public class SharedPreferenceUtils {
    private static SharedPreferences sharedPreferences;
    public static void put(final String key, final Object value) {
        Context context = App.getAppContext();
        sharedPreferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (value instanceof Integer) {
            editor.putInt(key, (Integer) value);
        } else if (value instanceof Float) {
            editor.putFloat(key, (Float) value);
        } else if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        } else if (value instanceof Long) {
            editor.putLong(key, (Long) value);
        } else {
            editor.putString(key, (String) value);
        }
        editor.commit();
    }

    public static Object get(Context context, String key, Object defaultObject) {
        try{
            SharedPreferences sp = App.getAppContext().getSharedPreferences(SP_NAME,
                    Context.MODE_PRIVATE);
            if (defaultObject instanceof String) {
                return sp.getString(key, (String) defaultObject);
            } else if (defaultObject instanceof Integer) {
                return sp.getInt(key, (Integer) defaultObject);
            } else if (defaultObject instanceof Boolean) {
                return sp.getBoolean(key, (Boolean) defaultObject);
            } else if (defaultObject instanceof Float) {
                return sp.getFloat(key, (Float) defaultObject);
            } else if (defaultObject instanceof Long) {
                return sp.getLong(key, (Long) defaultObject);
            }
        }catch(Exception e){
            put(key,0);
            e.printStackTrace();
            return defaultObject;
        }


        return defaultObject;
    }
}
