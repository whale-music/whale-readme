package org.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang3.StringUtils;
import org.core.common.exception.BaseException;
import org.core.common.result.ResultCode;
import org.core.mybatis.iservice.impl.SysUserServiceImpl;
import org.core.mybatis.pojo.SysUserPojo;
import org.core.service.AccountService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service("Account")
public class AccountServiceImpl extends SysUserServiceImpl implements AccountService {
    
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
            throw new BaseException(ResultCode.USER_NOT_EXIST);
        }
        one.setPassword(null);
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
}
