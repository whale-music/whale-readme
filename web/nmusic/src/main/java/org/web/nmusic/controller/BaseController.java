package org.web.nmusic.controller;

import cn.hutool.http.Header;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.api.nmusic.model.vo.user.Account;
import org.api.nmusic.model.vo.user.Profile;
import org.api.nmusic.model.vo.user.UserVo;
import org.core.common.constant.CookieConstant;
import org.core.common.result.NeteaseResult;
import org.core.mybatis.model.convert.UserConvert;
import org.core.mybatis.pojo.SysUserPojo;
import org.core.utils.CookieUtil;
import org.core.utils.token.TokenUtil;
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
    
        profile.setCreateTime((long) account.getCreateTime().getNano());
    
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
        
        NeteaseResult r = new NeteaseResult();
        return r.success();
    }
    
    @NotNull
    protected NeteaseResult getNeteaseResult(HttpServletRequest request, HttpServletResponse response, TokenUtil tokenUtil, SysUserPojo userPojo) {
        String sign = tokenUtil.neteasecloudmusicSignToken(userPojo.getUsername(), userPojo);
        // 写入用户信息到cookie
        Cookie cookie = new Cookie(CookieConstant.COOKIE_NAME_MUSIC_U, sign);
        cookie.setPath("/");
        response.addCookie(cookie);
        
        Cookie[] cookies = {
                CookieUtil.createCookieString("MUSIC_R_T", "1465815410743", 2147483647, "Mon, 29 Oct 2091 15:36:04 GMT", "/weapi/feedback"),
                CookieUtil.createCookieString("MUSIC_A_T", "1465815403512", 2147483647, "Mon, 29 Oct 2091 15:36:04 GMT", "/eapi/feedback"),
                CookieUtil.createCookieString("MUSIC_SNS", "", 0, "Wed, 11 Oct 2099 12:21:57 GMT", "/"),
                CookieUtil.createCookieString("MUSIC_U", sign, 2147483647, "Mon, 08 Apr 2099 12:21:57 GMT", "/")
        };
        String s = CookieUtil.cookieToString(cookies, request);
        NeteaseResult r = new NeteaseResult();
        r.put("token", sign);
        r.put("cookie", s);
        return r;
    }
}
