package jp.co.thcomp.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.view.View;

class InnerProgressDialogHelper implements ProgressDialogHelperInterface {
    private ProgressDialog mDialog;

    public InnerProgressDialogHelper(Context context, Integer progressType) {
        if (context == null) {
            throw new NullPointerException("context == null");
        }
        mDialog = new ProgressDialog(context, progressType);
    }

    @Override
    public void cancel() {
        try {
            mDialog.cancel();
        } catch (Exception e) {
            // 処理なし
        }
    }

    @Override
    public void dismiss() {
        try {
            mDialog.dismiss();
        } catch (Exception e) {
            // 処理なし
        }
    }

    @Override
    public void hide() {
        try {
            mDialog.hide();
        } catch (Exception e) {
            // 処理なし
        }
    }

    @Override
    public void show() {
    }

    @Override
    public boolean isShowing() {
        boolean ret = false;

        try {
            ret = mDialog.isShowing();
        } catch (Exception e) {
            // 処理なし
        }

        return ret;
    }

    @Override
    public DialogHelperInterface cancelable(boolean flag) {
        mDialog.setCancelable(flag);
        return this;
    }

    @Override
    public DialogHelperInterface canceledOnTouchOutside(boolean cancel) {
        mDialog.setCanceledOnTouchOutside(cancel);
        return this;
    }

    @Override
    public DialogHelperInterface cancelListener(DialogInterface.OnCancelListener listener) {
        mDialog.setOnCancelListener(listener);
        return this;
    }

    @Override
    public DialogHelperInterface dismissListener(DialogInterface.OnDismissListener listener) {
        mDialog.setOnDismissListener(listener);
        return this;
    }

    @Override
    public DialogHelperInterface keyListener(DialogInterface.OnKeyListener onKeyListener) {
        mDialog.setOnKeyListener(onKeyListener);
        return this;
    }

    @Override
    public DialogHelperInterface showListener(DialogInterface.OnShowListener listener) {
        mDialog.setOnShowListener(listener);
        return this;
    }

    @Override
    public DialogHelperInterface button(int whichButton, CharSequence text, DialogInterface.OnClickListener listener) {
        mDialog.setButton(whichButton, text, listener);
        return this;
    }

    @Override
    public DialogHelperInterface customTitle(View customTitleView) {
        mDialog.setCustomTitle(customTitleView);
        return this;
    }

    @Override
    public DialogHelperInterface icon(Drawable icon) {
        mDialog.setIcon(icon);
        return this;
    }

    @Override
    public DialogHelperInterface icon(int resId) {
        mDialog.setIcon(resId);
        return this;
    }

    @Override
    public DialogHelperInterface message(CharSequence message) {
        mDialog.setMessage(message);
        return this;
    }

    @Override
    public DialogHelperInterface title(int titleId) {
        mDialog.setTitle(titleId);
        return this;
    }

    @Override
    public DialogHelperInterface title(CharSequence title) {
        mDialog.setTitle(title);
        return this;
    }

    @Override
    public DialogHelperInterface view(View view) {
        mDialog.setView(view);
        return this;
    }

    @Override
    public DialogHelperInterface progressMax(int max) {
        mDialog.setMax(max);
        return this;
    }

    @Override
    public DialogHelperInterface progress(int progress) {
        mDialog.setProgress(progress);
        return this;
    }

    protected static class ButtonData {
        public CharSequence text;
        public DialogInterface.OnClickListener listener;

        public ButtonData(CharSequence text, DialogInterface.OnClickListener listener) {
            this.text = text;
            this.listener = listener;
        }
    }
}

