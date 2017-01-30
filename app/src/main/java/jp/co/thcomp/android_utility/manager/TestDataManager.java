package jp.co.thcomp.android_utility.manager;

import android.content.Context;

import jp.co.thcomp.android_utility.data.TestData;
import jp.co.thcomp.database.AbstractDatabaseManager;

public class TestDataManager extends AbstractDatabaseManager {
    private static final String DATABASE_NAME = "test_data.db";

    public TestDataManager(Context context, int version) {
        super(context, DATABASE_NAME, version);
    }

    @Override
    protected Class[] getTableClasses() {
        return new Class[]{TestData.class};
    }
}
