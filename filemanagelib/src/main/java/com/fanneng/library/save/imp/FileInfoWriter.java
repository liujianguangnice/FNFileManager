package com.fanneng.library.save.imp;

import com.fanneng.library.FileInfoReport;
import com.fanneng.library.save.ISave;
import com.fanneng.library.util.FileInfoUtil;

/**
 * 用于写入Log到本地
 * Created by liujg on 2016/7/9.
 */
public class FileInfoWriter {
    private static FileInfoWriter mFileInfoWriter;
    private static ISave mSave;

    private FileInfoWriter() {
    }


    public static FileInfoWriter getInstance() {
        if (mFileInfoWriter == null) {
            synchronized (FileInfoReport.class) {
                if (mFileInfoWriter == null) {
                    mFileInfoWriter = new FileInfoWriter();
                }
            }
        }
        return mFileInfoWriter;
    }


    public FileInfoWriter init(ISave save) {
        mSave = save;
        return this;
    }

    public static void writeFileInfo(String sourceTag, String content) {
        FileInfoUtil.d(sourceTag, content);
        mSave.writeFileInfo(sourceTag, content);
    }
}