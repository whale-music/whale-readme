package org.web.controller.admin.v1;

import lombok.extern.slf4j.Slf4j;
import org.api.admin.UploadMusicApi;
import org.api.admin.dto.AudioInfoDto;
import org.core.common.result.R;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController("AdminAddMusic")
@RequestMapping("/upload")
@Slf4j
@CrossOrigin
public class UploadMusicController {
    @Autowired
    private UploadMusicApi uploadMusic;
    
    
    @PostMapping("/file")
    public R uploadMusicFile(@RequestParam("file") MultipartFile uploadFile) throws CannotReadException, TagException, InvalidAudioFrameException, ReadOnlyFileException, IOException {
        return R.success(uploadMusic.uploadMusicFile(uploadFile));
    }
    
    /**
     * 获取临时上传音乐字节数据
     *
     * @param musicTempFile 临时文件
     * @return 字节数据
     */
    @GetMapping("/{music}")
    public ResponseEntity<FileSystemResource> getMusicFile(@PathVariable("music") String musicTempFile) {
        return uploadMusic.getMusicFile(musicTempFile);
    }
    
    /**
     * 上传音乐信息，包括临时文件名
     *
     * @param dto 音乐信息
     * @return 返回成功信息
     */
    @PostMapping("/music")
    public R uploadMusicInfo(@RequestBody AudioInfoDto dto) throws IOException {
        uploadMusic.saveMusicInfo(dto);
        return R.success();
    }
}
