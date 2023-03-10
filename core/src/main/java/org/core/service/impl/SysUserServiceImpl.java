package org.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.core.common.exception.BaseException;
import org.core.common.result.ResultCode;
import org.core.mapper.SysUserMapper;
import org.core.pojo.SysUserPojo;
import org.core.service.SysUserService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 * 系统用户表 服务实现类
 * </p>
 *
 * @author Sakura
 * @since 2022-12-02
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUserPojo> implements SysUserService {
    /**
     * 创建用户
     *
     * @param user 用户信息
     */
    public void createAccount(SysUserPojo user) {
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
     * 用户登录
     *
     * @param phone    账号
     * @param password 密码
     * @return 返回用户信息
     */
    public SysUserPojo login(String phone, String password) {
        LambdaQueryWrapper<SysUserPojo> lambdaQuery = new LambdaQueryWrapper<>();
        lambdaQuery.eq(SysUserPojo::getUsername, phone);
        lambdaQuery.eq(SysUserPojo::getPassword, password);
        SysUserPojo one = this.getOne(lambdaQuery);
        if (one == null) {
            throw new BaseException(ResultCode.USER_NOT_EXIST);
        }
        one.setPassword(null);
        return one;
    }
}
