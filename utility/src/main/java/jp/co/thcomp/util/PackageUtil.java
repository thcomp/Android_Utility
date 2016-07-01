package jp.co.thcomp.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

public class PackageUtil {
    public static String getApplicationVersionName(Context context){
        String ret = null;
        ApplicationInfo appInfo = context.getApplicationInfo();
        PackageManager packageManager = context.getPackageManager();

        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(appInfo.packageName, 0);
            ret = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return ret;
    }


}
