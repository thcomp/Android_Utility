package jp.co.thcomp.util;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

public class DialogHelper extends AbstractDialogHelper {
    public DialogHelper(Context context) {
        this(context, null);
    }

    public DialogHelper(Context context, Integer themeId) {
        super(context, themeId);
        mDialogHelper = new AlertDialogHelper(context, themeId);
    }
}
