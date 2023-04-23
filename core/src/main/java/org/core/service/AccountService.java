package org.core.service;


import org.core.iservice.SysUserService;
import org.core.pojo.SysUserPojo;

public interface AccountService extends SysUserService {
    
    void createAccount(SysUserPojo user);
    
    /**
     * 用户登录
     * @param username 用户名
     * @param password 密码
     * @return 用户信息
     */
    SysUserPojo login(String username, String password);
    
    /**
     * 查询用户信息
     * @param username 用户名
     * @return 用户信息
     */
    SysUserPojo getUser(String username);
}
