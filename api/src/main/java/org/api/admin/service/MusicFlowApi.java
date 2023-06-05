package org.api.admin.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.IterUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.api.admin.config.AdminConfig;
import org.api.admin.model.req.*;
import org.api.admin.model.res.AudioInfoRes;
import org.api.admin.model.res.MusicFileRes;
import org.api.admin.model.res.MusicInfoRes;
import org.api.common.service.QukuAPI;
import org.core.common.exception.BaseException;
import org.core.common.result.ResultCode;
import org.core.config.FileTypeConfig;
import org.core.config.LyricConfig;
import org.core.config.SaveConfig;
import org.core.iservice.*;
import org.core.model.convert.ArtistConvert;
import org.core.pojo.*;
import org.core.service.AccountService;
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
import org.oss.factory.OSSFactory;
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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service(AdminConfig.ADMIN + "MusicFlowApi")
@Slf4j
public class MusicFlowApi {
    /**
     * 音乐文件导入后缀名
     */
    @Autowired
    private FileTypeConfig fileType;
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
    @Autowired
    private TbLyricService lyricService;
    
    /**
     * 歌手服务
     */
    @Autowired
    private TbArtistService artistService;
    
    /**
     * 专辑表
     */
    @Autowired
    private TbAlbumService albumService;
    /**
     * 上传配置
     */
    @Autowired
    private SaveConfig config;
    
    @Autowired
    private TbAlbumArtistService albumSingerService;
    @Autowired
    private AccountService accountService;
    
    String pathTemp = FileUtil.getTmpDirPath() + FileUtil.FILE_SEPARATOR + "musicTemp";
    
    @Autowired
    private QukuAPI qukuService;
    
    @Autowired
    private TbMusicArtistService musicArtistService;
    
