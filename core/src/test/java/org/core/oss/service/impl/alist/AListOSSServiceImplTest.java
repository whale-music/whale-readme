package org.core.oss.service.impl.alist;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.core.TestApplication;
import org.core.common.properties.SaveConfig;
import org.core.oss.model.Resource;
import org.core.oss.service.OSSService;
import org.core.oss.service.impl.alist.enums.ResourceEnum;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.util.DigestUtils;

import java.io.File;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 测试该类时，需要创建AList服务，账号admin, 密码Lq9quPO
 */
@SpringBootTest(classes = TestApplication.class)
@TestPropertySource(properties = {
        "save-config.save-mode=Alist",
        "save-config.host=http://localhost:5244",
        "save-config.access-key=admin",
        "save-config.secret-key=LLq9quPO"
})
@Slf4j
class AListOSSServiceImplTest {
    
    @Autowired
    private OSSService ossService;
    
    @Autowired
    private SaveConfig config;
    
    
    @Test
    void getMode() {
        Assertions.assertEquals(AListOSSServiceImpl.SERVICE_NAME, ossService.getMode());
    }
    
    @Test
    void isConnected() {
        Assertions.assertDoesNotThrow(() -> ossService.isConnected());
    }
    
    @Test
    void testUpload() {
        String upload = upload();
        Assertions.assertTrue(StringUtils.isNotBlank(upload));
    }
    
    private String upload() {
        File touch = FileUtil.touch("./data/cache/test_music.txt");
        FileUtil.writeString("1231313", touch, CharsetUtil.CHARSET_UTF_8);
        String s = DigestUtils.md5DigestAsHex(FileUtil.readBytes(touch));
        return ossService.upload(List.of("/test/music"), config.getAssignObjectSave(), touch, s, ResourceEnum.MUSIC);
    }
    
    @Test
    void rename() {
        String upload = upload();
        String newName = "test_music1.txt";
        String rename = ossService.rename(upload, newName, ResourceEnum.MUSIC);
        Assertions.assertEquals(Path.of(upload).getParent() + "\\" + newName, rename);
    }
    
    @Test
    void getResourceList() {
        String upload = upload();
        Resource resource = ossService.getResource(upload, true, ResourceEnum.MUSIC);
        Assertions.assertNotNull(resource);
        Assertions.assertEquals(resource.getPath(), upload);
    }
    
    @Test
    void delete() {
        String upload = upload();
        ossService.delete(upload, ResourceEnum.MUSIC);
        Map<String, Resource> resourceList = ossService.getResourceList(Collections.singletonList(upload), true, ResourceEnum.MUSIC);
        Resource resource = resourceList.get(upload);
        Assertions.assertNull(resource);
    }
}