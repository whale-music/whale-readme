package org.musicbox.utils;

import org.musicbox.pojo.SysUserPojo;

public class ThreadUtils {
    
    private static ThreadLocal<SysUserPojo> USER = new ThreadLocal<>();
    
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
