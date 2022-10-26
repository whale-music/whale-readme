package org.musicbox.controller.neteasecloudmusicapi;

import org.musicbox.common.vo.user.Account;
import org.musicbox.common.vo.user.Profile;
import org.musicbox.common.vo.user.UserVo;
import org.musicbox.pojo.SysUserPojo;

public abstract class BaseController {
    
    protected static UserVo getUserVo(SysUserPojo account) {
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
        return userVo;
    }
}
