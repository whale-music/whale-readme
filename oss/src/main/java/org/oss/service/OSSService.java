package org.oss.service;

public interface OSSService {
    
    // 访问存储地址
    boolean isConnected(String host, String accessKey, String secretKey);
    
    // 存储地址是否存在
    void isExist();
    
    // 上传文件返回地址
    String upload(String filePath);
    
    // 删除文件
    boolean delete(String filePath);
}
