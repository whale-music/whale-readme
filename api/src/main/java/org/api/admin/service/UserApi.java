package org.api.admin.service;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.api.admin.config.AdminConfig;
import org.api.admin.model.req.UserReq;
import org.api.admin.model.res.UserRes;
import org.core.pojo.SysUserPojo;
import org.core.service.AccountService;
import org.core.utils.JwtUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service(AdminConfig.ADMIN + "UserApi")
public class UserApi {
    
    // 用户服务
    @Autowired
    private AccountService accountService;
    
    public void createAccount(UserReq req) {
        accountService.createAccount(req);
    }
    
    public UserRes login(String phone, String password) {
        SysUserPojo userPojo = accountService.login(phone, password);
        String sign = JwtUtil.sign(userPojo.getUsername(), JSON.toJSONString(userPojo));
        UserRes userRes = new UserRes();
        BeanUtils.copyProperties(userPojo, userRes);
        userRes.setToken(sign);
        return userRes;
    }
    
    public SysUserPojo getUserInfo(Long id) {
        SysUserPojo byId = accountService.getById(id);
        byId.setPassword("");
        return byId;
    }
}
