package org.web.controller.admin.v1;

import lombok.extern.slf4j.Slf4j;
import org.api.admin.config.AdminConfig;
import org.api.admin.model.req.AudioInfoReq;
import org.api.admin.service.UploadMusicApi;
import org.core.common.result.R;
import org.core.pojo.MusicDetails;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Set;

@RestController(AdminConfig.ADMIN + "MusicFlowController")
@RequestMapping("/admin/music")
@Slf4j
@CrossOrigin
public class MusicFlowController {
    @Autowired
    private UploadMusicApi uploadMusic;
    
    /**
     * 上传临时文件
     *
     * @param uploadFile 临时文件
     * @return 返回音乐数据
     */
    @PostMapping("/get/file")
    public R uploadMusicFile(@RequestParam(value = "file", required = false) MultipartFile uploadFile, @RequestParam(value = "url", required = false) String url) throws CannotReadException, TagException, ReadOnlyFileException, IOException {
        return R.success(uploadMusic.uploadMusicFile(uploadFile, url));
    }
    
    /**
     * 获取临时上传音乐字节数据
     *
     * @param musicTempFile 临时文件
     * @return 字节数据
     */
    @GetMapping("/get/temp/{music}")
    public ResponseEntity<FileSystemResource> getMusicTempFile(@PathVariable("music") String musicTempFile) {
        return uploadMusic.getMusicTempFile(musicTempFile);
    }
    
    /**
     * 上传音乐信息，包括临时文件名
     *
     * @param dto 音乐信息
     * @return 返回成功信息
     */
    @PostMapping("/upload/info")
    public R uploadMusicInfo(@Validated @RequestBody AudioInfoReq dto) throws IOException {
        MusicDetails musicDetails = uploadMusic.saveMusicInfo(dto);
        return R.success(musicDetails);
    }
    
    /**
     * 获取音乐URL
     *
     * @param musicId 音乐id
     * @return url
     */
    @GetMapping("/get/{musicId}")
    public R getMusicUrl(@PathVariable("musicId") Set<String> musicId) {
        return R.success(uploadMusic.getMusicUrl(musicId));
    }
}
