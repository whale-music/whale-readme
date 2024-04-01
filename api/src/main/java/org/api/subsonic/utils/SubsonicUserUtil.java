package org.api.subsonic.utils;

import org.core.common.exception.BaseException;
import org.core.common.result.ResultCode;
import org.core.mybatis.pojo.SysUserPojo;

import java.util.Objects;

public class SubsonicUserUtil {
    private SubsonicUserUtil() {}
    private static final ThreadLocal<SysUserPojo> USER = new ThreadLocal<>();
    
    public static SysUserPojo getUser() {
        if (USER.get() == null) {
            throw new BaseException(ResultCode.USER_NOT_LOGIN);
        }
        return USER.get();
    }
    
    public static void setUser(SysUserPojo userPojo) {
        if (Objects.isNull(userPojo)) {
            return;
        }
        USER.set(userPojo);
    }
    
    public static void removeUser() {
        USER.remove();
    }
}
