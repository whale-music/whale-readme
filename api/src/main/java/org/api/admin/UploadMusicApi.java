package org.api.admin;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.api.admin.dto.AudioInfoDto;
import org.api.admin.vo.AudioInfoVo;
import org.core.common.exception.BaseException;
import org.core.common.result.ResultCode;
import org.core.config.MusicConfig;
import org.core.pojo.TbMusicPojo;
import org.core.pojo.TbMusicUrlPojo;
import org.core.service.TbMusicService;
import org.core.service.TbMusicUrlService;
import org.core.utils.LocalFileUtil;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.TagException;
import org.oss.service.impl.LocalOSSServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UploadMusicApi {
    
    @Autowired
    private TbMusicService musicService;
    
    @Autowired
    private TbMusicUrlService musicUrlService;
    
    @Autowired
    private MusicConfig config;
    
    @Autowired
    private LocalOSSServiceImpl localOSSService;
    
    String[] fileType = {
            "mp3",
            "ogg",
            "flac"
    };
    
    String pathTemp = FileUtil.getTmpDirPath() + "\\musicTemp\\";
    
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
        String musicFileName = UUID.fastUUID() + "." + split[1];
        String path = pathTemp + musicFileName;
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
        audioInfoVo.setMusicFileTemp(musicFileName);
        return audioInfoVo;
    }
    
    /**
     * 获取临时文件字节
     *
     * @param musicTempFile 临时文件名
     * @return 字节数据
     */
    public ResponseEntity<FileSystemResource> getMusicTempFile(String musicTempFile) {
        LocalFileUtil.checkFileNameLegal(musicTempFile);
        File file = LocalFileUtil.checkFilePath(pathTemp, musicTempFile);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate");
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName());
        return ResponseEntity.ok()
                             .headers(headers)
                             .contentLength(file.length())
                             .contentType(MediaType.parseMediaType(MediaType.APPLICATION_OCTET_STREAM_VALUE))
                             .body(new FileSystemResource(file));
    }
    
    
    /**
     * 保存音乐
     *
     * @param dto 音乐信息
     */
    public void saveMusicInfo(AudioInfoDto dto) throws IOException {
        // 无文件
        File file = LocalFileUtil.checkFilePath(pathTemp, dto.getMusicFileTemp());
        // 检测文件md5值是否一样一样的就
        String md5 = DigestUtils.md5DigestAsHex(FileUtil.getInputStream(file));
        TbMusicUrlPojo one = musicUrlService.getOne(Wrappers.<TbMusicUrlPojo>lambdaQuery()
                                                            .eq(TbMusicUrlPojo::getMd5, md5));
        // music Info
        TbMusicPojo entity = new TbMusicPojo();
        entity.setMusicName(dto.getMusicName());
        entity.setLyric(dto.getLyric());
        entity.setTimeLength(LocalTime.now());
        entity.setPic(dto.getPic());
        entity.setAliaName(dto.getAliaName());
        entity.setSort(musicService.count());
        
        // music URL
        TbMusicUrlPojo urlPojo = new TbMusicUrlPojo();
        urlPojo.setSize(FileUtil.size(file));
        urlPojo.setRate(dto.getRate());
        urlPojo.setQuality(dto.getQuality());
        urlPojo.setMd5(md5);
        urlPojo.setEncodeType(dto.getFileType());
        if (one == null) {
            // 没有数据新增, 音乐信息
            boolean save = musicService.save(entity);
            if (!save) {
                throw new BaseException(ResultCode.SAVE_Fail);
            }
            
            // 上传文件
            String uploadPath = localOSSService.upload(file.getPath());
            urlPojo.setMusicId(entity.getId());
            urlPojo.setUrl(uploadPath);
            musicUrlService.save(urlPojo);
        } else {
            // 已有数据，对原来的数据更新，数据会直接覆盖原有数据
            entity.setId(one.getMusicId());
            boolean save = musicService.updateById(entity);
            if (!save) {
                throw new BaseException(ResultCode.SAVE_Fail);
            }
            
            // 对music_url表更新
            musicUrlService.updateById(urlPojo);
        }
    }
    
    /**
     * 查询音乐URL表
     *
     * @param musicId 音乐id
     * @return 音乐URL列表
     */
    public List<TbMusicUrlPojo> getMusicUrl(String musicId) {
        List<TbMusicUrlPojo> list = musicUrlService.list(Wrappers.<TbMusicUrlPojo>lambdaQuery()
                                                                 .eq(TbMusicUrlPojo::getMusicId, musicId));
        if (list == null || list.isEmpty()) {
            throw new BaseException(ResultCode.SONG_NOT_EXIST);
        }
        return list.stream()
                   .peek(tbMusicUrlPojo -> tbMusicUrlPojo.setUrl(config.getHost() + tbMusicUrlPojo.getUrl()))
                   .collect(Collectors.toList());
    }
    
    public ResponseEntity<FileSystemResource> downloadMusicFile(String musicFilePath) {
        LocalFileUtil.checkFileNameLegal(musicFilePath);
        File file = LocalFileUtil.checkFilePath(config.getObjectSave(), musicFilePath);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate");
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + musicFilePath);
        return ResponseEntity.ok()
                             .headers(headers)
                             .contentLength(file.length())
                             .contentType(MediaType.parseMediaType(MediaType.APPLICATION_OCTET_STREAM_VALUE))
                             .body(new FileSystemResource(file));
    }
}
