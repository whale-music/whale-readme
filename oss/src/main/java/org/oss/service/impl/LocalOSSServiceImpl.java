package org.oss.service.impl;

import cn.hutool.core.io.FileUtil;
import org.core.config.MusicConfig;
import org.oss.service.OSSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class LocalOSSServiceImpl implements OSSService {
    
    @Autowired
    private MusicConfig config;
    
    @Override
    public boolean isConnected(String host, String accessKey, String secretKey) {
        return false;
    }
    
    @Override
    public void isExist() {
        FileUtil.mkParentDirs("./" + config.getObjectSave());
    }
    
    @Override
    public String upload(String filePath) {
        File srcFile = new File(filePath);
        FileUtil.copy(srcFile, new File(config.getObjectSave(), srcFile.getName()), true);
        return srcFile.getName();
    }
    
    @Override
    public boolean delete(String filePath) {
        return FileUtil.del(filePath);
    }
}
