package org.api.admin.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.IterUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.text.StrPool;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.crypto.digest.MD5;
import cn.hutool.http.HttpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.houbb.opencc4j.util.ZhConverterUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;
import org.api.admin.config.AdminConfig;
import org.api.admin.model.req.SaveOrUpdateMusicReq;
import org.api.admin.model.req.SyncMusicMetaDataReq;
import org.api.admin.model.req.UploadMusicReq;
import org.api.admin.model.req.upload.AudioInfoReq;
import org.api.admin.model.res.AudioInfoRes;
import org.api.admin.model.res.MusicFileRes;
import org.api.admin.model.res.MusicInfoRes;
import org.api.common.service.QukuAPI;
import org.core.common.constant.LyricConstant;
import org.core.common.constant.defaultinfo.DefaultInfo;
import org.core.common.constant.defaultinfo.EnumNameType;
import org.core.common.exception.BaseException;
import org.core.common.result.ResultCode;
import org.core.config.FileTypeConfig;
import org.core.config.HttpRequestConfig;
import org.core.config.UploadConfig;
import org.core.jpa.repository.TbResourceEntityRepository;
import org.core.mybatis.iservice.*;
import org.core.mybatis.model.convert.ArtistConvert;
import org.core.mybatis.pojo.*;
import org.core.oss.model.Resource;
import org.core.oss.service.OSSService;
import org.core.service.AccountService;
import org.core.service.RemoteStorageService;
import org.core.service.RemoteStorePicService;
import org.core.utils.ExceptionUtil;
import org.core.utils.LocalFileUtil;
import org.core.utils.UserUtil;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldDataInvalidException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.images.Artwork;
import org.jaudiotagger.tag.images.StandardArtwork;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.BeanUtils;
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
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service(AdminConfig.ADMIN + "MusicFlowApi")
@Slf4j
@AllArgsConstructor
public class MusicFlowApi {
    /**
     * 音乐文件导入后缀名
     */
    private final FileTypeConfig fileType;
    /**
     * 音乐信息服务
     */
    private final TbMusicService musicService;
    
    static final List<String> imgSuffix = List.of("jpg", "png", "jpeg");
    
    private final TbLyricService lyricService;
    
    /**
     * 歌手服务
     */
    private final TbArtistService artistService;
    
    /**
     * 专辑表
     */
    private final TbAlbumService albumService;
    
    private final TbAlbumArtistService albumSingerService;
    
    private final AccountService accountService;
    
    private final QukuAPI qukuService;
    
    private final TbMusicArtistService musicArtistService;
    
    private final HttpRequestConfig requestConfig;
    
    private final DefaultInfo defaultInfo;
    
    private final TbOriginService originService;
    /**
     * 音乐保存数据链接表
     */
    private final TbResourceService resourceService;
    
    private final TbResourceEntityRepository resourceEntityRepository;
    
    private final UploadConfig uploadConfig;
    
    private final OSSService ossService;
    
    private final RemoteStorageService remoteStorageService;
    
    private final RemoteStorePicService remoteStorePicService;
    
    private final HttpRequestConfig httpRequestConfig;
    
