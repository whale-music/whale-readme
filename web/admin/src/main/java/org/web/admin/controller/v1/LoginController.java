package org.web.admin.controller.v1;

import cn.hutool.http.Header;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.api.admin.config.AdminConfig;
import org.api.admin.model.req.LoginReq;
import org.api.admin.model.req.UserReq;
import org.api.admin.model.res.RefreshTokenRes;
import org.api.admin.model.res.UserRes;
import org.api.admin.service.LoginApi;
import org.core.common.annotation.AnonymousAccess;
import org.core.common.result.R;
import org.core.common.weblog.annotation.WebLog;
import org.core.common.weblog.constant.LogNameConstant;
import org.core.model.UserLoginCacheModel;
import org.core.mybatis.model.convert.UserConvert;
import org.core.utils.UserUtil;
import org.core.utils.token.UserCacheServiceUtil;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController(AdminConfig.ADMIN + "LoginController")
@RequestMapping("/admin/user")
@AllArgsConstructor
public class LoginController {
    
    private final LoginApi loginApi;
    
    private final UserCacheServiceUtil userCacheServiceUtil;
    
    /**
     * 登录接口
     */
    @AnonymousAccess
    @WebLog(LogNameConstant.ADMIN)
    @PostMapping("/login")
    public R login(@RequestBody LoginReq dto) {
        UserRes userPojo = loginApi.login(dto.getUsername(), dto.getPassword());
        return R.success(userPojo);
    }
    
    @WebLog(LogNameConstant.ADMIN)
    @GetMapping("/info")
    public R getUserInfo() {
        UserConvert userPojo = loginApi.getUserInfo();
        return R.success(userPojo);
    }
    
    /**
     * 注册接口
     */
    @AnonymousAccess
    @WebLog(LogNameConstant.ADMIN)
    @PostMapping("/register")
    public R addUser(@RequestBody UserReq req) {
        loginApi.createAccount(req);
        return R.success();
    }
    
    /**
     * 登出接口
     */
    @WebLog(LogNameConstant.ADMIN)
    @GetMapping("/logout")
    public R userLogout(HttpServletResponse response) {
        // 删除cookie
        Cookie cookie = new Cookie(Header.COOKIE.getValue(), null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        
        try {
            UserLoginCacheModel user = UserUtil.getUser();
            userCacheServiceUtil.clearUserCache(user.getTokenKey());
        } catch (Exception e) {
            log.error("remove user token fail: {}", e.getMessage());
        }
        
        return R.success();
    }
    
    @AnonymousAccess
    @WebLog(LogNameConstant.ADMIN)
    @PostMapping("/refreshToken")
    public R refreshUserToken(@RequestBody RefreshTokenRes refresh) {
        RefreshTokenRes res = loginApi.refreshUserToken(refresh.getRefreshToken());
        return R.success(res);
    }
}
