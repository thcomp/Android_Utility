package jp.co.thcomp.util;

import android.content.Intent;
import android.net.Uri;

import java.io.File;

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

    public static Intent getLaunchMailerIntent(String toAddress, String title, String message, String attachmentFilePath){
        return getLaunchMailerIntent(toAddress, title, message, new File(attachmentFilePath));
    }

    public static Intent getLaunchMailerIntent(String toAddress, String title, String message, File attachmentFile){
        return getLaunchMailerIntent(toAddress, title, message, Uri.fromFile(attachmentFile));
    }

    public static Intent getLaunchMailerIntent(String toAddress, String title, String message, Uri attachmentFileUri){
        Intent intent = getLaunchMailerIntent(toAddress, title, message);
        intent.putExtra(Intent.EXTRA_STREAM, attachmentFileUri);
        return intent;
    }
}
