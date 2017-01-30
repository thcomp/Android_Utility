package jp.co.thcomp.android_utility;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.List;

import jp.co.thcomp.android_utility.data.TestData;
import jp.co.thcomp.android_utility.manager.TestDataManager;

public class MainActivity extends AppCompatActivity {
    private EditText mEtName;
    private EditText mEtValue;
    private TestDataManager mDataManager;
    private int mLastId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDataManager = new TestDataManager(this, 1);
        mEtName = (EditText) findViewById(R.id.etName);
        mEtValue = (EditText) findViewById(R.id.etValue);
        Button btnAction = (Button) findViewById(R.id.btnAdd);
        btnAction.setText("add");
        btnAction.setOnClickListener(mBtnClickListener);

        ExpandDatabaseTask task = new ExpandDatabaseTask();
        task.execute();
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
