package org.core.common.enums;

import org.core.oss.service.impl.alist.AListOSSServiceImpl;
import org.core.oss.service.impl.local.LocalOSSServiceImpl;

public enum SaveModeEnum {
    ALIST(AListOSSServiceImpl.SERVICE_NAME),
    LOCAL(LocalOSSServiceImpl.SERVICE_NAME);
    
    private final String serviceName;
    
    SaveModeEnum(String serviceName) {
        this.serviceName = serviceName;
    }
    
    @Override
    public String toString() {
        return serviceName;
    }
}
