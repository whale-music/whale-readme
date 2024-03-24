package org.core.utils.token;

import com.github.benmanes.caffeine.cache.Cache;
import org.core.mybatis.pojo.SysUserPojo;
import org.springframework.stereotype.Component;

@Component
public class UserCacheServiceUtil {
    
    private final Cache<String, SysUserPojo> cache;
    
    public UserCacheServiceUtil(Cache<String, SysUserPojo> cache) {
        this.cache = cache;
    }
    
    private Cache<String, SysUserPojo> getCache() {
        return cache;
    }
    
    public SysUserPojo getUserCache(String key) {
        return getCache().getIfPresent(key);
    }
    
    public void setUserCache(String key, SysUserPojo user) {
        getCache().put(key, user);
    }
    
    public void clearUserCache(String key) {
        getCache().invalidate(key);
    }
}
