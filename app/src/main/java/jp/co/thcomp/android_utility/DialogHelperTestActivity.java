package jp.co.thcomp.android_utility;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import jp.co.thcomp.util.DialogHelper;
import jp.co.thcomp.util.ThreadUtil;
import jp.co.thcomp.util.ToastUtil;

public class DialogHelperTestActivity extends Activity {
    private int mClickCount = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_dialog_helper);
        findViewById(R.id.btnTestStart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testDialogHelper();
            }
        });
        findViewById(R.id.btnListenerTestStart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listenerTestDialogHelper();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DialogHelper.clear();
    }

    private void testDialogHelper(){
        ThreadUtil.runOnMainThread(this, new Runnable() {
            @Override
            public void run() {
                DialogHelper dialogHelper = new DialogHelper(DialogHelperTestActivity.this);
                dialogHelper
                        .title("test1")
                        .message("check title and this message")
                        .button(
                                Dialog.BUTTON_POSITIVE,
                                "Next",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mClickCount = 0;
                                        final DialogHelper dialogHelper = new DialogHelper(DialogHelperTestActivity.this);
                                        dialogHelper
                                                .title("test2")
                                                .message("repeat show")
                                                .button(
                                                        Dialog.BUTTON_POSITIVE,
                                                        "close",
                                                        new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                if(mClickCount == 0){
                                                                    mClickCount++;

                                                                    ThreadUtil.delayRunOnMainThread(DialogHelperTestActivity.this, 1000, new Runnable() {
                                                                        @Override
                                                                        public void run() {
                                                                            ToastUtil.showToast(DialogHelperTestActivity.this, "Test OK", Toast.LENGTH_LONG);
                                                                        }
                                                                    });
                                                                }else{
                                                                    throw new RuntimeException("fail to test");
                                                                }
                                                            }
                                                        });
                                        for(int i=0; i<5; i++){
                                            ThreadUtil.runOnMainThread(DialogHelperTestActivity.this, new Runnable() {
                                                @Override
                                                public void run() {
                                                    dialogHelper.show();
                                                }
                                            });
                                        }
                                    }
                                })
                        .button(
                                Dialog.BUTTON_NEGATIVE,
                                "Not good",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        throw new RuntimeException("fail to test");
                                    }
                                }
                        )
                        .icon(R.mipmap.ic_launcher)
                        .show();
            }
        });
    }

    private int mTestCounter = 0;
    private void listenerTestDialogHelper(){
        ThreadUtil.runOnWorkThread(this, new Runnable() {
            @Override
            public void run() {
                {
                    int counter = mTestCounter = 0;
                    DialogHelper dialogHelper = new DialogHelper(DialogHelperTestActivity.this);
                    final ThreadUtil.OnetimeSemaphore semaphore = new ThreadUtil.OnetimeSemaphore();

                    // showListener
                    dialogHelper.showListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface dialog) {
                            if (!ThreadUtil.IsOnMainThread(DialogHelperTestActivity.this)) {
                                throw new RuntimeException("onShow is called on work thread");
                            }

                            mTestCounter++;
                            semaphore.stop();
                        }
                    });

                    dialogHelper.show();
                    semaphore.start(1000);

                    if (mTestCounter == counter) {
                        throw new RuntimeException("mTestCounter is not updated");
                    }
                    dialogHelper.dismiss();
                }

                {
                    int counter = mTestCounter = 0;
                    DialogHelper dialogHelper = new DialogHelper(DialogHelperTestActivity.this);
                    final ThreadUtil.OnetimeSemaphore semaphore = new ThreadUtil.OnetimeSemaphore();

                    // dismissListener
                    dialogHelper.dismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            if (!ThreadUtil.IsOnMainThread(DialogHelperTestActivity.this)) {
                                throw new RuntimeException("onDismiss is called on work thread");
                            }

                            mTestCounter++;
                            semaphore.stop();
                        }
                    });

                    dialogHelper.show();
                    dialogHelper.dismiss();
                    semaphore.start(1000);

                    if (mTestCounter == counter) {
                        throw new RuntimeException("mTestCounter is not updated");
                    }
                }

                {
                    int counter = mTestCounter = 0;
                    DialogHelper dialogHelper = new DialogHelper(DialogHelperTestActivity.this);
                    final ThreadUtil.OnetimeSemaphore semaphore = new ThreadUtil.OnetimeSemaphore();

                    // cancelListener
                    dialogHelper.cancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            if (!ThreadUtil.IsOnMainThread(DialogHelperTestActivity.this)) {
                                throw new RuntimeException("onDismiss is called on work thread");
                            }

                            mTestCounter++;
                            semaphore.stop();
                        }
                    });

                    dialogHelper.show();
                    dialogHelper.cancel();
                    semaphore.start(1000);

                    if (mTestCounter == counter) {
                        throw new RuntimeException("mTestCounter is not updated");
                    }
                    dialogHelper.dismiss();
                }

                {
                    DialogHelper dialogHelper = new DialogHelper(DialogHelperTestActivity.this);

                    if(dialogHelper.isShowing()){
                        throw new RuntimeException("isShowing is true");
                    }

                    dialogHelper.show();
                    synchronized (DialogHelperTestActivity.this){
                        try {
                            DialogHelperTestActivity.this.wait(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    if(!dialogHelper.isShowing()){
                        throw new RuntimeException("isShowing is false");
                    }

                    dialogHelper.hide();
                    if(!ThreadUtil.IsOnMainThread(DialogHelperTestActivity.this)){
                        synchronized (DialogHelperTestActivity.this){
                            try {
                                DialogHelperTestActivity.this.wait(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    if(dialogHelper.isShowing()){
                        throw new RuntimeException("isShowing is true");
                    }

                    dialogHelper.show();
                    if(!ThreadUtil.IsOnMainThread(DialogHelperTestActivity.this)){
                        synchronized (DialogHelperTestActivity.this){
                            try {
                                DialogHelperTestActivity.this.wait(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    if(!dialogHelper.isShowing()){
                        throw new RuntimeException("isShowing is false");
                    }

                    dialogHelper.cancel();
                    if(!ThreadUtil.IsOnMainThread(DialogHelperTestActivity.this)){
                        synchronized (DialogHelperTestActivity.this){
                            try {
                                DialogHelperTestActivity.this.wait(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    if(dialogHelper.isShowing()){
                        throw new RuntimeException("isShowing is true");
                    }

                    dialogHelper.dismiss();
                    if(!ThreadUtil.IsOnMainThread(DialogHelperTestActivity.this)){
                        synchronized (DialogHelperTestActivity.this){
                            try {
                                DialogHelperTestActivity.this.wait(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    if(dialogHelper.isShowing()) {
                        throw new RuntimeException("isShowing is true");
                    }
                }

                DialogHelper dialogHelper = new DialogHelper(DialogHelperTestActivity.this);
                dialogHelper.message("test success").button(DialogInterface.BUTTON_POSITIVE, "close", null).show();
            }
        });
    }
}
