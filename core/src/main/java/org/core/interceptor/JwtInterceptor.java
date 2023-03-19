package org.core.interceptor;

import com.alibaba.fastjson2.JSON;
import com.auth0.jwt.exceptions.JWTVerificationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.core.common.result.R;
import org.core.common.result.ResultCode;
import org.core.config.CookieConfig;
import org.core.pojo.SysUserPojo;
import org.core.utils.JwtUtil;
import org.core.utils.UserUtil;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

@Slf4j
public class JwtInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.debug("请求路径: {}", request.getServletPath());
        log.debug("请求参数: {}", request.getQueryString());
        // 如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        // 放行路径
        String[] passPath = {
                "/music/info",
                "/login/refresh",
                "/logout",
                "/refreshToken",
                "/user/subcount",
                "/user/account",
                "/like",
                "/activate/init/profile",
                "/playlist/create",
                "/playlist/name/update",
                "/playlist/desc/update",
                "/playlist/tags/update",
                "/playlist/delete",
                "/playlist/subscribe",
                "/playlist/tracks",
                "/album/sublist",
                "/artist/sublist",
                "/user/playlist",
                "/likelist",
                "/scrobble",
                "/admin/playlist/getAsyncRoutes",
                "/login/status",
        };
        // 放行登录和注册,注销
        if (!Arrays.asList(passPath).contains(request.getRequestURI())) {
            return true;
        }
        log.debug(request.getPathInfo());
        // 从 http 请求头中取出 token
        String token = request.getHeader("token");
        log.debug("token值：{}", token);
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
        // 判断是否携带用户信息
        if (token == null) {
            response.setHeader("content-type", "application/json; charset=utf-8");
            response.getWriter().println(R.error(ResultCode.COOKIE_INVALID.getCode(), ResultCode.COOKIE_INVALID.getResultMsg()));
            return false;
        }
    
        // 验证 token
        try {
            JwtUtil.checkSign(token);
        } catch (JWTVerificationException e) {
            response.setHeader("content-type", "application/json; charset=utf-8");
            response.getWriter().println(R.error(ResultCode.COOKIE_INVALID.getCode(), ResultCode.COOKIE_INVALID.getResultMsg()));
            return false;
        }
        
        // 验证通过后， 这里测试取出JWT中存放的数据
        // 获取 token 中的 userId
        String userId = JwtUtil.getUserId(token);
        log.debug("id : {}", userId);
        
        // 获取 token 中的其他数据
        String info = JwtUtil.getInfo(token);
        if (info == null) {
            return false;
        }
        SysUserPojo userPojo = JSON.parseObject(info, SysUserPojo.class);
        // 设置当前线程值
        UserUtil.setUser(userPojo);
        return true;
    }
}

