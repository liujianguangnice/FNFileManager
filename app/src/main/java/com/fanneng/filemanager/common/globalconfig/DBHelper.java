package com.fanneng.filemanager.common.globalconfig;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.apkfuns.logutils.LogUtils;
import com.fanneng.filemanager.bean.FileBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者： liujianguang on 2018/7/6 15:01
 * 邮箱： liujga@enn.cn
 */
public class DBHelper {

    private final String TAG = this.getClass().getSimpleName();
    public static String DATABASE_NAME = "fn_collect.db";
    public static String TabName = "collect_data";

    public static DBHelper instance() {
        return DBInit.dbInit;
    }

    private static class DBInit {
        private static DBHelper dbInit = new DBHelper();
    }

    /**
     * SQLiteDatabase 引用
     */
    public static SQLiteDatabaseDao dao;

    public static SQLiteDatabase mDb;

    /**
     * 支持用用户的 ID 区分数据表,创建数据库和表
     */
    public void initDbHelp(Context ctx, String loginId) {
        LogUtils.d(TAG, "初始化 dbinit");
        if (ctx == null || loginId.trim().length() <= 0) {
            throw new RuntimeException("#DBInterface# init DB exception!");
        }
        /** 切换用户的时候， openHelper 不是 null */
        DATABASE_NAME = "fn_" + loginId + ".db";


        LogUtils.d(TAG, "DB init,loginId: " + loginId);
        //创建或打开数据库，并创建表collect_data
        dao = new SQLiteDatabaseDao(DATABASE_NAME, TabName);

        mDb = dao.getDatabase();
    }

    /**
     * 创建数据库
     */
    @SuppressLint("WrongConstant")
    public void openOrCreateDatabase(String databaseName) {
        LogUtils.d(TAG, "初始化 dbinit");
        LogUtils.d(TAG, "DB init,DATABASE_NAME: " + databaseName);
        mDb = BaseApplication.getApplication().openOrCreateDatabase(databaseName,
                SQLiteDatabase.CREATE_IF_NECESSARY, null);
    }

    public SQLiteDatabaseDao getSQLiteDatabaseDao() {
        return dao;
    }

    public SQLiteDatabase getDataBase() {
        return mDb;
    }


    public void setTableName(String tableName) {
        TabName = tableName;
    }

    /**
     * 删除数据库
     */
    public void removeDatabase(String databaseName) {
        //删除students.db数据库
        dao.dropDatabase(databaseName);

        //重新获取数据库名称
        String[] dblist = dao.getDatabasesList();
        String rs = "";
        for (String s : dblist) {
            rs += s + "\n";
        }
        Log.i(TAG, "现有数据库的名称有：\n" + rs);
    }


    /**
     * 获取数据库中的表
     */
    @SuppressLint("WrongConstant")
    public String getTablesList() {
        //显示所有的表的数据
        mDb = BaseApplication.getApplication().openOrCreateDatabase(DATABASE_NAME,
                SQLiteDatabase.CREATE_IF_NECESSARY, null);

        String tableNames = dao.getTablesList(mDb);
        Log.i(TAG, "getTablesList: 数据库" + DATABASE_NAME + "中的表包括：" + tableNames);
        return tableNames;
    }

    /**
     * 重命名表名称
     */
    public void renameTable(String oldTable,
                            String newTableName) {
        if (dao == null) {
            Log.i(TAG, "dao 为空,请初始化数据库");
            return ;
        }
        if (dao.isTableExist(oldTable)) {
            //将testtable重命名为newtable
            boolean b = dao.alterTableRenameTable(mDb, oldTable, newTableName);
            if (b) {
                Log.i(TAG, "renameTable: 重命名成功");
            } else {
                Log.i(TAG, "重命名失败，请删除(drop table)后重试");
            }
        } else {
            Log.i(TAG, "该表" + oldTable + "不存在");
        }

    }

    /**
     * 对一个表添加一个数据
     */

    public boolean insertTable(FileBean fileBean) {
        if (dao == null) {
            Log.i(TAG, "dao 为空,请初始化数据库");
            return false;
        }
        boolean isSuccess = false;
        if(!dao.isDataBaseExist(DATABASE_NAME)){
            Log.i(TAG, "数据库不存在");
            return false;
        }

        if (dao.isTableExist(TabName)) {
            long idrow = dao.insert(mDb, TabName, fileBean);
            if (idrow == -1) {
                Log.i(TAG, "插入数据失败");
            } else {
                Log.i(TAG, "插入数据成功：" + "id为" + idrow);
                isSuccess = true;
            }
        } else {
            Log.i(TAG, "数据库中不存在表" + TabName);
        }
        return isSuccess;
    }


    /**
     * 查询一个表的所有数据
     */

