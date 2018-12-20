package com.fanneng.library.save.imp;

import android.content.Context;

import com.fanneng.library.save.BaseSaver;

/**
 * 信息都写在一个文件中
 */
public class FilesWriter extends BaseSaver {

    private final static String TAG = "FileWriter";

    /**
     * 初始化，继承父类
     *
     * @param context 上下文
     */
    public FilesWriter(Context context) {
        super(context);
    }



}
