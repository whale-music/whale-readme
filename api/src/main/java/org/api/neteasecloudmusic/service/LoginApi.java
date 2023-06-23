package org.api.neteasecloudmusic.service;

import lombok.extern.slf4j.Slf4j;
import org.api.common.service.QukuAPI;
import org.api.neteasecloudmusic.config.NeteaseCloudConfig;
import org.api.neteasecloudmusic.model.vo.login.status.Account;
import org.api.neteasecloudmusic.model.vo.login.status.DataJson;
import org.api.neteasecloudmusic.model.vo.login.status.LoginStatusRes;
import org.api.neteasecloudmusic.model.vo.login.status.Profile;
import org.core.mybatis.pojo.SysUserPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service(NeteaseCloudConfig.NETEASECLOUD + "LoginApi")
public class LoginApi {
    @Autowired
    private UserApi user;
    
    @Autowired
    private QukuAPI qukuAPI;
    
    public LoginStatusRes status(Long uid) {
        LoginStatusRes res = new LoginStatusRes();
        SysUserPojo account = user.getAccount(uid);
        DataJson dataJson = new DataJson();
        dataJson.setCode(200);
        Profile profile = new Profile();
        profile.setUserId(account.getId());
        profile.setUserName(account.getUsername());
        profile.setNickname(account.getNickname());
        profile.setBackgroundUrl(account.getBackgroundUrl());
        profile.setAvatarUrl(qukuAPI.getPicUrl(account.getAvatarId()));
        profile.setCreateTime(account.getCreateTime().getNano());
        
        dataJson.setProfile(profile);
        Account account1 = new Account();
        account1.setId(account.getId());
        account1.setStatus(0);
        account1.setUserName(account.getUsername());
        account1.setCreateTime(account.getCreateTime().getNano());
        account1.setType(1);
        
        dataJson.setAccount(account1);
        res.setData(dataJson);
        return res;
    }
}
