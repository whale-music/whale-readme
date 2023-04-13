package org.oss.service.impl.local;

import cn.hutool.core.io.FileUtil;
import org.apache.commons.lang3.StringUtils;
import org.core.common.exception.BaseException;
import org.core.common.result.ResultCode;
import org.oss.service.OSSService;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class LocalOSSServiceImpl implements OSSService {
    
    private static final String SERVICE_NAME = "Local";
    
    @Override
    public boolean isCurrentOSS(String serviceName) {
        return StringUtils.equals(SERVICE_NAME, serviceName);
    }
    
    @Override
    public String getMode() {
        return SERVICE_NAME;
    }
    
    @Override
    public boolean isConnected(String host, String accessKey, String secretKey) {
        throw new BaseException(ResultCode.LOCAL_FILE);
    }
    
    @Override
    public void isExist(String host, String objectSaveConfig, String file) {
        FileUtil.mkParentDirs("./" + objectSaveConfig);
    }
    
    @Override
    public String getMusicAddresses(String host, String objectSave, String musicFlag, boolean refresh) {
        return null;
    }
    
    @Override
    public String upload(String host, String objectSaveConfig, File srcFile) {
        FileUtil.copy(srcFile, new File(objectSaveConfig, srcFile.getName()), true);
        return srcFile.getName();
    }
    
    @Override
    public boolean delete(String host, String objectSaveConfig, String filePath) {
        return FileUtil.del(filePath);
    }
}
