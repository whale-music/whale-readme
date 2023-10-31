package org.api.admin.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.map.MapUtil;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.metadata.WriteSheet;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.map.DefaultedMap;
import org.apache.commons.lang3.StringUtils;
import org.api.admin.config.AdminConfig;
import org.api.admin.model.req.upload.AudioInfoReq;
import org.api.admin.model.res.ExportExcelRes;
import org.api.common.service.QukuAPI;
import org.core.common.constant.LyricConstant;
import org.core.common.exception.BaseException;
import org.core.common.result.ResultCode;
import org.core.config.HttpRequestConfig;
import org.core.jpa.entity.*;
import org.core.jpa.repository.TbAlbumEntityRepository;
import org.core.jpa.repository.TbArtistEntityRepository;
import org.core.jpa.repository.TbMusicEntityRepository;
import org.core.jpa.repository.TbResourceEntityRepository;
import org.core.mybatis.iservice.TbPicService;
import org.core.mybatis.model.convert.PicConvert;
import org.core.mybatis.pojo.TbLyricPojo;
import org.core.mybatis.pojo.TbOriginPojo;
import org.core.mybatis.pojo.TbPicPojo;
import org.core.mybatis.pojo.TbTagPojo;
import org.core.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.stream.Collectors;

@Service(AdminConfig.ADMIN + "ExchangeApi")
@Slf4j
public class ExchangeApi {
    
    public static final String SHEET_MUSIC = "music";
    private static final String SHEET_ALBUM = "album";
    private static final String SHEET_ARTIST = "artist";
    private static final String SHEET_SOURCE = "source";
    private static final String SHEET_PIC = "pic";
    
    @Autowired
    private TbMusicEntityRepository tbMusicEntityRepository;
    
    @Autowired
    private TbAlbumEntityRepository tbAlbumEntityRepository;
    
    @Autowired
    private TbArtistEntityRepository artistEntityRepository;
    
    @Autowired
    private TbResourceEntityRepository resourceEntityRepository;
    
    @Autowired
    private TbPicService picService;
    
    @Autowired
    private MusicFlowApi musicFlowApi;
    
    @Autowired
    private HttpRequestConfig requestConfig;
    
    @Autowired
    private QukuAPI qukuApi;
    
