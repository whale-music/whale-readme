package org.oss.factory;

import org.oss.service.OSSService;
import org.oss.service.impl.alist.AListOSSServiceImpl;
import org.oss.service.impl.local.LocalOSSServiceImpl;

import java.util.ArrayList;
import java.util.List;

public class OSSFactory {
    
    private static OSSService OSS = null;
    
    private OSSFactory() {}
    
    public static OSSService OssFactory(String serviceName) {
        List<OSSService> list = new ArrayList<>();
        list.add(new LocalOSSServiceImpl());
        list.add(new AListOSSServiceImpl());
    
        for (OSSService ossService : list) {
            if (OSS.isCurrentOSS(serviceName)) {
                OSS = ossService;
            }
        }
        
        return OSS;
    }
}
