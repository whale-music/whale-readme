package org.oss.service.impl;

import org.oss.service.OSSService;
import org.springframework.stereotype.Component;

@Component
public class MinioOSSServiceImpl implements OSSService {
    
    @Override
    public boolean isConnected(String host, String accessKey, String secretKey) {
        return false;
    }
    
    @Override
    public void isExist() {
    }
    
    @Override
    public String upload(String filePath) {
        return null;
    }
    
    @Override
    public boolean delete(String filePath) {
        return false;
    }
}
