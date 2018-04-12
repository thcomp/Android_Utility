package jp.co.thcomp.util;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

public class ProgressDialogHelper extends AbstractDialogHelper implements ProgressDialogHelperInterface {
    protected ProgressDialogHelperInterface mDialogHelper;

    public ProgressDialogHelper(Context context) {
        this(context, null);
    }

    public ProgressDialogHelper(Context context, Integer themeId) {
        super(context, themeId);
        mDialogHelper = new InnerProgressDialogHelper(context, themeId);
    }

    @Override
    public DialogHelperInterface progressMax(int max) {
        mDialogHelper.progressMax(max);
        return this;
    }

    @Override
    public DialogHelperInterface progress(int progress) {
        mDialogHelper.progress(progress);
        return this;
    }
}
