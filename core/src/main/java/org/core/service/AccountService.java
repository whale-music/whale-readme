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
    // rename method
    SysUserPojo getUser(String username);
    
    /**
     * 查询用户信息, 没有直接返回Null
     *
     * @param username 用户名
     * @return 用户信息
     */
    SysUserPojo getUserByName(String username);
    
    /**
     * 登录子账户
     *
     * @param account  账户
     * @param password 密码
     * @return 用户信息
     */
    SysUserPojo loginSub(String account, String password);
    
    /**
     * 登录用户，用户名或者子账户
     *
     * @param username 登录名
     * @return 用户信息
     */
    // todo: 替换一些方法
    SysUserPojo getUserOrSubAccount(String username);
    
    /**
     * 获取master账户信息
     *
     * @param account 子账户名
     * @return 用户信息
     */
    SysUserPojo getSubAccountMasterUserInfoBySubAccount(String account);
}
