package lc.test.case_fase.util;

import android.content.Context;
import android.content.SharedPreferences;

public interface SpTools {

    static SharedPreferences getSharedPreferences(Context context){
        return context.getSharedPreferences("config", Context.MODE_PRIVATE);
    }

    static void putBoolean(Context context,String key,boolean value){
        getSharedPreferences(context).edit().putBoolean(key,value).apply();
    }

    static boolean getBoolean(Context context,String key,boolean defaultValue){
        return getSharedPreferences(context).getBoolean(key,defaultValue);
    }

    static void putString(Context context,String key,String value){
        getSharedPreferences(context).edit().putString(key,value).apply();
    }

    static String getString(Context context,String key,String defaultValue){
        return getSharedPreferences(context).getString(key,defaultValue);
    }

    static void remove(Context context,String key){
        getSharedPreferences(context).edit().remove(key).apply();
    }
}
