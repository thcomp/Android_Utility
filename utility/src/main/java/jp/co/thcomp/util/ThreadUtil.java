package jp.co.thcomp.util;

import android.content.Context;

public class ThreadUtil {
    public static boolean IsOnMainThread(Context context){
        return Thread.currentThread().equals(context.getMainLooper().getThread());
    }
}
