package org.musicbox.controller.neteasecloudmusicapi.v1;

import cn.hutool.core.bean.BeanUtil;
import org.musicbox.common.result.NeteaseResult;
import org.musicbox.common.vo.user.Account;
import org.musicbox.common.vo.user.Profile;
import org.musicbox.common.vo.user.UserVo;
import org.musicbox.compatibility.UserCompatibility;
import org.musicbox.pojo.SysUserPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * <p>
 * NeteaseCloudMusicApi 用户控制器
 * </p>
 *
 * @author Sakura
 * @since 2022-10-22
 */
@RestController
@RequestMapping("/")
public class UserController {
    
    @Autowired
    private UserCompatibility user;
    
    @GetMapping("/user/account")
    public NeteaseResult getUser() {
        // 查找用户
        SysUserPojo account = user.getAccount(2L);
        // 包装返回结果
        UserVo userVo = new UserVo();
        userVo.setProfile(new Profile());
        Profile profile = userVo.getProfile();
        profile.setNickname(account.getNickname());
    
        profile.setUserId(account.getId());
        profile.setUserName(account.getUsername());
        profile.setShortUserName(account.getUsername());
    
        profile.setAvatarUrl(account.getAvatarUrl());
        profile.setBackgroundUrl(account.getBackgroundUrl());
    
        profile.setLastLoginIP(account.getLastLoginIp());
        profile.setLastLoginTime(account.getLastLoginTime());
    
        profile.setCreateTime(account.getCreateTime().getNano());
    
        userVo.setAccount(new Account());
        Account userVoAccount = userVo.getAccount();
        userVoAccount.setId(account.getId());
        userVoAccount.setUserName(account.getUsername());
    
    
        // 前端通用返回类
        NeteaseResult r = new NeteaseResult();
        r.putAll(BeanUtil.beanToMap(userVo));
        return r.success();
    }
    
    @GetMapping("/register/account")
    public void addUser(String account, String nickname, String password) {
        SysUserPojo userPojo = new SysUserPojo();
        userPojo.setUsername(account);
        userPojo.setNickname(nickname);
        userPojo.setPassword(password);
        userPojo.setCreateTime(LocalDateTime.now());
        user.createAccount(userPojo);
    }
}
