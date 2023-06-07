package org.oss.service;

import org.core.config.SaveConfig;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

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
    String getAddresses(String name, boolean refresh);
    
    /**
     * 获取音乐MD5值，为null获取所有md5
     *
     * @param md5     音乐的md5值
     * @param refresh 是否刷新缓存
     * @return MD5值
     */
    Collection<String> getAllMD5(String md5, boolean refresh);
    
    default Collection<String> getAllMD5(boolean refresh) {
        return this.getAllMD5(null, refresh);
    }
    
    /**
     * 上传文件返回地址
     *
     * @param paths   路径
     * @param index   选中上传路径
     * @param srcFile 上传文件
     * @param md5     上传文件md5 非必传
     * @return 文件路径相对
     */
    String upload(List<String> paths, Integer index, File srcFile, String md5);
    
    // 删除文件
    boolean delete(List<String> name);
    
    default boolean delete(String name) {
        return delete(Collections.singletonList(name));
    }
}
