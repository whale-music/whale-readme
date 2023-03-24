package org.core.utils;

import org.core.common.exception.BaseException;
import org.core.common.result.ResultCode;
import org.core.pojo.SysUserPojo;

public class UserUtil {
    
    private static final ThreadLocal<SysUserPojo> USER = new ThreadLocal<>();
    
    private UserUtil() {
    }
    
    public static SysUserPojo getUser() {
        if (USER.get() == null) {
            throw new BaseException(ResultCode.USER_NOT_LOGIN);
        }
        return USER.get();
    }
    
    public static void setUser(SysUserPojo userPojo) {
        USER.set(userPojo);
    }
    
    public static void removeUser() {
        USER.remove();
    }
}
