package org.oss.factory;

import cn.hutool.log.Log;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.oss.service.OSSService;
import org.oss.service.impl.alist.AListOSSServiceImpl;

import java.util.HashMap;

class OSSFactoryTest {
    @Mock
    Log log;
    @Mock
    OSSService OSS;
    @Mock
    HashMap<String, OSSService> map;
    @InjectMocks
    OSSFactory oSSFactory;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    
    @Test
    void testOssFactory() {
        OSSService result1 = OSSFactory.OssFactory("AList");
        OSSService result2 = OSSFactory.OssFactory("AList");
        
        Assertions.assertEquals(new AListOSSServiceImpl().getMode(), result1.getMode());
        Assertions.assertEquals(result1, result2);
    }
}

