package jp.co.thcomp.util;

import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.view.View;

public interface DialogHelperInterface {
    void cancel();

    void dismiss();

    void hide();

    void show();

    boolean isShowing();

    DialogHelperInterface cancelable(boolean flag);

    DialogHelperInterface canceledOnTouchOutside(boolean cancel);

    DialogHelperInterface cancelListener(DialogInterface.OnCancelListener listener);

    DialogHelperInterface dismissListener(DialogInterface.OnDismissListener listener);

    DialogHelperInterface keyListener(DialogInterface.OnKeyListener onKeyListener);

    DialogHelperInterface showListener(DialogInterface.OnShowListener listener);

    DialogHelperInterface button(int whichButton, CharSequence text, DialogInterface.OnClickListener listener);

    DialogHelperInterface customTitle(View customTitleView);

    DialogHelperInterface icon(Drawable icon);

    DialogHelperInterface icon(int resId);

    DialogHelperInterface message(CharSequence message);

    DialogHelperInterface title(int titleId);

    DialogHelperInterface title(CharSequence title);

    DialogHelperInterface view(View view);
}
