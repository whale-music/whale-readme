package org.oss.service;

import org.core.config.SaveConfig;

import java.io.File;

public interface OSSService {
    
    // 检测是否是当前存储地址
    boolean isCurrentOSS(String serviceName);
    
    String getMode();
    
    // 检查访问存储地址
    boolean isConnected(SaveConfig config);
    
    // 存储文件是否存在
    void isExist(String name);
    
    /**
     * 获取音乐地址
     *
     * @param name    音乐文件文件地址
     * @param refresh 是否刷新缓存
     * @return 音乐地址
     */
    String getMusicAddresses(String name, boolean refresh);
    
    // 上传文件返回地址
    String upload(File srcFile);
    
    // 删除文件
    boolean delete(String name);
}
