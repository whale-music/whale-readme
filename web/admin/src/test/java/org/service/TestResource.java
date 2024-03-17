package org.service;

import lombok.extern.slf4j.Slf4j;
import org.core.mybatis.iservice.TbPicService;
import org.core.mybatis.iservice.TbResourceService;
import org.core.mybatis.pojo.TbPicPojo;
import org.core.mybatis.pojo.TbResourcePojo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.web.admin.AdminSpringBootApplication;

import java.util.List;

@SpringBootTest(classes = AdminSpringBootApplication.class)
@Slf4j
class TestResource {
    @Value("${spring.datasource.url}")
    private String url;
    
    @Autowired
    private TbResourceService tbResourceService;
    
    @Autowired
    private TbPicService tbPicService;
    
    @Test
    void testUpdateResource() {
        List<TbResourcePojo> list = tbResourceService.list();
        list.stream().forEach(tbResourcePojo -> {
            String path = tbResourcePojo.getPath();
            tbResourcePojo.setPath("/music/" + path);
        });
        tbResourceService.updateBatchById(list);
    }
    
    
    @Test
    void testUpdatePic() {
        List<TbPicPojo> list = tbPicService.list();
        list.stream().forEach(tbResourcePojo -> {
            String path = tbResourcePojo.getPath();
            tbResourcePojo.setPath("/img/" + path);
        });
        tbPicService.updateBatchById(list);
    }
}
