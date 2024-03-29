package org.api.subsonic.aspect;

import org.core.common.constant.UserCacheKeyFieldConstant;
import org.core.config.MybatisAutoFillUpDateConfig;
import org.core.mybatis.pojo.SysUserPojo;
import org.core.service.AccountService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class SubsonicUserCache {
    
    private static final String USER_OR_SUB_ACCOUNT = "userOrSubAccount";
    private final AccountService accountService;
    
    public SubsonicUserCache(AccountService accountService) {
        this.accountService = accountService;
    }
    
    @Cacheable(value = UserCacheKeyFieldConstant.SUBSONIC_USER_OR_SUB_ACCOUNT, key = "#username")
    public SysUserPojo checkUser(String username) {
        return accountService.getUserOrSubAccount(username);
    }
    
    /**
     * 该方法没有任何调用，缓存刷新只会在数据库用户表{@link MybatisAutoFillUpDateConfig}更新中进行
     */
    @CacheEvict(value = USER_OR_SUB_ACCOUNT, allEntries = true)
    public void refreshAllCache() {
        // refresh webdav all cache
    }
}
