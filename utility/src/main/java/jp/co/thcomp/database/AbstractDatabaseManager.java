package jp.co.thcomp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

abstract public class AbstractDatabaseManager extends SQLiteOpenHelper {
    @Retention(RetentionPolicy.RUNTIME)
    public @interface TableName {
        String value();
    }

    @Retention(RetentionPolicy.RUNTIME)
    public @interface ColumnNotNull {
    }

    @Retention(RetentionPolicy.RUNTIME)
    public @interface ColumnPrimaryKey {
    }

    @Retention(RetentionPolicy.RUNTIME)
    public @interface ColumnOption {
        String value();
    }

    protected AbstractDatabaseManager(Context context, String name, int version) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Class[] tableClasses = getTableClasses();
        if (tableClasses != null && tableClasses.length > 0) {
            for (Class tableClass : tableClasses) {
                String createTableSQL = createTableDefinition(tableClass);
                db.execSQL(createTableSQL);
            }
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private String createTableDefinition(Class tableClass) {
        Field[] fields = tableClass.getDeclaredFields();
        StringBuilder execStr = new StringBuilder("create table if not exists ").append(getTableName(tableClass)).append("(");

        // カラム定義
        boolean firstColumn = true;
        for (int i = 0, size = fields.length; i < size; i++) {
            if (!java.lang.reflect.Modifier.isStatic(fields[i].getModifiers())) {
                String type = null;
                Class fieldType = fields[i].getType();
                ColumnNotNull fieldAnnotationNotNull = fields[i].getAnnotation(ColumnNotNull.class);
                ColumnPrimaryKey fieldAnnotationPrimaryKey = fields[i].getAnnotation(ColumnPrimaryKey.class);
                ColumnOption fieldAnnotationOption = fields[i].getAnnotation(ColumnOption.class);

                StringBuilder columnOptionBuilder = new StringBuilder();
                if (fieldAnnotationNotNull != null) {
                    columnOptionBuilder.append("not null ");
                }
                if (fieldAnnotationPrimaryKey != null) {
                    columnOptionBuilder.append("primary key ");
                }
                if (fieldAnnotationOption != null) {
                    columnOptionBuilder.append(fieldAnnotationOption.value());
                }

                if (fieldType.equals(int.class) || fieldType.equals(Integer.class)) {
                    type = "integer";
                } else if (fieldType.equals(long.class) || fieldType.equals(Long.class)) {
                    // DB上はintegerも8バイトなのでlongもintegerに丸め込む
                    type = "integer";
                } else if (fieldType.equals(short.class) || fieldType.equals(Short.class)) {
                    // shortはintegerに丸め込む
                    type = "integer";
                } else if (fieldType.equals(char.class) || fieldType.equals(Character.class)) {
                    // charはintegerに丸め込む
                    type = "integer";
                } else if (fieldType.equals(byte.class) || fieldType.equals(Byte.class)) {
                    // byteはintegerに丸め込む
                    type = "integer";
                } else if (fieldType.equals(boolean.class) || fieldType.equals(Boolean.class)) {
                    // booleanはintegerに丸め込む
                    type = "integer";
                } else if (fieldType.equals(String.class)) {
                    type = "text";
                } else if (fieldType.equals(byte[].class)) {
                    type = "blob";
                } else if (fieldType.equals(float.class) || fieldType.equals(Float.class) || fieldType.equals(double.class) || fieldType.equals(Double.class)) {
                    type = "real";
                }

                if (fields[i].getName() != null && type != null) {
                    if (firstColumn) {
                        firstColumn = false;
                    } else {
                        execStr.append(",");
                    }
                    execStr.append(fields[i].getName()).append(" ").append(type);
                    if (columnOptionBuilder.length() > 0) {
                        execStr.append(" ").append(columnOptionBuilder.toString());
                    }
                }
            }
        }
        execStr.append(");");

        return execStr.toString();
    }

    public <T> List<T> query(Class<T> tableClass, String[] columns, String selection,
                             String[] selectionArgs) {
        return query(tableClass, false, columns, selection, selectionArgs, null,
                null, null, null);
    }

    public <T> List<T> query(Class<T> tableClass, String[] columns, String selection,
                             String[] selectionArgs, String groupBy) {
        return query(tableClass, false, columns, selection, selectionArgs, groupBy,
                null, null, null);
    }

    public <T> List<T> query(Class<T> tableClass, String[] columns, String selection,
                             String[] selectionArgs, String groupBy, String having) {
        return query(tableClass, false, columns, selection, selectionArgs, groupBy,
                having, null, null);
    }

    public <T> List<T> query(Class<T> tableClass, String[] columns, String selection,
                             String[] selectionArgs, String groupBy, String having,
                             String orderBy) {
        return query(tableClass, false, columns, selection, selectionArgs, groupBy,
                having, orderBy, null);
    }

    public <T> List<T> query(Class<T> tableClass, String[] columns, String selection,
                             String[] selectionArgs, String groupBy, String having,
                             String orderBy, String limit) {
        return query(tableClass, false, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
    }

    public <T> List<T> query(Class<T> tableClass, boolean distinct, String[] columns, String selection,
                             String[] selectionArgs, String groupBy, String having,
                             String orderBy, String limit) {
        SQLiteDatabase database = getReadableDatabase();
        String tableName = getTableName(tableClass);
        ArrayList resultList = new ArrayList<>();

        Cursor cursor = database.query(distinct, tableName, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
        expandCursorToInstance(resultList, cursor, tableClass);

        return resultList;
    }

    public <T> long update(T dataInstance, String selection, String[] selectionArgs) {
        Class targetClass = dataInstance.getClass();
        String tableName = getTableName(targetClass);
        ContentValues values = new ContentValues();
        expandInstanceToValues(dataInstance, values);

        SQLiteDatabase database = getWritableDatabase();
        database.beginTransaction();
        long ret = database.update(tableName, values, selection, selectionArgs);
        database.setTransactionSuccessful();
        database.endTransaction();

        return ret;
    }

    public <T> long insert(T dataInstance) {
        Class targetClass = dataInstance.getClass();
        String tableName = getTableName(targetClass);
        ContentValues values = new ContentValues();
        expandInstanceToValues(dataInstance, values);

        SQLiteDatabase database = getWritableDatabase();
        database.beginTransaction();
        long ret = database.insert(tableName, null, values);
        database.setTransactionSuccessful();
        database.endTransaction();

        return ret;
    }

    public <T> long delete(Class<T> tableClass, String whereClause, String[] whereArgs) {
        SQLiteDatabase database = getWritableDatabase();
        database.beginTransaction();
        long ret = database.delete(getTableName(tableClass), whereClause, whereArgs);
        database.setTransactionSuccessful();
        database.endTransaction();

        return ret;
    }

    protected static String getTableName(Class tableClass) {
        String tableName = null;
        Annotation tableNameAnnnotation = tableClass.getAnnotation(TableName.class);

        if (tableNameAnnnotation != null) {
            tableName = ((TableName) tableNameAnnnotation).value();
        } else {
            tableName = tableClass.getSimpleName();
        }

        return tableName;
    }

    private static boolean expandCursorToInstance(List resultList, Cursor cursor, Class targetClass) {
        boolean ret = false;

        if (cursor != null && cursor.getCount() > 0) {
            ret = true;

            try {
                cursor.moveToFirst();
                int columnCount = cursor.getColumnCount();

                while (true) {
                    Object targetClassInstance = null;
                    try {
                        targetClassInstance = targetClass.getConstructor().newInstance();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }

                    if (targetClassInstance != null) {
                        for (int i = 0; i < columnCount; i++) {
                            String columnName = cursor.getColumnName(i);

                            try {
                                Field targetField = targetClass.getField(columnName);
                                Class fieldTypeClass = targetField.getType();

                                if (fieldTypeClass.equals(byte.class)) {
                                    targetField.setByte(targetClassInstance, (byte) cursor.getInt(i));
                                } else if (fieldTypeClass.equals(boolean.class)) {
                                    int tempValue = cursor.getInt(i);
                                    targetField.setBoolean(targetClassInstance, tempValue == 1 ? true : false);
                                } else if (fieldTypeClass.equals(char.class)) {
                                    targetField.setChar(targetClassInstance, (char) cursor.getShort(i));
                                } else if (fieldTypeClass.equals(short.class)) {
                                    targetField.setShort(targetClassInstance, cursor.getShort(i));
                                } else if (fieldTypeClass.equals(int.class)) {
                                    targetField.setInt(targetClassInstance, cursor.getInt(i));
                                } else if (fieldTypeClass.equals(long.class)) {
                                    targetField.setLong(targetClassInstance, cursor.getLong(i));
                                } else if (fieldTypeClass.equals(float.class)) {
                                    targetField.setFloat(targetClassInstance, cursor.getFloat(i));
                                } else if (fieldTypeClass.equals(double.class)) {
                                    targetField.setDouble(targetClassInstance, cursor.getDouble(i));
                                } else if (fieldTypeClass.equals(byte[].class)) {
                                    targetField.set(targetClassInstance, cursor.getBlob(i));
                                } else {
                                    targetField.set(targetClassInstance, cursor.getString(i));
                                }
                            } catch (NoSuchFieldException e) {
                                e.printStackTrace();
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                        }

                        resultList.add(targetClassInstance);
                    }

                    if (!cursor.moveToNext()) {
                        break;
                    }
                }
            } finally {
                cursor.close();
            }
        }

        return ret;
    }

    private static boolean expandInstanceToValues(Object dataInstance, ContentValues outValues) {
        Class targetClass = dataInstance.getClass();
        String tableName = getTableName(targetClass);
        Field[] fields = targetClass.getFields();

        for (Field field : fields) {
            Class fieldTypeClass = field.getType();
            String fieldName = field.getName();

            if (!java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
                if (fieldTypeClass.isPrimitive()) {
                    try {
                        if (fieldTypeClass.equals(byte.class)) {
                            outValues.put(fieldName, field.getByte(dataInstance));
                        } else if (fieldTypeClass.equals(boolean.class)) {
                            outValues.put(fieldName, field.getBoolean(dataInstance));
                        } else if (fieldTypeClass.equals(char.class)) {
                            // ContentValuesにcharを引数に指定するメソッドがないため、shortで代用
                            outValues.put(fieldName, (short) field.getChar(dataInstance));
                        } else if (fieldTypeClass.equals(short.class)) {
                            outValues.put(fieldName, field.getShort(dataInstance));
                        } else if (fieldTypeClass.equals(int.class)) {
                            outValues.put(fieldName, field.getInt(dataInstance));
                        } else if (fieldTypeClass.equals(long.class)) {
                            outValues.put(fieldName, field.getLong(dataInstance));
                        } else if (fieldTypeClass.equals(float.class)) {
                            outValues.put(fieldName, field.getFloat(dataInstance));
                        } else if (fieldTypeClass.equals(double.class)) {
                            outValues.put(fieldName, field.getDouble(dataInstance));
                        } else if (fieldTypeClass.equals(byte[].class)) {
                            outValues.put(fieldName, (byte[]) field.get(dataInstance));
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        Object data = field.get(dataInstance);
                        outValues.put(field.getName(), String.valueOf(data));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return true;
    }

    abstract protected Class[] getTableClasses();
}
