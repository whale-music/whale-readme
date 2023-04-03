package org.oss.factory;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.oss.service.OSSService;
import org.oss.service.impl.alist.AListOSSServiceImpl;

class OSSFactoryTest {
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    
    @Test
    void testOssFactory() {
        OSSService result1 = OSSFactory.ossFactory("AList");
        OSSService result2 = OSSFactory.ossFactory("AList");
        
        Assertions.assertEquals(new AListOSSServiceImpl().getMode(), result1.getMode());
        Assertions.assertEquals(result1, result2);
    }
}

