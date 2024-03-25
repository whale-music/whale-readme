package org.web.admin.controller.v1;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.api.admin.config.AdminConfig;
import org.api.admin.service.PicApi;
import org.core.common.annotation.AnonymousAccess;
import org.core.common.result.R;
import org.core.common.weblog.annotation.WebLog;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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
    @AnonymousAccess
    @WebLog
    @GetMapping("/get/temp/{file}")
    public ResponseEntity<FileSystemResource> getMusicTempFile(@PathVariable("file") String musicTempFile) {
        return picApi.getMusicTempFile(musicTempFile);
    }
    
    /**
     * 上传临时文件
     *
     * @param uploadFile 临时文件
     * @return 返回音乐数据
     */
    @AnonymousAccess
    @WebLog
    @PostMapping("/temp/upload")
    public R uploadPicFile(@RequestParam(value = "file", required = false) MultipartFile uploadFile, @RequestParam(value = "url", required = false) String url) throws IOException {
        return R.success(picApi.uploadPicFile(uploadFile, url));
    }
    
    
    @AnonymousAccess
    @WebLog
    @PostMapping("/upload")
    public R uploadPic(@RequestParam(value = "file", required = false) MultipartFile uploadFile, @RequestParam("id") Long id, @RequestParam("type") String type) throws IOException {
        String picUrl = picApi.uploadPic(uploadFile, id, type);
        return R.success(picUrl);
    }
}
