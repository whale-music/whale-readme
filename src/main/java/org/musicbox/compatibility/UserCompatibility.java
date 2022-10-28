package org.musicbox.compatibility;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.musicbox.common.exception.ResultCode;
import org.musicbox.exception.DuplicateUserNameException;
import org.musicbox.exception.UserDoesNotExistException;
import org.musicbox.pojo.SysUserPojo;
import org.musicbox.pojo.TbCollectPojo;
import org.musicbox.service.SysUserService;
import org.musicbox.service.TbCollectMusicService;
import org.musicbox.service.TbCollectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    // 用户服务
    @Autowired
    private SysUserService userService;
    
    // 歌单表
    @Autowired
    private TbCollectService collectService;
    
    @Autowired
    private TbCollectMusicService collectMusicService;
    
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
            throw new DuplicateUserNameException(ResultCode.DUPLICATE_USER_NAME_ERROR.getResultCode(), ResultCode.DUPLICATE_USER_NAME_ERROR.getResultMsg());
        }
    }
    
    /**
     * 获取用户ID信息
     *
     * @param userId 用户ID
     * @return 用户信息
     */
    public SysUserPojo getAccount(Long userId) {
        SysUserPojo userPojo = userService.getById(userId);
        if (userPojo == null) {
            throw new UserDoesNotExistException(ResultCode.USER_NOT_EXIST.getResultCode(), ResultCode.USER_NOT_EXIST.getResultMsg());
        }
        return userPojo;
    }
    
    /**
     * 用户登录
     *
     * @param phone    账号
     * @param password 密码
     * @return 返回用户信息
     */
    public SysUserPojo loginEfficacy(String phone, String password) {
        LambdaQueryWrapper<SysUserPojo> lambdaQuery = new LambdaQueryWrapper<>();
        lambdaQuery.eq(SysUserPojo::getUsername, phone);
        lambdaQuery.eq(SysUserPojo::getPassword, password);
        SysUserPojo one = userService.getOne(lambdaQuery);
        if (one == null) {
            throw new UserDoesNotExistException(ResultCode.USER_NOT_EXIST.getResultCode(), ResultCode.USER_NOT_EXIST.getResultMsg());
        }
        return one;
    }
    
    /**
     * 跟新用户信息
     *
     * @param userPojo 用户信息
     */
    public void updateUserPojo(SysUserPojo userPojo) {
        boolean b = userService.updateById(userPojo);
        if (b) {
            log.debug("用户名初始化成功");
        } else {
            throw new UserDoesNotExistException(ResultCode.USER_NOT_EXIST.getResultCode(), ResultCode.USER_NOT_EXIST.getResultMsg());
        }
    }
    
    /**
     * 查询用户所有歌单
     *
     * @param uid 用户ID
     * @return 返回用户所有歌单
     */
    public List<TbCollectPojo> getPlayList(String uid) {
        LambdaQueryWrapper<TbCollectPojo> lambdaQueryWrapper = Wrappers.<TbCollectPojo>lambdaQuery().eq(TbCollectPojo::getUserId, Long.valueOf(uid));
        return collectService.list(lambdaQueryWrapper);
    }
}
