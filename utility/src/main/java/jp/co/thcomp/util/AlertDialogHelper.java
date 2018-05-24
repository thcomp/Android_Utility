package jp.co.thcomp.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.view.View;

class AlertDialogHelper implements DialogHelperInterface {
    private Context mContext;
    private AlertDialog.Builder mBuilder;
    private AlertDialog mDialog;
    private boolean mCanceledOnTouchOutside = false;
    private boolean mIsShowing = false;
    private DialogInterface.OnDismissListener mDismissListener;
    private DialogInterface.OnShowListener mShowListener;

    public AlertDialogHelper(Context context, Integer themeId) {
        if (context == null) {
            throw new NullPointerException("context == null");
        }
        mContext = context;

        if (themeId != null) {
            mBuilder = new AlertDialog.Builder(mContext, themeId);
        } else {
            mBuilder = new AlertDialog.Builder(mContext);
        }
    }

    @Override
    public void cancel() {
        try {
            mDialog.cancel();
            mIsShowing = false;
        } catch (Exception e) {
            // 処理なし
        }
    }

    @Override
    public void dismiss() {
        try {
            mDialog.dismiss();
            mIsShowing = false;
        } catch (Exception e) {
            // 処理なし
        }
    }

    @Override
    public void hide() {
        try {
            mDialog.hide();
            mIsShowing = false;
        } catch (Exception e) {
            // 処理なし
        }
    }

    @Override
    public void show() {
        if (mDialog == null) {
            mDialog = mBuilder.create();
            mDialog.setOnShowListener(mShowListener);
            mDialog.setOnDismissListener(mDismissListener);
            mDialog.setCanceledOnTouchOutside(mCanceledOnTouchOutside);
        }

        if(!mIsShowing){
            mDialog.show();
            mIsShowing = true;
        }
    }

    @Override
    public boolean isShowing() {
        return mIsShowing;
    }

    @Override
    public DialogHelperInterface cancelable(boolean flag) {
        mBuilder.setCancelable(flag);
        if (mDialog != null) {
            mDialog.setCancelable(flag);
        }
        return this;
    }

    @Override
    public DialogHelperInterface canceledOnTouchOutside(boolean cancel) {
        try {
            mDialog.setCanceledOnTouchOutside(cancel);
        } catch (Exception e) {
            mCanceledOnTouchOutside = cancel;
        }
        return this;
    }

    @Override
    public DialogHelperInterface cancelListener(DialogInterface.OnCancelListener listener) {
        mBuilder.setOnCancelListener(listener);
        if (mDialog != null) {
            mDialog.setOnCancelListener(listener);
        }
        return this;
    }

    @Override
    public DialogHelperInterface dismissListener(DialogInterface.OnDismissListener listener) {
        try {
            mDialog.setOnDismissListener(listener);
        } catch (Exception e) {
            mDismissListener = listener;
        }
        return this;
    }

    @Override
    public DialogHelperInterface keyListener(DialogInterface.OnKeyListener onKeyListener) {
        mBuilder.setOnKeyListener(onKeyListener);
        if (mDialog != null) {
            mDialog.setOnKeyListener(onKeyListener);
        }
        return this;
    }

    @Override
    public DialogHelperInterface showListener(DialogInterface.OnShowListener listener) {
        try {
            mDialog.setOnShowListener(listener);
        } catch (Exception e) {
            mShowListener = listener;
        }
        return this;
    }

    @Override
    public DialogHelperInterface button(int whichButton, CharSequence text, DialogInterface.OnClickListener listener) {
        switch (whichButton) {
            case Dialog.BUTTON_POSITIVE:
                mBuilder.setPositiveButton(text, listener);
                break;
            case Dialog.BUTTON_NEGATIVE:
                mBuilder.setNegativeButton(text, listener);
                break;
            case Dialog.BUTTON_NEUTRAL:
                mBuilder.setNeutralButton(text, listener);
                break;
        }

        if (mDialog != null) {
            mDialog.setButton(whichButton, text, listener);
        }

        return this;
    }

    @Override
    public DialogHelperInterface customTitle(View customTitleView) {
        mBuilder.setCustomTitle(customTitleView);
        if (mDialog != null) {
            mDialog.setCustomTitle(customTitleView);
        }
        return this;
    }

    @Override
    public DialogHelperInterface icon(Drawable icon) {
        mBuilder.setIcon(icon);
        if (mDialog != null) {
            mDialog.setIcon(icon);
        }
        return this;
    }

    @Override
    public DialogHelperInterface icon(int resId) {
        mBuilder.setIcon(resId);
        if (mDialog != null) {
            mDialog.setIcon(resId);
        }
        return this;
    }

    @Override
    public DialogHelperInterface message(CharSequence message) {
        mBuilder.setMessage(message);
        if (mDialog != null) {
            mDialog.setMessage(message);
        }
        return this;
    }

    @Override
    public DialogHelperInterface title(int titleId) {
        mBuilder.setTitle(titleId);
        if (mDialog != null) {
            mDialog.setTitle(titleId);
        }
        return this;
    }

    @Override
    public DialogHelperInterface title(CharSequence title) {
        mBuilder.setTitle(title);
        if (mDialog != null) {
            mDialog.setTitle(title);
        }
        return this;
    }

    @Override
    public DialogHelperInterface view(View view) {
        mBuilder.setView(view);
        if (mDialog != null) {
            mDialog.setView(view);
        }
        return this;
    }
}

