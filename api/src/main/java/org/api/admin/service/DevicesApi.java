package org.api.admin.service;

import com.github.benmanes.caffeine.cache.Cache;
import org.apache.commons.lang3.StringUtils;
import org.api.admin.config.AdminConfig;
import org.api.admin.model.res.ActivityDeviceRes;
import org.core.common.constant.UserCacheTypeConstant;
import org.core.model.UserLoginCacheModel;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

@Service(AdminConfig.ADMIN + "DevicesApi")
public class DevicesApi {
    
    private final Cache<String, UserLoginCacheModel> loginCacheUsers;
    
    public DevicesApi(Cache<String, UserLoginCacheModel> loginCacheUsers) {
        this.loginCacheUsers = loginCacheUsers;
    }
    
    public ActivityDeviceRes getActivityDevice() {
        Set<Map.Entry<String, UserLoginCacheModel>> entries = loginCacheUsers.asMap().entrySet();
        
        ActivityDeviceRes e = new ActivityDeviceRes();
        for (Map.Entry<String, UserLoginCacheModel> entry : entries) {
            UserLoginCacheModel value = entry.getValue();
            if (StringUtils.startsWithIgnoreCase((entry.getKey()), UserCacheTypeConstant.ADMIN)) {
                e.getAdminDevices().add(new ActivityDeviceRes.Devices(entry.getKey(), value));
            }
            if (StringUtils.startsWithIgnoreCase((entry.getKey()), UserCacheTypeConstant.NMUSIC)) {
                e.getNMusicDevices().add(new ActivityDeviceRes.Devices(entry.getKey(), value));
            }
            if (StringUtils.startsWithIgnoreCase((entry.getKey()), UserCacheTypeConstant.SUBSONIC)) {
                e.getSubsonicDevices().add(new ActivityDeviceRes.Devices(entry.getKey(), value));
            }
            if (StringUtils.startsWithIgnoreCase((entry.getKey()), UserCacheTypeConstant.WEBDAV)) {
                e.getWebdavDevices().add(new ActivityDeviceRes.Devices(entry.getKey(), value));
            }
        }
        return e;
    }
    
    public void removeActivityDevice(String id) {
        loginCacheUsers.invalidate(id);
    }
}
