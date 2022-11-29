package org.api.admin;

import cn.hutool.core.io.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.core.common.exception.BaseException;
import org.core.common.result.ResultCode;
import org.core.common.vo.admin.AudioInfoVo;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.TagException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;

@Service
@Slf4j
public class AddMusicCompatibility {
    
    String[] fileType = {
            "mp3",
            "ogg",
            "flac"
    };
    
    public AudioInfoVo uploadMusicFile(MultipartFile uploadFile) throws IOException, CannotReadException, TagException, InvalidAudioFrameException, ReadOnlyFileException {
        String filename = uploadFile.getOriginalFilename();
        if (StringUtils.isBlank(filename)) {
            throw new BaseException(ResultCode.FILENAME_INVALID);
        }
        int indexOf = filename.lastIndexOf('.');
        String[] split = filename.split(String.valueOf(new char[]{'\\', filename.charAt(indexOf)}));
        if (split.length < 1) {
            throw new BaseException(ResultCode.FILENAME_INVALID);
        }
        // 检测文件类型是否有效
        if (!StringUtils.containsAny(split[1], fileType)) {
            throw new BaseException(ResultCode.FILENAME_INVALID);
        }
        String path = FileUtil.getTmpDirPath() + "\\musicTemp\\" + LocalDateTime.now().getNano() + "." + split[1];
        BufferedOutputStream outputStream = FileUtil.getOutputStream(path);
        outputStream.write(uploadFile.getBytes());
        outputStream.close();
        AudioFile read = AudioFileIO.read(new File(path));
        log.info(" ----- ----- ");
        log.info("标题:" + read.getTag().getFirst(FieldKey.TITLE));
        log.info("作者:" + read.getTag().getFirst(FieldKey.ARTIST));
        log.info("专辑:" + read.getTag().getFirst(FieldKey.ALBUM));
        log.info("比特率:" + read.getAudioHeader().getBitRate());
        log.info("时长:" + read.getAudioHeader().getTrackLength() + "s");
        log.info("大小:" + (read.getFile().length() / 1024F / 1024F) + "MB");
        log.info(" ----- ----- ");
        AudioInfoVo audioInfoVo = new AudioInfoVo();
        audioInfoVo.setMusicName(read.getTag().getFirst(FieldKey.TITLE));
        audioInfoVo.setSinger(Collections.singletonList(read.getTag().getFirst(FieldKey.ARTIST)));
        audioInfoVo.setAlbum(read.getTag().getFirst(FieldKey.ALBUM));
        audioInfoVo.setTimeLength(read.getAudioHeader().getTrackLength());
        audioInfoVo.setSize(read.getFile().length());
        return audioInfoVo;
    }
    
}
