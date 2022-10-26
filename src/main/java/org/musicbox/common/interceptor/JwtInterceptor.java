package org.musicbox.common.interceptor;

import com.alibaba.fastjson2.JSON;
import org.musicbox.common.exception.ResultCode;
import org.musicbox.common.result.R;
import org.musicbox.pojo.SysUserPojo;
import org.musicbox.utils.JwtUtil;
import org.musicbox.utils.ThreadUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Optional;

public class JwtInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        
        // 从 http 请求头中取出 token
        String token = request.getHeader("token");
        System.out.println("token值：" + token);
        if (token == null) {
            Optional<Cookie> first = Arrays.stream(request.getCookies())
                                           .filter(cookie -> "Cookie".equals(cookie.getName()))
                                           .findFirst();
            token = first.map(Cookie::getValue).orElse(null);
        }
        
        if (token == null) {
            response.setHeader("content-type", "application/json; charset=utf-8");
            PrintWriter writer = response.getWriter();
            writer.println(R.error(ResultCode.TOKEN_INVALID.getResultCode(), ResultCode.TOKEN_INVALID.getResultMsg()));
            return false;
        }
        
        // 验证 token
        JwtUtil.checkSign(token);
        
        // 验证通过后， 这里测试取出JWT中存放的数据
        // 获取 token 中的 userId
        String userId = JwtUtil.getUserId(token);
        System.out.println("id : " + userId);
        
        // 获取 token 中的其他数据
        String info = JwtUtil.getInfo(token);
        if (info == null) {
            return false;
        }
        SysUserPojo userPojo = JSON.parseObject(info, SysUserPojo.class);
        // 设置当前线程值
        ThreadUtils.setUser(userPojo);
        return true;
    }
}

