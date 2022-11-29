package org.core.utils;

import org.core.pojo.SysUserPojo;

public class UserUtil {
    
    private static final ThreadLocal<SysUserPojo> USER = new ThreadLocal<>();
    
    private UserUtil() {
    }
    
    public static SysUserPojo getUser() {
        return USER.get();
    }
    
    public static void setUser(SysUserPojo userPojo) {
        USER.set(userPojo);
    }
    
    public static void removeUser() {
        USER.remove();
    }
}
