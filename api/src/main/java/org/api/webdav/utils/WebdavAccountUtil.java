package org.api.webdav.utils;

import org.core.mybatis.pojo.SysUserPojo;

public class WebdavAccountUtil {
    private static final ThreadLocal<SysUserPojo> ACCOUNT_USER = new ThreadLocal<>();
    
    private WebdavAccountUtil() {
    }
    
    public static SysUserPojo getAccount() {
        return ACCOUNT_USER.get();
    }
    
    public static void setAccount(SysUserPojo account) {
        ACCOUNT_USER.set(account);
    }
    
    public static void clear() {
        ACCOUNT_USER.remove();
    }
}
