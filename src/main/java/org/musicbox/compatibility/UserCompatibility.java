package org.musicbox.compatibility;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.musicbox.common.exception.ExceptionEnum;
import org.musicbox.exception.DuplicateUserNameException;
import org.musicbox.exception.UserDoesNotExistException;
import org.musicbox.pojo.SysUserPojo;
import org.musicbox.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * User 用户服务兼容层
 * </p>
 *
 * @author Sakura
 * @since 2022-10-22
 */
@Slf4j
@Service
public class UserCompatibility {
    @Autowired
    private SysUserService userService;
    
    
    /**
     * 创建用户
     *
     * @param user 用户信息
     */
    public void createAccount(SysUserPojo user) {
        long count = userService.count(Wrappers.<SysUserPojo>lambdaQuery().eq(StringUtils.isNotBlank(user.getUsername()), SysUserPojo::getUsername, user.getUsername()));
        if (count == 0) {
            userService.save(user);
        } else {
            throw new DuplicateUserNameException(ExceptionEnum.DUPLICATE_USER_NAME_ERROR.getResultCode(), ExceptionEnum.DUPLICATE_USER_NAME_ERROR.getResultMsg());
        }
    }
    
    /**
     * 获取用户信息
     *
     * @param userId 用户ID
     * @return 用户信息
     */
    public SysUserPojo getAccount(Long userId) {
        SysUserPojo userPojo = userService.getById(userId);
        if (userPojo == null) {
            throw new UserDoesNotExistException(ExceptionEnum.USER_DOES_NOT_EXIST.getResultCode(), ExceptionEnum.USER_DOES_NOT_EXIST.getResultMsg());
        }
        return userPojo;
    }
}
