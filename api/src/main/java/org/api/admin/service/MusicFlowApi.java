package org.api.admin.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.IterUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.api.admin.config.AdminConfig;
import org.api.admin.model.req.ArtistReq;
import org.api.admin.model.req.AudioInfoReq;
import org.api.admin.model.res.AudioInfoRes;
import org.api.admin.model.res.MusicFileRes;
import org.core.common.exception.BaseException;
import org.core.common.page.LambdaQueryWrapper;
import org.core.common.page.Wrappers;
import org.core.common.result.ResultCode;
import org.core.config.FileTypeConfig;
import org.core.config.SaveConfig;
import org.core.iservice.*;
import org.core.pojo.*;
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
import org.oss.factory.OSSFactory;
import org.oss.service.OSSService;
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
    private MusicService musicService;
    /**
     * 音乐保存数据链接表
     */
    @Autowired
    private MusicUrlService musicUrlService;
    
    /**
     * 歌手服务
     */
    @Autowired
    private ArtistService artistService;
    
    /**
     * 专辑表
     */
    @Autowired
    private AlbumService albumService;
    /**
     * 上传配置
     */
    @Autowired
    private SaveConfig config;
    
    @Autowired
    private AlbumArtistService albumSingerService;
    
    String pathTemp = FileUtil.getTmpDirPath() + "\\musicTemp";
    
    private static final Object lock = new Object();
    
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
        long count = musicUrlService.count(Wrappers.<MusicUrlPojo>lambdaQuery().eq(MusicUrlPojo::getMd5, md5));
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
    public MusicDetails saveMusicInfo(AudioInfoReq dto) throws IOException {
        // 检测是读取本地缓存文件
        if (Boolean.FALSE.equals(dto.getUploadFlag())) {
            // 检查文件目录是否合法
            LocalFileUtil.checkFilePath(pathTemp, dto.getMusicTemp());
        }
        // 保存歌手和音乐中间表
        List<ArtistPojo> tbArtistPojos = saveAndReturnMusicAndSingList(dto);
        Set<Long> singIds = tbArtistPojos.stream().map(ArtistPojo::getId).collect(Collectors.toSet());
        
        // 保存专辑表，如果没有则新建。
        AlbumPojo albumPojo = saveAndReturnAlbumPojo(dto, singIds);
        // 保存音乐表
        MusicPojo musicPojo = saveAndReturnMusicPojo(dto, singIds, albumPojo);
        // 上传文件
        MusicUrlPojo tbMusicUrlPojo = uploadFile(dto, musicPojo);
        
        MusicDetails musicDetails = new MusicDetails();
        musicDetails.setMusic(musicPojo);
        musicDetails.setMusicUrl(tbMusicUrlPojo);
        musicDetails.setAlbum(albumPojo);
        musicDetails.setSinger(tbArtistPojos);
        return musicDetails;
    }
    
    
    /**
     * 上传文件
     *
     * @param dto       前端请求数据
     * @param musicPojo 音乐表数据
     * @throws IOException 文件读取异常
     */
    private MusicUrlPojo uploadFile(AudioInfoReq dto, MusicPojo musicPojo) throws IOException {
        MusicUrlPojo urlPojo = musicUrlService.getOne(Wrappers.<MusicUrlPojo>lambdaQuery().eq(MusicUrlPojo::getMd5, dto.getMd5())).orElse(new MusicUrlPojo());
        urlPojo.setMusicId(musicPojo.getId());
        urlPojo.setOrigin(dto.getOrigin());
        urlPojo.setUserId(dto.getUserId() == null ? UserUtil.getUser().getId() : dto.getUserId());
        // 上传到本地时读取本地文件数据，否则使用前端传入的数据
        if (Boolean.TRUE.equals(dto.getUploadFlag())) {
            if (dto.getSize() == null || dto.getRate() == null || dto.getRate() == 0 || StringUtils.isBlank(dto.getLevel()) || StringUtils.isBlank(dto.getMd5()) || StringUtils.isBlank(
                    dto.getType()) || StringUtils.isBlank(dto.getMusicTemp())) {
                return new MusicUrlPojo();
            }
            // 只保存到数据库，不上传文件
            urlPojo.setSize(dto.getSize());
            urlPojo.setRate(dto.getRate());
            urlPojo.setLevel(dto.getLevel());
            urlPojo.setMd5(dto.getMd5());
            urlPojo.setEncodeType(dto.getType());
            urlPojo.setUrl(dto.getMusicTemp());
        } else {
            // 读取本地文件
            File file = new File(pathTemp, dto.getMusicTemp());
            String uploadPath = OSSFactory.ossFactory(config.getSaveMode()).upload(config.getObjectSave(), file.getPath());
            Files.delete(file.toPath());
            // music URL 地址表
            urlPojo.setSize(FileUtil.size(file));
            urlPojo.setRate(dto.getRate());
            urlPojo.setLevel(dto.getLevel());
            urlPojo.setMd5(dto.getMd5());
            urlPojo.setEncodeType(FileUtil.extName(file));
            urlPojo.setUrl(uploadPath);
        }
        musicUrlService.saveOrUpdate(urlPojo);
        return urlPojo;
    }
    
    /**
     * 保存音乐数据
     * 有音乐ID直接保存
     * 有关联专辑信息，歌手，查询是否有已存在歌曲
     * 没有任何关联信息，直接保存
     *
     * @param dto       前端请求数据
     * @param singerIds 歌手ID
     * @param albumPojo 专辑数据
     * @return 音乐信息
     */
    @NotNull
    private MusicPojo saveAndReturnMusicPojo(AudioInfoReq dto, Set<Long> singerIds, AlbumPojo albumPojo) {
        String aliaNames = CollUtil.join(dto.getAliaName(), ",");
        // 如果有ID则直接更新
        if (dto.getId() != null) {
            MusicPojo musicPojo = musicService.getById(dto.getId());
            return saveMusicInfoTable(dto, albumPojo, musicPojo, aliaNames);
        }
        // 判断歌手ID是否有值
        boolean singerFlag = IterUtil.isNotEmpty(singerIds);
        // 判断专辑数据是否有值
        boolean albumFlag = albumPojo != null && albumPojo.getId() != null;
        if (singerFlag && albumFlag) {
            // 获取数据库中音乐数据
            Optional<MusicPojo> musicPojo = musicService.getOne(Wrappers.<MusicPojo>lambdaQuery()
                                                                  .eq(MusicPojo::getAlbumId, albumPojo.getId())
                                                                  .eq(MusicPojo::getMusicName, dto.getMusicName()));
            // 通过专辑查询出来音乐数据ID存在歌手中间中，直接返回
            if (musicPojo.isPresent()) {
                return saveMusicInfoTable(dto, albumPojo, musicPojo.get(), aliaNames);
            }
        }
        
        LambdaQueryWrapper<MusicPojo> musicPojoLambdaQueryWrapper = Wrappers.lambdaQuery();
        musicPojoLambdaQueryWrapper.eq(StringUtils.isNotBlank(dto.getMusicName()), MusicPojo::getMusicName, dto.getMusicName());
        String join = StringUtils.join(dto.getAliaName(), ",");
        musicPojoLambdaQueryWrapper.eq(StringUtils.isNotBlank(join), MusicPojo::getAliasName, join);
        musicPojoLambdaQueryWrapper.eq(StringUtils.isNotBlank(dto.getPic()), MusicPojo::getPic, dto.getPic());
        
        musicPojoLambdaQueryWrapper.eq(StringUtils.isNotBlank(dto.getLyric()), MusicPojo::getLyric, dto.getLyric());
        musicPojoLambdaQueryWrapper.eq(StringUtils.isNotBlank(dto.getKLyric()), MusicPojo::getkLyric, dto.getKLyric());
        MusicPojo one = musicService.getOne(musicPojoLambdaQueryWrapper).orElse(new MusicPojo());
        return saveMusicInfoTable(dto, albumPojo, one, aliaNames);
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
    private MusicPojo saveMusicInfoTable(AudioInfoReq dto, AlbumPojo albumPojo, MusicPojo musicPojo, String aliaNames) {
        MusicPojo tbMusicPojo = musicPojo == null ? new MusicPojo() : musicPojo;
        synchronized (lock) {
            long sort = musicService.count() + 1;
            if (tbMusicPojo.getSort() == null) {
                tbMusicPojo.setSort(sort);
            }
        }
        // music 信息表
        tbMusicPojo.setMusicName(dto.getMusicName());
        tbMusicPojo.setAliasName(aliaNames);
        tbMusicPojo.setPic(dto.getPic());
        tbMusicPojo.setLyric(dto.getLyric());
        tbMusicPojo.setkLyric(dto.getKLyric());
        tbMusicPojo.setAlbumId(albumPojo == null ? null : albumPojo.getId());
        tbMusicPojo.setTimeLength(dto.getTimeLength());
        // 保存音乐表
        MusicPojo pojo = musicService.saveOrUpdate(tbMusicPojo);
        // 保存错误，抛出异常
        ExceptionUtil.isNull(pojo == null, ResultCode.SAVE_FAIL);
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
    private AlbumPojo saveAndReturnAlbumPojo(AudioInfoReq dto, Set<Long> singerIds) {
        // 专辑没有值直接返回
        if (dto.getAlbum() == null || StringUtils.isBlank(dto.getAlbum().getAlbumName())) {
            return new AlbumPojo();
        }
        // 如果是数据库中已有数据，直接更新
        Long albumId = dto.getAlbum().getId();
        if (albumId != null) {
            AlbumPojo byId = albumService.getById(albumId);
            ExceptionUtil.isNull(byId == null, ResultCode.ALBUM_NOT_EXIST);
            return byId;
        }
        // 查询专辑名在数据库中是否存在专辑
        List<AlbumPojo> list = albumService.list(Wrappers.<AlbumPojo>lambdaQuery().eq(AlbumPojo::getAlbumName, dto.getAlbum().getAlbumName()));
        // 获取所有专辑ID
        List<Long> albumList = list.stream().map(AlbumPojo::getId).collect(Collectors.toList());
        List<Long> albumIds = new ArrayList<>();
        // 根据歌手和专辑ID查找关联ID，如果有则更新专辑表。
        if (IterUtil.isNotEmpty(albumList) && IterUtil.isNotEmpty(singerIds)) {
            // 专辑表找到后，在中间表同时满足专辑ID和歌手ID两个列表，只找到同一个专辑ID
            List<AlbumArtistPojo> tbAlbumArtistPojoList = albumSingerService.list(Wrappers.<AlbumArtistPojo>lambdaQuery()
                                                                                            .in(AlbumArtistPojo::getArtistId, singerIds)
                                                                                            .in(AlbumArtistPojo::getAlbumId, albumList));
            albumIds = tbAlbumArtistPojoList.stream().map(AlbumArtistPojo::getAlbumId).collect(Collectors.toList());
        }
        List<Long> distinct = CollUtil.distinct(albumIds);
        ExceptionUtil.isNull(distinct.size() > 1, ResultCode.ALBUM_NOT_EXIST);
        AlbumPojo albumPojo = new AlbumPojo();
        // 有一个更新数据库，并且保证数据中的专辑ID和根据专辑名查询出来的ID相同，否则不更新,直接新增专辑数据
        if (distinct.size() == 1) {
            // 获取关联的专辑表ID
            Long id = distinct.get(0);
            // 获取关联的专辑表数据
            Optional<AlbumPojo> first = list.stream().filter(tbAlbumPojo -> Objects.equals(tbAlbumPojo.getId(), id)).findFirst();
            albumPojo = first.orElseThrow(() -> new BaseException(ResultCode.ALBUM_ERROR));
            BeanUtils.copyProperties(dto.getAlbum(), albumPojo, "id", "updateTime", "createTime");
            // 更新专辑表
            albumService.updateById(albumPojo);
        }
        // 如果没有歌手数据则新增专辑表
        // 默认新增歌手为歌曲歌手
        if (distinct.isEmpty()) {
            albumPojo = new AlbumPojo();
            BeanUtils.copyProperties(dto.getAlbum(), albumPojo);
            albumService.saveOrUpdate(albumPojo);
            // 更新歌手和专辑中间表
            if (IterUtil.isNotEmpty(singerIds)) {
                List<AlbumArtistPojo> albumSingerPojoArrayList = new ArrayList<>();
                for (Long id : singerIds) {
                    AlbumArtistPojo e = new AlbumArtistPojo();
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
     * 前端传入歌手ID直接查询，并返回
     * 前端传入数据有歌手，数据库中有，查询出歌手和音乐主键ID
     * 前端传入数据有歌手，数据库中没有，添加歌手数据并返回歌手存入数据
     * 前端有数据，数据库中也有，但是数据库中一半有歌手，一半没有歌手。则更新数据
     *
     * @param dto 前端请求数据
     * @return 返回音乐和歌手ID
     */
    private List<ArtistPojo> saveAndReturnMusicAndSingList(AudioInfoReq dto) {
        // 没有歌手直接返回
        if (IterUtil.isEmpty(dto.getSinger()) || StringUtils.isBlank(dto.getSinger().get(0).getArtistName())) {
            return Collections.emptyList();
        }
        // 有歌手ID直接返回数据
        if (CollUtil.isNotEmpty(dto.getSinger()) && dto.getSinger().get(0) != null && dto.getSinger().get(0).getId() != null) {
            List<Long> singerIds = dto.getSinger().stream().map(ArtistPojo::getId).filter(Objects::isNull).collect(Collectors.toList());
            return artistService.listByIds(singerIds);
        }
        // 有歌手，数据库中有，查询出歌手和音乐主键ID
        // 获取前端传入所有歌手
        List<String> singerNameList = dto.getSinger().stream().map(ArtistPojo::getArtistName).collect(Collectors.toList());
        
        // 查询数据库中歌手
        List<ArtistPojo> singList = artistService.list(Wrappers.<ArtistPojo>lambdaQuery().in(ArtistPojo::getArtistName, singerNameList));
        
        
        List<ArtistPojo> saveBatch = new ArrayList<>();
        // 遍历前端传入的所有歌手信息, 与前端进行比较，歌手名相同的则更新数据库。没有歌手就添加到数据库。注意这个更新条件是根据歌手名来更新的
        List<ArtistReq> singerReqList = dto.getSinger();
        for (ArtistReq singerReq : singerReqList) {
            ArtistPojo pojo = new ArtistPojo();
            BeanUtils.copyProperties(singerReq, pojo);
            // 覆盖数据中的数据，防止出现脏数据(多个相同的歌手)
            for (ArtistPojo tbArtistPojo : singList) {
                if (StringUtils.equalsIgnoreCase(singerReq.getArtistName(), tbArtistPojo.getArtistName())) {
                    // 歌曲家名字相同，就把数据库中的ID拷贝到前端传入的数据中，直接更新数据库
                    pojo.setId(tbArtistPojo.getId());
                }
            }
            saveBatch.add(pojo);
        }
        artistService.saveOrUpdateBatch(saveBatch);
        return saveBatch;
    }
    
    
    /**
     * 查询音乐URL表
     *
     * @param musicIds 音乐id
     * @return 音乐URL列表
     */
    public List<MusicFileRes> getMusicUrl(Set<String> musicIds) {
        List<MusicFileRes> files = new ArrayList<>();
        List<MusicUrlPojo> list = musicUrlService.list(Wrappers.<MusicUrlPojo>lambdaQuery().in(MusicUrlPojo::getMusicId, musicIds));
        for (MusicUrlPojo tbMusicUrlPojo : list) {
            MusicFileRes musicFileRes = new MusicFileRes();
            try {
                OSSService aList = OSSFactory.ossFactory("AList");
                String musicAddresses = aList.getMusicAddresses(config.getHost(),
                        config.getObjectSave(),
                        tbMusicUrlPojo.getMd5() + "." + tbMusicUrlPojo.getEncodeType());
                musicFileRes.setId(String.valueOf(tbMusicUrlPojo.getMusicId()));
                musicFileRes.setSize(tbMusicUrlPojo.getSize());
                musicFileRes.setLevel(tbMusicUrlPojo.getLevel());
                musicFileRes.setMd5(tbMusicUrlPojo.getMd5());
                musicFileRes.setRawUrl(musicAddresses);
                musicFileRes.setExists(true);
            } catch (Exception e) {
                musicFileRes.setRawUrl("");
                musicFileRes.setExists(false);
                continue;
            }
            files.add(musicFileRes);
        }
        return files;
    }
}
