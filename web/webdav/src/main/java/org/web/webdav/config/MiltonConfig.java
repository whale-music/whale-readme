package org.web.webdav.config;

import io.milton.http.fs.NullSecurityManager;
import io.milton.servlet.DefaultMiltonConfigurator;

/**
 * 此类配置 milton 并禁用 milton 身份验证。
 */
public class MiltonConfig extends DefaultMiltonConfigurator {
    private final NullSecurityManager securityManager;
    
    public MiltonConfig() {
        this.securityManager = new NullSecurityManager();
    }
    
    @Override
    protected void build() {
        builder.setSecurityManager(securityManager);
        super.build();
    }
}