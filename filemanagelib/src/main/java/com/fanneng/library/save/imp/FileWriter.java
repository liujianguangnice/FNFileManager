package com.fanneng.library.save.imp;

import android.content.Context;
import android.os.Environment;

import com.fanneng.library.FileInfoReport;
import com.fanneng.library.save.BaseSaver;
import com.fanneng.library.util.FileUtil;
import com.fanneng.library.util.FileInfoUtil;

import java.io.File;
import java.util.Date;

/**
 * 信息都写在一个文件中
 */
public class FileWriter extends BaseSaver {

    private final static String TAG = "FileWriter";

    /**
     * 初始化，继承父类
     *
     * @param context 上下文
     */
    public FileWriter(Context context) {
        super(context);
    }



}