    /**
     * 上传文件或音乐URL下载到临时目录
     *
     * @param uploadFile 临时文件
     * @param url        上传音乐地址
     * @return 音乐信息
     */
    public AudioInfoRes uploadMusicFile(MultipartFile uploadFile, String url) throws IOException, CannotReadException, TagException, ReadOnlyFileException {
        File path;
        String fileSuffix;
        AudioInfoRes audioInfoRes = new AudioInfoRes();
        String md5;
        if (StringUtils.isBlank(url)) {
            md5 = DigestUtils.md5DigestAsHex(uploadFile.getBytes());
            // 上传文件
            String filename = uploadFile.getOriginalFilename();
            if (StringUtils.isBlank(filename)) {
                throw new BaseException(ResultCode.FILENAME_INVALID);
            }
            fileSuffix = LocalFileUtil.getFileSuffix(filename, fileType.getSuffix());
            path = checkFileMd5(md5, requestConfig.getTempPathFile(md5 + "." + fileSuffix));
            // 本地没有则保存
            if (path == null) {
                String musicFileName = md5 + "." + fileSuffix;
                path = requestConfig.getTempPathFile(musicFileName);
                BufferedOutputStream outputStream = FileUtil.getOutputStream(path);
                outputStream.write(uploadFile.getBytes());
                outputStream.close();
                audioInfoRes.setIsExist(false);
            }
            audioInfoRes.setIsExist(true);
        } else {
            // 下载文件
            fileSuffix = LocalFileUtil.getFileSuffix(url, fileType.getSuffix());
            byte[] bytes = HttpUtil.downloadBytes(url);
            md5 = DigestUtils.md5DigestAsHex(bytes);
            File dest = requestConfig.getTempPathFile(md5 + "." + fileSuffix);
            path = checkFileMd5(md5, dest);
            // 本地没有则保存
            if (path == null) {
                path = FileUtil.writeBytes(bytes, dest);
                audioInfoRes.setIsExist(false);
            }
            audioInfoRes.setIsExist(true);
        }
        audioInfoRes.setMd5(md5);
        audioInfoRes.setEncodeType(fileSuffix);
        audioInfoRes.setSize(FileUtil.size(path));
        AudioFile read;
        try {
            read = AudioFileIO.read(path);
            int rate = Integer.parseInt(read.getAudioHeader().getBitRate());
            audioInfoRes.setRate(rate);
            getLevel(audioInfoRes, rate);
        } catch (InvalidAudioFrameException e) {
            log.warn("该音频文件没有包含音乐信息!!");
            audioInfoRes.setType(fileSuffix);
            audioInfoRes.setSize(path.length());
            audioInfoRes.setMusicFileTemp(path.getName());
            return audioInfoRes;
        }
        log.info(" ----- ----- ");
        log.info("标题:" + read.getTag().getFirst(FieldKey.TITLE));
        log.info("作者:" + read.getTag().getFirst(FieldKey.ARTIST));
        log.info("专辑:" + read.getTag().getFirst(FieldKey.ALBUM));
        log.info("比特率:" + read.getAudioHeader().getBitRate());
        log.info("时长:" + read.getAudioHeader().getTrackLength() + "s");
        log.info("大小:" + (read.getFile().length() / 1024F / 1024F) + "MB");
        log.info(" ----- ----- ");
        audioInfoRes.setMusicName(read.getTag().getFirst(FieldKey.TITLE));
        audioInfoRes.setOriginFileName(uploadFile == null || uploadFile.getOriginalFilename() == null ? "" : uploadFile.getOriginalFilename());
        audioInfoRes.setArtists(Collections.singletonList(read.getTag().getFirst(FieldKey.ARTIST)));
        audioInfoRes.setAlbum(read.getTag().getFirst(FieldKey.ALBUM));
        audioInfoRes.setTimeLength(read.getAudioHeader().getTrackLength() * 1000);
        audioInfoRes.setSize(read.getFile().length());
        audioInfoRes.setMusicFileTemp(path.getName());
        return audioInfoRes;
    }
    
    
    private static void getLevel(TbResourcePojo entity, int rate) {
        // 标准
        if (128000 >= rate) {
            entity.setLevel("standard");
            return;
        }
        // 较高
        if (192000 == rate) {
            entity.setLevel("higher");
            return;
        }
        // 极高
        if (320_000 >= rate) {
            entity.setLevel("exhigh");
            return;
        }
        // 无损
        entity.setLevel("lossless");
        // hires rate < 1411_000
    }
    
    
    /**
     * 获取临时文件字节
     *
     * @param musicTempFile 临时文件名
     * @return 字节数据
     */
    public ResponseEntity<FileSystemResource> getMusicTempFile(String musicTempFile) {
        LocalFileUtil.checkFileNameLegal(musicTempFile);
        File file = LocalFileUtil.checkFilePath(requestConfig.getTempPath(), musicTempFile);
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
     * 保存歌词数据
     *
     * @param dto       前端数据
     * @param musicPojo 歌词信息
     * @return 返回保存歌词
     */
    private List<TbLyricPojo> saveLyric(AudioInfoReq dto, TbMusicPojo musicPojo) {
        if (musicPojo == null || musicPojo.getId() == null) {
            return Collections.emptyList();
        }
        
        List<TbLyricPojo> list = new ArrayList<>();
        if (StringUtils.isNotBlank(dto.getMusic().getLyric())) {
            TbLyricPojo one = lyricService.getOne(Wrappers.<TbLyricPojo>lambdaQuery()
                                                          .eq(TbLyricPojo::getMusicId, musicPojo.getId())
                                                          .eq(TbLyricPojo::getType, LyricConstant.LYRIC));
            TbLyricPojo entity = Optional.ofNullable(one).orElse(new TbLyricPojo());
            entity.setMusicId(musicPojo.getId());
            entity.setType(LyricConstant.LYRIC);
            entity.setLyric(dto.getMusic().getLyric());
            list.add(entity);
        }
        if (StringUtils.isNotBlank(dto.getMusic().getTLyric())) {
            TbLyricPojo one = lyricService.getOne(Wrappers.<TbLyricPojo>lambdaQuery()
                                                          .eq(TbLyricPojo::getMusicId, musicPojo.getId())
                                                          .eq(TbLyricPojo::getType, LyricConstant.T_LYRIC));
            TbLyricPojo entity = Optional.ofNullable(one).orElse(new TbLyricPojo());
            entity.setMusicId(musicPojo.getId());
            entity.setType(LyricConstant.T_LYRIC);
            entity.setLyric(dto.getMusic().getTLyric());
            list.add(entity);
        }
        if (StringUtils.isNotBlank(dto.getMusic().getKLyric())) {
            TbLyricPojo one = lyricService.getOne(Wrappers.<TbLyricPojo>lambdaQuery()
                                                          .eq(TbLyricPojo::getMusicId, musicPojo.getId())
                                                          .eq(TbLyricPojo::getType, LyricConstant.K_LYRIC));
            TbLyricPojo entity = Optional.ofNullable(one).orElse(new TbLyricPojo());
            entity.setMusicId(musicPojo.getId());
            entity.setType(LyricConstant.K_LYRIC);
            entity.setLyric(dto.getMusic().getKLyric());
            list.add(entity);
        }
        lyricService.saveOrUpdateBatch(list);
        return list;
    }
    
    private void saveRandReturnArtistList(Set<Long> artistIds, TbMusicPojo musicPojo) {
        ArrayList<TbMusicArtistPojo> artistList = new ArrayList<>();
        for (Long artistId : artistIds) {
            TbMusicArtistPojo e = new TbMusicArtistPojo();
            e.setArtistId(artistId);
            e.setMusicId(musicPojo.getId());
            artistList.add(e);
        }
        musicArtistService.saveOrUpdateBatch(artistList);
    }
    
    private File checkFileMd5(String md5, File file) {
        if (FileUtil.isFile(file)) {
            return file;
        }
        long count = resourceService.count(Wrappers.<TbResourcePojo>lambdaQuery().eq(TbResourcePojo::getMd5, md5));
        if (count > 0) {
            throw new BaseException(ResultCode.SONG_EXIST);
        }
        return null;
    }
    
    private void uploadLocalFile(AudioInfoReq.AudioSource source, TbMusicPojo musicPojo, TbAlbumPojo albumPojo, List<TbArtistPojo> musicArtistList, TbResourcePojo urlPojo, List<TbLyricPojo> lyricPojoList) {
        File file = null;
        String md5 = "";
        String uploadPath;
        long size;
        try {
            if (StringUtils.startsWithIgnoreCase(source.getPathTemp(), "http")) {
                // 没有md5，取文件的md5
                file = new File(UUID.fastUUID().toString(true) + StrPool.DOT + FileUtil.getSuffix(source.getPathTemp()));
                HttpUtil.downloadFile(source.getPathTemp(), file, requestConfig.getTimeout());
                ExceptionUtil.isNull(FileUtil.isEmpty(file) || FileUtil.size(file) < 100, ResultCode.DOWNLOAD_ERROR);
            } else {
                // 读取本地文件
                file = requestConfig.getTempPathFile(source.getPathTemp());
            }
            // 设置音乐元数据, 然后上传
            uploadPath = writeMusicMetaAndUploadMusicFile(file, source.getRate(), musicPojo, albumPojo, musicArtistList, musicArtistList, lyricPojoList);
            
            size = FileUtil.size(file);
            FileUtil.del(file);
        } catch (Exception e) {
            FileUtil.del(file);
            throw new BaseException(null, e.getMessage(), e.getCause());
        }
        // music URL 地址表
        urlPojo.setSize(size);
        urlPojo.setRate(source.getRate());
        urlPojo.setLevel(source.getLevel());
        urlPojo.setMd5(md5);
        urlPojo.setEncodeType(FileUtil.extName(file));
        urlPojo.setPath(uploadPath);
    }
    
    /**
     * 保存音乐数据
     * 有音乐ID直接保存
     * 有关联专辑信息，歌手，查询是否有已存在歌曲
     * 没有任何关联信息，直接保存
     *
     * @param dto       前端请求数据
     * @param artistIds 歌手ID
     * @param albumPojo 专辑数据
     * @return 音乐信息
     */
    @NotNull
    private TbMusicPojo saveAndReturnMusicPojo(AudioInfoReq dto, Set<Long> artistIds, TbAlbumPojo albumPojo) {
        String aliaNames = CollUtil.join(dto.getMusic().getAliaName(), ",");
        // 如果有ID则直接更新
        if (dto.getMusic().getId() != null) {
            TbMusicPojo musicPojo = musicService.getById(dto.getMusic().getId());
            return saveMusicInfoTable(dto, albumPojo, musicPojo, aliaNames);
        }
        // 判断专辑数据是否有值
        boolean albumFlag = albumPojo != null && albumPojo.getId() != null;
        if (CollUtil.isNotEmpty(artistIds) && albumFlag) {
            // 获取数据库中音乐数据
            TbMusicPojo musicPojo = musicService.getOne(Wrappers.<TbMusicPojo>lambdaQuery()
                                                                .eq(TbMusicPojo::getAlbumId, albumPojo.getId())
                                                                .eq(TbMusicPojo::getMusicName, dto.getMusic().getMusicName()));
            // 如果不相等则查询数据库相同音乐中关联的歌手，一样则覆盖
            if (musicPojo == null) {
                TbMusicPojo dto1 = getMusicPojo(dto, artistIds, albumPojo, aliaNames);
                if (dto1 != null) {
                    return dto1;
                }
            } else {
                // 通过专辑查询出来音乐数据ID存在歌手中间中，直接返回
                return saveMusicInfoTable(dto, albumPojo, musicPojo, aliaNames);
            }
        }
        
        LambdaQueryWrapper<TbMusicPojo> musicPojoLambdaQueryWrapper = Wrappers.lambdaQuery();
        musicPojoLambdaQueryWrapper.eq(StringUtils.isNotBlank(dto.getMusic().getMusicName()), TbMusicPojo::getMusicName, dto.getMusic().getMusicName());
        String join = StringUtils.join(dto.getMusic().getAliaName(), ",");
        musicPojoLambdaQueryWrapper.eq(StringUtils.isNotBlank(join), TbMusicPojo::getAliasName, join);
        TbMusicPojo one = musicService.getOne(musicPojoLambdaQueryWrapper);
        return saveMusicInfoTable(dto, albumPojo, one, aliaNames);
    }
    
    /**
     * 根据空专辑查询歌曲信息
     *
     * @param dto       前端信息
     * @param artistIds 歌手ID
     * @param albumPojo 专辑信息
     * @param aliaNames 别名
     * @return 歌曲信息
     */
    @Nullable
    private TbMusicPojo getMusicPojo(AudioInfoReq dto, Set<Long> artistIds, TbAlbumPojo albumPojo, String aliaNames) {
        List<TbMusicPojo> list = musicService.list(Wrappers.<TbMusicPojo>lambdaQuery()
                                                           .eq(TbMusicPojo::getMusicName, dto.getMusic().getMusicName()));
        Set<Long> albumSets = list.parallelStream().map(TbMusicPojo::getAlbumId).collect(Collectors.toSet());
        if (CollUtil.isNotEmpty(albumSets)) {
            Map<Long, TbAlbumPojo> albumMaps = albumService.listByIds(albumSets)
                                                           .parallelStream()
                                                           .collect(Collectors.toMap(TbAlbumPojo::getId, tbAlbumPojo -> tbAlbumPojo));
            for (Long albumSet : albumSets) {
                List<TbAlbumArtistPojo> list1 = albumSingerService.list(Wrappers.<TbAlbumArtistPojo>lambdaQuery()
                                                                                .eq(TbAlbumArtistPojo::getAlbumId, albumSet));
                Set<Long> tempArtistList = list1.parallelStream().map(TbAlbumArtistPojo::getArtistId).collect(Collectors.toSet());
                if (CollUtil.isEqualList(tempArtistList, artistIds) && StringUtils.isEmpty(albumMaps.get(albumSet).getAlbumName())) {
                    Optional<TbMusicPojo> first = list.parallelStream().filter(tbMusicPojo ->
                            StringUtils.equals(tbMusicPojo.getMusicName(), dto.getMusic().getMusicName()) && Objects.equals(tbMusicPojo.getAlbumId(),
                                    albumSet)
                    ).findFirst();
                    if (first.isPresent()) {
                        return saveMusicInfoTable(dto, albumPojo, first.get(), aliaNames);
                    }
                }
            }
        }
        return null;
    }
    
    /**
     * 保存信息到音乐表中
     *
     * @param dto       前端信息
     * @param albumPojo 专辑信息
     * @param musicPojo 查询出的音乐信息
     * @param aliaNames 别名
     * @return 保存后的音乐信息
     */
    @NotNull
    private TbMusicPojo saveMusicInfoTable(AudioInfoReq dto, TbAlbumPojo albumPojo, TbMusicPojo musicPojo, String aliaNames) {
        TbMusicPojo tbMusicPojo = musicPojo == null ? new TbMusicPojo() : musicPojo;
        // music 信息表
        tbMusicPojo.setMusicName(dto.getMusic().getMusicName());
        tbMusicPojo.setAliasName(aliaNames);
        tbMusicPojo.setAlbumId(albumPojo == null ? null : albumPojo.getId());
        tbMusicPojo.setTimeLength(dto.getMusic().getTimeLength());
        // 保存音乐表
        boolean save = musicService.saveOrUpdate(tbMusicPojo);
        boolean pathFlag = StringUtils.isNotBlank(dto.getMusic().getPic().getPath());
        if (pathFlag && StringUtils.isNotBlank(dto.getMusic().getPic().getMd5())) {
            remoteStorePicService.saveOrUpdateMusicPic(tbMusicPojo.getId(), dto.getMusic().getPic());
        } else {
            if (pathFlag) {
                remoteStorePicService.saveOrUpdateMusicPicUrl(tbMusicPojo.getId(), dto.getMusic().getPic().getPath());
            }
        }
        // 保存错误，抛出异常
        ExceptionUtil.isNull(!save, ResultCode.SAVE_FAIL);
        return tbMusicPojo;
    }
    
    /**
     * 查询表中专辑信息，没有则新建
     * 有一下几种情况:
     * 有专辑ID，直接更新信息，并更新歌手与专辑中间表
     * 无歌手表ID，直接保存专辑数据
     * 有歌手ID，查询关联表，更新信息
     *
     * @param dto       前端请求
     * @param singerIds 音乐和歌手列表
     * @return 专辑表
     */
    private TbAlbumPojo saveAndReturnAlbumPojo(AudioInfoReq dto, Set<Long> singerIds) {
        // 专辑没有值直接返回
        AudioInfoReq.AudioAlbum album = dto.getAlbum();
        if (album == null || StringUtils.isBlank(album.getAlbumName())) {
            return new TbAlbumPojo();
        }
        // 如果是数据库中已有数据，直接更新
        Long albumId = album.getId();
        if (albumId != null) {
            if (StringUtils.isNotBlank(album.getPic().getPath()) && (StringUtils.isNotBlank(album.getPic().getMd5()))) {
                remoteStorePicService.saveOrUpdateAlbumPic(albumId, album.getPic());
            } else {
                remoteStorePicService.saveOrUpdateAlbumPicUrl(albumId, album.getPic().getPath());
            }
            albumService.updateById(album);
            return album;
        }
        // 查询专辑名在数据库中是否存在专辑
        List<TbAlbumPojo> list = albumService.list(Wrappers.<TbAlbumPojo>lambdaQuery().eq(TbAlbumPojo::getAlbumName, album.getAlbumName()));
        // 获取所有专辑ID
        List<Long> albumList = list.stream().map(TbAlbumPojo::getId).toList();
        Set<Long> albumIds = new HashSet<>();
        // 根据歌手和专辑ID查找关联ID，如果有则更新专辑表。
        if (IterUtil.isNotEmpty(albumList) && IterUtil.isNotEmpty(singerIds)) {
            // 专辑表找到后，在中间表同时满足专辑ID和歌手ID两个列表，只找到同一个专辑ID
            List<TbAlbumArtistPojo> tbAlbumArtistPojoList = albumSingerService.list(Wrappers.<TbAlbumArtistPojo>lambdaQuery()
                                                                                            .in(TbAlbumArtistPojo::getArtistId, singerIds)
                                                                                            .in(TbAlbumArtistPojo::getAlbumId, albumList));
            albumIds = tbAlbumArtistPojoList.stream().map(TbAlbumArtistPojo::getAlbumId).collect(Collectors.toSet());
        }
        ExceptionUtil.isNull(albumIds.size() > 1, ResultCode.ALBUM_NOT_EXIST);
        TbAlbumPojo albumPojo = new TbAlbumPojo();
        // 数据库中有数据，并且保证数据中的专辑ID和根据专辑名查询出来的ID相同，否则直接抛出异常
        if (albumIds.size() == 1) {
            // 获取关联的专辑表ID
            Long id = albumIds.iterator().next();
            // 获取关联的专辑表数据
            Optional<TbAlbumPojo> first = list.stream()
                                              .filter(tbAlbumPojo -> Objects.equals(tbAlbumPojo.getId(), id))
                                              .findFirst();
            albumPojo = first.orElseThrow(() -> new BaseException(ResultCode.ALBUM_ERROR));
            BeanUtils.copyProperties(album, albumPojo, "id", "updateTime", "createTime", "picId");
            // 更新专辑表
            albumService.updateById(albumPojo);
            return albumPojo;
        }
        // 如果没有歌手数据则只新增专辑表
        if (CollUtil.isEmpty(albumIds)) {
            albumPojo = new TbAlbumPojo();
            BeanUtils.copyProperties(album, albumPojo, "picId");
            // 新增专辑
            albumService.saveOrUpdate(albumPojo);
            // 更新歌手和专辑中间表
            if (IterUtil.isNotEmpty(singerIds)) {
                List<TbAlbumArtistPojo> albumSingerPojoArrayList = new ArrayList<>();
                for (Long id : singerIds) {
                    TbAlbumArtistPojo e = new TbAlbumArtistPojo();
                    e.setAlbumId(albumPojo.getId());
                    e.setArtistId(id);
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
     * 前端传递数据中没有歌手，直接返回空数据
     * 前端传入数据有歌手，数据库中有，查询出歌手和音乐主键ID
     * 前端传入数据有歌手，数据库中没有，添加歌手数据并返回歌手存入数据
     * 前端有数据，数据库中也有，但是数据库中一半有歌手，一半没有歌手。则更新数据
     *
     * @param dto 前端请求数据
     * @return 返回音乐和歌手ID
     */
    private List<TbArtistPojo> saveAndReturnMusicAndSingList(AudioInfoReq dto) {
        // 没有歌手直接返回
        List<AudioInfoReq.AudioArtist> artists = dto.getArtists();
        if (IterUtil.isEmpty(artists) || StringUtils.isBlank(artists.get(0).getArtistName())) {
            return Collections.emptyList();
        }
        // 有歌手，数据库中有，查询出歌手和音乐主键ID
        // 获取前端传入所有歌手
        List<String> singerNameList = artists
                .stream()
                .map(TbArtistPojo::getArtistName)
                .toList();
        
        // 查询数据库中歌手
        List<TbArtistPojo> singList = artistService.list(Wrappers.<TbArtistPojo>lambdaQuery()
                                                                 .in(TbArtistPojo::getArtistName, singerNameList));
        
        
        List<TbArtistPojo> saveBatch = new ArrayList<>();
        // 遍历前端传入的所有歌手信息, 与前端进行比较，歌手名相同的则更新数据库。没有歌手就添加到数据库。注意这个更新条件是根据歌手名来更新的
        for (AudioInfoReq.AudioArtist singerReq : artists) {
            TbArtistPojo pojo = new TbArtistPojo();
            BeanUtils.copyProperties(singerReq, pojo);
            // 覆盖数据中的数据，防止出现脏数据(多个相同的歌手)
            for (TbArtistPojo tbArtistPojo : singList) {
                if (StringUtils.equalsIgnoreCase(singerReq.getArtistName(), tbArtistPojo.getArtistName())) {
                    // 歌曲家名字相同，就把数据库中的ID拷贝到前端传入的数据中，直接更新数据库
                    pojo.setId(tbArtistPojo.getId());
                }
            }
            artistService.saveOrUpdate(pojo);
            // 更新封面
            if (StringUtils.isNotBlank(singerReq.getPic().getPath()) && (StringUtils.isNotBlank(singerReq.getPic().getMd5()))) {
                remoteStorePicService.saveOrUpdateArtistPic(pojo.getId(), singerReq.getPic());
            } else {
                remoteStorePicService.saveOrUpdateArtistPicUrl(pojo.getId(), singerReq.getPic().getPath());
            }
            saveBatch.add(pojo);
        }
        return saveBatch;
    }
    
    private void setMusicMetaData(File file, TbMusicPojo musicPojo, TbAlbumPojo albumPojo, List<TbArtistPojo> musicArtistList, List<TbArtistPojo> albumArtistList, List<TbLyricPojo> lyricPojo) {
        String extName = FileUtil.extName(file);
        boolean isMatching = Stream.of("mp3", "flac").anyMatch(s -> StringUtils.equals(s, extName));
        if (isMatching) {
            // 读取音乐文件的元数据
            try {
                AudioFile audioFile = AudioFileIO.read(file);
                Tag tag = audioFile.getTag();
                
                // 设置元数据（例如，设置标题和艺术家）
                tag.setField(FieldKey.TITLE, musicPojo.getMusicName());
                if (StringUtils.isNotBlank(musicPojo.getAliasName())) {
                    tag.setField(FieldKey.SUBTITLE, musicPojo.getAliasName());
                }
                if (StringUtils.isNotBlank(albumPojo.getAlbumName())) {
                    tag.setField(FieldKey.ALBUM, albumPojo.getAlbumName());
                }
                // 音乐歌手
                if (CollUtil.isNotEmpty(musicArtistList)) {
                    tag.setField(FieldKey.ARTIST, StringUtils.join(musicArtistList.parallelStream().map(TbArtistPojo::getArtistName).toList(), ' '));
                }
                // 专辑歌手
                if (CollUtil.isNotEmpty(albumArtistList)) {
                    tag.setField(FieldKey.ALBUM_ARTIST, StringUtils.join(albumArtistList.parallelStream().map(TbArtistPojo::getArtistName).toList(), ' '));
                }
                // 发布日期
                if (Objects.nonNull(musicPojo.getPublishTime())) {
                    tag.setField(FieldKey.YEAR, LocalDateTimeUtil.format(musicPojo.getPublishTime(), DatePattern.NORM_DATE_FORMATTER));
                }
                if (CollUtil.isNotEmpty(lyricPojo)) {
                    Optional<TbLyricPojo> first = lyricPojo.parallelStream()
                                                           .filter(lyricPojo1 -> StringUtils.equals(LyricConstant.LYRIC, lyricPojo1.getType()))
                                                           .findFirst();
                    // 设置歌词
                    if (first.isPresent()) {
                        first.ifPresent(lyricPojo1 -> {
                            try {
                                tag.setField(FieldKey.LYRICS, lyricPojo1.getLyric());
                            } catch (FieldDataInvalidException e) {
                                log.error("lyric write error: {}\n text: {}", lyricPojo1.getMusicId(), lyricPojo1.getLyric());
                            }
                        });
                    }
                }
                // 设置封面
                String musicPicUrl = remoteStorePicService.getMusicPicUrl(musicPojo.getId());
                if (StringUtils.isNotBlank(musicPicUrl)) {
                    Artwork artwork = new StandardArtwork();
                    artwork.setBinaryData(HttpUtil.downloadBytes(musicPicUrl));
                    tag.addField(artwork);
                }
                
                // 保存更改到音乐文件
                AudioFileIO.write(audioFile);
            } catch (CannotReadException | IOException | TagException | ReadOnlyFileException | InvalidAudioFrameException | CannotWriteException e) {
                throw new BaseException(ResultCode.DATA_WRONG);
            }
        }
    }
    
    public List<TbLyricPojo> getMusicLyric(Long musicId) {
        return qukuService.getMusicLyric(musicId);
    }
    
    /**
     * 删除音乐
     *
     * @param musicId 音乐ID
     * @param compel  是否强制删除
     */
    public void deleteMusic(List<Long> musicId, Boolean compel) {
        qukuService.deleteMusic(musicId, compel);
    }
    
    
    /**
     * 保存或更新歌词
     *
     * @param musicId 音乐ID
     * @param type    歌词类型
     * @param lyric   歌词
     */
    public void saveOrUpdateLyric(Long musicId, String type, String lyric) {
        qukuService.saveOrUpdateLyric(musicId, type, lyric);
    }
    
    /**
     * 查询音乐URL表
     *
     * @param musicIds 音乐id
     * @param refresh  是否刷新
     * @return 音乐URL列表
     */
    public List<MusicFileRes> getMusicUrl(Set<String> musicIds, Boolean refresh) {
        List<MusicFileRes> files = new ArrayList<>();
        List<TbResourcePojo> list = resourceService.list(Wrappers.<TbResourcePojo>lambdaQuery().in(TbResourcePojo::getMusicId, musicIds));
        for (TbResourcePojo tbMusicUrlPojo : list) {
            MusicFileRes musicFileRes = new MusicFileRes();
            try {
                String address = tbMusicUrlPojo.getPath();
                TbResourcePojo url = qukuService.getMusicUrlByMusicUrlList(tbMusicUrlPojo, refresh);
                BeanUtils.copyProperties(url, musicFileRes);
                musicFileRes.setPath(address);
                musicFileRes.setSize(tbMusicUrlPojo.getSize());
                musicFileRes.setLevel(tbMusicUrlPojo.getLevel());
                musicFileRes.setMd5(tbMusicUrlPojo.getMd5());
                musicFileRes.setRawUrl(url.getPath());
                musicFileRes.setExists(StringUtils.isNotBlank(url.getPath()));
            } catch (BaseException e) {
                if (StringUtils.equals(e.getCode(), ResultCode.OSS_LOGIN_ERROR.getCode())) {
                    throw new BaseException(ResultCode.OSS_LOGIN_ERROR);
                }
                musicFileRes.setRawUrl("");
                musicFileRes.setExists(false);
                continue;
            } catch (Exception e) {
                throw new BaseException(e.getMessage());
            }
            files.add(musicFileRes);
        }
        return files;
    }
    
    /**
     * 上传文件
     *
     * @param dto             前端请求数据
     * @param musicPojo       音乐表数据
     * @param albumPojo       专辑数据
     * @param musicArtistList 音乐歌手
     * @param lyricPojoList   歌词数据
     */
    private List<TbResourcePojo> uploadFile(AudioInfoReq dto, TbMusicPojo musicPojo, TbAlbumPojo albumPojo, List<TbArtistPojo> musicArtistList, List<TbLyricPojo> lyricPojoList) {
        List<TbResourcePojo> resourceList = new ArrayList<>();
        if (Optional.ofNullable(dto.getSources()).orElse(dto.getSources()) != null) {
            for (AudioInfoReq.AudioSource source : dto.getSources()) {
                TbResourcePojo urlPojo = resourceService.getOne(Wrappers.<TbResourcePojo>lambdaQuery().eq(TbResourcePojo::getMd5, source.getMd5()));
                urlPojo = urlPojo == null ? new TbResourcePojo() : urlPojo;
                urlPojo.setMusicId(musicPojo.getId());
                Long userId = dto.getUserId() == null ? UserUtil.getUser().getId() : dto.getUserId();
                SysUserPojo byId = accountService.getById(userId);
                ExceptionUtil.isNull(byId == null, ResultCode.USER_NOT_EXIST);
                urlPojo.setUserId(byId.getId());
                // 上传到本地时读取本地文件数据，否则使用前端传入的数据
                if (Boolean.TRUE.equals(dto.getUploadFlag())) {
                    if (source.getSize() == null
                            || source.getRate() == null
                            || source.getRate() == 0
                            || StringUtils.isBlank(source.getLevel())
                            || StringUtils.isBlank(source.getMd5())
                            || StringUtils.isBlank(source.getEncodeType())
                            || StringUtils.isBlank(source.getPathTemp())) {
                        return Collections.emptyList();
                    }
                    // 只保存到数据库，不上传文件
                    urlPojo.setSize(source.getSize());
                    urlPojo.setRate(source.getRate());
                    urlPojo.setLevel(source.getLevel());
                    urlPojo.setMd5(source.getMd5());
                    urlPojo.setEncodeType(source.getEncodeType());
                    urlPojo.setPath(source.getPathTemp());
                } else {
                    uploadLocalFile(source, musicPojo, albumPojo, musicArtistList, urlPojo, lyricPojoList);
                }
                // 检测是否重复上传相同的MD5音乐文件
                if (urlPojo.getId() == null) {
                    TbResourcePojo one = resourceService.getOne(Wrappers.<TbResourcePojo>lambdaQuery().eq(TbResourcePojo::getMd5, urlPojo.getMd5()));
                    ExceptionUtil.isNull(one != null, ResultCode.OSS_MD5_REPEAT);
                }
                resourceService.saveOrUpdate(urlPojo);
                
                // 保存音源来源
                if (source.getOrigin() != null && StringUtils.isNotBlank(source.getOrigin().getOrigin())) {
                    TbOriginPojo origin = source.getOrigin();
                    origin.setMusicId(musicPojo.getId());
                    origin.setResourceId(urlPojo.getId());
                    originService.save(origin);
                }
                
                resourceList.add(urlPojo);
            }
        }
        return resourceList;
    }
    
    /**
     * 更新音乐信息
     *
     * @param req 信息
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdateMusic(SaveOrUpdateMusicReq req) {
        if (StringUtils.isBlank(req.getMusicName())) {
            throw new BaseException(ResultCode.PARAM_IS_BLANK);
        }
        // 音乐
        musicService.saveOrUpdate(req);
        // 流派
        if (StringUtils.isNotBlank(req.getMusicGenre())) {
            qukuService.addMusicGenreLabel(req.getId(), StringUtils.trim(req.getMusicGenre()));
        }
        // 音乐tag
        if (StringUtils.isNotBlank(req.getMusicTag())) {
            qukuService.addMusicLabelTag(req.getId(), List.of(StringUtils.split(req.getMusicTag(), ',')).parallelStream().map(StringUtils::trim).toList());
        }
        // 更新封面
        if (StringUtils.isNotBlank(req.getTempPicFile())) {
            File file = requestConfig.getTempPathFile(req.getTempPicFile());
            ExceptionUtil.isNull(FileUtil.isEmpty(file), ResultCode.FILENAME_NO_EXIST);
            remoteStorePicService.saveOrUpdateMusicPicFile(req.getId(), file);
        }
        // 如果是更新则删除原有数据
        TbMusicPojo byId = musicService.getById(req.getId());
        // 删除歌曲关联歌手
        musicArtistService.remove(Wrappers.<TbMusicArtistPojo>lambdaQuery().eq(TbMusicArtistPojo::getMusicId, byId.getId()));
        List<TbMusicArtistPojo> musicArtistList = req.getArtistIds().parallelStream().map(aLong -> new TbMusicArtistPojo(req.getId(), aLong)).toList();
        // 重新保存关联歌手
        musicArtistService.saveBatch(musicArtistList);
        
        // 保存音源
        TbResourcePojo resource = req.getResource();
        if (Objects.nonNull(resource) && Objects.nonNull(resource.getMd5())) {
            long count = resourceEntityRepository.countByMd5EqualsIgnoreCase(req.getResource().getMd5());
            ExceptionUtil.isNull(count >= 1, ResultCode.RESOURCE_DATA_EXISTED);
            File file = requestConfig.getTempPathFile(req.getTempMusicFile());
            
            if (FileUtil.isEmpty(file)) {
                resourceService.save(resource);
            } else {
                // 专辑名
                TbAlbumPojo albumPojo = albumService.getById(byId.getAlbumId());
                // 音乐歌手
                List<ArtistConvert> musicArtistByMusicId = qukuService.getMusicArtistByMusicId(byId.getId());
                // 专辑歌手
                List<ArtistConvert> albumArtistListByAlbumIds = qukuService.getAlbumArtistListByAlbumIds(albumPojo.getId());
                // 歌词
                List<TbLyricPojo> musicLyric = qukuService.getMusicLyric(byId.getId());
                String path = writeMusicMetaAndUploadMusicFile(file, resource.getRate(), byId,
                        albumPojo,
                        musicArtistByMusicId.parallelStream().map(TbArtistPojo.class::cast).toList(),
                        albumArtistListByAlbumIds.parallelStream().map(TbArtistPojo.class::cast).toList(),
                        musicLyric);
                
                resource.setMusicId(req.getId());
                resource.setPath(path);
                resourceService.save(resource);
            }
        }
    }
    
    private String writeMusicMetaAndUploadMusicFile(File file, Integer rate, TbMusicPojo byId, TbAlbumPojo albumPojo, List<TbArtistPojo> musicArtistByMusicId, List<TbArtistPojo> albumArtistList, List<TbLyricPojo> lyricPojoList) {
        String formatName = uploadConfig.getMusicNameTemplate(byId.getMusicName(),
                byId.getAliasName(),
                albumPojo.getAlbumName(),
                StringUtils.join(musicArtistByMusicId.parallelStream().map(TbArtistPojo::getArtistName).filter(StringUtils::isNotBlank).toList(), ' '),
                String.valueOf(rate)
        );
        file = FileUtil.rename(file, formatName, true, true);
        
        setMusicMetaData(file,
                byId,
                albumPojo, musicArtistByMusicId, albumArtistList, lyricPojoList);
        
        return remoteStorageService.uploadAudioFile(file);
    }
    
    private void saveMusicTagAndGenre(AudioInfoReq dto, TbMusicPojo musicPojo) {
        saveMusicGenre(dto, musicPojo);
        saveMusicTag(dto, musicPojo);
    }
    
    private void saveMusicTag(AudioInfoReq dto, TbMusicPojo musicPojo) {
        String tags = dto.getMusic().getTags();
        if (StringUtils.isNotBlank(tags)) {
            List<String> list = Arrays.asList(tags.split(","));
            qukuService.addMusicLabelTag(musicPojo.getId(), list);
        }
    }
    
    private void saveMusicGenre(AudioInfoReq dto, TbMusicPojo musicPojo) {
        String musicGenre = dto.getMusic().getMusicGenre();
        if (StringUtils.isNotBlank(musicGenre)) {
            List<String> list = Arrays.asList(musicGenre.split(","));
            qukuService.addMusicGenreLabel(musicPojo.getId(), list);
        }
    }
    
    private void saveAlbumTagAndPic(AudioInfoReq dto, TbAlbumPojo albumPojo) {
        if (Objects.nonNull(albumPojo) && Objects.nonNull(albumPojo.getId())) {
            // 保存专辑封面
            saveAlbumPic(dto, albumPojo);
            // 专辑流派
            saveAlbumGenre(dto, albumPojo);
        }
    }
    
    private void saveAlbumPic(AudioInfoReq dto, TbAlbumPojo albumPojo) {
        // 更新封面
        if (StringUtils.isNotBlank(dto.getAlbum().getPic().getPath()) && (StringUtils.isNotBlank(dto.getAlbum().getPic().getMd5()))) {
            remoteStorePicService.saveOrUpdateAlbumPic(albumPojo.getId(), dto.getAlbum().getPic());
        } else {
            remoteStorePicService.saveOrUpdateAlbumPicUrl(albumPojo.getId(), dto.getAlbum().getPic().getPath());
        }
    }
    
    private void saveAlbumGenre(AudioInfoReq dto, TbAlbumPojo albumPojo) {
        qukuService.addAlbumGenreLabel(albumPojo.getId(), dto.getAlbum().getGenre());
    }
    
    private void nameHandler(AudioInfoReq dto) {
        EnumNameType music = defaultInfo.getName().getMusic();
        switch (music) {
            case CN:
                dto.getMusic().setMusicName(ZhConverterUtil.toSimple(dto.getMusic().getMusicName()));
                dto.getMusic().setAliaName(dto.getMusic().getAliaName().parallelStream().map(ZhConverterUtil::toSimple).toList());
                break;
            case TC:
                dto.getMusic().setMusicName(ZhConverterUtil.toTraditional(dto.getMusic().getMusicName()));
                dto.getMusic().setAliaName(dto.getMusic().getAliaName().parallelStream().map(ZhConverterUtil::toTraditional).toList());
                break;
            case DEFAULT:
            default:
                break;
        }
        
        EnumNameType artist = defaultInfo.getName().getArtist();
        switch (artist) {
            case CN:
                dto.getArtists().parallelStream().forEach(
                        req -> {
                            req.setArtistName(ZhConverterUtil.toSimple(req.getArtistName()));
                            req.setAliasName(ZhConverterUtil.toSimple(req.getAliasName()));
                        }
                );
                break;
            case TC:
                dto.getArtists().parallelStream().forEach(
                        req -> {
                            req.setArtistName(ZhConverterUtil.toTraditional(req.getArtistName()));
                            req.setAliasName(ZhConverterUtil.toTraditional(req.getAliasName()));
                        }
                );
                break;
            case DEFAULT:
            default:
                break;
        }
        
        EnumNameType album = defaultInfo.getName().getAlbum();
        switch (album) {
            case CN:
                dto.getAlbum().setAlbumName(ZhConverterUtil.toSimple(dto.getAlbum().getAlbumName()));
                break;
            case TC:
                dto.getAlbum().setAlbumName(ZhConverterUtil.toTraditional(dto.getAlbum().getAlbumName()));
                break;
            case DEFAULT:
            default:
                break;
        }
    }
    
    public MusicInfoRes getMusicInfo(Long id) {
        // 音乐
        TbMusicPojo byId = musicService.getById(id);
        List<TbTagPojo> labelMusic = qukuService.getLabelMusicTag(byId.getId()).get(id);
        List<TbTagPojo> musicGenre = qukuService.getLabelMusicGenre(byId.getId()).get(id);
        // 专辑艺术家
        List<ArtistConvert> artistListByAlbumIds = qukuService.getAlbumArtistListByAlbumIds(byId.getAlbumId());
        // 歌曲艺术家
        List<ArtistConvert> musicArtistByMusicId = qukuService.getMusicArtistByMusicId(id);
        // 专辑
        TbAlbumPojo albumPojo = Optional.ofNullable(albumService.getById(byId.getAlbumId())).orElse(new AudioInfoReq.AudioAlbum());
        
        MusicInfoRes musicInfoRes = new MusicInfoRes();
        musicInfoRes.setMusicTag(labelMusic.parallelStream().map(TbTagPojo::getTagName).toList());
        musicInfoRes.setMusicGenre(musicGenre.parallelStream().map(TbTagPojo::getTagName).findFirst().orElse(""));
        musicInfoRes.setMusicArtist(musicArtistByMusicId);
        musicInfoRes.setAlbumArtist(artistListByAlbumIds);
        musicInfoRes.setAlbumName(albumPojo.getAlbumName());
        musicInfoRes.setAlbumId(albumPojo.getId());
        BeanUtils.copyProperties(byId, musicInfoRes);
        musicInfoRes.setPicUrl(remoteStorePicService.getMusicPicUrl(byId.getId()));
        musicInfoRes.setPublishTime(albumPojo.getPublishTime());
        return musicInfoRes;
    }
    
    private AudioFile getAudioInfo(File path) {
        AudioFile read = new AudioFile();
        try {
            read = AudioFileIO.read(path);
        } catch (InvalidAudioFrameException | CannotReadException | TagException | ReadOnlyFileException | IOException e) {
            log.info(e.getMessage(), e.getCause());
        }
        return read;
    }
    
    /**
     * 保存音乐
     * 更新表: 音乐信息表  歌手表 专辑表
     * 如果上传文件更新: 音乐地址表
     *
     * @param dto 音乐信息
     */
    @Transactional(rollbackFor = Exception.class)
    public MusicDetails saveMusicInfo(AudioInfoReq dto) {
        // 处理名字繁简转移
        nameHandler(dto);
        for (AudioInfoReq.AudioSource audioSource : Optional.ofNullable(dto.getSources()).orElse(new ArrayList<>())) {
            // 检测是读取本地缓存文件， 并且是本地地址
            if (Boolean.FALSE.equals(dto.getUploadFlag()) && !StringUtils.startsWithIgnoreCase(audioSource.getPathTemp(), "http")) {
                // 检查文件目录是否合法
                LocalFileUtil.checkFilePath(requestConfig.getTempPath(), audioSource.getPathTemp());
            }
        }
        // 保存专辑歌手中间表
        List<TbArtistPojo> musicArtistPojoList = saveAndReturnMusicAndSingList(dto);
        Set<Long> artistIds = musicArtistPojoList.stream().map(TbArtistPojo::getId).collect(Collectors.toSet());
        
        // 保存专辑表，如果没有则新建。
        TbAlbumPojo albumPojo = saveAndReturnAlbumPojo(dto, artistIds);
        saveAlbumTagAndPic(dto, albumPojo);
        // 保存音乐表
        TbMusicPojo musicPojo = saveAndReturnMusicPojo(dto, artistIds, albumPojo);
        // 保存音乐Tag和流派
        saveMusicTagAndGenre(dto, musicPojo);
        // 保存歌手音乐表
        saveRandReturnArtistList(artistIds, musicPojo);
        // 保存歌词
        List<TbLyricPojo> lyricPojoList = saveLyric(dto, musicPojo);
        // 上传文件
        List<TbResourcePojo> resourcePojo = uploadFile(dto, musicPojo, albumPojo, musicArtistPojoList, lyricPojoList);
        
        MusicDetails musicDetails = new MusicDetails();
        musicDetails.setMusic(musicPojo);
        musicDetails.setResource(resourcePojo);
        musicDetails.setAlbum(albumPojo);
        musicDetails.setSinger(musicArtistPojoList);
        musicDetails.setLyrics(lyricPojoList);
        return musicDetails;
    }
    
    public void uploadAutoMusicFile(Long userId, File uploadFile, Long musicId) {
        TbResourcePojo entity = new TbResourcePojo();
        entity.setMusicId(musicId);
        userId = userId == null ? UserUtil.getUser().getId() : userId;
        String tempMd5 = MD5.create().digestHex(uploadFile);
        TbResourcePojo tempOne = resourceService.getOne(Wrappers.<TbResourcePojo>lambdaQuery().eq(TbResourcePojo::getMd5, tempMd5));
        // 如果上传的歌曲MD5值在数据中，并且与存储的歌曲ID不符合则不上传
        if (tempOne != null && !Objects.equals(tempOne.getMusicId(), musicId)) {
            throw new BaseException(ResultCode.UPLOAD_MUSIC_ID_NOT_MATCH.getCode(),
                    ResultCode.UPLOAD_MUSIC_ID_NOT_MATCH.getResultMsg() + ", 歌曲: " + tempOne.getMusicId() + "-" + tempOne.getPath());
        }
        String[] nameArr = StringUtils.split(uploadFile.getName(), ".");
        if (nameArr == null || nameArr.length < 1) {
            throw new BaseException(ResultCode.FILENAME_INVALID);
        }
        File dest = httpRequestConfig.getTempPathFile(tempMd5 + "." + nameArr[1]);
        FileUtil.move(uploadFile, dest, true);
        AudioFile audioInfo = getAudioInfo(dest);
        // 写入音乐元数据到音频文件中
        // 音乐名
        TbMusicPojo byId = musicService.getById(musicId);
        // 专辑名
        TbAlbumPojo albumPojo = albumService.getById(byId.getAlbumId());
        // 音乐歌手
        List<ArtistConvert> musicArtistByMusicId = qukuService.getMusicArtistByMusicId(musicId);
        List<ArtistConvert> albumArtistListByAlbumIds = qukuService.getAlbumArtistListByAlbumIds(albumPojo.getId());
        // 上传
        List<TbLyricPojo> musicLyric = qukuService.getMusicLyric(byId.getId());
        int rate = Math.toIntExact(audioInfo.getAudioHeader().getBitRateAsNumber());
        String upload = writeMusicMetaAndUploadMusicFile(dest,
                rate, byId,
                albumPojo,
                musicArtistByMusicId.parallelStream().map(TbArtistPojo.class::cast).toList(),
                albumArtistListByAlbumIds.parallelStream().map(TbArtistPojo.class::cast).toList(),
                musicLyric);
        TbResourcePojo one = resourceService.getOne(Wrappers.<TbResourcePojo>lambdaQuery().eq(TbResourcePojo::getMd5, tempMd5));
        rate = rate * 1000;
        if (one == null) {
            entity.setRate(rate);
            getLevel(entity, rate);
            entity.setPath(upload);
            entity.setUserId(userId);
            entity.setEncodeType(nameArr[1]);
            entity.setSize(FileUtil.size(dest));
            entity.setMd5(tempMd5);
            resourceService.save(entity);
            log.info("文件保存到数据库{}", entity);
        } else {
            one.setRate(rate);
            getLevel(one, rate);
            one.setPath(upload);
            one.setSize(FileUtil.size(dest));
            one.setEncodeType(nameArr[1]);
            resourceService.updateById(one);
            log.info("更新成功{}", entity);
        }
        FileUtil.del(dest);
    }
    
    public void uploadManualMusic(UploadMusicReq musicSource) {
        TbResourcePojo one = resourceService.getOne(Wrappers.<TbResourcePojo>lambdaQuery().eq(TbResourcePojo::getMd5, musicSource.getMd5()));
        String[] nameArr = StringUtils.split(musicSource.getName(), ".");
        if (nameArr.length == 1) {
            throw new BaseException(ResultCode.FILENAME_INVALID);
        }
        if (one == null) {
            TbResourcePojo entity = new TbResourcePojo();
            getLevel(entity, musicSource.getRate());
            entity.setRate(musicSource.getRate());
            entity.setUserId(musicSource.getUserId());
            entity.setEncodeType(nameArr[1]);
            entity.setMd5(musicSource.getMd5());
            entity.setSize(musicSource.getSize());
            entity.setPath(musicSource.getName());
            entity.setMusicId(musicSource.getMusicId());
            resourceService.save(entity);
            qukuService.getMusicUrlByMusicId(entity.getMusicId(), true);
        } else {
            throw new BaseException(ResultCode.SONG_UPLOADED);
        }
    }
    
    public void updateSource(TbResourcePojo source) {
        resourceService.updateById(source);
    }
    
    public void deleteSource(Long id) {
        resourceService.removeById(id);
    }
    
    public List<Map<String, Object>> selectResources(String md5) {
        Map<String, Resource> address = remoteStorageService.getMusicResourceByMd5(md5, false);
        return address.entrySet()
                      .parallelStream()
                      .filter(stringMapEntry -> {
                          // 过滤非音乐文件
                          String suffix = StringUtils.split(stringMapEntry.getKey(), ".")[1];
                          return !CollUtil.contains(imgSuffix, suffix);
                      })
                      .map(stringStringEntry -> {
                          Resource value = stringStringEntry.getValue();
                          Map<String, Object> map = new HashMap<>();
                          map.put("md5", value.getMd5());
                          map.put("fileName", value.getName());
                          map.put("audio", value.getUrl());
                          map.put("size", value.getSize());
                          return map;
                      })
                      .limit(20)
                      .toList();
    }
    
    /**
     * 同步音乐元数据
     *
     * @param req 音乐数据
     */
    public void syncMetaMusicFile(SyncMusicMetaDataReq req) {
        TbResourcePojo byId = resourceService.getById(req.getResourceId());
        String path = byId.getPath();
        String addresses = remoteStorageService.getMusicResourceUrl(path, false);
        String originPath = UUID.fastUUID().toString(true) + "." + byId.getEncodeType();
        File destFile = requestConfig.getTempPathFile(originPath);
        HttpUtil.downloadFile(addresses, destFile, requestConfig.getTimeout());
        // 防止文件下载错误，返回数据是json而不是音频，所以判断必须大于100byte才算音频文件
        if (FileUtil.isEmpty(destFile) || FileUtil.size(destFile) < 100) {
            throw new BaseException(ResultCode.STORAGE_FILE_DOES_NOT_EXIST_ERROR);
        }
        
        String extName = FileUtil.extName(destFile);
        boolean isMatching = Stream.of("mp3", "flac").anyMatch(s -> StringUtils.equals(s, extName));
        if (isMatching) {
            // 读取音乐文件的元数据
            try {
                AudioFile audioFile = AudioFileIO.read(destFile);
                Tag tag = audioFile.getTag();
                
                // 设置元数据（例如，设置标题和艺术家）
                tag.setField(FieldKey.TITLE, req.getMusicName());
                if (StringUtils.isNotBlank(req.getMusicAliasName())) {
                    tag.setField(FieldKey.SUBTITLE, req.getMusicAliasName());
                }
                if (StringUtils.isNotBlank(req.getAlbumName())) {
                    tag.setField(FieldKey.ALBUM, req.getAlbumName());
                }
                // 音乐歌手
                if (StringUtils.isNotBlank(req.getMusicArtist())) {
                    tag.setField(FieldKey.ARTIST, req.getMusicArtist());
                }
                // 专辑歌手
                if (StringUtils.isNotBlank(req.getAlbumArtist())) {
                    tag.setField(FieldKey.ALBUM_ARTIST, req.getAlbumArtist());
                }
                // 发布日期
                if (StringUtils.isNotBlank(req.getYear())) {
                    tag.setField(FieldKey.YEAR, req.getYear());
                }
                if (StringUtils.isNotBlank(req.getLyric())) {
                    // 设置歌词
                    String lyric = req.getLyric();
                    String replaceLyric = StringUtils.replace(lyric, "\\n", "\n");
                    try {
                        tag.setField(FieldKey.LYRICS, replaceLyric);
                    } catch (FieldDataInvalidException e) {
                        log.error("music name: {},lyric write error: {}\n text: {}", req.getMusicName(), req.getResourceId().shortValue(), replaceLyric);
                    }
                }
                // 设置封面
                if (StringUtils.isNotBlank(req.getPicBase64())) {
                    String picBase64 = RegExUtils.replaceFirst(req.getPicBase64(), "^data:image/jpeg;base64,", "");
                    byte[] decode = Base64.getDecoder().decode(picBase64);
                    Artwork artwork = new StandardArtwork();
                    artwork.setBinaryData(decode);
                    tag.deleteField(FieldKey.COVER_ART); // 删除现有的封面（可选）
                    tag.addField(artwork);
                } else if (StringUtils.isNotBlank(req.getPicUrl())) {
                    Artwork artwork = new StandardArtwork();
                    artwork.setBinaryData(HttpUtil.downloadBytes(req.getPicUrl()));
                    tag.deleteField(FieldKey.COVER_ART); // 删除现有的封面（可选）
                    tag.addField(artwork);
                }
                // 保存更改到音乐文件
                AudioFileIO.write(audioFile);
            } catch (CannotReadException | IOException | TagException | ReadOnlyFileException | InvalidAudioFrameException | CannotWriteException e) {
                log.error(e.getMessage(), e);
                throw new BaseException(ResultCode.DATA_WRONG);
            }
        }
        String format = uploadConfig.getMusicNameTemplate(req.getMusicName(),
                StringUtils.defaultString(req.getMusicAliasName()),
                req.getAlbumName(),
                req.getMusicArtist(),
                String.valueOf(byId.getRate()));
        // 删除与重命名文件相同的文件名
        File newFile = FileUtil.rename(destFile, format, true, true);
        
        String newName = UUID.fastUUID() + "." + extName;
        remoteStorageService.renameMusic(path, newName);
        try {
            String md5 = DigestUtil.md5Hex(newFile);
            byId.setMd5(md5);
            String upload = remoteStorageService.uploadAudioFile(newFile, md5);
            byId.setPath(upload);
            byId.setSize(FileUtil.size(newFile));
            resourceService.updateById(byId);
            log.debug("上传成功: {}", format);
            remoteStorageService.deleteMusic(newName);
            log.debug("清除oss文件缓存成功: {}", newName);
        } catch (Exception e) {
            remoteStorageService.renameMusic(newName, path);
            throw new BaseException(ResultCode.UPLOAD_ERROR, e);
        } finally {
            // 删除缓存
            FileUtil.del(newFile);
            log.debug("清除本地文件缓存成功: {}", newName);
        }
    }
}
