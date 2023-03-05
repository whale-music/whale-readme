package org.oss.service.impl.alist;

import org.apache.commons.lang3.StringUtils;
import org.core.common.exception.BaseException;
import org.core.common.result.ResultCode;
import org.oss.service.OSSService;
import org.oss.service.impl.alist.util.Request;

public class AListOSSServiceImpl implements OSSService {
    
    private static final String SERVICE_NAME = "AList";
    
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
        return false;
    }
    
    @Override
    public void isExist(String host, String objectSaveConfig, String file) {
        getMusicAddresses(host, objectSaveConfig, file);
    }
    
    @Override
    public String getMusicAddresses(String host, String objectSave, String path) {
        try {
            String musicAddress = Request.getMusicAddress(host, objectSave, path);
            if (StringUtils.isBlank(musicAddress)) {
                throw new BaseException();
            } else {
                return musicAddress;
            }
        } catch (Exception e) {
            throw new BaseException(ResultCode.SONG_NOT_EXIST);
        }
    }
    
    @Override
    public String upload(String objectSaveConfig, String filePath) {
        throw new BaseException(ResultCode.PERMISSION_NO_ACCESS);
    }
    
    @Override
    public boolean delete(String filePath) {
        throw new BaseException(ResultCode.PERMISSION_NO_ACCESS);
    }
}
