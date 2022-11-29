package org.web.controller.neteasecloudmusicapi;

import org.core.common.vo.neteasecloudmusic.user.Account;
import org.core.common.vo.neteasecloudmusic.user.Profile;
import org.core.common.vo.neteasecloudmusic.user.UserVo;
import org.core.pojo.SysUserPojo;

public class BaseController {
    
    protected UserVo getUserVo(SysUserPojo account) {
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