    public List<FileBean> queryTableAllData() {
        if (dao == null) {
            Log.i(TAG, "dao 为空,请初始化数据库");
            return null;
        }
        List<FileBean> fileBeanList = new ArrayList<>();
        if (dao == null) {
            Log.i(TAG, "queryTableAllData: dao为空");
            return fileBeanList;
        }

        //默认查询mytable所有数据
        Cursor c = dao.getAllData(mDb, TabName);
        FileBean file;
        //获取表的内容
        while (c.moveToNext()) {
            file = new FileBean();
            String id = c.getString(0);
            String name_collect = c.getString(1);
            String time_duration = c.getString(2);
            String time_create = c.getString(3);
            String file_path = c.getString(4);
            String unique_key = c.getString(5);
            String other = c.getString(6);


            file.setId(Integer.parseInt(id));
            file.setName_collect(name_collect);
            file.setTime_duration(time_duration);
            file.setTime_create(time_create);
            file.setFile_path(file_path);
            file.setUnique_key(unique_key);
            file.setOther(other);
            fileBeanList.add(file);
            file = null;
        }

        Log.i(TAG, "queryTableAllData: 查询到" + fileBeanList.size() + "条数据");
        return fileBeanList;
    }

    /**
     * 通过id 查询一个表的一条数据
     */

    public FileBean queryDataById(int id) {
        if (dao == null) {
            Log.i(TAG, "dao 为空,请初始化数据库");
            return null;
        }
        List<FileBean> fileBeanList = new ArrayList<>();
        FileBean file = new FileBean();

        Cursor c = dao.queryById(mDb, TabName, id);
        //获取表的内容
        while (c.moveToNext()) {
            file = new FileBean();
            String ids = c.getString(0);
            String name_collect = c.getString(1);
            String time_duration = c.getString(2);
            String time_create = c.getString(3);
            String file_path = c.getString(4);
            String unique_key = c.getString(5);
            String other = c.getString(6);


            file.setId(Integer.parseInt(ids));
            file.setName_collect(name_collect);
            file.setTime_duration(time_duration);
            file.setTime_create(time_create);
            file.setFile_path(file_path);
            file.setUnique_key(unique_key);
            file.setOther(other);
            fileBeanList.add(file);
        }

        Log.i(TAG, "queryTableAllData: 查询到" + fileBeanList.size() + "条数据");

        if(fileBeanList.size()<1){
            return null;
        }
        return file;
    }

    /**
     * 更新一个表的一条数据
     */
    public void updateDataById(int id, String name) {
        if (dao == null) {
            Log.i(TAG, "dao 为空,请初始化数据库");
            return ;
        }
        if(!dao.isTableExist(TabName)){
            Log.i(TAG, "updateDataById：该表"+TabName+"不存在");
            return;
        }
        if(!dao.isDataExist(TabName,id)){
            Log.i(TAG, "updateDataById：该条记录不存在");
            return;
        }
        dao.updateName(mDb, TabName, id, name);
        Log.i(TAG, "updateDataById：成功更新id为:" + id + "的记录name_collect字段！");
    }

    /**
     * 更新一个表的一条数据
     */
    public void updateData(int id, FileBean fileBean) {
        if (dao == null) {
            Log.i(TAG, "dao 为空,请初始化数据库");
            return ;
        }
        if(!dao.isTableExist(TabName)){
            Log.i(TAG, "updateData：该表"+TabName+"不存在");
            return;
        }
        if(!dao.isDataExist(TabName,id)){
            Log.i(TAG, "updateData：id为:" + id + "的记录不存在");
            return;
        }
        dao.update(mDb, TabName, id, fileBean);
        Log.i(TAG, "updateData：成功更新id为:" + id + "的记录！");
    }


    /**
     * 删除一个表的一条数据
     */
    public void deleteDataById(int id) {
        if (dao == null) {
            Log.i(TAG, "dao 为空,请初始化数据库");
            return ;
        }

        if(!dao.isDataBaseExist(DATABASE_NAME)){
            Log.i(TAG, "数据库不存在");
            return ;
        }
        if(!dao.isTableExist(TabName)){
            Log.i(TAG, "表"+TabName+"不存在");
            return ;
        }

        if (dao.isDataExist(TabName, id)) {
            boolean b = dao.delete(mDb, TabName, id);
            if (b) {
                Log.i(TAG, "成功删除id为:" + id + "的记录！\n");
            }
        } else {
            Log.i(TAG, "表" + TabName + "中不存在id为" + id + "的记录！\n");
        }


    }

    /**
     * 删除一个表的所有数据
     */
    public void deleteAllData() {
        if (dao == null) {
            Log.i(TAG, "dao 为空,请初始化数据库");
            return ;
        }

        if(!dao.isDataBaseExist(DATABASE_NAME)){
            Log.i(TAG, "数据库不存在");
            return ;
        }
        if(!dao.isTableExist(TabName)){
            Log.i(TAG, "表"+TabName+"不存在");
            return ;
        }

        boolean b = dao.delete(mDb, TabName);
        if (b) {
            Log.i(TAG, "deleteAllData: 成功删除表collect_data的所有记录！");
        }
    }


    /**
     * 退出时关闭数据库
     */
    public void closeDataBase() {
        if (dao == null) {
            Log.i(TAG, "dao 为空,请初始化数据库");
            return ;
        }
        dao.closeConnection();
    }

}