    @Autowired
    private TbPicService picService;
    
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
        if (StringUtils.isBlank(url)) {
            String md5 = DigestUtils.md5DigestAsHex(uploadFile.getBytes());
            // 上传文件
            String filename = uploadFile.getOriginalFilename();
            if (StringUtils.isBlank(filename)) {
                throw new BaseException(ResultCode.FILENAME_INVALID);
            }
            fileSuffix = LocalFileUtil.getFileSuffix(filename, fileType.getSuffix());
            path = checkFileMd5(md5, new File(pathTemp, md5 + "." + fileSuffix));
            // 本地没有则保存
            if (path == null) {
                String musicFileName = md5 + "." + fileSuffix;
                path = new File(pathTemp, musicFileName);
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
            String md5 = DigestUtils.md5DigestAsHex(bytes);
            File dest = new File(pathTemp, md5 + "." + fileSuffix);
            path = checkFileMd5(md5, dest);
            // 本地没有则保存
            if (path == null) {
                path = FileUtil.writeBytes(bytes, dest);
                audioInfoRes.setIsExist(false);
            }
            audioInfoRes.setIsExist(true);
        }
        AudioFile read;
        try {
            read = AudioFileIO.read(path);
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
        audioInfoRes.setSinger(Collections.singletonList(read.getTag().getFirst(FieldKey.ARTIST)));
        audioInfoRes.setAlbum(read.getTag().getFirst(FieldKey.ALBUM));
        audioInfoRes.setTimeLength(read.getAudioHeader().getTrackLength());
        audioInfoRes.setSize(read.getFile().length());
        audioInfoRes.setMusicFileTemp(path.getName());
        return audioInfoRes;
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
    
    private static File getMusicFile(AudioInfoReq dto, String path) throws IOException {
        // 检测本地是否有已上传的数据
        if (FileUtil.isFile(path)) {
            File file = new File(path);
            BufferedInputStream inputStream = FileUtil.getInputStream(file);
            String md5 = DigestUtils.md5DigestAsHex(inputStream);
            inputStream.close();
            if (StringUtils.equals(md5, dto.getMd5())) {
                return file;
            }
        }
        File file = new File(path);
        HttpUtil.downloadFile(dto.getMusicTemp(), file, 600000);
        BufferedInputStream inputStream = FileUtil.getInputStream(file);
        String md5 = DigestUtils.md5DigestAsHex(inputStream);
        inputStream.close();
        if (StringUtils.equals(md5, dto.getMd5())) {
            return file;
        }
        throw new BaseException(ResultCode.DOWNLOAD_ERROR);
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
        if (StringUtils.isNotBlank(dto.getLyric())) {
            TbLyricPojo one = lyricService.getOne(Wrappers.<TbLyricPojo>lambdaQuery()
                                                          .eq(TbLyricPojo::getMusicId, musicPojo.getId())
                                                          .eq(TbLyricPojo::getType, LyricConfig.LYRIC));
            TbLyricPojo entity = Optional.ofNullable(one).orElse(new TbLyricPojo());
            entity.setMusicId(musicPojo.getId());
            entity.setType(LyricConfig.LYRIC);
            entity.setLyric(dto.getLyric());
            list.add(entity);
        }
        if (StringUtils.isNotBlank(dto.getTLyric())) {
            TbLyricPojo one = lyricService.getOne(Wrappers.<TbLyricPojo>lambdaQuery()
                                                          .eq(TbLyricPojo::getMusicId, musicPojo.getId())
                                                          .eq(TbLyricPojo::getType, LyricConfig.T_LYRIC));
            TbLyricPojo entity = Optional.ofNullable(one).orElse(new TbLyricPojo());
            entity.setMusicId(musicPojo.getId());
            entity.setType(LyricConfig.T_LYRIC);
            entity.setLyric(dto.getTLyric());
            list.add(entity);
        }
        if (StringUtils.isNotBlank(dto.getKLyric())) {
            TbLyricPojo one = lyricService.getOne(Wrappers.<TbLyricPojo>lambdaQuery()
                                                          .eq(TbLyricPojo::getMusicId, musicPojo.getId())
                                                          .eq(TbLyricPojo::getType, LyricConfig.K_LYRIC));
            TbLyricPojo entity = Optional.ofNullable(one).orElse(new TbLyricPojo());
            entity.setMusicId(musicPojo.getId());
            entity.setType(LyricConfig.K_LYRIC);
            entity.setLyric(dto.getKLyric());
            list.add(entity);
        }
        lyricService.saveOrUpdateBatch(list);
        return list;
    }
    
    private static void getLevel(TbMusicUrlPojo entity, int rate) {
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
    
    /**
     * 上传文件
     *
     * @param dto       前端请求数据
     * @param musicPojo 音乐表数据
     */
    private TbMusicUrlPojo uploadFile(AudioInfoReq dto, TbMusicPojo musicPojo) {
        TbMusicUrlPojo urlPojo = musicUrlService.getOne(Wrappers.<TbMusicUrlPojo>lambdaQuery().eq(TbMusicUrlPojo::getMd5, dto.getMd5()));
        urlPojo = urlPojo == null ? new TbMusicUrlPojo() : urlPojo;
        urlPojo.setMusicId(musicPojo.getId());
        urlPojo.setOrigin(dto.getOrigin());
        Long userId = dto.getUserId() == null ? UserUtil.getUser().getId() : dto.getUserId();
        SysUserPojo byId = accountService.getById(userId);
        ExceptionUtil.isNull(byId == null, ResultCode.USER_NOT_EXIST);
        urlPojo.setUserId(byId.getId());
        // 上传到本地时读取本地文件数据，否则使用前端传入的数据
        if (Boolean.TRUE.equals(dto.getUploadFlag())) {
            if (dto.getSize() == null
                    || dto.getRate() == null
                    || dto.getRate() == 0
                    || StringUtils.isBlank(dto.getLevel())
                    || StringUtils.isBlank(dto.getMd5())
                    || StringUtils.isBlank(dto.getType())
                    || StringUtils.isBlank(dto.getMusicTemp())) {
                return new TbMusicUrlPojo();
            }
            // 只保存到数据库，不上传文件
            urlPojo.setSize(dto.getSize());
            urlPojo.setRate(dto.getRate());
            urlPojo.setLevel(dto.getLevel());
            urlPojo.setMd5(dto.getMd5());
            urlPojo.setEncodeType(dto.getType());
            urlPojo.setUrl(dto.getMusicTemp());
        } else {
            uploadLocalFile(dto, urlPojo);
        }
        // 检测是否重复上传相同的MD5音乐文件
        if (urlPojo.getId() == null) {
            TbMusicUrlPojo one = musicUrlService.getOne(Wrappers.<TbMusicUrlPojo>lambdaQuery().eq(TbMusicUrlPojo::getMd5, urlPojo.getMd5()));
            ExceptionUtil.isNull(one != null, ResultCode.OSS_MD5_REPEAT);
        }
        musicUrlService.saveOrUpdate(urlPojo);
        return urlPojo;
    }
    
    private void uploadLocalFile(AudioInfoReq dto, TbMusicUrlPojo urlPojo) {
        File file = null;
        String md5 = "";
        String uploadPath;
        long size;
        try {
            if (StringUtils.startsWithIgnoreCase(dto.getMusicTemp(), "http")) {
                // 没有md5，取文件的md5
                if (StringUtils.isBlank(dto.getMd5())) {
                    File file1 = new File(pathTemp);
                    HttpUtil.downloadFile(dto.getMusicTemp(), file1, 600000);
                    File[] files = Objects.requireNonNull(file1.listFiles());
                    File lastFile = StringUtils.isBlank(FileUtil.getSuffix(files[files.length - 1])) ? null : files[files.length - 1];
                    ExceptionUtil.isNull(lastFile == null, ResultCode.DOWNLOAD_ERROR);
                    BufferedInputStream inputStream = FileUtil.getInputStream(lastFile);
                    md5 = DigestUtils.md5DigestAsHex(inputStream);
                    inputStream.close();
                    file = FileUtil.rename(lastFile, md5, true, true);
                } else {
                    String pathname = pathTemp + FileUtil.FILE_SEPARATOR + dto.getMd5() + "." + dto.getType();
                    file = getMusicFile(dto, pathname);
                    file = FileUtil.rename(file, dto.getMd5(), true, true);
                    md5 = dto.getMd5();
                }
            } else {
                // 读取本地文件
                file = new File(pathTemp, dto.getMusicTemp());
            }
            uploadPath = OSSFactory.ossFactory(config).upload(config.getObjectSave(), config.getAssignObjectSave(), file, md5);
            size = FileUtil.size(file);
            FileUtil.del(file);
        } catch (Exception e) {
            FileUtil.del(file);
            throw new BaseException(null, e.getMessage(), e.getCause());
        }
        // music URL 地址表
        urlPojo.setSize(size);
        urlPojo.setRate(dto.getRate());
        urlPojo.setLevel(dto.getLevel());
        urlPojo.setMd5(md5);
        urlPojo.setEncodeType(FileUtil.extName(file));
        urlPojo.setUrl(uploadPath);
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
        String aliaNames = CollUtil.join(dto.getAliaName(), ",");
        // 如果有ID则直接更新
        if (dto.getId() != null) {
            TbMusicPojo musicPojo = musicService.getById(dto.getId());
            return saveMusicInfoTable(dto, albumPojo, musicPojo, aliaNames);
        }
        // 判断歌手ID是否有值
        boolean singerFlag = IterUtil.isNotEmpty(artistIds);
        // 判断专辑数据是否有值
        boolean albumFlag = albumPojo != null && albumPojo.getId() != null;
        if (singerFlag && albumFlag) {
            // 获取数据库中音乐数据
            TbMusicPojo musicPojo = musicService.getOne(Wrappers.<TbMusicPojo>lambdaQuery()
                                                                .eq(TbMusicPojo::getAlbumId, albumPojo.getId())
                                                                .eq(TbMusicPojo::getMusicName, dto.getMusicName()));
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
        musicPojoLambdaQueryWrapper.eq(StringUtils.isNotBlank(dto.getMusicName()), TbMusicPojo::getMusicName, dto.getMusicName());
        String join = StringUtils.join(dto.getAliaName(), ",");
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
                                                           .eq(TbMusicPojo::getMusicName, dto.getMusicName()));
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
                            StringUtils.equals(tbMusicPojo.getMusicName(), dto.getMusicName()) && Objects.equals(tbMusicPojo.getAlbumId(),
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
        tbMusicPojo.setMusicName(dto.getMusicName());
        tbMusicPojo.setAliasName(aliaNames);
        tbMusicPojo.setPicId(qukuService.saveOrUpdatePic(dto.getPic()).getId());
        tbMusicPojo.setAlbumId(albumPojo == null ? null : albumPojo.getId());
        tbMusicPojo.setTimeLength(dto.getTimeLength());
        boolean save;
        // 保存音乐表
        save = musicService.saveOrUpdate(tbMusicPojo);
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
        if (dto.getAlbum() == null || StringUtils.isBlank(dto.getAlbum().getAlbumName())) {
            return new TbAlbumPojo();
        }
        // 如果是数据库中已有数据，直接更新
        Long albumId = dto.getAlbum().getId();
        if (albumId != null) {
            AlbumReq album = dto.getAlbum();
            album.setId(albumId);
            TbPicPojo tbPicPojo = qukuService.saveOrUpdatePic(dto.getAlbum().getPic());
            album.setPicId(tbPicPojo.getId());
            albumService.updateById(album);
            return album;
        }
        // 查询专辑名在数据库中是否存在专辑
        List<TbAlbumPojo> list = albumService.list(Wrappers.<TbAlbumPojo>lambdaQuery()
                                                           .eq(TbAlbumPojo::getAlbumName, dto.getAlbum()
                                                                                             .getAlbumName()));
        // 获取所有专辑ID
        List<Long> albumList = list.stream().map(TbAlbumPojo::getId).collect(Collectors.toList());
        List<Long> albumIds = new ArrayList<>();
        // 根据歌手和专辑ID查找关联ID，如果有则更新专辑表。
        if (IterUtil.isNotEmpty(albumList) && IterUtil.isNotEmpty(singerIds)) {
            // 专辑表找到后，在中间表同时满足专辑ID和歌手ID两个列表，只找到同一个专辑ID
            List<TbAlbumArtistPojo> tbAlbumArtistPojoList = albumSingerService.list(Wrappers.<TbAlbumArtistPojo>lambdaQuery()
                                                                                            .in(TbAlbumArtistPojo::getArtistId, singerIds)
                                                                                            .in(TbAlbumArtistPojo::getAlbumId, albumList));
            albumIds = tbAlbumArtistPojoList.stream().map(TbAlbumArtistPojo::getAlbumId).collect(Collectors.toList());
        }
        List<Long> distinct = CollUtil.distinct(albumIds);
        ExceptionUtil.isNull(distinct.size() > 1, ResultCode.ALBUM_NOT_EXIST);
        TbAlbumPojo albumPojo = new TbAlbumPojo();
        // 数据库中有数据，并且保证数据中的专辑ID和根据专辑名查询出来的ID相同，否则直接抛出异常
        if (distinct.size() == 1) {
            // 获取关联的专辑表ID
            Long id = distinct.get(0);
            // 获取关联的专辑表数据
            Optional<TbAlbumPojo> first = list.stream()
                                              .filter(tbAlbumPojo -> Objects.equals(tbAlbumPojo.getId(), id))
                                              .findFirst();
            albumPojo = first.orElseThrow(() -> new BaseException(ResultCode.ALBUM_ERROR));
            BeanUtils.copyProperties(dto.getAlbum(), albumPojo, "id", "updateTime", "createTime");
            // 更新或新增封面
            albumPojo.setPicId(qukuService.saveOrUpdatePic(dto.getAlbum().getPic()).getId());
            // 更新专辑表
            albumService.updateById(albumPojo);
            return albumPojo;
        }
        // 如果没有歌手数据则只新增专辑表
        if (distinct.isEmpty()) {
            albumPojo = new TbAlbumPojo();
            BeanUtils.copyProperties(dto.getAlbum(), albumPojo);
            // 更新或新增封面
            albumPojo.setPicId(qukuService.saveOrUpdatePic(dto.getAlbum().getPic()).getId());
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
        List<ArtistReq> artists = dto.getArtists();
        if (IterUtil.isEmpty(artists) || StringUtils.isBlank(artists.get(0).getArtistName())) {
            return Collections.emptyList();
        }
        // 有歌手，数据库中有，查询出歌手和音乐主键ID
        // 获取前端传入所有歌手
        List<String> singerNameList = artists
                .stream()
                .map(TbArtistPojo::getArtistName)
                .collect(Collectors.toList());
    
        // 查询数据库中歌手
        List<TbArtistPojo> singList = artistService.list(Wrappers.<TbArtistPojo>lambdaQuery()
                                                                 .in(TbArtistPojo::getArtistName, singerNameList));
    
    
        List<TbArtistPojo> saveBatch = new ArrayList<>();
        // 遍历前端传入的所有歌手信息, 与前端进行比较，歌手名相同的则更新数据库。没有歌手就添加到数据库。注意这个更新条件是根据歌手名来更新的
        for (ArtistReq singerReq : artists) {
            TbArtistPojo pojo = new TbArtistPojo();
            BeanUtils.copyProperties(singerReq, pojo);
            // 覆盖数据中的数据，防止出现脏数据(多个相同的歌手)
            for (TbArtistPojo tbArtistPojo : singList) {
                if (StringUtils.equalsIgnoreCase(singerReq.getArtistName(), tbArtistPojo.getArtistName())) {
                    // 歌曲家名字相同，就把数据库中的ID拷贝到前端传入的数据中，直接更新数据库
                    pojo.setId(tbArtistPojo.getId());
                }
            }
            // 更新封面
            pojo.setPicId(qukuService.saveOrUpdatePic(singerReq.getPic()).getId());
            saveBatch.add(pojo);
        }
        artistService.saveOrUpdateBatch(saveBatch);
        return saveBatch;
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
        List<TbMusicUrlPojo> list = musicUrlService.list(Wrappers.<TbMusicUrlPojo>lambdaQuery().in(TbMusicUrlPojo::getMusicId, musicIds));
        for (TbMusicUrlPojo tbMusicUrlPojo : list) {
            MusicFileRes musicFileRes = new MusicFileRes();
            try {
                String address = tbMusicUrlPojo.getUrl();
                TbMusicUrlPojo url = qukuService.getMusicUrlByMusicUrlList(tbMusicUrlPojo, refresh);
                BeanUtils.copyProperties(url, musicFileRes);
                musicFileRes.setUrl(address);
                musicFileRes.setSize(tbMusicUrlPojo.getSize());
                musicFileRes.setLevel(tbMusicUrlPojo.getLevel());
                musicFileRes.setMd5(tbMusicUrlPojo.getMd5());
                musicFileRes.setRawUrl(url.getUrl());
                musicFileRes.setExists(StringUtils.isNotBlank(url.getUrl()));
            } catch (BaseException e) {
                if (StringUtils.equals(e.getErrorCode(), ResultCode.OSS_LOGIN_ERROR.getCode())) {
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
    
    public List<TbLyricPojo> getMusicLyric(Long musicId) {
        return qukuService.getMusicLyric(musicId);
    }
    
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
     * 更新音乐信息
     *
     * @param req 信息
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateMusic(MusicInfoReq req) {
        TbMusicPojo entity = new TbMusicPojo();
        entity.setId(req.getId());
        entity.setMusicName(req.getMusicName());
        entity.setAliasName(req.getMusicNameAlias());
        entity.setAlbumId(req.getAlbumId());
        entity.setTimeLength(req.getTimeLength());
        // 更新封面
        entity.setPicId(qukuService.saveOrUpdatePic(req.getPic()).getId());
    
        TbMusicPojo musicPojo = musicService.getById(req.getId());
        // 删除歌曲关联歌手
        musicArtistService.remove(Wrappers.<TbMusicArtistPojo>lambdaQuery().eq(TbMusicArtistPojo::getMusicId, musicPojo.getId()));
        ArrayList<TbMusicArtistPojo> musicArtistList = new ArrayList<>();
        for (TbArtistPojo tbArtistPojo : req.getMusicArtist()) {
            TbMusicArtistPojo e = new TbMusicArtistPojo();
            e.setArtistId(tbArtistPojo.getId());
            e.setMusicId(req.getId());
            musicArtistList.add(e);
        }
        // 重新保存
        musicArtistService.saveBatch(musicArtistList);
    
        // 删除专辑原来关联的歌手
        albumSingerService.remove(Wrappers.<TbAlbumArtistPojo>lambdaQuery()
                                          .eq(TbAlbumArtistPojo::getAlbumId, musicPojo.getAlbumId()));
        // 重新添加关联歌手
        ArrayList<TbAlbumArtistPojo> entityList = new ArrayList<>();
        for (TbArtistPojo tbArtistPojo : req.getAlbumArtist()) {
            TbAlbumArtistPojo tbAlbumArtistPojo = new TbAlbumArtistPojo();
            tbAlbumArtistPojo.setAlbumId(req.getAlbumId());
            tbAlbumArtistPojo.setArtistId(tbArtistPojo.getId());
            entityList.add(tbAlbumArtistPojo);
        }
        // 重新保存
        albumSingerService.saveBatch(entityList);
    
        // 更新专辑
        TbAlbumPojo entity1 = new TbAlbumPojo();
        entity1.setId(req.getAlbumId());
        entity1.setPublishTime(req.getPublishTime());
        albumService.updateById(entity1);
        // 音乐
        musicService.updateById(entity);
    }
    
    public MusicInfoRes getMusicInfo(Long id) {
        // 音乐
        TbMusicPojo byId = musicService.getById(id);
        // 专辑艺术家
        List<ArtistConvert> artistListByAlbumIds = qukuService.getAlbumArtistListByAlbumIds(byId.getAlbumId());
        // 歌曲艺术家
        List<ArtistConvert> musicArtistByMusicId = qukuService.getMusicArtistByMusicId(id);
        // 专辑
        TbAlbumPojo albumPojo = albumService.getById(byId.getAlbumId());
    
        MusicInfoRes musicInfoRes = new MusicInfoRes();
        musicInfoRes.setMusicArtist(musicArtistByMusicId);
        musicInfoRes.setAlbumArtist(artistListByAlbumIds);
        musicInfoRes.setAlbumName(albumPojo.getAlbumName());
        musicInfoRes.setAlbumId(albumPojo.getId());
        BeanUtils.copyProperties(byId, musicInfoRes);
        musicInfoRes.setPic(qukuService.getPicUrl(byId.getPicId()));
        musicInfoRes.setPublishTime(albumPojo.getPublishTime());
        return musicInfoRes;
    }
    
    /**
     * TODO: 保存时简繁比较
     * 保存音乐
     * 更新表: 音乐信息表  歌手表 专辑表
     * 如果上传文件更新: 音乐地址表
     *
     * @param dto 音乐信息
     */
    // @Transactional(rollbackFor = Exception.class)
    public MusicDetails saveMusicInfo(AudioInfoReq dto) {
        // 检测是读取本地缓存文件， 并且是本地地址
        if (Boolean.FALSE.equals(dto.getUploadFlag()) && !StringUtils.startsWithIgnoreCase(dto.getMusicTemp(), "http")) {
            // 检查文件目录是否合法
            LocalFileUtil.checkFilePath(pathTemp, dto.getMusicTemp());
        }
        // 保存专辑歌手中间表
        List<TbArtistPojo> albumArtist = saveAndReturnMusicAndSingList(dto);
        Set<Long> artistIds = albumArtist.stream().map(TbArtistPojo::getId).collect(Collectors.toSet());
        
        // 保存专辑表，如果没有则新建。
        TbAlbumPojo albumPojo = saveAndReturnAlbumPojo(dto, artistIds);
        // 保存音乐表
        TbMusicPojo musicPojo = saveAndReturnMusicPojo(dto, artistIds, albumPojo);
        // 保存歌手表
        saveRandReturnArtistList(artistIds, musicPojo);
        // 保存歌词
        List<TbLyricPojo> lyricPojoList = saveLyric(dto, musicPojo);
        // 上传文件
        TbMusicUrlPojo tbMusicUrlPojo = uploadFile(dto, musicPojo);
        
        MusicDetails musicDetails = new MusicDetails();
        musicDetails.setMusic(musicPojo);
        musicDetails.setMusicUrl(tbMusicUrlPojo);
        musicDetails.setAlbum(albumPojo);
        musicDetails.setSinger(albumArtist);
        musicDetails.setLyrics(lyricPojoList);
        return musicDetails;
    }
    
    public void uploadAutoMusic(Long userId, MultipartFile uploadFile, Long musicId) {
        TbMusicUrlPojo entity = new TbMusicUrlPojo();
        entity.setMusicId(musicId);
        userId = userId == null ? UserUtil.getUser().getId() : userId;
        try {
            String tempMd5 = DigestUtils.md5DigestAsHex(uploadFile.getBytes());
            TbMusicUrlPojo tempOne = musicUrlService.getOne(Wrappers.<TbMusicUrlPojo>lambdaQuery().eq(TbMusicUrlPojo::getMd5, tempMd5));
            // 如果上传的歌曲MD5值在数据中，并且与存储的歌曲ID不符合则不上传
            if (tempOne != null && !Objects.equals(tempOne.getMusicId(), musicId)) {
                throw new BaseException(ResultCode.UPLOAD_MUSIC_ID_NOT_MATCH.getCode(),
                        ResultCode.UPLOAD_MUSIC_ID_NOT_MATCH.getResultMsg() + ", 歌曲ID: " + tempOne.getMusicId());
            }
            String[] nameArr = StringUtils.split(uploadFile.getOriginalFilename(), ".");
            if (nameArr == null || nameArr.length < 1) {
                throw new BaseException(ResultCode.FILENAME_INVALID);
            }
            File dest = new File(FileUtil.getTmpDir() + FileUtil.FILE_SEPARATOR + "temp" + FileUtil.FILE_SEPARATOR + tempMd5 + "." + nameArr[1]);
            FileUtil.writeBytes(uploadFile.getBytes(), dest);
            AudioFile audioInfo = getAudioInfo(dest);
            String upload = OSSFactory.ossFactory(config).upload(config.getObjectSave(), config.getAssignObjectSave(), dest, tempMd5);
            TbMusicUrlPojo one = musicUrlService.getOne(Wrappers.<TbMusicUrlPojo>lambdaQuery().eq(TbMusicUrlPojo::getMd5, tempMd5));
            int rate = Math.toIntExact(audioInfo.getAudioHeader().getBitRateAsNumber());
            rate = rate * 1000;
            if (one == null) {
                entity.setRate(rate);
                getLevel(entity, rate);
                entity.setUrl(upload);
                entity.setUserId(userId);
                entity.setEncodeType(nameArr[1]);
                entity.setOrigin("auto");
                entity.setSize(FileUtil.size(dest));
                entity.setMd5(tempMd5);
                musicUrlService.save(entity);
                log.info("文件保存到数据库{}", entity);
            } else {
                one.setRate(rate);
                getLevel(one, rate);
                one.setUrl(upload);
                one.setSize(FileUtil.size(dest));
                one.setEncodeType(nameArr[1]);
                musicUrlService.updateById(one);
                log.info("更新成功{}", entity);
            }
            FileUtil.del(dest);
        } catch (IOException e) {
            throw new BaseException(ResultCode.UPLOAD_ERROR);
        }
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
    
    public void uploadManualMusic(UploadMusicReq musicSource) {
        TbMusicUrlPojo one = musicUrlService.getOne(Wrappers.<TbMusicUrlPojo>lambdaQuery().eq(TbMusicUrlPojo::getMd5, musicSource.getMd5()));
        String[] nameArr = StringUtils.split(musicSource.getName(), ".");
        if (nameArr.length == 1) {
            throw new BaseException(ResultCode.FILENAME_INVALID);
        }
        if (one == null) {
            TbMusicUrlPojo entity = new TbMusicUrlPojo();
            getLevel(entity, musicSource.getRate());
            entity.setRate(musicSource.getRate());
            entity.setUserId(musicSource.getUserId());
            entity.setEncodeType(nameArr[1]);
            entity.setOrigin("manual");
            entity.setMd5(musicSource.getMd5());
            entity.setSize(musicSource.getSize());
            entity.setUrl(musicSource.getName());
            entity.setMusicId(musicSource.getMusicId());
            musicUrlService.save(entity);
            qukuService.getMusicUrlByMusicId(entity.getMusicId(), true);
        } else {
            throw new BaseException(ResultCode.SONG_UPLOADED);
        }
    }
    
    public void updateSource(TbMusicUrlPojo source) {
        musicUrlService.updateById(source);
    }
    
    public void deleteSource(Long id) {
        musicUrlService.removeById(id);
    }
}
