package jp.co.thcomp.util;

import android.content.Context;

public class DeviceUtil {
    public static String getAndroidID(Context context){
        return android.provider.Settings.Secure.getString(context.getContentResolver(),android.provider.Settings.Secure.ANDROID_ID);
    }
}
