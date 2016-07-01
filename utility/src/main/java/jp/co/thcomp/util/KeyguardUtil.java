package jp.co.thcomp.util;

import android.app.KeyguardManager;
import android.content.Context;

public class KeyguardUtil {
    public static boolean isKeyguardExist(Context context){
        KeyguardManager keyguardManager = (KeyguardManager)context.getSystemService(Context.KEYGUARD_SERVICE);
        return keyguardManager.inKeyguardRestrictedInputMode();
    }
}
