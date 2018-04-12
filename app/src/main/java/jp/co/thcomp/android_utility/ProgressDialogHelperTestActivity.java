package jp.co.thcomp.android_utility;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import jp.co.thcomp.util.ProgressDialogHelper;

public class ProgressDialogHelperTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_dialog_helper_test);

        findViewById(R.id.bStartProgressDialogTest).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startProgressDialogHelperTest();
            }
        });
    }

    protected void startProgressDialogHelperTest(){
        final ProgressDialogHelper dialogHelper = new ProgressDialogHelper(this, ProgressDialog.STYLE_HORIZONTAL);
    }
}
