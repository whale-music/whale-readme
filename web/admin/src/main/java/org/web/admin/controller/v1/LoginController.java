package org.web.admin.controller.v1;

import cn.hutool.http.Header;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.api.admin.config.AdminConfig;
import org.api.admin.model.req.UserReq;
import org.api.admin.model.res.RefreshTokenRes;
import org.api.admin.model.res.UserRes;
import org.api.admin.service.LoginApi;
import org.core.common.annotation.AnonymousAccess;
import org.core.common.result.R;
import org.springframework.web.bind.annotation.*;


@RestController(AdminConfig.ADMIN + "LoginController")
@RequestMapping("/admin/user")
@AllArgsConstructor
public class LoginController {
    
    private final LoginApi loginApi;
    
    /**
     * 登录接口
     */
    @AnonymousAccess
    @PostMapping("/login")
    public R login(@RequestBody UserReq dto) {
        UserRes userPojo = loginApi.login(dto.getUsername(), dto.getPassword());
        return R.success(userPojo);
    }
    
    /**
     * 注册接口
     */
    @AnonymousAccess
    @PostMapping("/register")
    public R addUser(@RequestBody UserReq req) {
        loginApi.createAccount(req);
        return R.success();
    }
    
    /**
     * 登出接口
     */
    @AnonymousAccess
    @GetMapping("/logout")
    public R userLogout(HttpServletResponse response) {
        // 删除cookie
        Cookie cookie = new Cookie(Header.COOKIE.getValue(), null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        
        return R.success();
    }
    
    @AnonymousAccess
    @PostMapping("/refreshToken")
    public R refreshUserToken(@RequestBody RefreshTokenRes refresh) {
        RefreshTokenRes res = loginApi.refreshUserToken(refresh.getRefreshToken());
        return R.success(res);
    }
}
