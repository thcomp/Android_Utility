package jp.co.thcomp.util;

import android.content.Intent;
import android.net.Uri;

public class IntentUtil {
    public static Intent getLaunchMailerIntent(String toAddress, String title, String message){
        Intent intent = new Intent();

        if(!toAddress.startsWith("mailto:")){
            toAddress = "mailto:" + toAddress;
        }

        intent.putExtra(Intent.EXTRA_EMAIL, "mailto:");
        intent.setData(Uri.parse(toAddress));
        intent.setAction(Intent.ACTION_SENDTO);
        intent.putExtra(Intent.EXTRA_SUBJECT, title);
        intent.putExtra(Intent.EXTRA_TEXT, message);

        return intent;
    }
}
