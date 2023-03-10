package org.api.admin.service;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.api.admin.model.req.UserReq;
import org.api.admin.model.res.UserRes;
import org.core.config.JwtConfig;
import org.core.pojo.SysUserPojo;
import org.core.service.SysUserService;
import org.core.utils.JwtUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service("adminUser")
public class UserApi {
    @Autowired
    private JwtConfig jwtConfig;
    
    // 用户服务
    @Autowired
    private SysUserService userService;
    
    public void createAccount(UserReq req) {
        userService.createAccount(req);
    }
    
    public UserRes login(String phone, String password) {
        SysUserPojo userPojo = userService.login(phone, password);
        String sign = JwtUtil.sign(jwtConfig.getSeedKey(), jwtConfig.getExpireTime(), userPojo.getUsername(), JSON.toJSONString(userPojo));
        UserRes userRes = new UserRes();
        BeanUtils.copyProperties(userPojo, userRes);
        userRes.setToken(sign);
        return userRes;
    }
}