    public StreamingResponseBody exportExcel() {
        List<TbMusicEntity> all = tbMusicEntityRepository.findAll();
        Collection<ExportExcelRes.MusicInfo> exportExcelRes = new ConcurrentLinkedDeque<>();
        
        Set<Long> musicIds = all.parallelStream().map(TbMusicEntity::getId).collect(Collectors.toSet());
        
        Map<Long, List<TbTagPojo>> labelMusicGenreMap = qukuApi.getLabelMusicGenre(musicIds);
        Map<Long, String> musicPicUrl = qukuApi.getMusicPicPath(musicIds);
        Map<Long, List<TbLyricPojo>> musicLyricMap = qukuApi.getMusicLyric(musicIds);
        Map<Long, List<TbTagPojo>> labelMusicTagMap = qukuApi.getLabelMusicTag(musicIds);
        
        all.parallelStream().forEach(tbMusicEntity -> {
            try {
                ExportExcelRes.MusicInfo musicInfo = new ExportExcelRes.MusicInfo();
                musicInfo.setMusicName(tbMusicEntity.getMusicName());
                musicInfo.setMusicAlias(tbMusicEntity.getAliasName());
                
                musicInfo.setMusicPicId(musicPicUrl.get(tbMusicEntity.getId()));
                String musicGenre = CollUtil.join(labelMusicGenreMap.get(tbMusicEntity.getId()), ",");
                musicInfo.setMusicGenre(musicGenre);
                List<TbTagPojo> tbTagPojos = Optional.ofNullable(labelMusicTagMap.get(tbMusicEntity.getId())).orElse(new ArrayList<>());
                String musicTag = CollUtil.join(tbTagPojos.stream().map(TbTagPojo::getTagName).toList(), ",");
                musicInfo.setMusicTag(musicTag);
                musicInfo.setDuration(String.valueOf(tbMusicEntity.getTimeLength()));
                List<TbLyricPojo> musicLyric = musicLyricMap.get(tbMusicEntity.getId());
                if (CollUtil.isNotEmpty(musicLyric)) {
                    musicInfo.setCommonLyrics(musicLyric.stream()
                                                        .filter(tbLyricPojo -> StringUtils.equals(tbLyricPojo.getType(), LyricConstant.LYRIC))
                                                        .findFirst()
                                                        .orElse(new TbLyricPojo())
                                                        .getLyric());
                    musicInfo.setCommonLyrics(musicLyric.stream()
                                                        .filter(tbLyricPojo -> StringUtils.equals(tbLyricPojo.getType(), LyricConstant.K_LYRIC))
                                                        .findFirst()
                                                        .orElse(new TbLyricPojo())
                                                        .getLyric());
                }
                List<TbMusicArtistEntity> tbMusicArtistEntities = new ArrayList<>(CollUtil.isEmpty(tbMusicEntity.getTbMusicArtistsById()) ? Collections.emptyList() : tbMusicEntity.getTbMusicArtistsById());
                if (CollUtil.isNotEmpty(tbMusicArtistEntities)) {
                    String musicArtistJoin = CollUtil.join(tbMusicArtistEntities.stream()
                                                                                .map(TbMusicArtistEntity::getArtistId)
                                                                                .filter(Objects::nonNull)
                                                                                .toList(), ",");
                    musicInfo.setMusicArtist(musicArtistJoin);
                }
                musicInfo.setAlbum(String.valueOf(tbMusicEntity.getAlbumId()));
                
                Collection<TbResourceEntity> tbResourceEntities1 = tbMusicEntity.getTbResourcesById();
                if (CollUtil.isNotEmpty(tbResourceEntities1)) {
                    musicInfo.setSource(CollUtil.join(tbResourceEntities1.stream().map(TbResourceEntity::getId).toList(), ","));
                }
                
                exportExcelRes.add(musicInfo);
            } catch (IllegalStateException e) {
                throw new RuntimeException(e);
            }
        });
        
        List<TbAlbumEntity> albumList = tbAlbumEntityRepository.findAll();
        List<Long> albumIds = albumList.parallelStream().map(TbAlbumEntity::getId).toList();
        Map<Long, String> albumPicUrl = qukuApi.getAlbumPicPath(albumIds);
        Map<Long, List<TbTagPojo>> labelAlbumGenre = qukuApi.getLabelAlbumGenre(albumIds);
        Collection<ExportExcelRes.AlbumInfo> albumInfos = new ConcurrentLinkedDeque<>();
        
        albumList.parallelStream().forEach(entity -> {
            ExportExcelRes.AlbumInfo albumInfo = new ExportExcelRes.AlbumInfo();
            albumInfo.setAlbumId(String.valueOf(entity.getId()));
            albumInfo.setAlbumName(entity.getAlbumName());
            albumInfo.setAlbumPicId(albumPicUrl.get(entity.getId()));
            List<TbTagPojo> tbTagPojos = MapUtil.get(labelAlbumGenre, entity.getId(), new TypeReference<>() {
            }, Collections.emptyList());
            albumInfo.setAlbumGenre(CollUtil.join(tbTagPojos.parallelStream().map(TbTagPojo::getTagName).toList(), ","));
            albumInfo.setAlbumArtist(CollUtil.join(entity.getTbAlbumArtistsById().parallelStream().map(TbAlbumArtistEntity::getArtistId).toList(), ","));
            albumInfo.setAlbumSubType(entity.getSubType());
            albumInfo.setAlbumCompany(entity.getCompany());
            albumInfo.setAlbumPublicTime(String.valueOf(Objects.isNull(entity.getPublishTime()) ? null : entity.getPublishTime().getTime()));
            albumInfo.setAlbumDescribe(entity.getDescription());
            albumInfos.add(albumInfo);
        });
        
        List<TbArtistEntity> artistList = artistEntityRepository.findAll();
        List<Long> artistIds = artistList.parallelStream().map(TbArtistEntity::getId).toList();
        Map<Long, String> artistPicUrlMap = qukuApi.getArtistPicPath(artistIds);
        List<ExportExcelRes.ArtistInfo> artistInfos = new LinkedList<>();
        for (TbArtistEntity artistEntity : artistList) {
            ExportExcelRes.ArtistInfo artistInfo = new ExportExcelRes.ArtistInfo();
            artistInfo.setArtistId(String.valueOf(artistEntity.getId()));
            artistInfo.setArtistName(artistEntity.getArtistName());
            artistInfo.setArtistAliasName(artistInfo.getArtistAliasName());
            artistInfo.setArtistSex(artistEntity.getSex());
            
            artistInfo.setArtistPicId(artistPicUrlMap.get(artistEntity.getId()));
            artistInfo.setArtistBirth(String.valueOf(artistEntity.getBirth().getTime()));
            artistInfo.setArtistLocation(artistEntity.getLocation());
            artistInfo.setArtistDescribe(artistInfo.getArtistDescribe());
            artistInfos.add(artistInfo);
        }
        
        List<TbResourceEntity> resourceList = resourceEntityRepository.findAll();
        List<ExportExcelRes.SourceInfo> sourceInfos = new LinkedList<>();
        for (TbResourceEntity tbResourceEntity : resourceList) {
            ExportExcelRes.SourceInfo sourceInfo = new ExportExcelRes.SourceInfo();
            sourceInfo.setSourceId(String.valueOf(tbResourceEntity.getId()));
            sourceInfo.setSourceMd5(tbResourceEntity.getMd5());
            sourceInfo.setSourcePath(tbResourceEntity.getPath());
            sourceInfo.setSourceSize(String.valueOf(tbResourceEntity.getSize()));
            sourceInfo.setSourceBitRate(String.valueOf(tbResourceEntity.getRate()));
            sourceInfo.setEncodeType(tbResourceEntity.getEncodeType());
            sourceInfo.setMusicLevel(tbResourceEntity.getLevel());
            sourceInfos.add(sourceInfo);
        }
        
        List<TbPicPojo> list = picService.list();
        List<ExportExcelRes.PicInfo> picInfos = new LinkedList<>();
        for (TbPicPojo tbPicPojo : list) {
            ExportExcelRes.PicInfo e = new ExportExcelRes.PicInfo();
            e.setPicId(String.valueOf(tbPicPojo.getId()));
            e.setPicPath(tbPicPojo.getPath());
            e.setPicMd5(tbPicPojo.getMd5());
            picInfos.add(e);
        }
        
        ByteArrayOutputStream out = new ByteArrayOutputStream(1024 * 10);
        try (ExcelWriter build = EasyExcelFactory.write(new BufferedOutputStream(out)).build()) {
            WriteSheet music = EasyExcelFactory.writerSheet(0, SHEET_MUSIC).head(ExportExcelRes.MusicInfo.class).build();
            build.write(exportExcelRes, music);
            
            WriteSheet album = EasyExcelFactory.writerSheet(1, SHEET_ALBUM).head(ExportExcelRes.AlbumInfo.class).build();
            build.write(albumInfos, album);
            
            WriteSheet artist = EasyExcelFactory.writerSheet(2, SHEET_ARTIST).head(ExportExcelRes.ArtistInfo.class).build();
            build.write(artistInfos, artist);
            
            WriteSheet source = EasyExcelFactory.writerSheet(3, SHEET_SOURCE).head(ExportExcelRes.SourceInfo.class).build();
            build.write(sourceInfos, source);
            
            WriteSheet pic = EasyExcelFactory.writerSheet(4, SHEET_PIC).head(ExportExcelRes.SourceInfo.class).build();
            build.write(picInfos, pic);
            
            build.finish();
        }
        
        ByteArrayInputStream inputStream = new ByteArrayInputStream(out.toByteArray());
        log.info("写入成功");
        return outputStream -> {
            int bytesRead;
            byte[] buffer = new byte[4096];  // 缓冲区大小
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            inputStream.close();
        };
    }
    
