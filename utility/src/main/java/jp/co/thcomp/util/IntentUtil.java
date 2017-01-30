package jp.co.thcomp.util;

import android.content.Intent;
import android.net.Uri;

public class IntentUtil {
    public static Intent getLaunchMailerIntent(String title, String message){
        Intent intent = new Intent();

        intent.putExtra(Intent.EXTRA_EMAIL, "mailto:");
        intent.setData(Uri.parse("mailto:"));
        intent.setAction(Intent.ACTION_SENDTO);
        intent.putExtra(Intent.EXTRA_SUBJECT, title);
        intent.putExtra(Intent.EXTRA_TEXT, message);

        return intent;
    }
}
