package jp.co.thcomp.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class DisplayUtil {
    private static final DisplayMetrics mDefaultDisplayMetrics = new DisplayMetrics();
    private static final String TAG = "DisplayUtil";

    public static DisplayMetrics getDefaultDisplayMetrics(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(mDefaultDisplayMetrics);

        return mDefaultDisplayMetrics;
    }

    public static float getDefaultDisplayDensity(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(mDefaultDisplayMetrics);

        return mDefaultDisplayMetrics.density;
    }

    public static float getDefaultDisplayHeight(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(mDefaultDisplayMetrics);

        return mDefaultDisplayMetrics.heightPixels;
    }

    public static float getDefaultDisplayWidth(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(mDefaultDisplayMetrics);

        return mDefaultDisplayMetrics.widthPixels;
    }

}
