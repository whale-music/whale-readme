package org.api.admin.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.metadata.WriteSheet;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.api.admin.config.AdminConfig;
import org.api.admin.model.req.upload.AudioInfoReq;
import org.api.admin.model.res.ExportExcelRes;
import org.api.common.service.QukuAPI;
import org.core.common.exception.BaseException;
import org.core.common.result.ResultCode;
import org.core.config.HttpRequestConfig;
import org.core.config.LyricConfig;
import org.core.jpa.entity.*;
import org.core.jpa.repository.TbAlbumEntityRepository;
import org.core.jpa.repository.TbArtistEntityRepository;
import org.core.jpa.repository.TbMusicEntityRepository;
import org.core.jpa.repository.TbResourceEntityRepository;
import org.core.mybatis.pojo.TbLyricPojo;
import org.core.mybatis.pojo.TbOriginPojo;
import org.core.mybatis.pojo.TbTagPojo;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

@Service(AdminConfig.ADMIN + "ExchangeApi")
@Slf4j
@AllArgsConstructor
public class ExchangeApi {
    
    public static final String SHEET_MUSIC = "music";
    private static final String SHEET_ALBUM = "album";
    private static final String SHEET_ARTIST = "artist";
    private static final String SHEET_SOURCE = "source";
    private final TbMusicEntityRepository tbMusicEntityRepository;
    private final TbAlbumEntityRepository tbAlbumEntityRepository;
    private final TbArtistEntityRepository artistEntityRepository;
    private final TbResourceEntityRepository resourceEntityRepository;
    private final MusicFlowApi musicFlowApi;
    private final HttpRequestConfig requestConfig;
    private QukuAPI qukuApi;
    
