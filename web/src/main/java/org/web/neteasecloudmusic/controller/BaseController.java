package org.web.neteasecloudmusic.controller;

import cn.hutool.http.Header;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.api.neteasecloudmusic.model.vo.user.Account;
import org.api.neteasecloudmusic.model.vo.user.Profile;
import org.api.neteasecloudmusic.model.vo.user.UserVo;
import org.core.common.constant.CookieConfig;
import org.core.common.result.NeteaseResult;
import org.core.mybatis.model.convert.UserConvert;
import org.core.mybatis.pojo.SysUserPojo;
import org.core.utils.TokenUtil;
import org.jetbrains.annotations.NotNull;

public class BaseController {
    
    protected UserVo getUserVo(UserConvert account) {
        // 包装返回结果
        UserVo userVo = new UserVo();
        userVo.setProfile(new Profile());
        Profile profile = userVo.getProfile();
        profile.setNickname(account.getNickname());
        
        profile.setUserId(account.getId());
        profile.setUserName(account.getUsername());
        profile.setShortUserName(account.getUsername());
        
        profile.setAvatarUrl(account.getAvatarUrl());
        profile.setBackgroundUrl(account.getBackgroundPicUrl());
        
        profile.setLastLoginIP(account.getLastLoginIp());
        profile.setLastLoginTime(account.getLastLoginTime());
    
        profile.setCreateTime(account.getCreateTime().getNano());
    
        userVo.setAccount(new Account());
        Account userVoAccount = userVo.getAccount();
        userVoAccount.setId(account.getId());
        userVoAccount.setUserName(account.getUsername());
        return userVo;
    }
    
    protected NeteaseResult logout(HttpServletResponse response, HttpSession session) {
        // 删除cookie
        Cookie cookie = new Cookie(Header.COOKIE.getValue(), null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        
        NeteaseResult r = new NeteaseResult();
        return r.success();
    }
    
    @NotNull
    protected NeteaseResult getNeteaseResult(HttpServletResponse response, SysUserPojo userPojo) {
        String sign = TokenUtil.sign(userPojo.getUsername(), userPojo);
        // 写入用户信息到cookie
        Cookie cookie = new Cookie(CookieConfig.COOKIE_NAME_MUSIC_U, sign);
        cookie.setPath("/");
        response.addCookie(cookie);
    
        NeteaseResult r = new NeteaseResult();
        r.put("token", sign);
        return r;
    }
}
