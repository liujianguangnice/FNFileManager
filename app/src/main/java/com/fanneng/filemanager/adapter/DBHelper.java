package com.fanneng.filemanager.adapter;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 作者： liujianguang on 2018/7/6 16:05
 * 邮箱： liujga@enn.cn
 */
public class DBHelper {
    private static DatabaseHelper mDbHelper;
    private static SQLiteDatabase mDb;

    private static final String DATABASE_NAME = "shanhy.db";

    private static final int DATABASE_VERSION = 1;

    private final Context mCtx;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }

    public DBHelper(Context ctx) {
        this.mCtx = ctx;
    }

    public DBHelper open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    /**
     * 关闭数据源
     *
     * @author SHANHY
     */
    public void closeConnection() {
        if (mDb != null && mDb.isOpen()){
            mDb.close();

        }
        if (mDbHelper != null){
            mDbHelper.close();
        }
    }

    /**
     * 插入数据 参数
     *
     * @param tableName
     *            表名
     * @param initialValues
     *            要插入的列对应值
     * @return
     * @author SHANHY
     */
    public long insert(String tableName, ContentValues initialValues) {

        return mDb.insert(tableName, null, initialValues);
    }

    /**
     * 删除数据
     *
     * @param tableName
     *            表名
     * @param deleteCondition
     *            条件
     * @param deleteArgs
     *            条件对应的值（如果deleteCondition中有“？”号，将用此数组中的值替换，一一对应）
     * @return
     * @author SHANHY
     */
    public boolean delete(String tableName, String deleteCondition, String[] deleteArgs) {

        return mDb.delete(tableName, deleteCondition, deleteArgs) > 0;
    }

    /**
     * 更新数据
     *
     * @param tableName
     *            表名
     * @param initialValues
     *            要更新的列
     * @param selection
     *            更新的条件
     * @param selectArgs
     *            更新条件中的“？”对应的值
     * @return
     * @author SHANHY
     */
    public boolean update(String tableName, ContentValues initialValues, String selection, String[] selectArgs) {
        return mDb.update(tableName, initialValues, selection, selectArgs) > 0;
    }

    /**
     * 取得一个列表
     *
     * @param distinct
     *            是否去重复
     * @param tableName
     *            表名
     * @param columns
     *            要返回的列
     * @param selection
     *            条件
     * @param selectionArgs
     *            条件中“？”的参数值
     * @param groupBy
     *            分组
     * @param having
     *            分组过滤条件
     * @param orderBy
     *            排序
     * @return
     * @author SHANHY
     */
    public Cursor findList(boolean distinct, String tableName, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit) {

        return mDb.query(distinct, tableName, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
    }

    /**
     * 取得单行记录
     *
     * @param tableName
     *            表名
     * @param columns
     *            获取的列数组
     * @param selection
     *            条件
     * @param selectionArgs
     *            条件中“？”对应的值
     * @param groupBy
     *            分组
     * @param having
     *            分组条件
     * @param orderBy
     *            排序
     * @param limit
     *            数据区间
     * @param distinct
     *            是否去重复
     * @return
     * @throws SQLException
     * @author SHANHY
     */
    public Cursor findOne(boolean distinct,String tableName, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit) throws SQLException {

        Cursor mCursor = findList(distinct, tableName, columns, selection, selectionArgs, groupBy, having, orderBy, limit);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    /**
     * 执行SQL(带参数)
     *
     * @param sql
     * @param args
     *            SQL中“？”参数值
     * @author SHANHY
     */
    public void execSQL(String sql, Object[] args) {
        mDb.execSQL(sql, args);

    }

    /**
     * 执行SQL
     *
     * @param sql
     * @author SHANHY
     */
    public void execSQL(String sql) {
        mDb.execSQL(sql);

    }

    /**
     * 判断某张表是否存在
     *
     * @param tableName
     *            表名
     * @return
     */
    public boolean isTableExist(String tableName) {
        boolean result = false;
        if (tableName == null) {
            return false;
        }

        try {
            Cursor cursor = null;
            String sql = "select count(1) as c from sqlite_master where type ='table' and name ='" + tableName.trim() + "'";
            cursor = mDb.rawQuery(sql, null);
            if (cursor.moveToNext()) {
                int count = cursor.getInt(0);
                if (count > 0) {
                    result = true;
                }
            }

            cursor.close();
        } catch (Exception e) {
        }
        return result;
    }

    /**
     * 判断某张表中是否存在某字段(注，该方法无法判断表是否存在，因此应与isTableExist一起使用)
     *
     * @param tableName
     *            表名
     * @param columnName
     *            列名
     * @return
     */
    public boolean isColumnExist(String tableName, String columnName) {
        boolean result = false;
        if (tableName == null) {
            return false;
        }

        try {
            Cursor cursor = null;
            String sql = "select count(1) as c from sqlite_master where type ='table' and name ='" + tableName.trim() + "' and sql like '%" + columnName.trim() + "%'";
            cursor = mDb.rawQuery(sql, null);
            if (cursor.moveToNext()) {
                int count = cursor.getInt(0);
                if (count > 0) {
                    result = true;
                }
            }

            cursor.close();
        } catch (Exception e) {
        }
        return result;
    }

}



