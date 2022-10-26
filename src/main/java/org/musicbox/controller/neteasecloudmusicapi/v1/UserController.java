package org.musicbox.controller.neteasecloudmusicapi.v1;

import cn.hutool.core.bean.BeanUtil;
import org.musicbox.common.result.NeteaseResult;
import org.musicbox.common.vo.user.UserVo;
import org.musicbox.compatibility.UserCompatibility;
import org.musicbox.controller.neteasecloudmusicapi.BaseController;
import org.musicbox.pojo.SysUserPojo;
import org.musicbox.utils.ThreadUtils;
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
public class UserController extends BaseController {
    
    @Autowired
    private UserCompatibility user;
    
    /**
     * 获取用户信息
     *
     * @return
     */
    @GetMapping("/user/account")
    public NeteaseResult getUser() {
        SysUserPojo userPojo = ThreadUtils.getUser();
        // 查找用户
        SysUserPojo account = user.getAccount(userPojo.getId());
        UserVo userVo = getUserVo(account);
        
        // 前端通用返回类
        NeteaseResult r = new NeteaseResult();
        r.putAll(BeanUtil.beanToMap(userVo));
        return r.success();
    }
    
}
