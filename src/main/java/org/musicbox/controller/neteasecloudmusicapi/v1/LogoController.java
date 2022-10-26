package org.musicbox.controller.neteasecloudmusicapi.v1;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson2.JSON;
import org.musicbox.common.result.NeteaseResult;
import org.musicbox.common.vo.user.UserVo;
import org.musicbox.compatibility.UserCompatibility;
import org.musicbox.controller.neteasecloudmusicapi.BaseController;
import org.musicbox.pojo.SysUserPojo;
import org.musicbox.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

/**
 * <p>
 * NeteaseCloudMusicApi 登录控制器
 * </p>
 *
 * @author Sakura
 * @since 2022-10-22
 */
@RestController
@RequestMapping("/")
public class LogoController extends BaseController {
    
    
    @Autowired
    private UserCompatibility user;
    
    /**
     * 登录接口
     *
     * @return 返回登录结果
     */
    @GetMapping("/login/cellphone")
    public NeteaseResult login(HttpServletResponse response, String phone, String password) {
        SysUserPojo userPojo = user.loginEfficacy(phone, password);
        UserVo userVo = getUserVo(userPojo);
        
        String userStr = JSON.toJSONString(userPojo);
        String sign = JwtUtil.sign(userPojo.getUsername(), userStr);
        
        Cookie cookie = new Cookie("Cookie", sign);
        response.addCookie(cookie);
        
        NeteaseResult r = new NeteaseResult();
        r.putAll(BeanUtil.beanToMap(userVo));
        r.put("token", sign);
        
        return r;
    }
    
    /**
     * 注册接口
     *
     * @param account
     * @param nickname
     * @param password
     * @return
     */
    @GetMapping("/register/account")
    public NeteaseResult addUser(String account, String nickname, String password) {
        SysUserPojo userPojo = new SysUserPojo();
        userPojo.setUsername(account);
        userPojo.setNickname(nickname);
        userPojo.setPassword(password);
        userPojo.setCreateTime(LocalDateTime.now());
        userPojo.setUpdateTime(LocalDateTime.now());
        user.createAccount(userPojo);
        return new NeteaseResult().success();
    }
}
