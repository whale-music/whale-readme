package org.oss.service;

public interface OSSService {
    
    // 检测是否是当前存储地址
    boolean isCurrentOSS(String serviceName);
    
    // 检查访问存储地址
    boolean isConnected(String host, String accessKey, String secretKey);
    
    // 存储文件是否存在
    void isExist(String objectSaveConfig, String file);
    
    String getMusicAddresses(String objectSaveConfig, String file);
    
    // 上传文件返回地址
    String upload(String objectSaveConfig, String filePath);
    
    // 删除文件
    boolean delete(String filePath);
}
