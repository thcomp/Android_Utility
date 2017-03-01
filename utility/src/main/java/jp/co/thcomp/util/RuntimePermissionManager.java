package jp.co.thcomp.util;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class RuntimePermissionManager {
    public interface OnRequestPermissionsResultListener {
        public void onRequestPermissionsResult(@NonNull String[] permissions, @NonNull int[] grantResults);
    }

    private HashMap<Integer, OnRequestPermissionsResultListener> mRequestCodeListenerMap = new HashMap<Integer, OnRequestPermissionsResultListener>();

    /**
     * 実行時パーミッション許可のリクエスト送信
     *
     * @param activity
     * @param permissions
     */
    public void requestPermission(final Activity activity, final String[] permissions, final OnRequestPermissionsResultListener listener) {
        boolean needRequestPermission = false;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> needRequestPermissionList = new ArrayList<String>();

            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_DENIED) {
                    needRequestPermission = true;
                    break;
                }
            }

            if (needRequestPermission) {
                int requestCode = permissions.hashCode() & 0x0000FFFF;
                mRequestCodeListenerMap.put(requestCode, listener);
                ActivityCompat.requestPermissions(activity, permissions, requestCode);
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
    public boolean onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        OnRequestPermissionsResultListener listener = mRequestCodeListenerMap.remove(requestCode);
        if (listener != null) {
            listener.onRequestPermissionsResult(permissions, grantResults);
            return true;
        }

        return false;
    }
}
