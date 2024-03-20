package org.api.subsonic.aspect;

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
    
    // todo 需要设置缓存刷新事件，防止密码修改，缓存没有更新
    @Cacheable(value = USER_OR_SUB_ACCOUNT, key = "#username")
    public SysUserPojo checkUser(String username) {
        return accountService.getUserOrSubAccount(username);
    }
    
    @CacheEvict(value = USER_OR_SUB_ACCOUNT, allEntries = true)
    public void refreshAllCache() {
        // refresh webdav all cache
    }
}
