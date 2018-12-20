package com.fanneng.library;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.text.TextUtils;

import com.fanneng.library.encryption.IEncryption;
import com.fanneng.library.save.ISave;
import com.fanneng.library.save.imp.FileInfoWriter;
import com.fanneng.library.upload.IFileUpload;
import com.fanneng.library.upload.UploadService;
import com.fanneng.library.util.FileInfoUtil;
import com.fanneng.library.util.NetUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * 文件信息（text）存储管理框架
 */
public class FileInfoReport {

    /**
     * 时间戳
     */
    public final static SimpleDateFormat yyyy_MM_dd_HH_mm_ss = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss", Locale.getDefault());


    private static FileInfoReport mFileInfoReport;
    /**
     * 设置上传的方式
     */
    public IFileUpload mUpload;
    /**
     * 设置缓存文件夹的大小,默认是30MB
     */
    private long mCacheSize = 30 * 1024 * 1024;

    /**
     * 设置日志保存的路径
     */
    private String mROOT;

    /**
     * 设置加密方式
     */
    private IEncryption mEncryption;

    /**
     * 设置文件信息的保存方式
     */
    private ISave mFileInfoSaver;

    /**
     * 设置在哪种网络状态下上传，true为只在wifi模式下上传，false是wifi和移动网络都上传
     */
    private boolean mWifiOnly = true;


    private FileInfoReport() {
    }


    public static FileInfoReport getInstance() {
        if (mFileInfoReport == null) {
            synchronized (FileInfoReport.class) {
                if (mFileInfoReport == null) {
                    mFileInfoReport = new FileInfoReport();
                }
            }
        }
        return mFileInfoReport;
    }

    public FileInfoReport setCacheSize(long cacheSize) {
        this.mCacheSize = cacheSize;
        return this;
    }

    public FileInfoReport setEncryption(IEncryption encryption) {
        this.mEncryption = encryption;
        return this;
    }

    public FileInfoReport setUploadType(IFileUpload logUpload) {
        mUpload = logUpload;
        return this;
    }

    public FileInfoReport setWifiOnly(boolean wifiOnly) {
        mWifiOnly = wifiOnly;
        return this;
    }


