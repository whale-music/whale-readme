package org.api.admin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.api.admin.config.AdminConfig;
import org.api.admin.model.req.UserReq;
import org.api.admin.model.res.RefreshTokenRes;
import org.api.admin.model.res.UserRes;
import org.core.common.exception.BaseException;
import org.core.common.result.ResultCode;
import org.core.config.JwtConfig;
import org.core.mybatis.pojo.SysUserPojo;
import org.core.service.AccountService;
import org.core.utils.RoleUtil;
import org.core.utils.token.TokenUtil;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;

@Slf4j
@Service(AdminConfig.ADMIN + "LoginApi")
@RequiredArgsConstructor
public class LoginApi {
    private final AccountService accountService;
    
    private final TokenUtil tokenUtil;
    
    public UserRes login(String phone, String password) {
        SysUserPojo userPojo = accountService.login(phone, password);
        Date date = new Date(System.currentTimeMillis() + JwtConfig.getExpireTime());
        String sign = tokenUtil.adminSignToken(date, userPojo.getUsername(), userPojo);
        
        Date refreshDate = new Date(System.currentTimeMillis() + JwtConfig.getRefreshExpireTime());
        String refreshToken = tokenUtil.adminRefreshSignToken(refreshDate, userPojo.getUsername(), userPojo);
        
        return new UserRes(userPojo.getId(), userPojo.getUsername(), sign, refreshToken, RoleUtil.getRoleNames(userPojo.getRoleName()), date.getTime());
    }
    
    public void createAccount(UserReq req) {
        accountService.createAccount(req);
    }
    
    public RefreshTokenRes refreshUserToken(String refresh) {
        tokenUtil.isJwtExpired(refresh);
        tokenUtil.checkSign(refresh);
        
        SysUserPojo userInfo = tokenUtil.getRefreshUserInfo(refresh);
        if (Objects.isNull(userInfo)) {
            throw new BaseException(ResultCode.TOKEN_EXPIRED_ERROR);
        }
        Date date = new Date(System.currentTimeMillis() + JwtConfig.getExpireTime());
        Date refreshDate = new Date(System.currentTimeMillis() + JwtConfig.getRefreshExpireTime());
        String token = tokenUtil.adminSignToken(date, userInfo.getUsername(), userInfo);
        String newRefresh = tokenUtil.adminRefreshSignToken(refreshDate, userInfo.getUsername(), userInfo);
        
        return new RefreshTokenRes(token, newRefresh, date.getTime());
    }
}
