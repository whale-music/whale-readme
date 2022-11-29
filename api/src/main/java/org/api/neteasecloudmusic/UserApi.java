package org.api.neteasecloudmusic;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.core.common.exception.BaseException;
import org.core.common.result.ResultCode;
import org.core.pojo.SysUserPojo;
import org.core.pojo.TbCollectPojo;
import org.core.pojo.TbUserSingerPojo;
import org.core.service.SysUserService;
import org.core.service.TbCollectService;
import org.core.service.TbUserSingerService;
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
public class UserApi {
    // 用户服务
    @Autowired
    private SysUserService userService;
    
    // 歌单表
    @Autowired
    private TbCollectService collectService;
    
    
    // 用户关注歌手表
    @Autowired
    private TbUserSingerService userSingerService;
    
    /**
     * 创建用户
     *
     * @param user 用户信息
     */
    public void createAccount(SysUserPojo user) {
        long count = userService.count(Wrappers.<SysUserPojo>lambdaQuery()
                                               .eq(StringUtils.isNotBlank(user.getUsername()),
                                                       SysUserPojo::getUsername,
                                                       user.getUsername()));
        if (count == 0) {
            userService.save(user);
        } else {
            throw new BaseException(ResultCode.DUPLICATE_USER_NAME_ERROR);
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
            throw new BaseException(ResultCode.USER_NOT_EXIST);
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
            throw new BaseException(ResultCode.USER_NOT_EXIST);
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
            throw new BaseException(ResultCode.USER_NOT_EXIST);
        }
    }
    
    /**
     * 查询用户所有歌单
     *
     * @param uid 用户ID
     * @return 返回用户所有歌单
     */
    public Page<TbCollectPojo> getPlayList(String uid, Long pageIndex, Long pageSize) {
        LambdaQueryWrapper<TbCollectPojo> lambdaQueryWrapper = Wrappers.<TbCollectPojo>lambdaQuery()
                                                                       .eq(TbCollectPojo::getUserId, Long.valueOf(uid))
                                                                       .orderByDesc(TbCollectPojo::getSort);
        return collectService.page(new Page<>(pageIndex, pageSize), lambdaQueryWrapper);
    }
    
    /**
     * 获取用户创建歌单数量
     *
     * @param userId 用户ID
     * @return 歌单数量
     */
    public Long getCreatedPlaylistCount(Long userId) {
        LambdaQueryWrapper<TbCollectPojo> lambdaQueryWrapper = Wrappers.<TbCollectPojo>lambdaQuery()
                                                                       .eq(TbCollectPojo::getUserId, userId);
        return collectService.count(lambdaQueryWrapper);
    }
    
    
    /**
     * 获取订阅(收藏)歌单总数
     *
     * @param userId 用户ID
     * @return 订阅歌单总数
     */
    public Long getSubPlaylistCount(Long userId) {
        LambdaQueryWrapper<TbCollectPojo> lambdaQueryWrapper = Wrappers.<TbCollectPojo>lambdaQuery()
                                                                       .eq(TbCollectPojo::getUserId, userId)
                                                                       .eq(TbCollectPojo::getSubscribed, true);
        return collectService.count(lambdaQueryWrapper);
    }
    
    /**
     * 获取用户关注歌曲家数量
     *
     * @return 关注数量
     */
    public Long getUserBySinger(Long userId) {
        LambdaQueryWrapper<TbUserSingerPojo> lambdaQueryWrapper = Wrappers.<TbUserSingerPojo>lambdaQuery()
                                                                          .eq(TbUserSingerPojo::getUserId, userId);
        return userSingerService.count(lambdaQueryWrapper);
    }
}
