package jp.co.thcomp.util;

import android.content.Context;
import android.os.Handler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ThreadUtil {
    public static boolean IsOnMainThread(Context context){
        return Thread.currentThread().equals(context.getMainLooper().getThread());
    }

    public static void runOnMainThread(final Context context, final Runnable runnable){
        if(IsOnMainThread(context)){
            runnable.run();
        }else{
            Handler handler = new Handler(context.getMainLooper());
            handler.post(runnable);
        }
    }

    public static void runOnMainThread(final Context context, final Object targetObject, final Method method, final Object... params){
        if(IsOnMainThread(context)){
            try {
                method.invoke(targetObject, params);
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            }
        }else{
            Handler handler = new Handler(context.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    runOnMainThread(context, targetObject, method, params);
                }
            });
        }
    }

    public static void runOnWorkThread(final Context context, final Runnable runnable){
        if(IsOnMainThread(context)){
            new Thread(runnable).start();
        }else{
            runnable.run();
        }
    }

    public static void runOnWorkThread(final Context context, final Object targetObject, final Method method, final Object... params){
        if(IsOnMainThread(context)){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    runOnWorkThread(context, targetObject, method, params);
                }
            }).start();
        }else{
            try {
                method.invoke(targetObject, params);
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            }
        }
    }
}
