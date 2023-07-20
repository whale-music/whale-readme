package org.core.service;


import org.core.mybatis.iservice.SysUserService;
import org.core.mybatis.pojo.SysUserPojo;

public interface AccountService extends SysUserService {
    
    void createAccount(SysUserPojo user);
    
    /**
     * 创建管理员用户
     *
     * @param admin 管理员信息
     */
    void createAdmin(SysUserPojo admin);
    
    /**
     * 用户登录
     *
     * @param username 用户名
     * @param password 密码
     * @return 用户信息
     */
    SysUserPojo login(String username, String password);
    
    /**
     * 查询用户信息, 没有会抛出异常
     *
     * @param username 用户名
     * @return 用户信息
     */
    SysUserPojo getUser(String username);
    
    /**
     * 查询用户信息, 没有直接返回Null
     *
     * @param username 用户名
     * @return 用户信息
     */
    SysUserPojo getUserByName(String username);
}
