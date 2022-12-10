package org.api.admin.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.IterUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpUtil;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.api.admin.model.dto.AudioInfoDto;
import org.api.admin.model.dto.SingerDto;
import org.api.admin.model.vo.AudioInfoVo;
import org.core.common.exception.BaseException;
import org.core.common.result.ResultCode;
import org.core.config.MusicConfig;
import org.core.pojo.*;
import org.core.service.*;
import org.core.utils.ExceptionUtil;
import org.core.utils.LocalFileUtil;
import org.core.utils.UserUtil;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.TagException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.oss.service.impl.LocalOSSServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UploadMusicApi {
    
    List<String> fileType = Arrays.asList("mp3", "ogg", "flac");
    /**
     * 音乐信息服务
     */
    @Autowired
    private TbMusicService musicService;
    /**
     * 音乐保存数据链接表
     */
    @Autowired
    private TbMusicUrlService musicUrlService;
    /**
     * 歌手服务
     */
    @Autowired
    private TbSingerService singerService;
    /**
     * 音乐和歌曲家中间表
     */
    @Autowired
    private TbMusicSingerService musicSingerService;
    /**
     * 专辑表
     */
    @Autowired
    private TbAlbumService albumService;
    /**
     * 上传配置
     */
    @Autowired
    private MusicConfig config;
    /**
     * 文件上传服务
     */
    @Autowired
    private LocalOSSServiceImpl localOSSService;
    
    @Autowired
    private TbAlbumSingerService albumSingerService;
    
    String pathTemp = FileUtil.getTmpDirPath() + "/musicTemp";
    
    /**
     * 上传文件或音乐URL下载到临时目录
     *
     * @param uploadFile 临时文件
     * @param url        上传音乐地址
     * @return 音乐信息
     */
    public AudioInfoVo uploadMusicFile(MultipartFile uploadFile, String url) throws IOException, CannotReadException, TagException, ReadOnlyFileException {
        File path;
        String fileSuffix;
        AudioInfoVo audioInfoVo = new AudioInfoVo();
        if (StringUtils.isBlank(url)) {
            String md5 = DigestUtils.md5DigestAsHex(uploadFile.getBytes());
            // 上传文件
            String filename = uploadFile.getOriginalFilename();
            if (StringUtils.isBlank(filename)) {
                throw new BaseException(ResultCode.FILENAME_INVALID);
            }
            fileSuffix = LocalFileUtil.getFileSuffix(filename, fileType);
            path = checkFileMd5(md5, new File(pathTemp, md5 + "." + fileSuffix));
            // 本地没有则保存
            if (path == null) {
                String musicFileName = md5 + "." + fileSuffix;
                path = new File(pathTemp, musicFileName);
                BufferedOutputStream outputStream = FileUtil.getOutputStream(path);
                outputStream.write(uploadFile.getBytes());
                outputStream.close();
                audioInfoVo.setIsExist(false);
            }
            audioInfoVo.setIsExist(true);
        } else {
            // 下载文件
            fileSuffix = LocalFileUtil.getFileSuffix(url, fileType);
            byte[] bytes = HttpUtil.downloadBytes(url);
            String md5 = DigestUtils.md5DigestAsHex(bytes);
            File dest = new File(pathTemp, md5 + "." + fileSuffix);
            path = checkFileMd5(md5, dest);
            // 本地没有则保存
            if (path == null) {
                path = FileUtil.writeBytes(bytes, dest);
                audioInfoVo.setIsExist(false);
            }
            audioInfoVo.setIsExist(true);
        }
        AudioFile read;
        try {
            read = AudioFileIO.read(path);
        } catch (InvalidAudioFrameException e) {
            log.warn("该音频文件没有包含音乐信息!!");
            audioInfoVo.setType(fileSuffix);
            audioInfoVo.setSize(path.length());
            audioInfoVo.setMusicFileTemp(path.getName());
            return audioInfoVo;
        }
        log.info(" ----- ----- ");
        log.info("标题:" + read.getTag().getFirst(FieldKey.TITLE));
        log.info("作者:" + read.getTag().getFirst(FieldKey.ARTIST));
        log.info("专辑:" + read.getTag().getFirst(FieldKey.ALBUM));
        log.info("比特率:" + read.getAudioHeader().getBitRate());
        log.info("时长:" + read.getAudioHeader().getTrackLength() + "s");
        log.info("大小:" + (read.getFile().length() / 1024F / 1024F) + "MB");
        log.info(" ----- ----- ");
        audioInfoVo.setMusicName(read.getTag().getFirst(FieldKey.TITLE));
        audioInfoVo.setOriginFileName(uploadFile == null || uploadFile.getOriginalFilename() == null ? "" : uploadFile.getOriginalFilename());
        audioInfoVo.setSinger(Collections.singletonList(read.getTag().getFirst(FieldKey.ARTIST)));
        audioInfoVo.setAlbum(read.getTag().getFirst(FieldKey.ALBUM));
        audioInfoVo.setTimeLength(read.getAudioHeader().getTrackLength());
        audioInfoVo.setSize(read.getFile().length());
        audioInfoVo.setMusicFileTemp(path.getName());
        return audioInfoVo;
    }
    
    private File checkFileMd5(String md5, File file) {
        if (FileUtil.isFile(file)) {
            return file;
        }
        long count = musicUrlService.count(Wrappers.<TbMusicUrlPojo>lambdaQuery().eq(TbMusicUrlPojo::getMd5, md5));
        if (count > 0) {
            throw new BaseException(ResultCode.SONG_EXIST);
        }
        return null;
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
     * 更新表: 音乐信息表  歌手表 专辑表
     * 如果上传文件更新: 音乐地址表
     *
     * @param dto 音乐信息
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveMusicInfo(AudioInfoDto dto) throws IOException {
        checkTempFile(dto);
        // 保存歌手和音乐中间表
        List<Long> singIds = saveAndReturnMusicAndSingList(dto);
        // 保存专辑表，如果没有则新建。
        TbAlbumPojo albumPojo = saveAndReturnAlbumPojo(dto, singIds);
        // 保存音乐表
        TbMusicPojo musicPojo = saveAndReturnMusicPojo(dto, singIds, albumPojo);
        // 保存音乐和歌手中间表
        saveMusicAndSinger(singIds, musicPojo);
        // 上传文件
        uploadFile(dto, musicPojo);
    }
    
    private void saveMusicAndSinger(List<Long> singIds, TbMusicPojo musicPojo) {
        if (IterUtil.isNotEmpty(singIds)) {
            List<TbMusicSingerPojo> entityList = new ArrayList<>();
            for (Long singId : singIds) {
                TbMusicSingerPojo entity = new TbMusicSingerPojo();
                entity.setMusicId(musicPojo.getId());
                entity.setSingerId(singId);
                entityList.add(entity);
            }
            musicSingerService.saveOrUpdateBatch(entityList);
        }
    }
    
    /**
     * 上传文件
     *
     * @param dto       前端请求数据
     * @param musicPojo 音乐表数据
     * @throws IOException 文件读取异常
     */
    private void uploadFile(AudioInfoDto dto, TbMusicPojo musicPojo) throws IOException {
        if (StringUtils.isNotBlank(dto.getMusicFileTemp())) {
            File file = new File(pathTemp, dto.getMusicFileTemp());
            String uploadPath = localOSSService.upload(file.getPath());
            Files.delete(file.toPath());
            // music URL 地址表
            TbMusicUrlPojo urlPojo = new TbMusicUrlPojo();
            urlPojo.setId(IdWorker.getId());
            urlPojo.setSize(FileUtil.size(file));
            urlPojo.setRate(dto.getRate());
            urlPojo.setQuality(dto.getQuality());
            urlPojo.setMd5(dto.getMd5());
            urlPojo.setEncodeType(FileUtil.extName(file));
            urlPojo.setMusicId(musicPojo.getId());
            urlPojo.setUrl(uploadPath);
            urlPojo.setUserId(UserUtil.getUser().getId());
            musicUrlService.save(urlPojo);
        }
    }
    
    /**
     * 获取音乐数据
     *
     * @param dto       前端请求数据
     * @param singerIds 音乐ID
     * @param albumPojo 专辑数据
     * @return 音乐信息
     */
    @NotNull
    private TbMusicPojo saveAndReturnMusicPojo(AudioInfoDto dto, List<Long> singerIds, TbAlbumPojo albumPojo) {
        String aliaNames = CollUtil.join(dto.getAliaName(), ",");
        // 如果有ID则直接更新
        if (dto.getId() != null) {
            TbMusicPojo musicPojo = musicService.getById(dto.getId());
            return saveMusicInfoTable(dto, albumPojo, musicPojo, aliaNames);
        }
        // 如果数据库中该歌曲有关联专辑信息则更新，并且音乐名相同则更新
        if (dto.getId() == null && albumPojo.getId() != null) {
            TbMusicPojo musicPojo = musicService.getOne(Wrappers.<TbMusicPojo>lambdaQuery()
                                                                .eq(TbMusicPojo::getAliaName, albumPojo.getId())
                                                                .eq(TbMusicPojo::getMusicName, dto.getMusicName()));
            return saveMusicInfoTable(dto, albumPojo, musicPojo, aliaNames);
        }
        
        List<TbAlbumSingerPojo> albumSingerPojoList = albumSingerService.list(Wrappers.<TbAlbumSingerPojo>lambdaQuery()
                                                                                      .in(TbAlbumSingerPojo::getSingerId, singerIds));
        List<Long> albumIds = albumSingerPojoList.stream()
                                                 .map(TbAlbumSingerPojo::getAlbumId)
                                                 .collect(Collectors.toList());
        
        List<TbMusicPojo> list = musicService.list(Wrappers.<TbMusicPojo>lambdaQuery()
                                                           .in(TbMusicPojo::getAlbumId, albumIds));
        Optional<TbMusicPojo> first = list.stream()
                                          .filter(tbMusicPojo -> StringUtils.equalsIgnoreCase(tbMusicPojo.getMusicName(), dto.getMusicName()))
                                          .findFirst();
        // 如果数据库中该歌曲有关联歌手则更新
        if (dto.getId() == null && first.isPresent()) {
            TbMusicPojo musicPojo = first.get();
            return saveMusicInfoTable(dto, albumPojo, musicPojo, aliaNames);
        }
        return saveMusicInfoTable(dto, albumPojo, new TbMusicPojo(), aliaNames);
    }
    
    @NotNull
    private TbMusicPojo saveMusicInfoTable(AudioInfoDto dto, TbAlbumPojo albumPojo, TbMusicPojo musicPojo, String aliaNames) {
        TbMusicPojo tbMusicPojo = musicPojo == null ? new TbMusicPojo() : musicPojo;
        // music 信息表
        tbMusicPojo.setMusicName(dto.getMusicName());
        tbMusicPojo.setAliaName(aliaNames);
        tbMusicPojo.setPic(dto.getPic());
        tbMusicPojo.setLyric(dto.getLyric());
        tbMusicPojo.setAlbumId(albumPojo.getId());
        tbMusicPojo.setSort(musicService.count());
        tbMusicPojo.setTimeLength(dto.getTimeLength());
        // 保存音乐表
        boolean save = musicService.saveOrUpdate(tbMusicPojo);
        // 保存错误，抛出异常
        ExceptionUtil.isNull(!save, ResultCode.SAVE_FAIL);
        return tbMusicPojo;
    }
    
    /**
     * 查询表中专辑信息，没有则新建
     *
     * @param dto                 前端请求
     * @param singerIds 音乐和歌手列表
     * @return 专辑表
     */
    @Nullable
    private TbAlbumPojo saveAndReturnAlbumPojo(AudioInfoDto dto, List<Long> singerIds) {
        // 专辑没有值直接返回
        if (dto.getAlbum() == null || StringUtils.isBlank(dto.getAlbum().getAlbumName())) {
            return new TbAlbumPojo();
        }
        // 如果是数据库中已有数据，直接更新
        Long albumId = dto.getAlbum().getId();
        if (albumId != null) {
            TbAlbumPojo byId = albumService.getById(albumId);
            ExceptionUtil.isNull(byId == null, ResultCode.ALBUM_NOT_EXIST);
            return byId;
        }
        // 查询该歌曲在数据中是否存在专辑
        List<TbAlbumPojo> list = albumService.list(Wrappers.<TbAlbumPojo>lambdaQuery()
                                                           .eq(TbAlbumPojo::getAlbumName, dto.getAlbum()
                                                                                             .getAlbumName()));
        // 获取所有专辑ID
        List<Long> albumList = list.stream().map(TbAlbumPojo::getId).collect(Collectors.toList());
        List<Long> albumIds = null;
        if (IterUtil.isNotEmpty(albumList) && IterUtil.isNotEmpty(singerIds)) {
            // 专辑表找到后，在中间表同时满足专辑ID和歌手ID两个列表，只找到同一个专辑ID
            List<TbAlbumSingerPojo> tbAlbumSingerPojoList = albumSingerService.list(Wrappers.<TbAlbumSingerPojo>lambdaQuery()
                                                                                            .in(TbAlbumSingerPojo::getSingerId, singerIds)
                                                                                            .in(TbAlbumSingerPojo::getAlbumId, albumList));
            albumIds = tbAlbumSingerPojoList.stream().map(TbAlbumSingerPojo::getAlbumId).collect(Collectors.toList());
        }
        List<Long> distinct = CollUtil.distinct(albumIds);
        // 数据库中有数据
        // 有两个以上数据，表示数据库中有专辑名，歌手，歌曲，相同的数据。直接抛出异常。让用户手动修改后添加
        ExceptionUtil.isNull(distinct.size() > 1, ResultCode.ALBUM_NOT_EXIST);
        TbAlbumPojo albumPojo = null;
        // 有一个更新数据库，并且保证数据中的专辑ID和根据专辑名查询出来的ID相同，否则不更新
        if (distinct.size() == 1) {
            Optional<TbAlbumPojo> first = list.stream()
                                              .filter(tbAlbumPojo -> Objects.equals(tbAlbumPojo.getId(), distinct.get(0)))
                                              .findFirst();
            ExceptionUtil.isNull(!first.isPresent(), ResultCode.ALBUM_ERROR);
            BeanUtils.copyProperties(dto.getAlbum(), first.get());
            albumService.updateById(first.get());
        }
        // 如果没有数据则新增专辑表
        // 默认新增歌手为歌曲歌手
        if (distinct.isEmpty()) {
            albumPojo = new TbAlbumPojo();
            BeanUtils.copyProperties(dto.getAlbum(), albumPojo);
            albumService.saveOrUpdate(albumPojo);
            // 更新歌手和专辑中间表
            if (IterUtil.isNotEmpty(singerIds)) {
                List<TbAlbumSingerPojo> albumSingerPojoArrayList = new ArrayList<>();
                for (Long id : singerIds) {
                    TbAlbumSingerPojo e = new TbAlbumSingerPojo();
                    e.setAlbumId(albumPojo.getId());
                    e.setSingerId(id);
                    albumSingerPojoArrayList.add(e);
                }
                albumSingerService.saveBatch(albumSingerPojoArrayList);
            }
        }
        return albumPojo;
    }
    
    /**
     * 返回添加歌手列表
     * 四种情况:
     * 没有歌手，直接返回空数据
     * 有歌手，数据库中有，查询出歌手和音乐主键ID
     * 有歌手，数据库中没有，添加歌曲
     * 数据库中，一半有歌手，一半没有歌手
     *
     * @param dto 前端请求数据
     * @return 返回音乐和歌手ID
     */
    private List<Long> saveAndReturnMusicAndSingList(AudioInfoDto dto) {
        // 没有歌手直接返回
        if (IterUtil.isEmpty(dto.getSinger()) || StringUtils.isBlank(dto.getSinger().get(0).getSingerName())) {
            return new ArrayList<>();
        }
        List<SingerDto> singerDtoList = dto.getSinger();
        List<TbSingerPojo> saveBatch = new ArrayList<>();
        // 有歌手，数据库中有，查询出歌手和音乐主键ID
        // 获取前端传入所有歌手
        List<String> singerNameList = dto.getSinger()
                                         .stream()
                                         .map(TbSingerPojo::getSingerName)
                                         .collect(Collectors.toList());
    
        // 获取所有歌手数据，取前端参数和数据库数据差集
        List<TbSingerPojo> singList = singerService.list(Wrappers.<TbSingerPojo>lambdaQuery()
                                                                 .in(TbSingerPojo::getSingerName, singerNameList));
    
        for (TbSingerPojo tbSingerPojo : singList) {
            for (SingerDto singerDto : singerDtoList) {
                TbSingerPojo pojo = new TbSingerPojo();
                BeanUtils.copyProperties(singerDto, pojo);
                if (StringUtils.equalsIgnoreCase(singerDto.getSingerName(), tbSingerPojo.getSingerName())) {
                    // 歌曲家名字相同，就把数据库中的ID拷贝到前端传入的数据中，直接更新数据库
                    pojo.setId(tbSingerPojo.getId());
                }
                saveBatch.add(pojo);
            }
        }
        singerService.saveOrUpdateBatch(saveBatch);
        return saveBatch.stream().map(TbSingerPojo::getId).collect(Collectors.toList());
    }
    
    private void checkTempFile(AudioInfoDto dto) throws IOException {
        // 上传文件则检测是否合法
        if (StringUtils.isNotBlank(dto.getMusicFileTemp()) && StringUtils.isBlank(dto.getMd5())) {
            // 检查文件目录是否合法
            File file = LocalFileUtil.checkFilePath(pathTemp, dto.getMusicFileTemp());
            // 检测文件md5值是否一样，一样则不上传
            dto.setMd5(DigestUtils.md5DigestAsHex(FileUtil.getInputStream(file)));
        }
        long count = musicUrlService.count(Wrappers.<TbMusicUrlPojo>lambdaQuery()
                                                   .eq(TbMusicUrlPojo::getMd5, dto.getMd5()));
        // 如果大于0则表示数据库中已经有该数据了
        ExceptionUtil.isNull(count > 0, ResultCode.SONG_EXIST);
    }
    
    /**
     * 查询音乐URL表
     *
     * @param musicId 音乐id
     * @return 音乐URL列表
     */
    public List<TbMusicUrlPojo> getMusicUrl(String musicId) {
        List<TbMusicUrlPojo> list = musicUrlService.list(Wrappers.<TbMusicUrlPojo>lambdaQuery()
                                                                 .eq(TbMusicUrlPojo::getId, musicId));
        if (list == null || list.isEmpty()) {
            throw new BaseException(ResultCode.SONG_NOT_EXIST);
        }
        for (TbMusicUrlPojo tbMusicUrlPojo : list) {
            tbMusicUrlPojo.setUrl(config.getHost() + tbMusicUrlPojo.getUrl());
        }
        return list;
    }
    
    /**
     * 下载音乐接口
     *
     * @param musicFilePath 音乐地址
     * @return 音乐数据
     */
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
