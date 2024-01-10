package org.oss.factory;

import org.core.common.properties.SaveConfig;
import org.core.oss.factory.OSSFactory;
import org.core.oss.service.OSSService;
import org.junit.jupiter.api.Test;

class OSSFactoryTest {
    
    @Test
    void testOssFactory() {
        SaveConfig config = new SaveConfig();
        config.setSaveMode("AList");
        OSSService result1 = OSSFactory.ossFactory(config);
        OSSService result2 = OSSFactory.ossFactory(config);
    
    }
}

