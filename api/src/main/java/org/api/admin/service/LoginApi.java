package org.api.admin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.api.admin.config.AdminConfig;
import org.api.admin.model.req.UserReq;
import org.api.admin.model.res.RefreshTokenRes;
import org.api.admin.model.res.UserRes;
import org.core.common.exception.BaseException;
import org.core.common.result.ResultCode;
import org.core.model.UserLoginCacheModel;
import org.core.mybatis.model.convert.UserConvert;
import org.core.mybatis.pojo.SysUserPojo;
import org.core.service.AccountService;
import org.core.service.RemoteStorePicService;
import org.core.utils.RoleUtil;
import org.core.utils.UserUtil;
import org.core.utils.token.TokenUtil;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service(AdminConfig.ADMIN + "LoginApi")
@RequiredArgsConstructor
public class LoginApi {
    private final AccountService accountService;
    
    private final TokenUtil tokenUtil;
    
    private final RemoteStorePicService remoteStorePicService;
    
    public UserRes login(String phone, String password) {
        SysUserPojo userPojo = accountService.login(phone, password);
        // Date date = new Date(System.currentTimeMillis() + JwtConfig.getExpireTime());
        // String sign = tokenUtil.adminSignToken(date, userPojo.getUsername(), userPojo);
        //
        // Date refreshDate = new Date(System.currentTimeMillis() + JwtConfig.getRefreshExpireTime());
        // String refreshToken = tokenUtil.adminRefreshSignToken(refreshDate, userPojo.getUsername(), userPojo);
        
        UserLoginCacheModel userToken = tokenUtil.adminSignAndRefreshToken(userPojo);
        return new UserRes(userPojo.getId(),
                userPojo.getUsername(),
                userToken.getToken(),
                userToken.getRefreshToken(),
                RoleUtil.getRoleNames(userPojo.getRoleName()),
                userToken.getExpires().getTime());
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
        // Date date = new Date(System.currentTimeMillis() + JwtConfig.getExpireTime());
        // Date refreshDate = new Date(System.currentTimeMillis() + JwtConfig.getRefreshExpireTime());
        // String token = tokenUtil.adminSignToken(date, userInfo.getUsername(), userInfo);
        // String newRefresh = tokenUtil.adminRefreshSignToken(refreshDate, userInfo.getUsername(), userInfo);
        
        UserLoginCacheModel userToken = tokenUtil.adminSignAndRefreshToken(userInfo);
        
        return new RefreshTokenRes(userInfo.getId(),
                userInfo.getUsername(),
                userInfo.getRoleNamesSet(),
                userToken.getToken(),
                userToken.getRefreshToken(),
                userToken.getExpires().getTime());
    }
    
    public UserConvert getUserInfo() {
        SysUserPojo byId = accountService.getById(UserUtil.getUser().getId());
        if (Objects.isNull(byId)) {
            return new UserConvert();
        }
        byId.setPassword("");
        byId.setSubAccountPassword("");
        UserConvert res = new UserConvert(byId);
        res.setBackgroundPicUrl(remoteStorePicService.getUserBackgroundPicUrl(byId.getId()));
        res.setAvatarUrl(remoteStorePicService.getUserAvatarPicUrl(byId.getId()));
        return res;
    }
}
