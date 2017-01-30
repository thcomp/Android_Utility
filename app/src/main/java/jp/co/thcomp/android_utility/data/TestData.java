package jp.co.thcomp.android_utility.data;

import jp.co.thcomp.database.AbstractDatabaseManager;

@AbstractDatabaseManager.TableName("test_data")
public class TestData {
    @AbstractDatabaseManager.ColumnNotNull
    @AbstractDatabaseManager.ColumnPrimaryKey
    public int _id;

    public String name;

    public String value;
}
