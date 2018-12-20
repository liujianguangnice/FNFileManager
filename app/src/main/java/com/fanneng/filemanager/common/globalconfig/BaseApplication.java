package com.fanneng.filemanager.common.globalconfig;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.fanneng.filemanager.common.bean.DaoMaster;
import com.fanneng.filemanager.common.bean.DaoSession;
import com.fanneng.library.FileInfoReport;
import com.fanneng.library.save.imp.FileWriter;
import com.fanneng.library.save.imp.FilesWriter;
import com.fanneng.library.upload.http.HttpReporter;


/**
 * 作者：王文彬 on 2017/4/1 10：46 邮箱：wwb199055@126.com
 */
public class BaseApplication extends Application {


    private static DaoSession daoSession;
    private static String  DbName = "FanNeng.db";
    /**
     * DEBUG 模式
     * 注意！请不要在正式发布的 App 中使用 Debug 模式！
     */
    private static BaseApplication application = null;


    public static BaseApplication getApplication() {
        return application;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        application = this;

        //配置数据库
        setupDatabase(DbName);

        //配置文件存储和上传类
        setupFileManager();
    }

    private void setupFileManager() {
        //在本地存储信息
        FileInfoReport.getInstance()
                .setCacheSize(30 * 1024 * 1024)//支持设置缓存大小，超出后清空
                .setSaveFileInfoDir(getApplicationContext())
                .setWifiOnly(true)//设置只在Wifi状态下上传，设置为false为Wifi和移动网络都上传
                .setFileSaver(new FilesWriter(getApplicationContext()))//支持自定义保存信息的样式
                //.setEncryption(new AESEncode()) //支持文件数据AES加密或者DES加密，默认不开启
                .init(getApplicationContext());

        //initHttpReporter();
    }

    /**
     * 使用HTTP发送日志
     */
    private void initHttpReporter() {
        HttpReporter http = new HttpReporter(this);
        http.setUrl("http://10.4.94.166:8080");//发送请求的地址
        http.setFileParam("fileName");//文件的参数名
        http.setToParam("to");//收件人参数名
        http.setTo("你的接收邮箱");//收件人
        http.setTitleParam("标题");//标题
        http.setBodyParam("内容");//内容
        FileInfoReport.getInstance().setUploadType(http);
    }


    /**
     * 配置数据库
     */
    private void setupDatabase(String DbName) {
        //创建数据库shop.db"
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, DbName, null);
        //获取可写数据库
        SQLiteDatabase db = helper.getWritableDatabase();
        //获取数据库对象
        DaoMaster daoMaster = new DaoMaster(db);
        //获取Dao对象管理者
        daoSession = daoMaster.newSession();


        /*DevOpenHelper：创建SQLite数据库的SQLiteOpenHelper的具体实现
        DaoMaster：GreenDao的顶级对象，作为数据库对象、用于创建表和删除表
        DaoSession：管理所有的Dao对象，Dao对象中存在着增删改查等API*/
    }

    public static DaoSession getDaoInstant() {
        return daoSession;
    }
}
