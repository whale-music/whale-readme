package org.core.utils.token;

import com.github.benmanes.caffeine.cache.Cache;
import org.core.model.UserLoginCacheModel;
import org.core.mybatis.pojo.SysUserPojo;
import org.springframework.stereotype.Component;

@Component
public class UserCacheServiceUtil {
    
    private final Cache<String, UserLoginCacheModel> cache;
    
    public UserCacheServiceUtil(Cache<String, UserLoginCacheModel> cache) {
        this.cache = cache;
    }
    
    private Cache<String, UserLoginCacheModel> getCache() {
        return cache;
    }
    
    public SysUserPojo getUserCache(String key) {
        return getCache().getIfPresent(key);
    }
    
    public void setUserCache(String key, UserLoginCacheModel user) {
        getCache().put(key, user);
    }
    
    public void clearUserCache(String key) {
        getCache().invalidate(key);
    }
}