    public FileInfoReport setFileInfoDir(Context context, String logDir) {
        if (TextUtils.isEmpty(logDir)) {
            //如果SD不可用，则存储在沙盒中
            //如果我们想要读取或者向SD卡写入，这时就必须先要判断一个SD卡的状态,MEDIA_MOUNTED：SD卡正常挂载，手机装有SDCard,并且可以进行读写
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) || !Environment.isExternalStorageRemovable()) {
                /*Google已经提供了最佳的外部存储方案,那就是统一路径为:/Android/data/< package name >/files/… (该路径通常挂载在/mnt/sdcard/下)
                这个方法获得的文件存储路径适用于6.0以后系统,主要AndroidManifest.xml配置读写权限了,就不需要用户再授权了.
                外部存储（SD卡存储）：
                应用程序在运行的过程中如果需要向手机上保存数据，一般是把数据保存在SDcard中的。
                大部分应用是直接在SDCard的根目录下创建一个文件夹，然后把数据保存在该文件夹中。
                这样当该应用被卸载后，这些数据还保留在SDCard中，留下了垃圾数据。
                如果你想让你的应用被卸载后，与该应用相关的数据也清除掉，该怎么办呢？
                通过Context.getExternalFilesDir()方法可以获取到 SDCard/Android/data/你的应用的包名/files/ 目录，一般放一些长时间保存的数据
                通过Context.getExternalCacheDir()方法可以获取到 SDCard/Android/data/你的应用包名/cache/目录，一般存放临时缓存数据
                如果使用上面的方法，当你的应用在被用户卸载后，SDCard/Android/data/你的应用的包名/ 这个目录下的所有文件都会被删除，不会留下垃圾信息。
                而且上面二个目录分别对应 设置->应用->应用详情里面的”清除数据“与”清除缓存“选项
                如果要保存下载的内容，就不要放在以上目录下
                */

                // /sdcard/Android/data/<application package>/cache
                mROOT = context.getExternalCacheDir().getAbsolutePath();
            } else {
                /*
                 * 内部存储（非SD卡，应用卸载，文件删除），
                 * getCacheDir()方法用于获取/data/data/<application package>/cache目录
                 * getFilesDir()方法用于获取/data/data/<application package>/files目录
                 */
                ///data/data/<application package>/cache
                mROOT = context.getCacheDir().getAbsolutePath();
            }
        } else {
            //应用程序在运行的过程中如果需要向手机上保存数据，一般是把数据保存在SDcard中的。
            //大部分应用是直接在SDCard的根目录下创建一个文件夹，然后把数据保存在该文件夹中。
            //例如：定义路径为：sdcard/[app name]/
            //这样当该应用被卸载后，这些数据还保留在SDCard中，留下了垃圾数据。
            /*获取SD卡根目录,然后自定义文件/文件名进行文件存储.这样做法的结果就是,当手机安装了大量的app时，SD卡根目录会迅速变得杂乱不堪。
            并且在API 6.0之后,根目录文件存储是需要用户授权的,就算你在AndroidManifest.xml中配置了存储权限,用户不授权也是写不进去了.*/
            mROOT = logDir;
        }
        return this;
    }


    /**
     * @param context 上下文对象
     * @param dir     存储目录
     * @return
     */
    public FileInfoReport setSaveFileInfoDir(Context context, String dir) {
        String directoryPath = "";
        //判断SD卡是否可用
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            /*外部存储，不需要6.0授权*/
            //SDCard/Android/data/你的应用的包名/files/ +dir
            directoryPath = context.getExternalFilesDir(dir).getAbsolutePath();
            // directoryPath =context.getExternalCacheDir().getAbsolutePath() ;
        } else {
            /**内部存储,没内存卡就存机身内存非SD卡，应用卸载，文件删除）
             * getCacheDir()方法用于获取/data/data/<application package>/cache目录  + /dir
             * getFilesDir()方法用于获取/data/data/<application package>/files目录  + /dir
             */
            directoryPath = context.getFilesDir() + File.separator + dir;
            // directoryPath=context.getCacheDir()+File.separator+dir;
        }
        File file = new File(directoryPath);
        //判断文件目录是否存在
        if (!file.exists()) {
            file.mkdirs();
        }

        FileInfoUtil.i("文件存储路径====>" + directoryPath);

        mROOT = directoryPath;
        return this;
    }

    /**
     * @param context 上下文对象
     * @return
     */
    public FileInfoReport setSaveFileInfoDir(Context context) {
        String directoryPath = "";
        String dir = "collect_data";
        String timeStr = yyyy_MM_dd_HH_mm_ss.format(Calendar.getInstance().getTime());
        dir =dir+File.separator+timeStr;
        //判断SD卡是否可用
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            /*外部存储，不需要6.0授权*/
            //SDCard/Android/data/你的应用的包名/files/ +dir
            directoryPath = context.getExternalFilesDir(dir).getAbsolutePath();
            // directoryPath =context.getExternalCacheDir().getAbsolutePath() ;
        } else {
            /**内部存储,没内存卡就存机身内存非SD卡，应用卸载，文件删除）
             * getCacheDir()方法用于获取/data/data/<application package>/cache目录  + /dir
             * getFilesDir()方法用于获取/data/data/<application package>/files目录  + /dir
             */
            directoryPath = context.getFilesDir() + File.separator + dir;
            // directoryPath=context.getCacheDir()+File.separator+dir;
        }
        File file = new File(directoryPath);
        //判断文件目录是否存在
        if (!file.exists()) {
            file.mkdirs();
        }

        FileInfoUtil.i("文件存储路径====>" + directoryPath);

        mROOT = directoryPath;
        return this;
    }

    public FileInfoReport setFileSaver(ISave logSaver) {
        this.mFileInfoSaver = logSaver;
        return this;
    }


    public String getROOT() {
        return mROOT;
    }

    public void init(Context context) {
        if (TextUtils.isEmpty(mROOT)) {
            //如果SD不可用，则存储在沙盒中
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                mROOT = context.getExternalCacheDir().getAbsolutePath();
            } else {
                mROOT = context.getCacheDir().getAbsolutePath();
            }
        }
        if (mEncryption != null) {
            mFileInfoSaver.setEncodeType(mEncryption);
        }
        FileInfoWriter.getInstance().init(mFileInfoSaver);
    }

    public IFileUpload getUpload() {
        return mUpload;
    }

    public long getCacheSize() {
        return mCacheSize;
    }

    /**
     * 调用此方法，上传信息
     *
     * @param applicationContext 全局的application context，避免内存泄露
     */
    public void upload(Context applicationContext) {
        //如果没有设置上传，则不执行
        if (mUpload == null) {
            return;
        }
        //如果网络可用，而且是移动网络，但是用户设置了只在wifi下上传，返回
        if (NetUtil.isConnected(applicationContext) && !NetUtil.isWifi(applicationContext) && mWifiOnly) {
            return;
        }
        Intent intent = new Intent(applicationContext, UploadService.class);
        applicationContext.startService(intent);
    }


}
