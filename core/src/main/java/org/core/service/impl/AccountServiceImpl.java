package org.core.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.core.common.exception.BaseException;
import org.core.common.reflection.ReflectionFieldName;
import org.core.common.result.ResultCode;
import org.core.iservice.impl.SysUserServiceImpl;
import org.core.pojo.SysUserPojo;
import org.core.service.AccountService;
import org.springframework.data.jpa.domain.Specification;
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
        long count = this.countLambda(StringUtils.isNotBlank(user.getUsername()), SysUserPojo::getUsername, user.getUsername());
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
        Specification<SysUserPojo> where = Specification.where((root, query, criteriaBuilder) -> query.where(
                criteriaBuilder.equal(root.get(ReflectionFieldName.getFieldName(SysUserPojo::getUsername)), phone),
                criteriaBuilder.equal(root.get(ReflectionFieldName.getFieldName(SysUserPojo::getPassword)), password)
        ).getRestriction());
        SysUserPojo one = this.getOne(where).orElseThrow(() -> new BaseException(ResultCode.USER_NOT_EXIST));
        one.setPassword(null);
        return one;
    }
}
