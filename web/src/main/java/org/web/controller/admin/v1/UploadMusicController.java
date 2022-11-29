package org.web.controller.admin.v1;

import lombok.extern.slf4j.Slf4j;
import org.api.admin.AddMusicApi;
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
    private AddMusicApi addMusic;
    
    
    @PostMapping("/file")
    public R upload(@RequestParam("file") MultipartFile uploadFile) throws CannotReadException, TagException, InvalidAudioFrameException, ReadOnlyFileException, IOException {
        return R.success(addMusic.uploadMusicFile(uploadFile));
    }
    
    /**
     * 获取临时上传音乐字节数据
     *
     * @param musicTempFile 临时文件
     * @return 字节数据
     */
    @GetMapping("/{music}")
    public ResponseEntity<FileSystemResource> getMusicFile(@PathVariable("music") String musicTempFile) {
        return addMusic.getMusicFile(musicTempFile);
    }
}
