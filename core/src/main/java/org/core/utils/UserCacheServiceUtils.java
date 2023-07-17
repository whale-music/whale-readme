package org.core.utils;

import cn.hutool.core.lang.TypeReference;
import com.github.benmanes.caffeine.cache.Cache;
import org.core.mybatis.pojo.SysUserPojo;
import org.core.utils.spring.SpringUtil;

public class UserCacheServiceUtils {
    
    private UserCacheServiceUtils() {
    }
    
    private static Cache<String, SysUserPojo> getCache() {
        return SpringUtil.getBean(new TypeReference<>() {
        });
    }
    
    
    public static SysUserPojo getUserCache(String key) {
        return getCache().getIfPresent(key);
    }
    
    public static void setUserCache(String key, SysUserPojo user) {
        getCache().put(key, user);
    }
    
    public static void clearUserCache(String key) {
        getCache().invalidate(key);
    }
}
