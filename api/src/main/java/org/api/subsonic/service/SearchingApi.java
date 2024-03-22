package org.api.subsonic.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.api.common.service.QukuAPI;
import org.api.subsonic.common.SubsonicCommonReq;
import org.api.subsonic.config.SubsonicConfig;
import org.api.subsonic.model.res.search.SearchRes;
import org.api.subsonic.model.res.search2.Search2Res;
import org.api.subsonic.model.res.search3.Search3Res;
import org.api.subsonic.utils.spring.SubsonicResourceReturnStrategyUtil;
import org.core.mybatis.iservice.TbAlbumService;
import org.core.mybatis.iservice.TbArtistService;
import org.core.mybatis.iservice.TbMusicService;
import org.core.mybatis.iservice.TbResourceService;
import org.core.mybatis.model.convert.AlbumConvert;
import org.core.mybatis.model.convert.ArtistConvert;
import org.core.mybatis.pojo.*;
import org.core.service.RemoteStorePicService;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.*;

@Service(SubsonicConfig.SUBSONIC + "SearchingApi")
public class SearchingApi {
    
    private final TbMusicService tbMusicService;
    
    private final TbArtistService tbArtistService;
    
    private final TbAlbumService tbAlbumService;
    
    private final QukuAPI qukuApi;
    
    private final TbResourceService tbResourceService;
    
    private final SubsonicResourceReturnStrategyUtil subsonicResourceReturnStrategyUtil;
    
    private final RemoteStorePicService remoteStorePicService;
    
    public SearchingApi(TbMusicService tbMusicService, TbArtistService tbArtistService, TbAlbumService tbAlbumService, QukuAPI qukuApi, TbResourceService tbResourceService, SubsonicResourceReturnStrategyUtil subsonicResourceReturnStrategyUtil, RemoteStorePicService remoteStorePicService) {
        this.tbMusicService = tbMusicService;
        this.tbArtistService = tbArtistService;
        this.tbAlbumService = tbAlbumService;
        this.qukuApi = qukuApi;
        this.tbResourceService = tbResourceService;
        this.subsonicResourceReturnStrategyUtil = subsonicResourceReturnStrategyUtil;
        this.remoteStorePicService = remoteStorePicService;
    }
    
    
    /**
     * 返回明星歌曲，专辑和艺术家
     *
     * @param req       账号信息
     * @param artist    艺术家寻找
     * @param album     专辑寻找
     * @param title     歌曲寻找
     * @param any       搜索所有字段
     * @param count     要返回的最大结果数
     * @param offset    搜索结果偏移量。用于分页
     * @param newerThan 只返回比此更新的匹配项。自1970年以来以毫秒计
     * @return 返回明星歌曲，专辑和艺术家
     */
    public SearchRes search(SubsonicCommonReq req, String artist, String album, String title, Boolean any, Long count, Long offset, Long newerThan) {
        return new SearchRes();
    }
    
