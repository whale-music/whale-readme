package org.musicbox.utils;

import org.musicbox.pojo.SysUserPojo;

public class UserUtil {
    
    private UserUtil() {
    }
    
    private static final ThreadLocal<SysUserPojo> USER = new ThreadLocal<>();
    
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
