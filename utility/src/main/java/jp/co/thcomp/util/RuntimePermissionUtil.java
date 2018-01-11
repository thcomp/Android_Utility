package jp.co.thcomp.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;

public class RuntimePermissionUtil {
    private static Method ActivityCompatCheckSelfPermission = null;
    private static Method ActivityCompatRequestPermissions = null;

    static{
        try {
            Class activityCompatCl = Class.forName("android.support.v4.app.ActivityCompat");
            Class contextCompatCl = Class.forName("android.support.v4.content.ContextCompat");
            ActivityCompatCheckSelfPermission = contextCompatCl.getMethod("checkSelfPermission", Context.class, String.class);
            ActivityCompatRequestPermissions = activityCompatCl.getMethod("requestPermissions", Activity.class, String[].class, int.class);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public interface OnRequestPermissionsResultListener {
        public void onRequestPermissionsResult(String[] permissions, int[] grantResults);
    }

    private static byte sRequestCode = 0;
    private static final HashMap<Integer, OnRequestPermissionsResultListener> sRequestCodeListenerMap = new HashMap<>();

    /**
     * 実行時パーミッション許可のリクエスト送信
     *
     * @param activity
     * @param permissions
     */
    public static void requestPermissions(final Activity activity, final String[] permissions, final OnRequestPermissionsResultListener listener) {
        boolean needRequestPermission = false;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String permission : permissions) {
                if (checkSelfPermission(activity, permission) == PackageManager.PERMISSION_DENIED) {
                    needRequestPermission = true;
                    break;
                }
            }

            if (needRequestPermission) {
                int requestCode;
                synchronized (sRequestCodeListenerMap){
                    requestCode = sRequestCode;
                    sRequestCodeListenerMap.put((int)sRequestCode, listener);
                    if(sRequestCode == Byte.MAX_VALUE){
                        sRequestCode = 0;
                    }
                    sRequestCode++;
                }

                requestPermissions(activity, permissions, requestCode);
            }
        }

        if (!needRequestPermission) {
            if (listener != null) {
                ThreadUtil.runOnMainThread(activity, new Runnable() {
                    @Override
                    public void run() {
                        int[] grantResults = new int[permissions.length];
                        Arrays.fill(grantResults, PackageManager.PERMISSION_GRANTED);
                        listener.onRequestPermissionsResult(permissions, grantResults);
                    }
                });
            }
        }
    }

    /**
     * onRequestPermissionsResultラッパーメソッド
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     * @return true: 処理済み、false: 未処理
     */
    public static boolean onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        OnRequestPermissionsResultListener listener = null;
        synchronized (sRequestCodeListenerMap){
            listener = sRequestCodeListenerMap.remove(requestCode);
        }

        if (listener != null) {
            listener.onRequestPermissionsResult(permissions, grantResults);
            return true;
        }

        return false;
    }

    private static int checkSelfPermission(Activity activity, String permission){
        int ret = PackageManager.PERMISSION_DENIED;

        try {
            // ActivityCompatがリンクされていない場合、ActivityCompatCheckSelfPermissionがnullのため、NullPointerExceptionが通知されて終了させる想定
            ret = (int)ActivityCompatCheckSelfPermission.invoke(null, activity, permission);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return ret;
    }

    private static void requestPermissions(Activity activity, String[] permissions, int requestCode){
        try {
            // ActivityCompatがリンクされていない場合、ActivityCompatRequestPermissionsがnullのため、NullPointerExceptionが通知されて終了させる想定
            ActivityCompatRequestPermissions.invoke(null, activity, permissions, requestCode);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
