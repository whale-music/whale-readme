package org.musicbox.controller.neteasecloudmusicapi.v1;

import cn.hutool.core.bean.BeanUtil;
import org.musicbox.common.result.NeteaseResult;
import org.musicbox.common.vo.user.Account;
import org.musicbox.common.vo.user.UserVo;
import org.musicbox.compatibility.UserCompatibility;
import org.musicbox.pojo.SysUserPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * NeteaseCloudMusicApi 用户控制器
 * </p>
 *
 * @author Sakura
 * @since 2022-10-22
 */
@RestController
@RequestMapping("/")
public class UserController {
    
    @Autowired
    private UserCompatibility user;
    
    @GetMapping("/user/account")
    public NeteaseResult getUser() {
        SysUserPojo account = user.getAccount(2L);
        UserVo userVo = new UserVo();
        userVo.setAccount(new Account());
        userVo.getAccount().setId(account.getId());
        
        NeteaseResult r = new NeteaseResult();
        r.putAll(BeanUtil.beanToMap(userVo));
        return r.success();
    }
    
    @GetMapping("/register/account")
    public void addUser(String account, String nickname, String password) {
        SysUserPojo userPojo = new SysUserPojo();
        userPojo.setUsername(account);
        userPojo.setNickname(nickname);
        userPojo.setPassword(password);
        user.createAccount(userPojo);
    }
}
