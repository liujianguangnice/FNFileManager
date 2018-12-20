package com.fanneng.filemanager.common.globalconfig;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.fanneng.filemanager.bean.FileBean;

/**
 * 作者： liujianguang on 2018/7/10 10:16
 * 邮箱： liujga@enn.cn
 *
 * 对Sqlite数据库进行操作的类
 */
public class SQLiteDatabaseDao {
    private final String TAG = this.getClass().getSimpleName();
    /**
     *SQLiteDatabase 引用
     */
    public  SQLiteDatabase mDb;


    @SuppressLint("WrongConstant")
    public SQLiteDatabaseDao() {
        mDb = BaseApplication.getApplication().openOrCreateDatabase("fn_base.db",
                SQLiteDatabase.CREATE_IF_NECESSARY, null);
    }

    @SuppressLint("WrongConstant")
    public SQLiteDatabaseDao(String dbName) {
        //判断数据库是否存在
        Boolean isDataBaseExist  = isDataBaseExist(dbName);

        if(isDataBaseExist){
            Log.i(TAG, "数据库已经存在");
        }else{
            mDb = BaseApplication.getApplication().openOrCreateDatabase(dbName,
                    SQLiteDatabase.CREATE_IF_NECESSARY, null);
            Log.i(TAG, "创建或打开数据库成功");
        }

    }

    @SuppressLint("WrongConstant")
    public SQLiteDatabaseDao(String dbName,String tabName) {
        //判断数据库是否存在
        Boolean isDataBaseExist  = isDataBaseExist(dbName);

        if(isDataBaseExist){
            Log.i(TAG, "数据库"+dbName+"已经存在");
            mDb = BaseApplication.getApplication().openOrCreateDatabase(dbName,
                    SQLiteDatabase.CREATE_IF_NECESSARY, null);
            Log.i(TAG, "打开数据库"+dbName);
        }else{
            mDb = BaseApplication.getApplication().openOrCreateDatabase(dbName,
                    SQLiteDatabase.CREATE_IF_NECESSARY, null);
            Log.i(TAG, "创建数据库"+dbName+"成功");
        }
        createTable(mDb,tabName);
    }


    public  SQLiteDatabase getDatabase() {
        return mDb;
    }


    /************ 对数据库的操作 ***********************/

    // 获取所有数据库的名称
    public String[] getDatabasesList() {
        return BaseApplication.getApplication().databaseList();
    }

    // 创建一个数据库
    @SuppressLint("WrongConstant")
    public void createDatabase(String db) {
        BaseApplication.getApplication().openOrCreateDatabase(db, SQLiteDatabase.CREATE_IF_NECESSARY, null);
    }

    // 删除一个数据库
    public void dropDatabase(String db) {

        if(isDataBaseExist(db)){
            try {
                BaseApplication.getApplication().deleteDatabase(db);
                Log.i(TAG, "dropDatabase: 删除数据库"+db+"成功");
            } catch (SQLException e) {
                Log.i(TAG, "dropDatabase: 删除数据库"+db+"失败");
            }
        }else{
            Log.i(TAG, "dropDatabase: 数据库"+db+"不存在");
        }

    }

    /************ 对数据库的表的属性添加修改操作 ***********************/

    // 获取某个数据库的表的名称
    public String getTablesList(SQLiteDatabase mDb) {

        Cursor c = mDb
                .rawQuery(
                        "select name from sqlite_master where type='table' order by name",
                        null);
        String str = "";
        while (c.moveToNext()) {
            str += c.getString(0) + "\n";

        }
        return "表的名称为:\n" + str;
    }

    // 创建一个表,默认创建一个username info字段的表,可以在后面的代码中添加相应的列
    public void createTable(SQLiteDatabase mDb, String table) {
        if(isTableExist(table)){
            Log.i(TAG, "createTable: 数据表"+table+"已经存在");
            return;
        }
        try {
            mDb.execSQL("create table if not exists " + table + " (id integer primary key autoincrement "
                    + ",name_collect text not null"
                    + ",time_duration text "
                    + ",time_create text "
                    + ",file_path text "
                    + ",unique_key text "
                    +", other text);");

            Log.i(TAG, "createTable: 数据表"+table+"创建成功");
        } catch (SQLException e) {
            Log.i(TAG, "createTable:数据表"+table+"创建失败");
        }
    }

    // 删除一个表
    public void dropTable(SQLiteDatabase mDb, String table) {

        try {
            mDb.execSQL("drop table if exists " + table);

        } catch (SQLException e) {
            Toast.makeText(BaseApplication.getApplication(), "数据表删除失败",
                    Toast.LENGTH_LONG).show();
        }

    }

