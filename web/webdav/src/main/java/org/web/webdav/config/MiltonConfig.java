package org.web.webdav.config;

import cn.hutool.core.collection.CollUtil;
import io.milton.http.fs.NullSecurityManager;
import io.milton.servlet.DefaultMiltonConfigurator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.web.webdav.controller.WebDavController;

import java.util.ArrayList;

/**
 * 此类配置 milton, 配置httpManager.这个会在Filter中起作用
 */
@Component
public class MiltonConfig extends DefaultMiltonConfigurator {
    private final NullSecurityManager securityManager;
    
    @Autowired
    private WebDavController webDavController;
    
    public MiltonConfig() {
        this.securityManager = new NullSecurityManager();
    }
    
    @Override
    protected void build() {
        builder.setSecurityManager(securityManager);
        if (CollUtil.isEmpty(builder.getControllers())) {
            builder.setControllers(new ArrayList<>());
        }
        builder.getControllers().add(webDavController);
        super.build();
    }
}