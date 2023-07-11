package org.core.interceptor;

import com.alibaba.fastjson2.JSON;
import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.core.common.exception.BaseException;
import org.core.common.result.ResultCode;
import org.core.config.CookieConfig;
import org.core.mybatis.pojo.SysUserPojo;
import org.core.utils.JwtUtil;
import org.core.utils.UserUtil;
import org.springframework.lang.NonNull;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Arrays;
import java.util.Objects;

@Slf4j
public class JwtInterceptor implements HandlerInterceptor {
    private static String getToken(HttpServletRequest request, String token) {
        // 如果token的值是空的就从cookie里面取值
        if (token == null && request.getCookies() != null && !Arrays.asList(request.getCookies()).isEmpty()) {
            for (Cookie cookie : request.getCookies()) {
                if (StringUtils.equalsIgnoreCase(cookie.getName(), CookieConfig.COOKIE_NAME_COOKIE) || StringUtils.equals(cookie.getName(),
                        CookieConfig.COOKIE_NAME_MUSIC_U)) {
                    token = cookie.getValue();
                    break;
                }
            }
        }
        return token;
    }
    
    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        // 如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        
        log.debug("请求路径: {}", request.getServletPath());
        log.debug("请求参数: {}", request.getQueryString());
        log.debug("请求信息: {}", request.getPathInfo());
        // 放行路径
        String[] passPath = {
                "/login/cellphone",
                "/register/account",
                "/logout",
                "/admin/login/register",
                "/admin/login/account",
                "/admin/user/register",
                "/admin/user/login",
        };
        // 放行登录和注册,注销
        if (Arrays.asList(passPath).contains(request.getRequestURI())) {
            return true;
        }
    
        String getTempFile = "/admin/music/get/temp/";
        int length = getTempFile.length();
        if (length < request.getRequestURI().length()) {
            String substring = request.getRequestURI().substring(0, length);
            if (StringUtils.equals(getTempFile, substring)) {
                return true;
            }
        }
    
        // 从 http 请求头中取出 token
        String token = request.getHeader("token");
        log.debug("token值：{}", token);
        token = getToken(request, token);
        // 判断是否携带用户信息，没有就放行
        if (token == null) {
            return true;
        }
    
        // 验证 token
        try {
            JwtUtil.checkSign(token);
        } catch (JWTVerificationException e) {
            log.warn("Cookie失效");
            throw new BaseException(ResultCode.USER_NOT_LOGIN);
        }
    
        // 获取 token 中的其他数据
        String info = JwtUtil.getInfo(token);
        if (info == null) {
            throw new BaseException(ResultCode.USER_NOT_LOGIN);
        }
        SysUserPojo userPojo = JSON.parseObject(info, SysUserPojo.class);
    
        Object attribute = request.getSession().getAttribute(String.valueOf(userPojo.getId()));
        if (Objects.isNull(attribute)) {
            log.warn("session timeout: {}", userPojo.getUsername());
            throw new BaseException(ResultCode.USER_NOT_LOGIN);
        }
        log.debug("user name : {}", userPojo);
        // 设置当前线程值
        UserUtil.setUser(userPojo);
        return true;
    }
}

