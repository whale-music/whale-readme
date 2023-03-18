package org.web.controller.admin.v1;

import cn.hutool.http.Header;
import com.alibaba.fastjson2.JSON;
import org.apache.commons.lang3.StringUtils;
import org.api.admin.config.AdminConfig;
import org.api.admin.model.req.UserReq;
import org.api.admin.model.res.UserRes;
import org.api.admin.service.UserApi;
import org.core.common.exception.BaseException;
import org.core.common.result.NeteaseResult;
import org.core.common.result.R;
import org.core.common.result.ResultCode;
import org.core.config.JwtConfig;
import org.core.pojo.SysUserPojo;
import org.core.utils.JwtUtil;
import org.core.utils.UserUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.web.controller.BaseController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@RestController(AdminConfig.ADMIN + "LoginController")
@RequestMapping("/admin/user")
public class LoginController extends BaseController {
    
    @Autowired
    private JwtConfig jwtConfig;
    
    @Autowired
    private UserApi user;
    
    /**
     * 登录接口
     */
    @PostMapping("/login")
    public R login(HttpServletResponse response, @RequestBody UserReq dto) {
        UserRes userPojo = user.login(dto.getUsername(), dto.getPassword());
        userPojo.setExpiryTime(System.currentTimeMillis() + jwtConfig.getExpireTime());
        // 写入用户信息到cookie
        Cookie cookie = new Cookie(Header.COOKIE.getValue(), userPojo.getToken());
        response.addCookie(cookie);
        return R.success(userPojo);
    }
    
    /**
     * 注册接口
     */
    @PostMapping("/register")
    public NeteaseResult addUser(@RequestBody UserReq req) {
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
    
    @PostMapping("/refreshToken")
    public R refreshUserToken(HttpServletResponse response, String id) {
        SysUserPojo userPojo = UserUtil.getUser();
        if (StringUtils.equals(String.valueOf(userPojo.getId()), id)) {
            String sign = JwtUtil.sign(jwtConfig.getSeedKey(), jwtConfig.getExpireTime(), userPojo.getUsername(), JSON.toJSONString(userPojo));
            UserRes userRes = new UserRes();
            BeanUtils.copyProperties(userPojo, userRes);
            userRes.setToken(sign);
            Cookie cookie = new Cookie(Header.COOKIE.getValue(), sign);
            response.addCookie(cookie);
            return R.success(userRes);
        }
        throw new BaseException(ResultCode.USER_NOT_EXIST);
    }
}
