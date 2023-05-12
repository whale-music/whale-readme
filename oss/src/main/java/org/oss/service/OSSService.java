package org.oss.service;

import org.core.config.SaveConfig;

import java.io.File;
import java.util.Collection;

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
    
    /**
     * 获取音乐MD5值，为null获取所有md5
     *
     * @param md5     音乐的md5值
     * @param refresh 是否刷新缓存
     * @return MD5值
     */
    Collection<String> getMusicAllMD5(String md5, boolean refresh);
    
    default Collection<String> getMusicAllMD5(boolean refresh) {
        return this.getMusicAllMD5(null, refresh);
    }
    
    // 上传文件返回地址
    String upload(File srcFile, String md5);
    
    // 删除文件
    boolean delete(String name);
}
