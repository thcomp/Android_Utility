package jp.co.thcomp.util;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {
	public static void showToast(final Context context, final CharSequence text, final int duration){
		ThreadUtil.runOnMainThread(context, new Runnable() {
			@Override
			public void run() {
				Toast.makeText(context, text, duration).show();
			}
		});
	}

	public static void showToast(final Context context, final int resId, final int duration){
		ThreadUtil.runOnMainThread(context, new Runnable() {
			@Override
			public void run() {
				Toast.makeText(context, resId, duration).show();
			}
		});
	}
}