    public void importExcel(MultipartFile file) throws IOException {
        if (file == null) {
            throw new BaseException(ResultCode.FILENAME_NO_EXIST);
        }
        File dest = new File(requestConfig.getTempPath(), Objects.requireNonNull(file.getOriginalFilename()));
        dest = FileUtil.writeBytes(file.getBytes(), dest);
        
        ExcelReaderBuilder read = EasyExcelFactory.read(dest).excelType(ExcelTypeEnum.XLSX);
        List<ExportExcelRes.MusicInfo> musicList = read.head(ExportExcelRes.MusicInfo.class).sheet(SHEET_MUSIC).doReadSync();
        
        List<ExportExcelRes.AlbumInfo> albumList = read.head(ExportExcelRes.AlbumInfo.class).sheet(SHEET_ALBUM).doReadSync();
        Map<String, ExportExcelRes.AlbumInfo> albumMap = DefaultedMap.defaultedMap(albumList.parallelStream()
                                                                                            .collect(Collectors.toMap(ExportExcelRes.AlbumInfo::getAlbumId,
                                                                                                    albumInfo -> albumInfo)),
                new ExportExcelRes.AlbumInfo());
        
        List<ExportExcelRes.ArtistInfo> artistList = read.head(ExportExcelRes.ArtistInfo.class).sheet(SHEET_ARTIST).doReadSync();
        Map<String, ExportExcelRes.ArtistInfo> artistMap = DefaultedMap.defaultedMap(artistList.parallelStream()
                                                                                               .collect(Collectors.toMap(ExportExcelRes.ArtistInfo::getArtistId,
                                                                                                       albumInfo -> albumInfo)),
                new ExportExcelRes.ArtistInfo());
        
        List<ExportExcelRes.SourceInfo> sourceList = read.head(ExportExcelRes.SourceInfo.class).sheet(SHEET_SOURCE).doReadSync();
        Map<String, ExportExcelRes.SourceInfo> sourceMap = DefaultedMap.defaultedMap(sourceList.parallelStream()
                                                                                               .collect(Collectors.toMap(ExportExcelRes.SourceInfo::getSourceId,
                                                                                                       albumInfo -> albumInfo)),
                new ExportExcelRes.SourceInfo());
        
        
        List<ExportExcelRes.PicInfo> picList = read.head(ExportExcelRes.PicInfo.class).sheet(SHEET_PIC).doReadSync();
        Map<String, ExportExcelRes.PicInfo> picMap = DefaultedMap.defaultedMap(picList.parallelStream()
                                                                                      .collect(Collectors.toMap(ExportExcelRes.PicInfo::getPicId,
                                                                                              albumInfo -> albumInfo)), new ExportExcelRes.PicInfo());
        // 设置音乐信息
        // 音源
        for (ExportExcelRes.MusicInfo musicInfo : musicList) {
            AudioInfoReq dto = new AudioInfoReq();
            dto.setMusic(new AudioInfoReq.AudioMusic());
            AudioInfoReq.AudioMusic music = dto.getMusic();
            music.setMusicName(musicInfo.getMusicName());
            music.setAliaName(ListUtil.toList(StringUtils.split(musicInfo.getMusicAlias(), ',')));
            music.setKLyric(musicInfo.getVerbatimLyrics());
            music.setLyric(musicInfo.getCommonLyrics());
            if (StringUtils.isNotBlank(musicInfo.getDuration())) {
                music.setTimeLength(Integer.valueOf(musicInfo.getDuration()));
            }
            
            PicConvert pic = new PicConvert();
            ExportExcelRes.PicInfo picInfo = picMap.get(musicInfo.getMusicPicId());
            pic.setPath(picInfo.getPicPath());
            pic.setMd5(picInfo.getPicMd5());
            music.setPic(pic);
            dto.setSources(new LinkedList<>());
            String[] split = Optional.ofNullable(StringUtils.split(musicInfo.getSource(), ',')).orElse(new String[]{});
            for (String s : split) {
                AudioInfoReq.AudioSource e = new AudioInfoReq.AudioSource();
                ExportExcelRes.SourceInfo sourceInfo = sourceMap.get(s);
                e.setLevel(sourceInfo.getMusicLevel());
                if (StringUtils.isNotBlank(sourceInfo.getSourceBitRate())) {
                    e.setRate(Integer.valueOf(sourceInfo.getSourceBitRate()));
                }
                if (StringUtils.isNotBlank(sourceInfo.getSourceSize())) {
                    e.setSize(Long.valueOf(sourceInfo.getSourceSize()));
                }
                e.setEncodeType(sourceInfo.getEncodeType());
                e.setMd5(sourceInfo.getSourceMd5());
                e.setPathTemp(sourceInfo.getSourcePath());
                
                TbOriginPojo origin = new TbOriginPojo();
                origin.setOrigin("excel");
                e.setOrigin(origin);
                dto.getSources().add(e);
            }
            dto.setArtists(new LinkedList<>());
            String[] artistArr = Optional.ofNullable(StringUtils.split(musicInfo.getMusicArtist(), ",")).orElse(new String[]{});
            for (String s : artistArr) {
                ExportExcelRes.ArtistInfo artistInfo = artistMap.get(s);
                AudioInfoReq.AudioArtist e = new AudioInfoReq.AudioArtist();
                e.setArtistName(artistInfo.getArtistName());
                
                PicConvert artistPic = new PicConvert();
                artistPic.setPath(picInfo.getPicPath());
                artistPic.setMd5(picInfo.getPicMd5());
                e.setPic(artistPic);
                e.setAliasName(artistInfo.getArtistAliasName());
                if (StringUtils.isNotBlank(artistInfo.getArtistBirth())) {
                    e.setBirth(LocalDateTimeUtil.of(Long.parseLong(artistInfo.getArtistBirth())).toLocalDate());
                }
                e.setIntroduction(artistInfo.getArtistDescribe());
                e.setSex(artistInfo.getArtistSex());
                
                dto.getArtists().add(e);
            }
            AudioInfoReq.AudioAlbum album = new AudioInfoReq.AudioAlbum();
            ExportExcelRes.AlbumInfo albumInfo = albumMap.get(musicInfo.getAlbum());
            
            PicConvert albumPic = new PicConvert();
            albumPic.setPath(picInfo.getPicPath());
            albumPic.setMd5(picInfo.getPicMd5());
            album.setPic(albumPic);
            album.setAlbumName(albumInfo.getAlbumName());
            album.setGenre(albumInfo.getAlbumGenre());
            album.setCompany(albumInfo.getAlbumCompany());
            album.setDescription(albumInfo.getAlbumDescribe());
            album.setSubType(albumInfo.getAlbumSubType());
            if (StringUtils.isNotBlank(albumInfo.getAlbumPublicTime())) {
                album.setPublishTime(LocalDateTimeUtil.of(Long.parseLong(albumInfo.getAlbumPublicTime())));
            }
            dto.setAlbum(album);
            dto.setUserId(UserUtil.getUser().getId());
            dto.setUploadFlag(true);
            musicFlowApi.saveMusicInfo(dto);
        }
        log.info("导入完成");
    }
}
