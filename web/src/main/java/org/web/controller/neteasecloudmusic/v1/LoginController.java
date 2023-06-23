package org.web.controller.neteasecloudmusic.v1;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.http.Header;
import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Base64Util;
import org.api.neteasecloudmusic.config.NeteaseCloudConfig;
import org.api.neteasecloudmusic.model.vo.login.status.LoginStatusRes;
import org.api.neteasecloudmusic.model.vo.user.UserVo;
import org.api.neteasecloudmusic.service.LoginApi;
import org.api.neteasecloudmusic.service.UserApi;
import org.core.common.exception.BaseException;
import org.core.common.result.NeteaseResult;
import org.core.common.result.ResultCode;
import org.core.config.CookieConfig;
import org.core.mybatis.model.convert.UserConvert;
import org.core.mybatis.pojo.SysUserPojo;
import org.core.utils.GlobeDataUtil;
import org.core.utils.JwtUtil;
import org.core.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.web.controller.BaseController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

/**
 * <p>
 * NeteaseCloudMusicApi 登录控制器
 * </p>
 *
 * @author Sakura
 * @since 2022-10-22
 */
@RestController(NeteaseCloudConfig.NETEASECLOUD + "LoginController")
@RequestMapping("/")
@Slf4j
public class LoginController extends BaseController {
    
    @Autowired
    private UserApi user;
    
    @Autowired
    private LoginApi loginApi;
    
    
    /**
     * 登录接口
     *
     * @return 返回登录结果
     */
    @RequestMapping(value = "/login/cellphone", method = {RequestMethod.GET, RequestMethod.POST})
    public NeteaseResult login(HttpServletResponse response, String phone, String password) {
        UserConvert userPojo = user.login(phone, password);
        UserVo userVo = getUserVo(userPojo);
        // 生成sign
        NeteaseResult r = getNeteaseResult(response, userPojo);
        r.putAll(BeanUtil.beanToMap(userVo));
        return r.success();
    }
    
    /**
     * 登录接口
     *
     * @return 返回登录结果
     */
    @RequestMapping(value = "/cellphone/existence/check", method = {RequestMethod.GET, RequestMethod.POST})
    public NeteaseResult cellphoneCheck(@RequestParam("phone") Long phone, @RequestParam(value = "countrycode", required = false, defaultValue = "86") String countrycode) {
        SysUserPojo userPojo = user.checkPhone(phone, countrycode);
        
        boolean b = userPojo == null;
        userPojo = b ? new SysUserPojo() : userPojo;
        NeteaseResult r = new NeteaseResult();
        r.put("exist", b ? -1 : 1);
        r.put("nickname", userPojo.getNickname());
        r.put("hasPassword", b);
        r.put("avatarUrl", userPojo.getAvatarId());
        r.put("hasSnsBinded", !b);
        r.put("countryCode", "86");
        r.put("cellphone", userPojo.getId());
        return r.success();
    }
    
    @RequestMapping(value = "/login/qr/key", method = {RequestMethod.GET, RequestMethod.POST})
    public NeteaseResult qrKey() {
        UUID uuid = UUID.randomUUID();
        GlobeDataUtil.setData(uuid.toString(), uuid.toString());
        
        NeteaseResult r = new NeteaseResult();
        HashMap<String, Object> map = new HashMap<>();
        map.put("code", 200);
        map.put("unikey", uuid.toString());
        return r.success(map);
    }
    
    @GetMapping("/login/qr/create")
    public NeteaseResult qrCreate(HttpServletRequest request, @RequestParam("key") String key) {
        String data = GlobeDataUtil.getData(key);
        NeteaseResult r = new NeteaseResult();
        if (data == null) {
            r.put(CookieConfig.COOKIE_NAME_MUSIC_U, "");
            return r.error(ResultCode.QR_ERROR);
        }
        String localhost = request.getServerName() + ":" + request.getServerPort();
        String value = "/login-key/index.html?key=" + data;
        r.put("qrurl", localhost + value);
        r.put("qrimg", Base64Util.encode(value));
        return r;
    }
    
    @GetMapping("/login/sure")
    public NeteaseResult qrSure(@RequestParam("codekey") String codekey, String phone, String password) {
        String data = GlobeDataUtil.getData(codekey);
        NeteaseResult r = new NeteaseResult();
        if (data == null) {
            r.put(CookieConfig.COOKIE_NAME_MUSIC_U, "");
            return r.error("800", "二维码不存在或已过期");
        }
        SysUserPojo userPojo = user.login(phone, password);
        GlobeDataUtil.setData(codekey, JSON.toJSONString(userPojo));
        return r.success();
    }
    
    @GetMapping("/login/qr/check")
    public NeteaseResult qrCreate(HttpServletResponse response, @RequestParam("key") String key) {
        String data = GlobeDataUtil.getData(key);
        if (data == null) {
            NeteaseResult r = new NeteaseResult();
            r.put(CookieConfig.COOKIE_NAME_MUSIC_U, "");
            return r.error("800", "二维码不存在或已过期");
        }
        if (Objects.equals(key, data)) {
            NeteaseResult r = new NeteaseResult();
            r.put(CookieConfig.COOKIE_NAME_MUSIC_U, "");
            return r.error("801", "等待扫码");
        }
        SysUserPojo userPojo = JSON.parseObject(data, SysUserPojo.class);
        String sign = JwtUtil.sign(userPojo.getUsername(), data);
        GlobeDataUtil.remove(key);
    
        Cookie cookie = new Cookie(Header.COOKIE.getValue(), sign);
        response.addCookie(cookie);
    
        NeteaseResult r = new NeteaseResult();
        r.put("code", 803);
        r.put("message", "授权登陆成功");
        r.put(CookieConfig.COOKIE_NAME_COOKIE, CookieConfig.COOKIE_NAME_MUSIC_U + "=" + sign);
        return r;
    }
    
    @RequestMapping(value = "/login/status", method = {RequestMethod.GET, RequestMethod.POST})
    public NeteaseResult loginStatus(@RequestParam(value = "uid", required = false) Long uid) {
        uid = uid == null ? UserUtil.getUser().getId() : uid;
        LoginStatusRes res = loginApi.status(uid);
        NeteaseResult r = new NeteaseResult();
        r.putAll(BeanUtil.beanToMap(res));
        return r.success();
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
        return getNeteaseResult(response, userPojo);
    }
    
    
    /**
     * 登出接口
     */
    @GetMapping("/logout")
    public NeteaseResult userLogout(HttpServletResponse response) {
        return super.logout(response);
    }
    
}
