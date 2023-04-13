package org.oss.service;

import java.io.File;

public interface OSSService {
    
    // 检测是否是当前存储地址
    boolean isCurrentOSS(String serviceName);
    
    String getMode();
    
    // 检查访问存储地址
    boolean isConnected(String host, String accessKey, String secretKey);
    
    // 存储文件是否存在
    void isExist(String host, String objectSaveConfig, String file);
    
    /**
     * 获取音乐地址
     *
     * @param host       存储对象host
     * @param objectSave 保存地址
     * @param musicFlag  音乐文件文件地址
     * @param refresh    是否刷新缓存
     * @return 音乐地址
     */
    String getMusicAddresses(String host, String objectSave, String musicFlag, boolean refresh);
    
    // 上传文件返回地址
    String upload(String host, String objectSaveConfig, File srcFile);
    
    // 删除文件
    boolean delete(String host, String objectSaveConfig, String filePath);
}
