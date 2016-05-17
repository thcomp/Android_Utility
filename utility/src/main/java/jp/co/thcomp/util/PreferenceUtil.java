package jp.co.thcomp.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.util.Log;

import java.util.HashMap;

public class PreferenceUtil {
    private static final String DEFAULT_PREF = "app_prefs";
    private static HashMap<String, HashMap<String, Object>> PREF_CACHE = new HashMap<String, HashMap<String, Object>>();

    public static final String getDefaultPrefName(){
    	return DEFAULT_PREF;
    }
    
    private static Object getPrefCache(String prefName, String key, Class<?> compClass){
        Object tempObject = null;
        HashMap<String, Object> keyCache = PREF_CACHE.get(prefName);
        if(keyCache == null){
            keyCache = new HashMap<String, Object>();
            PREF_CACHE.put(prefName, keyCache);
        }else{
            tempObject = PREF_CACHE.get(key);
        }

        if(tempObject != null && (tempObject.getClass() == compClass)){
        }else{
            tempObject = null;
        }

        return tempObject;
    }
    
    private static void setPrefCache(String prefName, String key, Object cacheObject){
        if(cacheObject != null){
            HashMap<String, Object> keyCache = PREF_CACHE.get(prefName);
            if(keyCache == null){
                keyCache = new HashMap<String, Object>();
                PREF_CACHE.put(prefName, keyCache);
            }

            keyCache.put(key, cacheObject);
        }
    }

    public static void registerOnSharedPreferenceChangeListener(Context context, String prefName, OnSharedPreferenceChangeListener listener){
        SharedPreferences pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        pref.registerOnSharedPreferenceChangeListener(listener);
    }

    public static void registerOnSharedPreferenceChangeListener(Context context, OnSharedPreferenceChangeListener listener){
        registerOnSharedPreferenceChangeListener(context, DEFAULT_PREF, listener);
    }

    public static void unregisterOnSharedPreferenceChangeListener(Context context, String prefName, OnSharedPreferenceChangeListener listener){
        SharedPreferences pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        pref.unregisterOnSharedPreferenceChangeListener(listener);
    }

    public static void unregisterOnSharedPreferenceChangeListener(Context context, OnSharedPreferenceChangeListener listener){
        unregisterOnSharedPreferenceChangeListener(context, DEFAULT_PREF, listener);
    }

    public static String readPrefString(Context context, String prefName, String key){
        String ret = null;
        Object tempObject = getPrefCache(prefName, key, String.class);

        if(tempObject != null){
            ret = (String)tempObject;
        }else{
            SharedPreferences pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
    
            try{
                ret = pref.getString(key, null);
                setPrefCache(prefName, key, ret);
            }catch(ClassCastException e){
                Log.e("PreferenceUtil", "readPrefString catch exception: " + e.getLocalizedMessage());
            }
        }

        return ret;
    }

    public static long readPrefLong(Context context, String prefName, String key, long defValue){
        long ret = defValue;
        Object tempObject = getPrefCache(prefName, key, Long.class);

        if(tempObject != null){
            ret = (Long)tempObject;
        }else{
            SharedPreferences pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);

            try{
                ret = pref.getLong(key, defValue);
                setPrefCache(prefName, key, ret);
            }catch(ClassCastException e){
                Log.e("PreferenceUtil", "readPrefLong catch exception: " + e.getLocalizedMessage());
            }
        }

        return ret;
    }

    public static int readPrefInt(Context context, String prefName, String key, int defValue){
        int ret = defValue;
        Object tempObject = getPrefCache(prefName, key, Integer.class);

        if(tempObject != null){
            ret = (Integer)tempObject;
        }else{
            SharedPreferences pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);

            try{
                ret = pref.getInt(key, defValue);
                setPrefCache(prefName, key, ret);
            }catch(ClassCastException e){
                Log.e("PreferenceUtil", "readPrefInt catch exception: " + e.getLocalizedMessage());
            }
        }

        return ret;
    }

    public static float readPrefFloat(Context context, String prefName, String key, float defValue){
        float ret = defValue;
        Object tempObject = getPrefCache(prefName, key, Float.class);

        if(tempObject != null){
            ret = (Integer)tempObject;
        }else{
            SharedPreferences pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
    
            try{
                ret = pref.getFloat(key, defValue);
                setPrefCache(prefName, key, ret);
            }catch(ClassCastException e){
                Log.e("PreferenceUtil", "readPrefFloat catch exception: " + e.getLocalizedMessage());
            }
        }

        return ret;
    }

    public static boolean readPrefBoolean(Context context, String prefName, String key, boolean defValue){
        boolean ret = defValue;
        Object tempObject = getPrefCache(prefName, key, Boolean.class);

        if(tempObject != null){
            ret = (Boolean)tempObject;
        }else{
            SharedPreferences pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
    
            try{
                ret = pref.getBoolean(key, defValue);
                setPrefCache(prefName, key, ret);
            }catch(ClassCastException e){
                Log.e("PreferenceUtil", "readPrefBoolean catch exception: " + e.getLocalizedMessage());
            }
        }

        return ret;
    }

    public static String readPrefString(Context context, String key){
        return readPrefString(context, DEFAULT_PREF, key);
    }

    public static long readPrefLong(Context context, String key, long defValue){
        return readPrefLong(context, DEFAULT_PREF, key, defValue);
    }

    public static int readPrefInt(Context context, String key, int defValue){
        return readPrefInt(context, DEFAULT_PREF, key, defValue);
    }

    public static float readPrefFloat(Context context, String key, float defValue){
        return readPrefFloat(context, DEFAULT_PREF, key, defValue);
    }

    public static boolean readPrefBoolean(Context context, String key, boolean defValue){
        return readPrefBoolean(context, DEFAULT_PREF, key, defValue);
    }

    public static void writePref(Context context, String prefName, String key, String value){
        setPrefCache(prefName, key, value);

        Editor edit = null;
        SharedPreferences pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        edit = pref.edit();
        edit.putString(key, value);
        edit.commit();
    }

    public static void writePref(Context context, String prefName, String key, long value){
        setPrefCache(prefName, key, value);

        Editor edit = null;
        SharedPreferences pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        edit = pref.edit();
        edit.putLong(key, value);
        edit.commit();
    }

    public static void writePref(Context context, String prefName, String key, int value){
        setPrefCache(prefName, key, value);

        Editor edit = null;
        SharedPreferences pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        edit = pref.edit();
        edit.putInt(key, value);
        edit.commit();
    }

    public static void writePref(Context context, String prefName, String key, float value){
        setPrefCache(prefName, key, value);

        Editor edit = null;
        SharedPreferences pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        edit = pref.edit();
        edit.putFloat(key, value);
        edit.commit();
    }

    public static void writePref(Context context, String prefName, String key, boolean value){
        setPrefCache(prefName, key, value);

        Editor edit = null;
        SharedPreferences pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        edit = pref.edit();
        edit.putBoolean(key, value);
        edit.commit();
    }

    public static void writePref(Context context, String key, String value){
        writePref(context, DEFAULT_PREF, key, value);
    }

    public static void writePref(Context context, String key, long value){
        writePref(context, DEFAULT_PREF, key, value);
    }

    public static void writePref(Context context, String key, int value){
        writePref(context, DEFAULT_PREF, key, value);
    }

    public static void writePref(Context context, String key, float value){
        writePref(context, DEFAULT_PREF, key, value);
    }

    public static void writePref(Context context, String key, boolean value){
        writePref(context, DEFAULT_PREF, key, value);
    }
}
