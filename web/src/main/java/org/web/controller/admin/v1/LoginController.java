package org.web.controller.admin.v1;

import cn.hutool.http.Header;
import org.api.admin.model.dto.UserDto;
import org.api.admin.model.vo.UserVo;
import org.api.admin.service.UserApi;
import org.core.common.result.NeteaseResult;
import org.core.common.result.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.web.controller.BaseController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@RestController("adminLogin")
@RequestMapping("/admin/login")
public class LoginController extends BaseController {
    
    @Autowired
    private UserApi user;
    
    /**
     * 登录接口
     */
    @GetMapping("/account")
    public R login(HttpServletResponse response, String phone, String password) {
        UserVo userPojo = user.login(phone, password);
        // 写入用户信息到cookie
        Cookie cookie = new Cookie(Header.COOKIE.getValue(), userPojo.getToken());
        response.addCookie(cookie);
        return R.success(userPojo);
    }
    
    /**
     * 注册接口
     */
    @PostMapping("/register")
    public NeteaseResult addUser(@RequestBody UserDto req) {
        user.createAccount(req);
        return new NeteaseResult().success();
    }
    
    /**
     * 登出接口
     */
    @GetMapping("/logout")
    public NeteaseResult userLogout(HttpServletResponse response) {
        return super.logout(response);
    }
}
