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
}
