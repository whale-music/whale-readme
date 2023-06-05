package org.web.controller;

import cn.hutool.http.Header;
import com.alibaba.fastjson2.JSON;
import org.api.neteasecloudmusic.model.vo.user.Account;
import org.api.neteasecloudmusic.model.vo.user.Profile;
import org.api.neteasecloudmusic.model.vo.user.UserVo;
import org.core.common.result.NeteaseResult;
import org.core.config.CookieConfig;
import org.core.model.convert.UserConvert;
import org.core.pojo.SysUserPojo;
import org.core.utils.JwtUtil;
import org.core.utils.UserUtil;
import org.jetbrains.annotations.NotNull;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

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
    
    protected NeteaseResult logout(HttpServletResponse response) {
        // 删除cookie
        Cookie cookie = new Cookie(Header.COOKIE.getValue(), null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        // 删除用户
        UserUtil.removeUser();
        NeteaseResult r = new NeteaseResult();
        r.success();
        return r;
    }
    
    @NotNull
    protected NeteaseResult getNeteaseResult(HttpServletResponse response, SysUserPojo userPojo) {
        String userStr = JSON.toJSONString(userPojo);
        String sign = JwtUtil.sign(userPojo.getUsername(), userStr);
        // 写入用户信息到cookie
        Cookie cookie1 = new Cookie(CookieConfig.COOKIE_NAME_COOKIE, sign);
        Cookie cookie2 = new Cookie(CookieConfig.COOKIE_NAME_MUSIC_U, sign);
        response.addCookie(cookie1);
        response.addCookie(cookie2);
    
        NeteaseResult r = new NeteaseResult();
        r.put("token", sign);
        return r;
    }
}
