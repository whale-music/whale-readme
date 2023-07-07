package org.web.admin.controller.v1;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.api.admin.config.AdminConfig;
import org.api.admin.service.PicApi;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController(AdminConfig.ADMIN + "PicController")
@RequestMapping("/admin/pic")
@Slf4j
@CrossOrigin
@AllArgsConstructor
public class PicController {
    
    private final PicApi picApi;
    
    
    /**
     * 获取临时上传音乐字节数据
     *
     * @param musicTempFile 临时文件
     * @return 字节数据
     */
    @GetMapping("/get/temp/{file}")
    public ResponseEntity<FileSystemResource> getMusicTempFile(@PathVariable("file") String musicTempFile) {
        return picApi.getMusicTempFile(musicTempFile);
    }
}