    /**
     * 返回符合给定搜索条件的专辑、艺术家和歌曲。支持对结果进行分页
     *
     * @param req           账号信息
     * @param query         搜索查询
     * @param artistCount   返回的最大艺术家数量
     * @param artistOffset  艺术家的搜索结果偏移量。用于分页
     * @param albumCount    返回的最大相册数
     * @param albumOffset   相册的搜索结果偏移量。用于分页
     * @param songCount     返回的最大歌曲数
     * @param songOffset    歌曲的搜索结果偏移量。用于分页
     * @param musicFolderId 如果指定，则只返回匹配此音乐文件夹ID的项目
     * @return 返回符合给定搜索条件的专辑、艺术家和歌曲。支持对结果进行分页
     */
    public Search2Res search2(SubsonicCommonReq req, String query, Long artistCount, Long artistOffset, Long albumCount, Long albumOffset, Long songCount, Long songOffset, Long musicFolderId) {
        Search2Res res = new Search2Res();
        Search2Res.SearchResult2 searchResult2 = new Search2Res.SearchResult2();
        if (StringUtils.isBlank(query)) {
            return new Search2Res();
        }
        LambdaQueryWrapper<TbArtistPojo> artistWrapper = Wrappers.lambdaQuery();
        artistWrapper.like(TbArtistPojo::getArtistName, query);
        artistWrapper.like(TbArtistPojo::getAliasName, query);
        Page<TbArtistPojo> artistPage = tbArtistService.page(new Page<>(artistOffset, artistCount), artistWrapper);
        if (CollUtil.isNotEmpty(artistPage.getRecords())) {
            ArrayList<Search2Res.Artist> artist = new ArrayList<>();
            
            Map<Long, Integer> artistAlbumCount = qukuApi.getArtistAlbumCountByArtistIds(artistPage.getRecords()
                                                                                                   .parallelStream()
                                                                                                   .map(TbArtistPojo::getId)
                                                                                                   .toList());
            for (TbArtistPojo artistPojo : artistPage.getRecords()) {
                Search2Res.Artist e = new Search2Res.Artist();
                e.setId(String.valueOf(artistPojo.getId()));
                e.setName(artistPojo.getArtistName());
                e.setStarred(LocalDateTimeUtil.format(artistPojo.getCreateTime(), DatePattern.UTC_PATTERN));
                e.setAlbumCount(artistAlbumCount.get(artistPojo.getId()));
                e.setCoverArt(String.valueOf(artistPojo.getId()));
                e.setArtistImageUrl(remoteStorePicService.getArtistPicUrl(artistPojo.getId()));
                e.setUserRating(0);
                
                artist.add(e);
            }
            searchResult2.setArtist(artist);
        }
        
        LambdaQueryWrapper<TbAlbumPojo> albumWrapper = Wrappers.lambdaQuery();
        albumWrapper.like(TbAlbumPojo::getAlbumName, query);
        Page<TbAlbumPojo> albumPage = tbAlbumService.page(new Page<>(albumOffset, albumCount), albumWrapper);
        if (CollUtil.isNotEmpty(albumPage.getRecords())) {
            ArrayList<Search2Res.Album> albums = new ArrayList<>();
            
            List<Long> albumIds = albumPage.getRecords().parallelStream().map(TbAlbumPojo::getId).toList();
            Map<Long, List<ArtistConvert>> albumArtistMapByAlbumIds = qukuApi.getArtistByAlbumIdsToMap(albumIds);
            Map<Long, List<TbTagPojo>> labelAlbumGenre = qukuApi.getLabelAlbumGenre(albumIds);
            Map<Long, Integer> albumDurationCount = qukuApi.getAlbumDurationCount(albumIds);
            Map<Long, Integer> albumMusicCountByMapAlbumId = qukuApi.getAlbumMusicCountByMapAlbumId(albumIds);
            for (TbAlbumPojo albumPojo : albumPage.getRecords()) {
                Search2Res.Album e = new Search2Res.Album();
                e.setId(String.valueOf(albumPojo.getId()));
                e.setIsDir(true);
                e.setTitle(albumPojo.getAlbumName());
                e.setName(albumPojo.getAlbumName());
                e.setAlbum(albumPojo.getAlbumName());
                e.setYear(albumPojo.getPublishTime().getYear());
                
                List<TbTagPojo> tbTagPojos = labelAlbumGenre.get(albumPojo.getId());
                if (CollUtil.isNotEmpty(tbTagPojos)) {
                    TbTagPojo tbTagPojo = tbTagPojos.get(0);
                    e.setGenre(tbTagPojo.getTagName());
                }
                List<ArtistConvert> artistConverts = albumArtistMapByAlbumIds.get(albumPojo.getId());
                if (CollUtil.isNotEmpty(artistConverts)) {
                    ArtistConvert artistConvert = artistConverts.get(0);
                    String artistId = String.valueOf(artistConvert.getId());
                    e.setParent(artistId);
                    e.setArtistId(artistId);
                    e.setArtist(artistConvert.getArtistName());
                }
                e.setCoverArt(String.valueOf(albumPojo.getId()));
                e.setDuration(albumDurationCount.get(albumPojo.getId()));
                e.setPlayCount(0);
                e.setPlayed(new Date());
                e.setCreated(Date.from(albumPojo.getCreateTime().atZone(ZoneId.systemDefault()).toInstant()));
                e.setSongCount(albumMusicCountByMapAlbumId.get(albumPojo.getId()));
                e.setIsVideo(false);
                
                albums.add(e);
            }
            searchResult2.setAlbum(albums);
        }
        
        LambdaQueryWrapper<TbMusicPojo> musicWrapper = Wrappers.lambdaQuery();
        musicWrapper.like(TbMusicPojo::getMusicName, query);
        Page<TbMusicPojo> musicPage = tbMusicService.page(new Page<>(songOffset, songCount), musicWrapper);
        if (CollUtil.isNotEmpty(musicPage.getRecords())) {
            ArrayList<Search2Res.Song> musics = new ArrayList<>();
            
            List<Long> musicIds = musicPage.getRecords().parallelStream().map(TbMusicPojo::getId).toList();
            Map<Long, AlbumConvert> musicAlbumByMusicIdToMap = qukuApi.getMusicAlbumByAlbumIdToMap(musicPage.getRecords()
                                                                                                            .parallelStream()
                                                                                                            .map(TbMusicPojo::getAlbumId)
                                                                                                            .toList());
            Map<Long, List<TbTagPojo>> labelMusicGenre = qukuApi.getLabelMusicGenre(musicIds);
            Map<Long, List<ArtistConvert>> musicArtistByMusicIdToMap = qukuApi.getMusicArtistByMusicIdToMap(musicIds);
            Map<Long, List<TbResourcePojo>> resourceMap = tbResourceService.getResourceMap(musicIds);
            for (TbMusicPojo musicPojo : musicPage.getRecords()) {
                Search2Res.Song e = new Search2Res.Song();
                e.setId(String.valueOf(musicPojo.getId()));
                e.setParent(String.valueOf(musicPojo.getAlbumId()));
                e.setIsDir(true);
                e.setTitle(musicPojo.getMusicName());
                
                AlbumConvert albumConvert = musicAlbumByMusicIdToMap.get(musicPojo.getId());
                if (Objects.nonNull(albumConvert)) {
                    e.setAlbumId(String.valueOf(albumConvert.getId()));
                    e.setAlbum(albumConvert.getAlbumName());
                    e.setYear(albumConvert.getPublishTime().getYear());
                }
                
                List<TbTagPojo> tbTagPojos = labelMusicGenre.get(musicPojo.getId());
                if (CollUtil.isNotEmpty(tbTagPojos)) {
                    TbTagPojo tbTagPojo = tbTagPojos.get(0);
                    e.setGenre(tbTagPojo.getTagName());
                }
                List<ArtistConvert> artistConverts = musicArtistByMusicIdToMap.get(musicPojo.getId());
                if (CollUtil.isNotEmpty(artistConverts)) {
                    ArtistConvert artistConvert = artistConverts.get(0);
                    String artistId = String.valueOf(artistConvert.getId());
                    e.setParent(artistId);
                    e.setArtistId(artistId);
                    e.setArtist(artistConvert.getArtistName());
                }
                e.setCoverArt(String.valueOf(musicPojo.getId()));
                
                e.setDuration(musicPojo.getTimeLength());
                List<TbResourcePojo> tbResourcePojos = resourceMap.get(musicPojo.getId());
                TbResourcePojo tbResourcePojo = subsonicResourceReturnStrategyUtil.handleResource(tbResourcePojos);
                if (Objects.nonNull(tbResourcePojo)) {
                    e.setSize(tbResourcePojo.getSize());
                    e.setPath(tbResourcePojo.getPath());
                    e.setSuffix(tbResourcePojo.getEncodeType());
                    e.setBitRate(tbResourcePojo.getRate());
                    if (StringUtils.equalsIgnoreCase(tbResourcePojo.getEncodeType(), "mp3")) {
                        e.setContentType("audio/mpeg");
                    } else {
                        e.setContentType("audio/" + tbResourcePojo.getEncodeType());
                    }
                }
                e.setTrack(0);
                e.setPlayCount(0);
                e.setType("music");
                e.setPlayed(new Date());
                e.setCreated(Date.from(musicPojo.getCreateTime().atZone(ZoneId.systemDefault()).toInstant()));
                e.setIsVideo(false);
                
                musics.add(e);
            }
            searchResult2.setSong(musics);
        }
        
        res.setSearchResult2(searchResult2);
        return res;
    }
    
