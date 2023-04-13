package org.oss.service.impl.local;

import cn.hutool.core.io.FileUtil;
import org.apache.commons.lang3.StringUtils;
import org.core.common.result.ResultCode;
import org.core.config.SaveConfig;
import org.core.utils.ExceptionUtil;
import org.oss.service.OSSService;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class LocalOSSServiceImpl implements OSSService {
    
    private static final String SERVICE_NAME = "Local";
    
    private SaveConfig config;
    
    @Override
    public boolean isCurrentOSS(String serviceName) {
        return StringUtils.equals(SERVICE_NAME, serviceName);
    }
    
    @Override
    public String getMode() {
        return SERVICE_NAME;
    }
    
    @Override
    public boolean isConnected(SaveConfig config) {
        this.config = config;
        return true;
    }
    
    @Override
    public void isExist(String name) {
        boolean flag = false;
        for (String s : config.getObjectSave()) {
            flag = FileUtil.isFile(s + FileUtil.FILE_SEPARATOR + name);
        }
        ExceptionUtil.isNull(flag, ResultCode.FILENAME_EXIST);
    }
    
    @Override
    public String getMusicAddresses(String name, boolean refresh) {
        return null;
    }
    
    @Override
    public String upload(File srcFile) {
        FileUtil.copy(srcFile, new File(config.getObjectSave().get(config.getAssignObjectSave()), srcFile.getName()), true);
        return srcFile.getName();
    }
    
    @Override
    public boolean delete(String name) {
        return FileUtil.del(name);
    }
}
