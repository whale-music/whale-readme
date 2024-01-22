package org.core.common.enums;

import org.core.oss.service.impl.alist.AListOSSServiceImpl;

public enum SaveModeEnum {
    ALIST(AListOSSServiceImpl.SERVICE_NAME),
    LOCAL("Local");
    
    private final String serviceName;
    
    SaveModeEnum(String serviceName) {
        this.serviceName = serviceName;
    }
    
    @Override
    public String toString() {
        return serviceName;
    }
}