    public Search3Res search3(SubsonicCommonReq req, String query, Long artistCount, Long artistOffset, Long albumCount, Long albumOffset, Long songCount, Long songOffset, Long musicFolderId) {
        Search3Res res = new Search3Res();
        Search3Res.SearchResult3 searchResult2 = new Search3Res.SearchResult3();
        if (StringUtils.isBlank(query)) {
            return new Search3Res();
        }
        LambdaQueryWrapper<TbArtistPojo> artistWrapper = Wrappers.lambdaQuery();
        artistWrapper.like(TbArtistPojo::getArtistName, query);
        artistWrapper.like(TbArtistPojo::getAliasName, query);
        Page<TbArtistPojo> artistPage = tbArtistService.page(new Page<>(artistOffset, artistCount), artistWrapper);
        if (CollUtil.isNotEmpty(artistPage.getRecords())) {
            ArrayList<Search3Res.Artist> artist = new ArrayList<>();
            
            Map<Long, Integer> artistAlbumCount = qukuApi.getArtistAlbumCountByArtistIds(artistPage.getRecords()
                                                                                                   .parallelStream()
                                                                                                   .map(TbArtistPojo::getId)
                                                                                                   .toList());
            for (TbArtistPojo artistPojo : artistPage.getRecords()) {
                Search3Res.Artist e = new Search3Res.Artist();
                e.setId(String.valueOf(artistPojo.getId()));
                e.setName(artistPojo.getArtistName());
                e.setStarred(LocalDateTimeUtil.format(artistPojo.getCreateTime(), DatePattern.UTC_PATTERN));
                e.setAlbumCount(artistAlbumCount.get(artistPojo.getId()));
                e.setCoverArt(String.valueOf(artistPojo.getId()));
                e.setArtistImageUrl(remoteStorePicService.getArtistPicUrl(artistPojo.getId()));
                e.setUserRating(0);
                
                artist.add(e);
            }
            searchResult2.setArtist(artist);
        }
        
        LambdaQueryWrapper<TbAlbumPojo> albumWrapper = Wrappers.lambdaQuery();
        albumWrapper.like(TbAlbumPojo::getAlbumName, query);
        Page<TbAlbumPojo> albumPage = tbAlbumService.page(new Page<>(albumOffset, albumCount), albumWrapper);
        if (CollUtil.isNotEmpty(albumPage.getRecords())) {
            ArrayList<Search3Res.Album> albums = new ArrayList<>();
            
            List<Long> albumIds = albumPage.getRecords().parallelStream().map(TbAlbumPojo::getId).toList();
            Map<Long, List<ArtistConvert>> albumArtistMapByAlbumIds = qukuApi.getArtistByAlbumIdsToMap(albumIds);
            Map<Long, List<TbTagPojo>> labelAlbumGenre = qukuApi.getLabelAlbumGenre(albumIds);
            Map<Long, Integer> albumDurationCount = qukuApi.getAlbumDurationCount(albumIds);
            Map<Long, Integer> albumMusicCountByMapAlbumId = qukuApi.getAlbumMusicCountByMapAlbumId(albumIds);
            for (TbAlbumPojo albumPojo : albumPage.getRecords()) {
                Search3Res.Album e = new Search3Res.Album();
                e.setId(String.valueOf(albumPojo.getId()));
                e.setIsDir(true);
                e.setTitle(albumPojo.getAlbumName());
                e.setName(albumPojo.getAlbumName());
                e.setAlbum(albumPojo.getAlbumName());
                e.setYear(albumPojo.getPublishTime().getYear());
                
                List<TbTagPojo> tbTagPojos = labelAlbumGenre.get(albumPojo.getId());
                if (CollUtil.isNotEmpty(tbTagPojos)) {
                    TbTagPojo tbTagPojo = tbTagPojos.get(0);
                    e.setGenre(tbTagPojo.getTagName());
                }
                List<ArtistConvert> artistConverts = albumArtistMapByAlbumIds.get(albumPojo.getId());
                if (CollUtil.isNotEmpty(artistConverts)) {
                    ArtistConvert artistConvert = artistConverts.get(0);
                    String artistId = String.valueOf(artistConvert.getId());
                    e.setParent(artistId);
                    e.setArtistId(artistId);
                    e.setArtist(artistConvert.getArtistName());
                }
                e.setCoverArt(String.valueOf(albumPojo.getId()));
                e.setDuration(albumDurationCount.get(albumPojo.getId()));
                e.setPlayCount(0);
                e.setPlayed(new Date());
                e.setCreated(Date.from(albumPojo.getCreateTime().atZone(ZoneId.systemDefault()).toInstant()));
                e.setSongCount(albumMusicCountByMapAlbumId.get(albumPojo.getId()));
                e.setIsVideo(false);
                
                albums.add(e);
            }
            searchResult2.setAlbum(albums);
        }
        
        LambdaQueryWrapper<TbMusicPojo> musicWrapper = Wrappers.lambdaQuery();
        musicWrapper.like(TbMusicPojo::getMusicName, query);
        Page<TbMusicPojo> musicPage = tbMusicService.page(new Page<>(songOffset, songCount), musicWrapper);
        if (CollUtil.isNotEmpty(musicPage.getRecords())) {
            ArrayList<Search3Res.Song> musics = new ArrayList<>();
            
            List<Long> musicIds = musicPage.getRecords().parallelStream().map(TbMusicPojo::getId).toList();
            Map<Long, AlbumConvert> musicAlbumByMusicIdToMap = qukuApi.getMusicAlbumByAlbumIdToMap(musicPage.getRecords()
                                                                                                            .parallelStream()
                                                                                                            .map(TbMusicPojo::getAlbumId)
                                                                                                            .toList());
            Map<Long, List<TbTagPojo>> labelMusicGenre = qukuApi.getLabelMusicGenre(musicIds);
            Map<Long, List<ArtistConvert>> musicArtistByMusicIdToMap = qukuApi.getMusicArtistByMusicIdToMap(musicIds);
            Map<Long, List<TbResourcePojo>> resourceMap = tbResourceService.getResourceMap(musicIds);
            for (TbMusicPojo musicPojo : musicPage.getRecords()) {
                Search3Res.Song e = new Search3Res.Song();
                e.setId(String.valueOf(musicPojo.getId()));
                e.setParent(String.valueOf(musicPojo.getAlbumId()));
                e.setIsDir(true);
                e.setTitle(musicPojo.getMusicName());
                
                AlbumConvert albumConvert = musicAlbumByMusicIdToMap.get(musicPojo.getId());
                if (Objects.nonNull(albumConvert)) {
                    e.setAlbumId(String.valueOf(albumConvert.getId()));
                    e.setAlbum(albumConvert.getAlbumName());
                    e.setYear(albumConvert.getPublishTime().getYear());
                }
                
                List<TbTagPojo> tbTagPojos = labelMusicGenre.get(musicPojo.getId());
                if (CollUtil.isNotEmpty(tbTagPojos)) {
                    TbTagPojo tbTagPojo = tbTagPojos.get(0);
                    e.setGenre(tbTagPojo.getTagName());
                }
                List<ArtistConvert> artistConverts = musicArtistByMusicIdToMap.get(musicPojo.getId());
                if (CollUtil.isNotEmpty(artistConverts)) {
                    ArtistConvert artistConvert = artistConverts.get(0);
                    String artistId = String.valueOf(artistConvert.getId());
                    e.setParent(artistId);
                    e.setArtistId(artistId);
                    e.setArtist(artistConvert.getArtistName());
                }
                e.setCoverArt(String.valueOf(musicPojo.getId()));
                
                e.setDuration(musicPojo.getTimeLength());
                List<TbResourcePojo> tbResourcePojos = resourceMap.get(musicPojo.getId());
                TbResourcePojo tbResourcePojo = subsonicResourceReturnStrategyUtil.handleResource(tbResourcePojos);
                if (Objects.nonNull(tbResourcePojo)) {
                    e.setSize(tbResourcePojo.getSize());
                    e.setPath(tbResourcePojo.getPath());
                    e.setSuffix(tbResourcePojo.getEncodeType());
                    e.setBitRate(tbResourcePojo.getRate());
                    if (StringUtils.equalsIgnoreCase(tbResourcePojo.getEncodeType(), "mp3")) {
                        e.setContentType("audio/mpeg");
                    } else {
                        e.setContentType("audio/" + tbResourcePojo.getEncodeType());
                    }
                }
                e.setTrack(0);
                e.setPlayCount(0);
                e.setType("music");
                e.setPlayed(new Date());
                e.setCreated(Date.from(musicPojo.getCreateTime().atZone(ZoneId.systemDefault()).toInstant()));
                e.setIsVideo(false);
                
                musics.add(e);
            }
            searchResult2.setSong(musics);
        }
        
        res.setSearchResult3(searchResult2);
        return res;
    }
}
