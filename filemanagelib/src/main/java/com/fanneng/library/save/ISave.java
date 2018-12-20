package com.fanneng.library.save;

import com.fanneng.library.encryption.IEncryption;

/**
 * 保存日志与崩溃信息的接口
 * Created by liujg on 2016/7/7.
 */
public interface ISave {

    void writeFileInfo(String tag, String content);


    void setEncodeType(IEncryption encodeType);

}
