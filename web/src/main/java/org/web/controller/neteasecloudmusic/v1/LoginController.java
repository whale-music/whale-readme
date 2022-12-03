package org.web.controller.neteasecloudmusic.v1;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.http.Header;
import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.api.neteasecloudmusic.UserApi;
import org.api.neteasecloudmusic.vo.user.UserVo;
import org.core.common.exception.BaseException;
import org.core.common.result.NeteaseResult;
import org.core.common.result.ResultCode;
import org.core.pojo.SysUserPojo;
import org.core.utils.JwtUtil;
import org.core.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.web.controller.BaseController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * NeteaseCloudMusicApi 登录控制器
 * </p>
 *
 * @author Sakura
 * @since 2022-10-22
 */
@RestController("NeteaseCloudLogin")
@RequestMapping("/")
@Slf4j
public class LoginController extends BaseController {
    
    @Autowired
    private UserApi user;
    
    /**
     * 登录接口
     *
     * @return 返回登录结果
     */
    @GetMapping("/login/cellphone")
    public NeteaseResult login(HttpServletResponse response, String phone, String password) {
        SysUserPojo userPojo = user.login(phone, password);
        UserVo userVo = getUserVo(userPojo);
        // 生成sign
        String userStr = JSON.toJSONString(userPojo);
        String sign = JwtUtil.sign(userPojo.getUsername(), userStr);
        // 写入用户信息到cookie
        Cookie cookie = new Cookie(Header.COOKIE.getValue(), sign);
        response.addCookie(cookie);
        
        NeteaseResult r = new NeteaseResult();
        r.putAll(BeanUtil.beanToMap(userVo));
        r.put("token", sign);
        
        return r;
    }
    
    /**
     * 注册接口
     *
     * @param account  账号
     * @param password 密码
     * @param nickname 昵称
     * @return 返回成功信息
     */
    @GetMapping("/register/account")
    public NeteaseResult addUser(String account, String password, String nickname) {
        SysUserPojo userPojo = new SysUserPojo();
        userPojo.setUsername(account);
        userPojo.setNickname(nickname);
        userPojo.setPassword(password);
        user.createAccount(userPojo);
        return new NeteaseResult().success();
    }
    
    /**
     * 登录刷新
     *
     * @param response servlet response
     * @return 返回token and Cookie
     */
    @GetMapping("/login/refresh")
    public NeteaseResult refresh(HttpServletResponse response) {
        SysUserPojo userPojo = UserUtil.getUser();
        if (userPojo == null) {
            log.warn(ResultCode.USER_NOT_EXIST.getResultMsg());
            throw new BaseException(ResultCode.USER_NOT_EXIST);
        }
        String userStr = JSON.toJSONString(userPojo);
        String sign = JwtUtil.sign(userPojo.getUsername(), userStr);
        // 写入用户信息到cookie
        Cookie cookie = new Cookie(Header.COOKIE.getValue(), sign);
        response.addCookie(cookie);
    
        NeteaseResult r = new NeteaseResult();
        r.put("token", sign);
        return r;
    }
    
    /**
     * 登出接口
     */
    @GetMapping("/logout")
    public NeteaseResult userLogout(HttpServletResponse response) {
        return super.logout(response);
    }
    
}
