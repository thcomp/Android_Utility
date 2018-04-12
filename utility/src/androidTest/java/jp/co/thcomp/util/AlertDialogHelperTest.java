package jp.co.thcomp.util;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.test.InstrumentationRegistry;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class AlertDialogHelperTest {
    private int mCounter = 0;

    @Test
    public void cancel() {
        int orgCounter = mCounter;

        Context appContext = InstrumentationRegistry.getTargetContext();
        AlertDialogHelper dialogHelper = new AlertDialogHelper(appContext, null);
        dialogHelper.cancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                mCounter++;
            }
        });
        dialogHelper.cancelable(true);
        dialogHelper.show();
        dialogHelper.cancel();

        assertFalse(orgCounter == mCounter);
    }

    @Test
    public void dismiss() {
        final int orgCounter = mCounter;
        final Context appContext = InstrumentationRegistry.getTargetContext();
        Handler handler = new Handler(appContext.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                AlertDialogHelper dialogHelper = new AlertDialogHelper(appContext, null);
                dialogHelper.dismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        mCounter++;
                    }
                });
                dialogHelper.cancelable(true);
                dialogHelper.show();
                dialogHelper.dismiss();
                assertFalse(orgCounter == mCounter);
            }
        });
    }

    @Test
    public void hide() {
    }

    @Test
    public void show() {
    }

    @Test
    public void isShowing() {
    }

    @Test
    public void cancelable() {
    }

    @Test
    public void canceledOnTouchOutside() {
    }

    @Test
    public void cancelListener() {
    }

    @Test
    public void dismissListener() {
    }

    @Test
    public void keyListener() {
    }

    @Test
    public void showListener() {
    }

    @Test
    public void button() {
    }

    @Test
    public void customTitle() {
    }

    @Test
    public void icon() {
    }

    @Test
    public void icon1() {
    }

    @Test
    public void message() {
    }

    @Test
    public void title() {
    }

    @Test
    public void title1() {
    }

    @Test
    public void view() {
    }
}