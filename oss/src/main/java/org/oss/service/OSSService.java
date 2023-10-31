package org.oss.service;

import org.apache.commons.lang3.StringUtils;
import org.common.properties.SaveConfig;

import java.io.File;
import java.util.*;

public interface OSSService {
    
    /**
     * 检测是否是当前存储地址
     *
     * @param serviceName 服务名
     * @return 是否相同
     */
    default boolean isCurrentOSS(String serviceName) {
        return StringUtils.equalsIgnoreCase(this.getMode(), serviceName);
    }
    
    /**
     * 返回当前服务名
     *
     * @return 服务名
     */
    String getMode();
    
    /**
     * 检查访问存储地址
     *
     * @param config 服务配置
     */
    void isConnected(SaveConfig config);
    
    /**
     * 存储文件是否存在
     *
     * @param name 文件名
     */
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
     * 获取音乐地址列表
     *
     * @param name    音乐集合
     * @param refresh 是否刷新缓存
     * @return 音乐地址集合
     */
    Set<String> getAddresses(Collection<String> name, boolean refresh);
    
    /**
     * 获取音乐地址
     *
     * @param md5     音乐文件文件MD5
     * @param refresh 是否刷新缓存
     * @return 音乐地址 key md5, value url, size
     */
    Map<String, Map<String, String>> getAddressByMd5(String md5, boolean refresh);
    
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
    
    /**
     * 删除文件
     *
     * @param name 文件名
     * @return 是否删除成功
     */
    boolean delete(List<String> name);
    
    default boolean delete(String name) {
        return delete(Collections.singletonList(name));
    }
}