    // 修改表--重命名表名
    public boolean alterTableRenameTable(SQLiteDatabase mDb, String oldTable,
                                         String newTableName) {
        try {
            mDb.execSQL("alter table " + oldTable + " rename to  "
                    + newTableName + ";");

        } catch (SQLException e) {
            Toast.makeText(BaseApplication.getApplication(), "数据表重命名失败",
                    Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    // 修改表--添加一列
    // @table 需要修改的table名
    // @column 添加的列的名称
    // @type 列的类型,如text,varchar等
    public boolean alterTableAddColumn(SQLiteDatabase mDb, String table,
                                       String column, String type) {
        try {
            mDb.execSQL("alter table  " + table + " add column " + column
                    + type + " ;");

        } catch (SQLException e) {
            Toast.makeText(BaseApplication.getApplication(), "数据表添加失败",
                    Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    // 获取表的列的名称
    public String getTableColumns(SQLiteDatabase mDb,String tableName) {

        Cursor c = getAllData(mDb, tableName);
        ;
        String[] columns = c.getColumnNames();
        String str = "";
        for (String s : columns) {

            str += s + "\n";

        }

        return str;
    }

    /************ 对数据库的表数据增删改查操作 ***********************/
    // 添加一条数据
    public long insert(SQLiteDatabase mDb, String table, FileBean user) {

        ContentValues values = new ContentValues();
        values.put("name_collect", user.getName_collect());
        values.put("time_duration", user.getTime_duration());
        values.put("time_create", user.getTime_create());
        values.put("file_path", user.getFile_path());
        values.put("unique_key", user.getUnique_key());
        values.put("other", user.getOther());
        return mDb.insert(table, null, values);
    }

    /**
     *
     * 删除一条数据
     */
    public boolean delete(SQLiteDatabase mDb, String table, int id) {

        String whereClause = "id=?";
        String[] whereArgs = new String[]{String.valueOf(id)};
        try {
            mDb.delete(table, whereClause, whereArgs);
        } catch (SQLException e) {
            Toast.makeText(BaseApplication.getApplication(), "删除数据库失败",
                    Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    /**
     *
     * 删除所有数据
     */
    public boolean delete(SQLiteDatabase mDb, String table) {

        String whereClause = "";
        String[] whereArgs = new String[]{};
        try {
            mDb.delete(table, whereClause, whereArgs);
        } catch (SQLException e) {
            Log.i(TAG, "delete: 删除数据库失败");
            return false;
        }
        return true;
    }

    /**
     *
     * 修改一条数据
     */
    public void update(SQLiteDatabase mDb, String table, int id, FileBean file) {

        ContentValues values = new ContentValues();
        values.put("name_collect", file.getName_collect());
        values.put("time_duration", file.getTime_duration());
        values.put("time_create", file.getTime_create());
        values.put("file_path", file.getFile_path());
        values.put("unique_key", file.getUnique_key());
        values.put("other", file.getOther());

        String whereClause = "id=?";
        String[] whereArgs = new String[]{String.valueOf(id)};
        mDb.update(table, values, whereClause, whereArgs);
    }

    /**
     *
     * 修改一条数据
     */
    public void updateName(SQLiteDatabase mDb, String table, int id, String name) {

        ContentValues values = new ContentValues();
        values.put("name_collect", name);

        String whereClause = "id=?";
        String[] whereArgs = new String[]{String.valueOf(id)};
        mDb.update(table, values, whereClause, whereArgs);
    }

    public Cursor queryById(SQLiteDatabase mDb, String table, int id) {

        // 第一个参数String：表名
        // 第二个参数String[]:要查询的列名
        // 第三个参数String：查询条件
        // 第四个参数String[]：查询条件的参数
        // 第五个参数String:对查询的结果进行分组
        // 第六个参数String：对分组的结果进行限制
        // 第七个参数String：对查询的结果进行排序

        String[] columns = new String[]{"id", "name_collect", "time_duration","time_create",
                "file_path", "unique_key", "other"
        };
        String selection = "id=?";
        String[] selectionArgs = {String.valueOf(id)};
        String groupBy = null;
        String having = null;
        String orderBy = null;
        return mDb.query(table, columns, selection,
                selectionArgs, groupBy, having, orderBy);
    }

    public Cursor getAllData(SQLiteDatabase mDb, String table) {

        //遍历表所有数据
        return mDb.rawQuery("select * from " + table, null);


        /** 如果需要返回指定的列,则执行以下语句
         String[] columns = new String[] { "id","username", "info" };
         // 调用SQLiteDatabase类的query函数查询记录
         return mDb.query(table, columns, null, null, null, null,
         null);

         */

    }


    /**
     * 判断数据库是否存在
     *
     * @param databaseName
     *            数据库名
     * @return
     */
    public boolean isDataBaseExist(String databaseName) {
        boolean result = false;
        if (databaseName == null) {
            return false;
        }

        try {
            String[] databasesList= getDatabasesList();
            for (int i = 0; i <databasesList.length; i++) {
                if(databasesList[i].equals(databaseName)){
                    result =true;
                    break;
                }
            }
        } catch (Exception e) {
        }
        return result;
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
     * 判断某条数据是否存在
     *
     * @param tableName
     *            表名
     * @return
     */
    public boolean isDataExist(String tableName,int id) {
        boolean result = false;
        if (tableName == null) {
            return false;
        }

        try {
            Cursor cursor = null;
            String sql = "select count(1) from '" + tableName.trim() + "' where id =  '" +id + "' ";
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
     * 关闭数据源
     *
     * @author SHANHY
     */
    public  void closeConnection() {
        if (mDb != null && mDb.isOpen()){
            mDb.close();

        }
    }

}
