package org.web.webdav.config;

import cn.hutool.core.collection.CollUtil;
import io.milton.servlet.DefaultMiltonConfigurator;
import org.springframework.stereotype.Component;
import org.web.webdav.controller.WebDavController;

import java.util.ArrayList;

/**
 * 此类配置 milton, 配置httpManager.这个会在Filter中起作用
 */
@Component
public class MiltonConfig extends DefaultMiltonConfigurator {
    private final WebdavSecurityManager webdavSecurityManager;
    
    private final WebDavController webDavController;
    
    public MiltonConfig(WebdavSecurityManager webdavSecurityManager, WebDavController webDavController) {
        this.webdavSecurityManager = webdavSecurityManager;
        this.webDavController = webDavController;
    }
    
    
    @Override
    protected void build() {
        builder.setSecurityManager(webdavSecurityManager);
        if (CollUtil.isEmpty(builder.getControllers())) {
            builder.setControllers(new ArrayList<>());
        }
        builder.getControllers().add(webDavController);
        super.build();
    }
}