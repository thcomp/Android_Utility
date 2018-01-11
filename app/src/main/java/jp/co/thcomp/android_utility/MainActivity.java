package jp.co.thcomp.android_utility;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import jp.co.thcomp.android_utility.data.TestData;
import jp.co.thcomp.android_utility.manager.TestDataManager;
import jp.co.thcomp.util.LogUtil;
import jp.co.thcomp.util.RuntimePermissionUtil;

public class MainActivity extends AppCompatActivity {
    private EditText mEtName;
    private EditText mEtValue;
    private TestDataManager mDataManager;
    private int mLastId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RuntimePermissionUtil.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, new RuntimePermissionUtil.OnRequestPermissionsResultListener() {
            @Override
            public void onRequestPermissionsResult(String[] permissions, int[] grantResults) {
                boolean allGrants = true;

                for(int grantResult : grantResults){
                    if(grantResult == PackageManager.PERMISSION_DENIED){
                        allGrants = false;
                        break;
                    }
                }

                if(allGrants){
                    File tempFile = new File("/mnt/sdcard/test.txt");
                    FileOutputStream outputStream = null;
                    try{
                        outputStream = new FileOutputStream(tempFile);
                        outputStream.write("test.txt".getBytes());
                    }catch(IOException e){
                        finish();
                        return;
                    }finally {
                        if(outputStream != null){
                            try {
                                outputStream.close();
                            } catch (IOException e) {
                                finish();
                                return;
                            }
                        }
                    }
                    FileInputStream inputStream = null;
                    try{
                        inputStream = new FileInputStream(tempFile);
                        byte[] readBuffer = new byte[1024];
                        int readSize = inputStream.read(readBuffer);
                        LogUtil.d("test",  new String(readBuffer, 0, readSize));
                    }catch(IOException e){
                        finish();
                        return;
                    }finally {
                        if(inputStream != null){
                            try {
                                inputStream.close();
                            } catch (IOException e) {
                                finish();
                                return;
                            }
                        }
                    }

                    mDataManager = new TestDataManager(MainActivity.this, 1);
                    mEtName = (EditText) findViewById(R.id.etName);
                    mEtValue = (EditText) findViewById(R.id.etValue);
                    Button btnAction = (Button) findViewById(R.id.btnAdd);
                    btnAction.setText("add");
                    btnAction.setOnClickListener(mBtnClickListener);

                    ExpandDatabaseTask task = new ExpandDatabaseTask();
                    task.execute();
                }else{
                    finish();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        RuntimePermissionUtil.onRequestPermissionsResult(requestCode,  permissions, grantResults);
    }

    private void addData() {
        TestData testData = new TestData();
        testData._id = ++mLastId;
        testData.name = mEtName.getText().toString();
        testData.value = mEtValue.getText().toString();
        mEtName.setText("");
        mEtValue.setText("");

        mDataManager.insert(testData);

        ExpandDatabaseTask task = new ExpandDatabaseTask();
        task.execute();
    }

    private void removeData(TestData data) {
        mDataManager.delete(data.getClass(), "_id=?", new String[]{String.valueOf(data._id)});

        ExpandDatabaseTask task = new ExpandDatabaseTask();
        task.execute();
    }

    private View.OnClickListener mBtnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();

            switch (id) {
                case R.id.btnAdd:
                    addData();
                    break;
                case R.id.btnRemove:
                    removeData((TestData) view.getTag());
                    break;
            }
        }
    };

    private class ExpandDatabaseTask extends AsyncTask<Void, Void, List<TestData>> {
        private ProgressDialog mDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mDialog = new ProgressDialog(MainActivity.this);
            mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mDialog.setCanceledOnTouchOutside(false);
            mDialog.show();
        }

        @Override
        protected List<TestData> doInBackground(Void... voids) {
            return mDataManager.query(TestData.class, null, null, null);
        }

        @Override
        protected void onPostExecute(List<TestData> dataList) {
            super.onPostExecute(dataList);
            mDialog.dismiss();

            LayoutInflater inflater = MainActivity.this.getLayoutInflater();
            LinearLayout llDataList = (LinearLayout) MainActivity.this.findViewById(R.id.llDataList);
            llDataList.removeAllViews();

            for (TestData data : dataList) {
                if (mLastId < data._id) {
                    mLastId = data._id;
                }

                View dataView = inflater.inflate(R.layout.item_test_data, llDataList, false);
                EditText etName = (EditText) dataView.findViewById(R.id.etName);
                EditText etValue = (EditText) dataView.findViewById(R.id.etValue);
                etName.setEnabled(false);
                etName.setText(data.name);
                etValue.setEnabled(false);
                etValue.setText(data.value);

                Button btnAction = (Button) dataView.findViewById(R.id.btnAdd);
                btnAction.setText("remove");
                btnAction.setId(R.id.btnRemove);
                btnAction.setOnClickListener(mBtnClickListener);
                btnAction.setTag(data);

                llDataList.addView(dataView);
            }
        }
    }
}
