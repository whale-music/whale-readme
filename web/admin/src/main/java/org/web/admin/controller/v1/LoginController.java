package org.web.admin.controller.v1;

import cn.hutool.http.Header;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.api.admin.config.AdminConfig;
import org.api.admin.model.req.UserReq;
import org.api.admin.model.res.UserRes;
import org.api.admin.service.UserApi;
import org.common.exception.BaseException;
import org.common.result.R;
import org.common.result.ResultCode;
import org.core.common.annotation.AnonymousAccess;
import org.core.common.constant.VerifyAuthIdentifierConstant;
import org.core.config.JwtConfig;
import org.core.mybatis.pojo.SysUserPojo;
import org.core.utils.RoleUtil;
import org.core.utils.TokenUtil;
import org.core.utils.UserUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;


@RestController(AdminConfig.ADMIN + "LoginController")
@RequestMapping("/admin/user")
@AllArgsConstructor
public class LoginController {
    
    private final UserApi user;
    
    /**
     * 登录接口
     */
    @AnonymousAccess
    @PostMapping("/login")
    public R login(@RequestBody UserReq dto, HttpServletResponse response) {
        UserRes userPojo = user.login(dto.getUsername(), dto.getPassword());
        userPojo.setExpiryTime(System.currentTimeMillis() + JwtConfig.getExpireTime());
        userPojo.setRoles(RoleUtil.getRoleNames(userPojo.getRoleName()));
        // 写入用户信息到Header
        response.addHeader(VerifyAuthIdentifierConstant.ADMIN_VERIFY_AUTH_IDENTIFIER, userPojo.getToken());
        return R.success(userPojo);
    }
    
    /**
     * 注册接口
     */
    @AnonymousAccess
    @PostMapping("/register")
    public R addUser(@RequestBody UserReq req) {
        user.createAccount(req);
        return R.success();
    }
    
    /**
     * 登出接口
     */
    @AnonymousAccess
    @GetMapping("/logout")
    public R userLogout(HttpServletResponse response, HttpSession session) {
        // 删除cookie
        Cookie cookie = new Cookie(Header.COOKIE.getValue(), null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    
        // 删除用户
        UserUtil.removeUser();
        return R.success();
    }
    
    @PostMapping("/refreshToken")
    public R refreshUserToken(HttpServletResponse response, String id) {
        SysUserPojo userPojo = UserUtil.getUser();
        if (StringUtils.equals(String.valueOf(userPojo.getId()), id)) {
            String sign = TokenUtil.sign(userPojo.getUsername(), userPojo);
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