    public StreamingResponseBody exportExcel() {
        List<TbMusicEntity> all = tbMusicEntityRepository.findAll();
        List<ExportExcelRes.MusicInfo> exportExcelRes = new LinkedList<>();
        
        Set<Long> musicIds = all.parallelStream()
                                .map(TbMusicEntity::getId)
                                .collect(Collectors.toSet());
        
        Map<Long, List<TbTagPojo>> labelMusicGenreMap = qukuApi.getLabelMusicGenre(musicIds);
        Map<Long, String> musicPicUrl = qukuApi.getMusicPicUrl(musicIds);
        Map<Long, List<TbLyricPojo>> musicLyricMap = qukuApi.getMusicLyric(musicIds);
        Map<Long, List<TbTagPojo>> labelMusicTagMap = qukuApi.getLabelMusicTag(musicIds);
        
        all.parallelStream().forEach(tbMusicEntity -> {
            try {
                ExportExcelRes.MusicInfo musicInfo = new ExportExcelRes.MusicInfo();
                musicInfo.setMusicName(tbMusicEntity.getMusicName());
                musicInfo.setMusicAlias(tbMusicEntity.getAliasName());
                byte[] bytes = HttpUtil.downloadBytes(musicPicUrl.get(tbMusicEntity.getId()));
                musicInfo.setMusicPicBase64(Base64.getEncoder().encodeToString(bytes));
                String musicGenre = CollUtil.join(labelMusicGenreMap.get(tbMusicEntity.getId()), ",");
                musicInfo.setMusicGenre(musicGenre);
                List<TbTagPojo> tbTagPojos = Optional.ofNullable(labelMusicTagMap.get(tbMusicEntity.getId())).orElse(new ArrayList<>());
                String musicTag = CollUtil.join(tbTagPojos.stream().map(TbTagPojo::getTagName).toList(), ",");
                musicInfo.setMusicTag(musicTag);
                musicInfo.setDuration(String.valueOf(tbMusicEntity.getTimeLength()));
                List<TbLyricPojo> musicLyric = musicLyricMap.get(tbMusicEntity.getId());
                if (CollUtil.isNotEmpty(musicLyric)) {
                    musicInfo.setCommonLyrics(musicLyric.stream()
                                                        .filter(tbLyricPojo -> StringUtils.equals(tbLyricPojo.getType(), LyricConfig.LYRIC))
                                                        .findFirst()
                                                        .orElse(new TbLyricPojo())
                                                        .getLyric());
                    musicInfo.setCommonLyrics(musicLyric.stream()
                                                        .filter(tbLyricPojo -> StringUtils.equals(tbLyricPojo.getType(), LyricConfig.K_LYRIC))
                                                        .findFirst()
                                                        .orElse(new TbLyricPojo())
                                                        .getLyric());
                }
                Optional.ofNullable(tbMusicEntity.getTbMusicArtistsById()).ifPresent(tbMusicArtistEntities -> {
                    String musicArtistJoin = CollUtil.join(ListUtil.toList(tbMusicArtistEntities).stream().map(TbMusicArtistEntity::getArtistId).toList(),
                            ",");
                    musicInfo.setMusicArtist(musicArtistJoin);
                });
                musicInfo.setAlbum(String.valueOf(tbMusicEntity.getAlbumId()));
                
                Optional.ofNullable(tbMusicEntity.getTbResourcesById()).ifPresent(tbResourceEntities1 -> {
                    musicInfo.setSource(CollUtil.join(tbResourceEntities1.stream().map(TbResourceEntity::getId).toList(), ","));
                });
                
                exportExcelRes.add(musicInfo);
            } catch (IllegalStateException e) {
                throw new RuntimeException(e);
            }
        });
        
        List<TbAlbumEntity> albumList = tbAlbumEntityRepository.findAll();
        List<ExportExcelRes.AlbumInfo> albumInfos = new LinkedList<>();
        for (TbAlbumEntity entity : albumList) {
            ExportExcelRes.AlbumInfo albumInfo = new ExportExcelRes.AlbumInfo();
            albumInfo.setAlbumId(String.valueOf(entity.getId()));
            albumInfo.setAlbumName(entity.getAlbumName());
            byte[] bytes = HttpUtil.downloadBytes(qukuApi.getAlbumPicUrl(entity.getId()));
            albumInfo.setAlbumPicBase64(Base64.getEncoder().encodeToString(bytes));
            albumInfo.setAlbumGenre(CollUtil.join(qukuApi.getLabelAlbumGenre(entity.getId()).parallelStream().map(TbTagPojo::getTagName).toList(), ","));
            albumInfo.setAlbumArtist(CollUtil.join(entity.getTbAlbumArtistsById().parallelStream().map(TbAlbumArtistEntity::getArtistId).toList(), ","));
            albumInfo.setAlbumSubType(entity.getSubType());
            albumInfo.setAlbumCompany(entity.getCompany());
            albumInfo.setAlbumPublicTime(String.valueOf(entity.getPublishTime().getTime()));
            albumInfo.setAlbumDescribe(entity.getDescription());
            albumInfos.add(albumInfo);
        }
        
        List<TbArtistEntity> artistList = artistEntityRepository.findAll();
        List<ExportExcelRes.ArtistInfo> artistInfos = new LinkedList<>();
        for (TbArtistEntity artistEntity : artistList) {
            ExportExcelRes.ArtistInfo artistInfo = new ExportExcelRes.ArtistInfo();
            artistInfo.setArtistId(String.valueOf(artistEntity.getId()));
            artistInfo.setArtistName(artistEntity.getArtistName());
            artistInfo.setArtistAliasName(artistInfo.getArtistAliasName());
            artistInfo.setArtistSex(artistEntity.getSex());
            byte[] src = HttpUtil.downloadBytes(qukuApi.getArtistPicUrl(artistEntity.getId()));
            artistInfo.setArtistPicBase64(Base64.getEncoder().encodeToString(src));
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
        
        ByteArrayOutputStream out = new ByteArrayOutputStream(1024 * 10);
        try (ExcelWriter build = EasyExcelFactory.write(new BufferedOutputStream(out)).build()) {
            
            WriteSheet music = EasyExcelFactory.writerSheet(0, SHEET_MUSIC).head(ExportExcelRes.MusicInfo.class).build();
            build.write(exportExcelRes, music);
            
            WriteSheet album = EasyExcelFactory.writerSheet(1, SHEET_ALBUM).head(ExportExcelRes.AlbumInfo.class).build();
            build.write(albumInfos, album);
            
            WriteSheet artist = EasyExcelFactory.writerSheet(2, SHEET_ARTIST).head(ExportExcelRes.ArtistInfo.class).build();
            build.write(artistInfos, artist);
            
            WriteSheet source = EasyExcelFactory.writerSheet(2, SHEET_SOURCE).head(ExportExcelRes.SourceInfo.class).build();
            build.write(sourceInfos, source);
            
            build.finish();
        }
        
        ByteArrayInputStream inputStream = new ByteArrayInputStream(out.toByteArray());
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
            throw new BaseException(ResultCode.FILENAME_EXIST);
        }
        File dest = new File(requestConfig.getTempPath(), Objects.requireNonNull(file.getOriginalFilename()));
        dest = FileUtil.writeBytes(file.getBytes(), dest);
        
        ExcelReaderBuilder read = EasyExcelFactory.read(dest).excelType(ExcelTypeEnum.XLSX);
        List<ExportExcelRes.MusicInfo> musicList = read.head(ExportExcelRes.MusicInfo.class).sheet(SHEET_MUSIC).doReadSync();
        
        List<ExportExcelRes.AlbumInfo> albumList = read.head(ExportExcelRes.AlbumInfo.class).sheet(SHEET_ALBUM).doReadSync();
        Map<String, ExportExcelRes.AlbumInfo> albumMap = albumList.parallelStream()
                                                                  .collect(Collectors.toMap(ExportExcelRes.AlbumInfo::getAlbumId, albumInfo -> albumInfo));
        
        List<ExportExcelRes.ArtistInfo> artistList = read.head(ExportExcelRes.ArtistInfo.class).sheet(SHEET_ARTIST).doReadSync();
        Map<String, ExportExcelRes.ArtistInfo> artistMap = artistList.parallelStream()
                                                                     .collect(Collectors.toMap(ExportExcelRes.ArtistInfo::getArtistId,
                                                                             albumInfo -> albumInfo));
        
        List<ExportExcelRes.SourceInfo> sourceList = read.head(ExportExcelRes.SourceInfo.class).sheet(SHEET_SOURCE).doReadSync();
        Map<String, ExportExcelRes.SourceInfo> sourceMap = sourceList.parallelStream()
                                                                     .collect(Collectors.toMap(ExportExcelRes.SourceInfo::getSourceId,
                                                                             albumInfo -> albumInfo));
        
        for (ExportExcelRes.MusicInfo musicInfo : musicList) {
            AudioInfoReq dto = new AudioInfoReq();
            // 设置音乐信息
            dto.setMusic(new AudioInfoReq.AudioMusic());
            AudioInfoReq.AudioMusic music = dto.getMusic();
            music.setMusicName(musicInfo.getMusicName());
            music.setAliaName(Arrays.asList(StringUtils.split(musicInfo.getMusicAlias(), ',')));
            music.setKLyric(music.getKLyric());
            music.setLyric(music.getLyric());
            music.setTimeLength(music.getTimeLength());
            music.setPic(musicInfo.getMusicPicBase64());
            
            // 音源
            dto.setSources(new LinkedList<>());
            String[] split = Optional.ofNullable(StringUtils.split(musicInfo.getSource(), ',')).orElse(new String[]{});
            for (String s : split) {
                AudioInfoReq.AudioSource e = new AudioInfoReq.AudioSource();
                ExportExcelRes.SourceInfo sourceInfo = sourceMap.get(s);
                e.setLevel(sourceInfo.getMusicLevel());
                e.setRate(Integer.valueOf(sourceInfo.getSourceBitRate()));
                e.setSize(Long.valueOf(sourceInfo.getSourceSize()));
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
                e.setPic(artistInfo.getArtistPicBase64());
                e.setAliasName(artistInfo.getArtistAliasName());
                e.setBirth(LocalDateTimeUtil.of(Long.parseLong(artistInfo.getArtistBirth())).toLocalDate());
                e.setIntroduction(artistInfo.getArtistDescribe());
                e.setSex(artistInfo.getArtistSex());
                
                dto.getArtists().add(e);
            }
            
            AudioInfoReq.AudioAlbum album = new AudioInfoReq.AudioAlbum();
            ExportExcelRes.AlbumInfo albumInfo = albumMap.get(musicInfo.getAlbum());
            album.setPic(albumInfo.getAlbumPicBase64());
            album.setAlbumName(albumInfo.getAlbumName());
            album.setGenre(albumInfo.getAlbumGenre());
            album.setCompany(albumInfo.getAlbumCompany());
            album.setDescription(albumInfo.getAlbumDescribe());
            album.setSubType(albumInfo.getAlbumSubType());
            album.setPublishTime(LocalDateTimeUtil.of(Long.parseLong(albumInfo.getAlbumPublicTime())));
            dto.setAlbum(album);
            
            musicFlowApi.saveMusicInfo(dto);
        }
    }
}
