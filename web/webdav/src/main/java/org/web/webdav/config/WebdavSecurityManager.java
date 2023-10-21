package org.web.webdav.config;

import io.milton.http.fs.SimpleSecurityManager;
import io.milton.http.http11.auth.DigestGenerator;
import io.milton.http.http11.auth.DigestResponse;
import org.apache.commons.lang3.StringUtils;
import org.core.common.constant.AccountTypeConstant;
import org.core.mybatis.pojo.SysUserPojo;
import org.core.service.AccountService;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class WebdavSecurityManager extends SimpleSecurityManager {
    public static final String REALM = "webdav";
    private final AccountService accountService;
    private final DigestGenerator digestGenerator;
    
    
    public WebdavSecurityManager(AccountService accountService) {
        this.accountService = accountService;
        this.digestGenerator = new DigestGenerator();
    }
    
    
    /**
     * @param host
     * @return
     */
    @Override
    public String getRealm(String host) {
        return REALM;
    }
    
    /**
     * @param user
     * @param password
     * @return
     */
    @Override
    public Object authenticate(String user, String password) {
        SysUserPojo userByName = accountService.getUserByName(user);
        if (Objects.nonNull(userByName) && StringUtils.equals(userByName.getPassword(), password)) {
            this.setRealm(AccountTypeConstant.getAccountType(userByName.getAccountType()));
            return user;
        } else {
            return null;
        }
    }
    
    /**
     * @param digestRequest
     * @return
     */
    @Override
    public Object authenticate(DigestResponse digestRequest) {
        String user = digestRequest.getUser();
        SysUserPojo userByName = accountService.getUserByName(user);
        if (Objects.isNull(userByName)) {
            return null;
        }
        String serverResponse = digestGenerator.generateDigest(digestRequest, userByName.getPassword());
        String clientResponse = digestRequest.getResponseDigest();
        
        if (serverResponse.equals(clientResponse)) {
            return user;
        } else {
            return null;
        }
    }
}
