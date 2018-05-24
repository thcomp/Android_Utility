package jp.co.thcomp.util;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

abstract public class AbstractDialogHelper implements DialogHelperInterface {
    protected static final HashMap<String, DialogHelperInterface> sManagedDialogMap = new HashMap<>();
    protected Context mContext;
    protected DialogHelperInterface mDialogHelper;
    protected String mShowingDialogId;

    public AbstractDialogHelper(Context context) {
        this(context, null);
    }

    public AbstractDialogHelper(Context context, Integer themeId) {
        mContext = context;
    }

    public static void clear(){
        synchronized (sManagedDialogMap){
            StringBuilder dumpLog = new StringBuilder();

            dumpLog.append("left dialogs:\n");
            for(Map.Entry<String, DialogHelperInterface> entry : sManagedDialogMap.entrySet()){
                dumpLog.append("\t").append(entry.getKey()).append("\n");
                entry.getValue().dismiss();
            }

            LogUtil.i(AbstractDialogHelper.class.getSimpleName(), dumpLog.toString());
            sManagedDialogMap.clear();
        }
    }

    @Override
    public void cancel() {
        if(ThreadUtil.IsOnMainThread(mContext)){
            mDialogHelper.cancel();
        }else{
            ThreadUtil.runOnMainThread(mContext, new Runnable() {
                @Override
                public void run() {
                    cancel();
                }
            });
        }
    }

    @Override
    public void hide() {
        dismiss();
    }

    @Override
    public void dismiss() {
        if(ThreadUtil.IsOnMainThread(mContext)) {
            synchronized (sManagedDialogMap) {
                if (mShowingDialogId != null) {
                    sManagedDialogMap.remove(mShowingDialogId);
                    mShowingDialogId = null;
                    mDialogHelper.dismiss();
                }
            }
        }else{
            ThreadUtil.runOnMainThread(mContext, new Runnable() {
                @Override
                public void run() {
                    dismiss();
                }
            });
        }
    }

    @Override
    public void show() {
        if(ThreadUtil.IsOnMainThread(mContext)) {
            synchronized (sManagedDialogMap) {
                if (mShowingDialogId == null) {
                    StackTraceElement element = StackTraceUtil.getClassController(AbstractDialogHelper.class);
                    mShowingDialogId = element.getClassName() + "#" + element.getLineNumber();
                    sManagedDialogMap.put(mShowingDialogId, mDialogHelper);
                    mDialogHelper.show();
                }
            }
        }else{
            ThreadUtil.runOnMainThread(mContext, new Runnable() {
                @Override
                public void run() {
                    show();
                }
            });
        }
    }

    @Override
    public boolean isShowing() {
        return mDialogHelper.isShowing();
    }

    @Override
    public DialogHelperInterface cancelable(boolean flag) {
        mDialogHelper.cancelable(flag);
        return this;
    }

    @Override
    public DialogHelperInterface canceledOnTouchOutside(boolean cancel) {
        mDialogHelper.canceledOnTouchOutside(cancel);
        return this;
    }

    @Override
    public DialogHelperInterface cancelListener(DialogInterface.OnCancelListener listener) {
        mDialogHelper.cancelListener(listener);
        return this;
    }

    @Override
    public DialogHelperInterface dismissListener(DialogInterface.OnDismissListener listener) {
        mDialogHelper.dismissListener(listener);
        return this;
    }

    @Override
    public DialogHelperInterface keyListener(DialogInterface.OnKeyListener onKeyListener) {
        mDialogHelper.keyListener(onKeyListener);
        return this;
    }

    @Override
    public DialogHelperInterface showListener(DialogInterface.OnShowListener listener) {
        mDialogHelper.showListener(listener);
        return this;
    }

    @Override
    public DialogHelperInterface button(int whichButton, CharSequence text, DialogInterface.OnClickListener listener) {
        mDialogHelper.button(whichButton, text, listener);
        return this;
    }

    @Override
    public DialogHelperInterface customTitle(View customTitleView) {
        mDialogHelper.customTitle(customTitleView);
        return this;
    }

    @Override
    public DialogHelperInterface icon(Drawable icon) {
        mDialogHelper.icon(icon);
        return this;
    }

    @Override
    public DialogHelperInterface icon(int resId) {
        mDialogHelper.icon(resId);
        return this;
    }

    @Override
    public DialogHelperInterface message(CharSequence setMessage) {
        mDialogHelper.message(setMessage);
        return this;
    }

    @Override
    public DialogHelperInterface title(int titleId) {
        mDialogHelper.title(titleId);
        return this;
    }

    @Override
    public DialogHelperInterface title(CharSequence title) {
        mDialogHelper.title(title);
        return this;
    }

    @Override
    public DialogHelperInterface view(View view) {
        mDialogHelper.view(view);
        return this;
    }
}
