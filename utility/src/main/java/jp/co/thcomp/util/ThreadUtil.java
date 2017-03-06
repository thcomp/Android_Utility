package jp.co.thcomp.util;

import android.content.Context;
import android.os.Handler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ThreadUtil {
    public static boolean IsOnMainThread(Context context) {
        return Thread.currentThread().equals(context.getMainLooper().getThread());
    }

    public static void runOnMainThread(final Context context, final Runnable runnable) {
        if (IsOnMainThread(context)) {
            runnable.run();
        } else {
            Handler handler = new Handler(context.getMainLooper());
            handler.post(runnable);
        }
    }

    public static void runOnMainThread(final Context context, final Object targetObject, final Method method, final Object... params) {
        if (IsOnMainThread(context)) {
            try {
                method.invoke(targetObject, params);
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            }
        } else {
            Handler handler = new Handler(context.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    runOnMainThread(context, targetObject, method, params);
                }
            });
        }
    }

    public static void delayRunOnMainThread(final Context context, final long delayTimeMS, final Runnable runnable) {
        Handler handler = new Handler(context.getMainLooper());
        handler.postDelayed(runnable, delayTimeMS);
    }

    public static void delayRunOnMainThread(final Context context, final long delayTimeMS, final Object targetObject, final Method method, final Object... params) {
        Handler handler = new Handler(context.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                runOnMainThread(context, targetObject, method, params);
            }
        }, delayTimeMS);
    }

    public static void runOnWorkThread(final Context context, final Runnable runnable) {
        if (IsOnMainThread(context)) {
            new Thread(runnable).start();
        } else {
            runnable.run();
        }
    }

    public static void runOnWorkThread(final Context context, final Object targetObject, final Method method, final Object... params) {
        if (IsOnMainThread(context)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    runOnWorkThread(context, targetObject, method, params);
                }
            }).start();
        } else {
            try {
                method.invoke(targetObject, params);
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            }
        }
    }

    public static void delayRunOnWorkThread(final Context context, final long delayTimeMS, final Runnable runnable) {
        Handler handler = new Handler(context.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                new Thread(runnable).start();
            }
        }, delayTimeMS);
    }

    public static void delayRunOnWorkThread(final Context context, final long delayTimeMS, final Object targetObject, final Method method, final Object... params) {
        Handler handler = new Handler(context.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        runOnWorkThread(context, targetObject, method, params);
                    }
                }).start();
            }
        }, delayTimeMS);
    }

    /**
     * ワンタイムセマフォ
     * startをコールする前にstopがコールされた場合にstartにて停止し続けないセマフォ
     * ※ waitをコールする前にリスナーを登録する機能を実施すると稀に実施した中で同一スレッド上でリスナーが
     * コールされ、そのタイミングでnotifyされてしまうと、その後waitし続けてしまうものを解消するのが目的
     * 再利用する場合はinitializeを実施すること
     */
    public static class OnetimeSemaphore {
        private boolean stopped = false;

        public OnetimeSemaphore() {
            initialize();
        }

        public void start() {
            synchronized (this) {
                if (!stopped) {
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                    }
                } else {
                    // 既に停止されている場合は、waitは掛けずに終了
                }
            }
        }

        public boolean start(long timeout) {
            boolean ret = true;

            synchronized (this) {
                if (!stopped) {
                    try {
                        this.wait(timeout);
                        ret = stopped;
                    } catch (InterruptedException e) {
                        ret = false;
                    }
                } else {
                    // 既に停止されている場合は、waitは掛けずに終了
                }
            }

            return ret;
        }

        public void stop() {
            synchronized (this) {
                if (!stopped) {
                    this.notify();
                    stopped = true;
                }
            }
        }

        public void initialize() {
            stopped = false;
        }
    }
}
