package org.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang3.StringUtils;
import org.core.common.exception.BaseException;
import org.core.common.result.ResultCode;
import org.core.config.UserSubPasswordConfig;
import org.core.mybatis.iservice.impl.SysUserServiceImpl;
import org.core.mybatis.pojo.SysUserPojo;
import org.core.service.AccountService;
import org.core.utils.ip.IpUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Objects;

@Service("account")
public class AccountServiceImpl extends SysUserServiceImpl implements AccountService {
    
    private final UserSubPasswordConfig userSubPasswordConfig;
    
    public AccountServiceImpl(UserSubPasswordConfig userSubPasswordConfig) {
        this.userSubPasswordConfig = userSubPasswordConfig;
    }
    
    /**
     * 创建用户
     *
     * @param user 用户信息
     */
    public void createAccount(SysUserPojo user) {
        user.setAccountType(1);
        createUser(user);
    }
    
    private void createUser(SysUserPojo user) {
        long count = this.count(Wrappers.<SysUserPojo>lambdaQuery()
                                        .eq(StringUtils.isNotBlank(user.getUsername()),
                                                SysUserPojo::getUsername,
                                                user.getUsername()));
        if (count == 0) {
            user.setLastLoginTime(LocalDateTime.now());
            user.setStatus(true);
            this.save(user);
        } else {
            throw new BaseException(ResultCode.DUPLICATE_USER_NAME_ERROR);
        }
    }
    
    /**
     * @param admin 管理用户信息
     */
    @Override
    public void createAdmin(SysUserPojo admin) {
        admin.setAccountType(0);
        createUser(admin);
    }
    
    /**
     * 用户登录
     *
     * @param username 账号
     * @param password 密码
     * @return 返回用户信息
     */
    public SysUserPojo login(String username, String password) {
        LambdaQueryWrapper<SysUserPojo> lambdaQuery = new LambdaQueryWrapper<>();
        lambdaQuery.eq(SysUserPojo::getUsername, username);
        lambdaQuery.eq(SysUserPojo::getPassword, password);
        SysUserPojo one = this.getOne(lambdaQuery);
        if (one == null) {
            throw new BaseException(ResultCode.ACCOUNT_DOES_NOT_EXIST_OR_WRONG_PASSWORD);
        }
        if (Boolean.FALSE.equals(one.getStatus())) {
            throw new BaseException(ResultCode.USER_ACCOUNT_FORBIDDEN);
        }
        one.setPassword(null);
        one.setLastLoginIp(IpUtils.getIpAddr());
        this.updateById(one);
        return one;
    }
    
    
    /**
     * 查询用户信息
     *
     * @param username 用户名
     * @return 用户信息
     */
    @Override
    public SysUserPojo getUser(String username) {
        SysUserPojo one = this.getOne(Wrappers.<SysUserPojo>lambdaQuery().eq(SysUserPojo::getUsername, username));
        if (one == null) {
            throw new BaseException(ResultCode.USER_NOT_EXIST);
        }
        return one;
    }
    
    /**
     * 查询用户信息, 没有直接返回Null
     *
     * @param username 用户名
     * @return 用户信息
     */
    @Override
    public SysUserPojo getUserByName(String username) {
        return this.getOne(Wrappers.<SysUserPojo>lambdaQuery().eq(SysUserPojo::getUsername, username));
    }
    
    /**
     * 登录子账户
     *
     * @param account  账户
     * @param password 密码
     * @return 用户信息
     */
    @Override
    public SysUserPojo loginSub(String account, String password) {
        if (Boolean.FALSE.equals(userSubPasswordConfig.accountExistence(account))) {
            throw new BaseException(ResultCode.ACCOUNT_DOES_NOT_EXIST_OR_WRONG_PASSWORD);
        }
        if (Boolean.FALSE.equals(userSubPasswordConfig.accountVerification(account, password))) {
            throw new BaseException(ResultCode.ACCOUNT_DOES_NOT_EXIST_OR_WRONG_PASSWORD);
        }
        SysUserPojo sysUserPojo = userSubPasswordConfig.getSubAccount(account);
        return this.login(sysUserPojo.getUsername(), sysUserPojo.getPassword());
    }
    
    /**
     * 登录用户或用户子账户
     *
     * @param account     账户
     * @param password    密码
     * @param md5Password 密码md5
     * @return 用户信息
     */
    @Override
    public SysUserPojo loginUserOrSubAccount(String account, String password, String md5Password) {
        SysUserPojo userPojo = this.getUserOrSubAccount(account);
        String md5 = StringUtils.isBlank(password) ? md5Password : DigestUtils.md5DigestAsHex(StringUtils.getBytes(password, StandardCharsets.UTF_8));
        if (StringUtils.equals(md5, DigestUtils.md5DigestAsHex(StringUtils.getBytes(userPojo.getPassword(), StandardCharsets.UTF_8)))) {
            return userPojo;
        }
        // 校验子账户
        String subPassword = userSubPasswordConfig.getSubPassword(account);
        if (StringUtils.equals(md5, DigestUtils.md5DigestAsHex(StringUtils.getBytes(subPassword, StandardCharsets.UTF_8)))) {
            return userPojo;
        }
        throw new BaseException(ResultCode.ACCOUNT_DOES_NOT_EXIST_OR_WRONG_PASSWORD);
    }
    
    /**
     * 登录用户，用户名或者子账户
     *
     * @param username 登录名
     * @return 用户信息
     */
    @Override
    public SysUserPojo getUserOrSubAccount(String username) {
        SysUserPojo userByName = this.getUserByName(username);
        if (Objects.nonNull(userByName)) {
            return userByName;
        }
        SysUserPojo user = this.getSubAccountMasterUserInfoBySubAccount(username);
        if (Objects.isNull(user)) {
            throw new BaseException(ResultCode.USER_NOT_EXIST);
        }
        return user;
    }
    
    /**
     * 获取master账户信息
     *
     * @param account 子账户名
     * @return 用户信息
     */
    @Override
    public SysUserPojo getSubAccountMasterUserInfoBySubAccount(String account) {
        SysUserPojo subAccount = userSubPasswordConfig.getSubAccount(account);
        if (Objects.isNull(subAccount)) {
            throw new BaseException(ResultCode.USER_NOT_EXIST);
        }
        subAccount.setPassword(userSubPasswordConfig.getSubPassword(account));
        return subAccount;
    }
}
