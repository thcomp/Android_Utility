package jp.co.thcomp.util;

import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.view.View;

public interface ProgressDialogHelperInterface extends DialogHelperInterface {
    public DialogHelperInterface progressMax(int max);

    public DialogHelperInterface progress(int progress);
}
