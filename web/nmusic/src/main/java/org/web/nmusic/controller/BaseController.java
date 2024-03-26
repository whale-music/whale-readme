package org.web.nmusic.controller;

import cn.hutool.http.Header;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.api.nmusic.model.vo.user.Account;
import org.api.nmusic.model.vo.user.Profile;
import org.api.nmusic.model.vo.user.UserVo;
import org.core.common.constant.CookieConstant;
import org.core.common.result.NeteaseResult;
import org.core.mybatis.model.convert.UserConvert;
import org.core.mybatis.pojo.SysUserPojo;
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
    protected NeteaseResult getNeteaseResult(HttpServletResponse response, TokenUtil tokenUtil, SysUserPojo userPojo) {
        String sign = tokenUtil.neteasecloudmusicSignToken(userPojo.getUsername(), userPojo);
        // 写入用户信息到cookie
        Cookie cookie = new Cookie(CookieConstant.COOKIE_NAME_MUSIC_U, sign);
        cookie.setPath("/");
        response.addCookie(cookie);
        
        Cookie musicRT = getCookie("MUSIC_R_T", "1465815410743", 2147483647, "Mon, 29 Oct 2091 15:36:04 GMT", "/weapi/feedback");
        Cookie musicAT = getCookie("MUSIC_A_T", "1465815403512", 2147483647, "Mon, 29 Oct 2091 15:36:04 GMT", "/eapi/feedback");
        Cookie musicSns = getCookie("MUSIC_SNS", "", 0, "Wed, 11 Oct 2099 12:21:57 GMT", "/");
        Cookie musicU = getCookie("MUSIC_U", sign, 2147483647, "Mon, 08 Apr 2099 12:21:57 GMT", "/");
        String sb = serializeCookieToString(musicRT)
                + serializeCookieToString(musicAT)
                + serializeCookieToString(musicSns)
                + serializeCookieToString(musicU);
        
        NeteaseResult r = new NeteaseResult();
        r.put("token", sign);
        r.put("cookie", sb);
        return r;
    }
    
    private Cookie getCookie(String name, String value, int maxAge, String expires, String path) {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(maxAge);
        cookie.setPath(path);
        cookie.setHttpOnly(true);
        // 设置Expires属性
        cookie.setAttribute("Expires", expires);
        return cookie;
    }
    
    public String serializeCookieToString(Cookie cookie) {
        StringBuilder cookieString = new StringBuilder();
        // 添加Cookie名称和值
        cookieString.append(cookie.getName()).append("=").append(cookie.getValue());
        // 添加Max-Age属性
        int maxAge = cookie.getMaxAge();
        if (maxAge >= 0) {
            cookieString.append("; Max-Age=").append(maxAge);
        }
        // 添加Expires属性
        long expires = cookie.getMaxAge();
        if (expires > 0) {
            cookieString.append("; Expires=").append(cookie.getMaxAge());
        }
        // 添加Path属性
        String path = cookie.getPath();
        if (path != null) {
            cookieString.append("; Path=").append(path);
        }
        // 添加Domain属性
        String domain = cookie.getDomain();
        if (domain != null) {
            cookieString.append("; Domain=").append(domain);
        }
        // 添加Secure属性
        if (cookie.getSecure()) {
            cookieString.append("; Secure");
        }
        // 添加HttpOnly属性
        if (cookie.isHttpOnly()) {
            cookieString.append("; HttpOnly");
        }
        return cookieString.toString();
    }
}
