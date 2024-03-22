package org.api.subsonic.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.api.common.service.QukuAPI;
import org.api.subsonic.common.SubsonicCommonReq;
import org.api.subsonic.config.SubsonicConfig;
import org.api.subsonic.model.res.album.Album;
import org.api.subsonic.model.res.album.AlbumRes;
import org.api.subsonic.model.res.album.SongItem;
import org.api.subsonic.model.res.albuminfo.AlbumInfoRes;
import org.api.subsonic.model.res.albuminfo2.AlbumInfo2Res;
import org.api.subsonic.model.res.artist.ArtistRes;
import org.api.subsonic.model.res.artistinfo.ArtistInfoRes;
import org.api.subsonic.model.res.artistinfo2.ArtistInfo2Res;
import org.api.subsonic.model.res.artists.ArtistsRes;
import org.api.subsonic.model.res.genres.GenresRes;
import org.api.subsonic.model.res.indexes.IndexesRes;
import org.api.subsonic.model.res.musicdirectory.MusicDirectoryRes;
import org.api.subsonic.model.res.musicfolders.MusicFoldersRes;
import org.api.subsonic.model.res.similarsongs.SimilarSongsRes;
import org.api.subsonic.model.res.similarsongs2.SimilarSongs2Res;
import org.api.subsonic.model.res.song.Song;
import org.api.subsonic.model.res.song.SongRes;
import org.api.subsonic.model.res.topsongs.TopSongsRes;
import org.api.subsonic.model.res.videoinfo.VideoInfoRes;
import org.api.subsonic.model.res.videos.VideosRes;
import org.api.subsonic.utils.spring.SubsonicResourceReturnStrategyUtil;
import org.core.common.constant.PicTypeConstant;
import org.core.common.exception.BaseException;
import org.core.common.result.ResultCode;
import org.core.mybatis.iservice.*;
import org.core.mybatis.model.convert.AlbumConvert;
import org.core.mybatis.model.convert.ArtistConvert;
import org.core.mybatis.model.convert.MusicConvert;
import org.core.mybatis.pojo.*;
import org.core.service.RemoteStorePicService;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.net.URLConnection;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service(SubsonicConfig.SUBSONIC + "BrowsingApi")
public class BrowsingApi {
    
    private static final String URL = "http://www.last.fm";
    private final QukuAPI qukuService;
    private final TbAlbumService albumService;
    private final TbMusicService musicService;
    private final TbArtistService tbArtistService;
    private final TbMiddleTagService tbMiddleTagService;
    private final TbTagService tbTagService;
    private final TbResourceService tbResourceService;
    private final SubsonicResourceReturnStrategyUtil resourceReturnStrategyUtil;
    private final TbMusicArtistService tbMusicArtistService;
    private final RemoteStorePicService remoteStorePicService;
    
    @NotNull
    private static Character getCharacterFirstLetter(String name) {
        switch (Optional.ofNullable(name).orElse("").charAt(0)) {
            case 'A', 'a':
                return 'A';
            case 'B', 'b':
                return 'B';
            case 'C', 'c':
                return 'C';
            case 'D', 'd':
                return 'D';
            case 'E', 'e':
                return 'E';
            case 'F', 'f':
                return 'F';
            case 'G', 'g':
                return 'G';
            case 'H', 'h':
                return 'H';
            case 'I', 'i':
                return 'I';
            case 'J', 'j':
                return 'J';
            case 'K', 'k':
                return 'K';
            case 'L', 'l':
                return 'L';
            case 'M', 'm':
                return 'M';
            case 'N', 'n':
                return 'N';
            case 'O', 'o':
                return 'O';
            case 'P', 'p':
                return 'P';
            case 'Q', 'q':
                return 'Q';
            case 'R', 'r':
                return 'R';
            case 'S', 's':
                return 'S';
            case 'T', 't':
                return 'T';
            case 'U', 'u':
                return 'U';
            case 'W', 'w':
                return 'W';
            case 'X', 'x':
                return 'X';
            case 'Y', 'y':
                return 'Y';
            case 'Z', 'z':
                return 'Z';
            default:
                return '#';
        }
    }
    
    public BrowsingApi(QukuAPI qukuService, TbAlbumService albumService, TbMusicService musicService, TbArtistService tbArtistService, TbMiddleTagService tbMiddleTagService, TbTagService tbTagService, TbResourceService tbResourceService, SubsonicResourceReturnStrategyUtil resourceReturnStrategyUtil, TbMusicArtistService tbMusicArtistService, RemoteStorePicService remoteStorePicService) {
        this.qukuService = qukuService;
        this.albumService = albumService;
        this.musicService = musicService;
        this.tbArtistService = tbArtistService;
        this.tbMiddleTagService = tbMiddleTagService;
        this.tbTagService = tbTagService;
        this.tbResourceService = tbResourceService;
        this.resourceReturnStrategyUtil = resourceReturnStrategyUtil;
        this.tbMusicArtistService = tbMusicArtistService;
        this.remoteStorePicService = remoteStorePicService;
    }
    
    public SongRes getSong(Long id) {
        TbMusicPojo musicPojo = musicService.getById(id);
        TbAlbumPojo albumByAlbumId = qukuService.getAlbumByAlbumId(musicPojo.getAlbumId());
        List<ArtistConvert> artistByMusicId = qukuService.getArtistByMusicIds(musicPojo.getId());
        List<TbResourcePojo> musicUrl = qukuService.getMusicPaths(CollUtil.newHashSet(musicPojo.getId()));
        ArtistConvert tbArtistPojo = CollUtil.isEmpty(artistByMusicId) ? new ArtistConvert() : artistByMusicId.get(0);
        Song song = new Song();
        song.setId(String.valueOf(musicPojo.getId()));
        song.setIsDir(false);
        song.setTitle(musicPojo.getMusicName());
        song.setAlbum(albumByAlbumId.getAlbumName());
        song.setAlbumId(String.valueOf(albumByAlbumId.getId()));
        song.setArtist(tbArtistPojo.getArtistName());
        song.setArtistId(String.valueOf(tbArtistPojo.getId()));
        song.setTrack(0);
        song.setYear(albumByAlbumId.getPublishTime().getYear());
        song.setCoverArt(String.valueOf(musicPojo.getId()));
        TbResourcePojo tbMusicUrlPojo = CollUtil.isEmpty(musicUrl) ? new TbResourcePojo() : musicUrl.get(0);
        if (Objects.isNull(tbMusicUrlPojo)) {
            throw new BaseException(ResultCode.RESOURCE_DATA_NOT_EXISTED);
        }
        song.setSize(Math.toIntExact((tbMusicUrlPojo.getSize() == null ? 0 : tbMusicUrlPojo.getSize())));
        song.setContentType(URLConnection.guessContentTypeFromName(tbMusicUrlPojo.getPath()));
        song.setSuffix(tbMusicUrlPojo.getEncodeType());
        song.setBitRate(tbMusicUrlPojo.getRate());
        song.setPath(tbMusicUrlPojo.getPath());
        
        song.setStarred(musicPojo.getUpdateTime().toString());
        song.setDuration(musicPojo.getTimeLength() / 1000);
        song.setParent(String.valueOf(albumByAlbumId.getId()));
        song.setDiscNumber(String.valueOf(0));
        song.setPlayCount(0);
        song.setPlayed(musicPojo.getUpdateTime().toString());
        song.setCreated(LocalDateTimeUtil.format(musicPojo.getCreateTime(), DatePattern.UTC_MS_PATTERN));
        song.setType("music");
        song.setUserRating(1);
        song.setIsVideo(false);
        
        SongRes songRes = new SongRes();
        songRes.setSong(song);
        return songRes;
    }
    
    public MusicFoldersRes getMusicFolders(SubsonicCommonReq req) {
        MusicFoldersRes musicFoldersRes = new MusicFoldersRes();
        MusicFoldersRes.MusicFolders musicFolders = new MusicFoldersRes.MusicFolders();
        ArrayList<MusicFoldersRes.MusicFolder> musicFolder = new ArrayList<>();
        MusicFoldersRes.MusicFolder e = new MusicFoldersRes.MusicFolder();
        e.setId(0L);
        e.setName("Music Library");
        musicFolder.add(e);
        musicFolders.setMusicFolder(musicFolder);
        musicFoldersRes.setMusicFolders(musicFolders);
        return musicFoldersRes;
    }
    
    public IndexesRes getIndexes(SubsonicCommonReq req, String musicFolderId, String ifModifiedSince) {
        List<TbArtistPojo> list = tbArtistService.list();
        Map<Character, ArrayList<TbArtistPojo>> artistMap = list.parallelStream()
                                                                .collect(Collectors.toMap(tbArtistPojo -> getCharacterFirstLetter(tbArtistPojo.getArtistName()),
                                                                        ListUtil::toList,
                                                                        (o1, o2) -> {
                                                                            o2.addAll(o1);
                                                                            return o2;
                                                                        }));
        
        
        IndexesRes indexesRes = new IndexesRes();
        IndexesRes.Indexes indexes = new IndexesRes.Indexes();
        ArrayList<IndexesRes.Index> index = new ArrayList<>();
        
        for (Map.Entry<Character, ArrayList<TbArtistPojo>> entryKey : artistMap.entrySet()) {
            IndexesRes.Index e = new IndexesRes.Index();
            e.setName(Objects.toString(entryKey.getKey()));
            ArrayList<IndexesRes.Artist> artist = new ArrayList<>();
            Map<Long, Integer> artistAlbumCountMap = qukuService.getArtistAlbumCountByArtistIds(entryKey.getValue()
                                                                                                        .parallelStream()
                                                                                                        .map(TbArtistPojo::getId)
                                                                                                        .toList());
            for (TbArtistPojo tbArtistPojo : entryKey.getValue()) {
                IndexesRes.Artist e1 = new IndexesRes.Artist();
                e1.setId(String.valueOf(tbArtistPojo.getId()));
                e1.setName(tbArtistPojo.getArtistName());
                e1.setArtistImageUrl(remoteStorePicService.getArtistPicUrl(tbArtistPojo.getId()));
                e1.setCoverArt(String.valueOf(tbArtistPojo.getId()));
                e1.setUserRating(0);
                e1.setAlbumCount(artistAlbumCountMap.get(tbArtistPojo.getId()) == null ? 0 : artistAlbumCountMap.get(tbArtistPojo.getId()));
                artist.add(e1);
            }
            e.setArtist(artist);
            index.add(e);
        }
        indexes.setIndex(index);
        indexesRes.setIndexes(indexes);
        return indexesRes;
    }
    
    public MusicDirectoryRes getMusicDirectory(SubsonicCommonReq req, Long id) {
        List<MusicConvert> musicListByAlbumId;
        TbAlbumPojo albumInfo = albumService.getById(id);
        List<Long> albumIds = new ArrayList<>();
        if (Objects.isNull(albumInfo)) {
            TbArtistPojo artistInfo = tbArtistService.getById(id);
            if (Objects.isNull(artistInfo)) {
                throw new BaseException(ResultCode.PARAM_IS_INVALID);
            }
            List<AlbumConvert> albumListByArtistIds = qukuService.getAlbumByArtistIds(Collections.singletonList(id));
            albumIds.addAll(albumListByArtistIds.parallelStream().map(TbAlbumPojo::getId).toList());
        } else {
            albumIds.add(albumInfo.getId());
        }
        musicListByAlbumId = qukuService.getMusicListByAlbumId(albumIds);
        if (CollUtil.isEmpty(musicListByAlbumId)) {
            return new MusicDirectoryRes();
        }
        
        List<Long> musicIds = musicListByAlbumId.parallelStream()
                                                .map(TbMusicPojo::getId)
                                                .toList();
        Map<Long, AlbumConvert> albumMaps = qukuService.getMusicAlbumByMusicIdToMap(musicIds);
        Map<Long, List<ArtistConvert>> artistMaps = qukuService.getArtistByMusicIdToMap(musicIds);
        MusicDirectoryRes res = new MusicDirectoryRes();
        Map<Long, List<TbResourcePojo>> resourceMap = tbResourceService.getResourceMap(musicIds);
        ArrayList<MusicDirectoryRes.Child> child = new ArrayList<>();
        for (int i = 0; i < musicListByAlbumId.size(); i++) {
            MusicConvert s = musicListByAlbumId.get(i);
            
            MusicDirectoryRes.Child c = new MusicDirectoryRes.Child();
            c.setId(s.getId());
            c.setParent(id);
            c.setIsDir(false);
            c.setTitle(s.getMusicName());
            
            TbAlbumPojo albumPojo = albumMaps.get(s.getId());
            if (Objects.nonNull(albumPojo)) {
                c.setAlbumId(albumPojo.getId());
                c.setAlbum(albumPojo.getAlbumName());
                c.setYear(albumPojo.getPublishTime().getYear());
            }
            
            List<ArtistConvert> albumArtistListByAlbumIds = artistMaps.get(s.getId());
            if (CollUtil.isNotEmpty(albumArtistListByAlbumIds) && Objects.nonNull(albumArtistListByAlbumIds.getFirst())) {
                ArtistConvert first = albumArtistListByAlbumIds.getFirst();
                c.setArtist(first.getArtistName());
                c.setArtistId(first.getId());
            }
            List<TbTagPojo> labelAlbumGenre = qukuService.getLabelAlbumGenre(id);
            if (CollUtil.isNotEmpty(labelAlbumGenre)) {
                c.setGenre(CollUtil.join(labelAlbumGenre, ","));
            }
            c.setTrack(i);
            TbResourcePojo tbResourcePojo = resourceReturnStrategyUtil.handleResource(resourceMap.get(s.getId()));
            // 跳过无音源
            if (Objects.isNull(tbResourcePojo)) {
                continue;
            }
            c.setCoverArt(s.getId());
            c.setSize(tbResourcePojo.getSize());
            c.setContentType(URLConnection.guessContentTypeFromName(tbResourcePojo.getPath()));
            c.setSuffix(tbResourcePojo.getEncodeType());
            c.setBitRate(tbResourcePojo.getRate());
            c.setPath(tbResourcePojo.getPath());
            c.setPlayCount(0);
            c.setType("music");
            c.setUserRating((byte) 0);
            c.setIsVideo(false);
            c.setPlayed(LocalDateTimeUtil.format(s.getUpdateTime(), DatePattern.UTC_MS_PATTERN));
            c.setComment(s.getComment());
            c.setSortName(s.getAliasName());
            c.setMediaType("song");
            c.setCreated(LocalDateTimeUtil.format(s.getCreateTime(), DatePattern.UTC_MS_PATTERN));
            c.setDuration(s.getTimeLength());
            c.setBpm((byte) 0);
            
            child.add(c);
        }
        MusicDirectoryRes.Directory directory = new MusicDirectoryRes.Directory();
        directory.setChild(child);
        res.setDirectory(directory);
        return res;
    }
    
    public GenresRes getGenres(SubsonicCommonReq req) {
        GenresRes genresRes = new GenresRes();
        ArrayList<GenresRes.Genres> genres = new ArrayList<>();
        
        List<TbMiddleTagPojo> list = tbMiddleTagService.list();
        if (CollUtil.isEmpty(list)) {
            return new GenresRes();
        }
        Map<Long, ArrayList<TbMiddleTagPojo>> middleMap = list.parallelStream()
                                                              .collect(Collectors.toMap(TbMiddleTagPojo::getTagId, ListUtil::toList, (o1, o2) -> {
                                                                  o2.addAll(o1);
                                                                  return o2;
                                                              }));
        List<TbTagPojo> tbTagPojos = tbTagService.listByIds(list.parallelStream().map(TbMiddleTagPojo::getTagId).toList());
        Map<Long, TbTagPojo> tagMap = tbTagPojos.parallelStream().collect(Collectors.toMap(TbTagPojo::getId, tbTagPojo -> tbTagPojo));
        
        for (TbMiddleTagPojo tbMiddleTagPojo : list) {
            GenresRes.Genres e = new GenresRes.Genres();
            ArrayList<TbMiddleTagPojo> tbMiddleTagPojos = middleMap.get(tbMiddleTagPojo.getTagId());
            long albumCount = tbMiddleTagPojos.parallelStream()
                                              .filter(tbMiddleTagPojo1 -> Objects.equals(tbMiddleTagPojo1.getType(), PicTypeConstant.ALBUM))
                                              .count();
            e.setAlbumCount(String.valueOf(albumCount));
            long songCount = tbMiddleTagPojos.parallelStream()
                                             .filter(tbMiddleTagPojo1 -> Objects.equals(tbMiddleTagPojo1.getType(), PicTypeConstant.MUSIC))
                                             .count();
            e.setSongCount(String.valueOf(songCount));
            TbTagPojo tbTagPojo = tagMap.get(tbMiddleTagPojo.getTagId());
            e.setGenre(tbTagPojo.getTagName());
            genres.add(e);
        }
        genresRes.setGenres(genres);
        return genresRes;
    }
    
    public ArtistsRes getArtists(SubsonicCommonReq req) {
        List<TbArtistPojo> list = tbArtistService.list();
        Map<Character, ArrayList<TbArtistPojo>> artistMap = list.parallelStream()
                                                                .collect(Collectors.toMap(tbArtistPojo -> getCharacterFirstLetter(tbArtistPojo.getArtistName()),
                                                                        ListUtil::toList,
                                                                        (o1, o2) -> {
                                                                            o2.addAll(o1);
                                                                            return o2;
                                                                        }));
        
        
        ArtistsRes artistsRes = new ArtistsRes();
        ArtistsRes.Artists indexes = new ArtistsRes.Artists();
        ArrayList<ArtistsRes.Index> index = new ArrayList<>();
        
        for (Map.Entry<Character, ArrayList<TbArtistPojo>> entryKey : artistMap.entrySet()) {
            ArtistsRes.Index e = new ArtistsRes.Index();
            e.setName(Objects.toString(entryKey.getKey()));
            ArrayList<ArtistsRes.Artist> artist = new ArrayList<>();
            Map<Long, Integer> artistAlbumCountMap = qukuService.getArtistAlbumCountByArtistIds(entryKey.getValue()
                                                                                                        .parallelStream()
                                                                                                        .map(TbArtistPojo::getId)
                                                                                                        .toList());
            for (TbArtistPojo tbArtistPojo : entryKey.getValue()) {
                ArtistsRes.Artist e1 = new ArtistsRes.Artist();
                e1.setId(String.valueOf(tbArtistPojo.getId()));
                e1.setName(tbArtistPojo.getArtistName());
                e1.setArtistImageUrl(remoteStorePicService.getArtistPicUrl(tbArtistPojo.getId()));
                e1.setCoverArt(String.valueOf(tbArtistPojo.getId()));
                e1.setUserRating(String.valueOf(0));
                e1.setAlbumCount(String.valueOf(artistAlbumCountMap.get(tbArtistPojo.getId()) == null ? 0 : artistAlbumCountMap.get(tbArtistPojo.getId())));
                artist.add(e1);
            }
            e.setArtist(artist);
            index.add(e);
        }
        indexes.setIndex(index);
        artistsRes.setArtists(indexes);
        return artistsRes;
    }
    
    public ArtistRes getArtist(SubsonicCommonReq req, String id) {
        ArtistRes artistsRes = new ArtistRes();
        TbArtistPojo artist = tbArtistService.getById(Long.parseLong(id));
        
        ArtistRes.Artist artistRes = new ArtistRes.Artist();
        artistRes.setName(artist.getArtistName());
        artistRes.setId(String.valueOf(artist.getId()));
        artistRes.setCoverArt(String.valueOf(artist.getId()));
        artistRes.setAlbumCount(String.valueOf(qukuService.getArtistAlbumCountByArtistId(artist.getId())));
        
        List<AlbumConvert> albumListByArtistIds = qukuService.getAlbumByArtistIds(Collections.singletonList(artist.getId()));
        List<Long> albumIds = albumListByArtistIds.parallelStream()
                                                  .map(TbAlbumPojo::getId)
                                                  .toList();
        Map<Long, Integer> albumDurationCountMap = qukuService.getAlbumDurationCount(albumIds);
        ArrayList<ArtistRes.Album> album = new ArrayList<>();
        Map<Long, Integer> albumMusicCountMap = qukuService.getAlbumMusicCountByMapAlbumId(albumIds);
        for (AlbumConvert albumListByArtistId : albumListByArtistIds) {
            ArtistRes.Album e = new ArtistRes.Album();
            e.setId(String.valueOf(albumListByArtistId.getId()));
            e.setName(albumListByArtistId.getAlbumName());
            e.setCoverArt(albumListByArtistId.getPicUrl());
            e.setParent(String.valueOf(artist.getId()));
            e.setIsDir(String.valueOf(true));
            e.setTitle(albumListByArtistId.getAlbumName());
            e.setAlbum(albumListByArtistId.getAlbumName());
            e.setArtist(artist.getArtistName());
            e.setYear(String.valueOf(albumListByArtistId.getPublishTime().getYear()));
            List<TbTagPojo> labelAlbumGenre = qukuService.getLabelAlbumGenre(albumListByArtistId.getId());
            if (CollUtil.isNotEmpty(labelAlbumGenre)) {
                e.setGenre(Optional.ofNullable(labelAlbumGenre.get(0)).orElse(new TbTagPojo()).getTagName());
            }
            e.setCoverArt(String.valueOf(albumListByArtistId.getId()));
            e.setDuration(String.valueOf(albumDurationCountMap.get(albumListByArtistId.getId())));
            // 播放数量
            e.setPlayCount(String.valueOf(0));
            e.setPlayed(new Date().toString());
            e.setCreated(LocalDateTimeUtil.format(albumListByArtistId.getPublishTime(), DatePattern.UTC_MS_PATTERN));
            e.setArtistId(String.valueOf(artist.getId()));
            e.setUserRating(String.valueOf(0));
            e.setSongCount(String.valueOf(albumMusicCountMap.get(albumListByArtistId.getId())));
            e.setIsVideo(String.valueOf(false));
            
            album.add(e);
        }
        artistRes.setAlbum(album);
        
        artistsRes.setArtist(artistRes);
        return artistsRes;
    }
    
    public VideosRes getVideos() {
        return new VideosRes();
    }
    
    public VideoInfoRes getVideoInfo(Long id) {
        return new VideoInfoRes();
    }
    
    public ArtistInfoRes getArtistInfo(SubsonicCommonReq req, Long id, Integer count, Boolean includeNotPresent) {
        ArtistInfoRes artistInfoRes = new ArtistInfoRes();
        ArtistInfoRes.ArtistInfo artistInfo = new ArtistInfoRes.ArtistInfo();
        TbArtistPojo artistPojo = tbArtistService.getById(id);
        if (Objects.nonNull(artistPojo)) {
            artistInfo.setLastFmUrl("https://www.last.fm");
            artistInfo.setBiography(artistPojo.getIntroduction());
            artistInfo.setSmallImageUrl(remoteStorePicService.getArtistPicUrl(artistPojo.getId()));
            artistInfo.setMediumImageUrl(remoteStorePicService.getArtistPicUrl(artistPojo.getId()));
            artistInfo.setLargeImageUrl(remoteStorePicService.getArtistPicUrl(artistPojo.getId()));
            artistInfoRes.setArtistInfo(artistInfo);
            return artistInfoRes;
        }
        TbAlbumPojo albumPojo = albumService.getById(id);
        if (Objects.nonNull(albumPojo)) {
            List<ArtistConvert> albumArtistListByAlbumIds = qukuService.getArtistByAlbumIds(albumPojo.getId());
            if (CollUtil.isNotEmpty(albumArtistListByAlbumIds)) {
                ArtistConvert artistConvert = albumArtistListByAlbumIds.get(0);
                artistInfo.setLastFmUrl("https://www.last.fm");
                artistInfo.setBiography(artistConvert.getIntroduction());
                artistInfo.setSmallImageUrl(remoteStorePicService.getArtistPicUrl(artistConvert.getId()));
                artistInfo.setMediumImageUrl(remoteStorePicService.getArtistPicUrl(artistConvert.getId()));
                artistInfo.setLargeImageUrl(remoteStorePicService.getArtistPicUrl(artistConvert.getId()));
                artistInfoRes.setArtistInfo(artistInfo);
                return artistInfoRes;
            }
        }
        TbMusicPojo byId = musicService.getById(id);
        List<TbMusicArtistPojo> list = tbMusicArtistService.list(Wrappers.<TbMusicArtistPojo>lambdaQuery()
                                                                         .eq(TbMusicArtistPojo::getMusicId, byId.getId()));
        if (CollUtil.isNotEmpty(list)) {
            TbMusicArtistPojo tbMusicArtistPojo = list.get(0);
            TbArtistPojo tbArtistPojo = tbArtistService.getById(tbMusicArtistPojo.getArtistId());
            artistInfo.setLastFmUrl("https://www.last.fm");
            artistInfo.setBiography(tbArtistPojo.getIntroduction());
            artistInfo.setSmallImageUrl(remoteStorePicService.getArtistPicUrl(tbArtistPojo.getId()));
            artistInfo.setMediumImageUrl(remoteStorePicService.getArtistPicUrl(tbArtistPojo.getId()));
            artistInfo.setLargeImageUrl(remoteStorePicService.getArtistPicUrl(tbArtistPojo.getId()));
            artistInfoRes.setArtistInfo(artistInfo);
            return artistInfoRes;
        }
        return artistInfoRes;
    }
    
    public ArtistInfo2Res getArtistInfo2(SubsonicCommonReq req, Long id, Integer count, Boolean includeNotPresent) {
        ArtistInfo2Res artistInfoRes = new ArtistInfo2Res();
        ArtistInfo2Res.ArtistInfo2 artistInfo = new ArtistInfo2Res.ArtistInfo2();
        TbArtistPojo artistPojo = tbArtistService.getById(id);
        if (Objects.nonNull(artistPojo)) {
            artistInfo.setLastFmUrl("https://www.last.fm");
            artistInfo.setBiography(artistPojo.getIntroduction());
            artistInfo.setSmallImageUrl(remoteStorePicService.getArtistPicUrl(artistPojo.getId()));
            artistInfo.setMediumImageUrl(remoteStorePicService.getArtistPicUrl(artistPojo.getId()));
            artistInfo.setLargeImageUrl(remoteStorePicService.getArtistPicUrl(artistPojo.getId()));
            artistInfoRes.setArtistInfo2(artistInfo);
            return artistInfoRes;
        }
        TbAlbumPojo albumPojo = albumService.getById(id);
        if (Objects.nonNull(albumPojo)) {
            List<ArtistConvert> albumArtistListByAlbumIds = qukuService.getArtistByAlbumIds(albumPojo.getId());
            if (CollUtil.isNotEmpty(albumArtistListByAlbumIds)) {
                ArtistConvert artistConvert = albumArtistListByAlbumIds.get(0);
                artistInfo.setLastFmUrl("https://www.last.fm");
                artistInfo.setBiography(artistConvert.getIntroduction());
                artistInfo.setSmallImageUrl(remoteStorePicService.getArtistPicUrl(artistConvert.getId()));
                artistInfo.setMediumImageUrl(remoteStorePicService.getArtistPicUrl(artistConvert.getId()));
                artistInfo.setLargeImageUrl(remoteStorePicService.getArtistPicUrl(artistConvert.getId()));
                artistInfoRes.setArtistInfo2(artistInfo);
                return artistInfoRes;
            }
        }
        TbMusicPojo byId = musicService.getById(id);
        List<TbMusicArtistPojo> list = tbMusicArtistService.list(Wrappers.<TbMusicArtistPojo>lambdaQuery()
                                                                         .eq(TbMusicArtistPojo::getMusicId, byId.getId()));
        if (CollUtil.isNotEmpty(list)) {
            TbMusicArtistPojo tbMusicArtistPojo = list.get(0);
            TbArtistPojo tbArtistPojo = tbArtistService.getById(tbMusicArtistPojo.getArtistId());
            artistInfo.setLastFmUrl("https://www.last.fm");
            artistInfo.setBiography(tbArtistPojo.getIntroduction());
            artistInfo.setSmallImageUrl(remoteStorePicService.getArtistPicUrl(tbArtistPojo.getId()));
            artistInfo.setMediumImageUrl(remoteStorePicService.getArtistPicUrl(tbArtistPojo.getId()));
            artistInfo.setLargeImageUrl(remoteStorePicService.getArtistPicUrl(tbArtistPojo.getId()));
            artistInfoRes.setArtistInfo2(artistInfo);
            return artistInfoRes;
        }
        return artistInfoRes;
    }
    
    public AlbumRes getAlbum(Long id) {
        TbAlbumPojo albumPojo = albumService.getById(id);
        Album album = new Album();
        album.setId(String.valueOf(albumPojo.getId()));
        album.setName(albumPojo.getAlbumName());
        List<ArtistConvert> artistListByAlbumIds = qukuService.getArtistByAlbumIds(albumPojo.getId());
        ArtistConvert artistPojo = CollUtil.isEmpty(artistListByAlbumIds) ? new ArtistConvert() : artistListByAlbumIds.get(0);
        album.setArtist(artistPojo.getArtistName());
        album.setArtistId(String.valueOf(artistPojo.getId()));
        album.setCoverArt(String.valueOf(albumPojo.getId()));
        album.setSongCount(qukuService.getAlbumMusicCountByAlbumId(albumPojo.getId()));
        album.setPlayCount(0);
        album.setPlayed(albumPojo.getUpdateTime().toString());
        album.setCreated(albumPojo.getCreateTime().toString());
        album.setYear(albumPojo.getPublishTime().getYear());
        album.setStarred(albumPojo.getUpdateTime().toString());
        
        ArrayList<SongItem> song = new ArrayList<>();
        List<MusicConvert> musicListByAlbumId = qukuService.getMusicListByAlbumId(id);
        List<ArtistConvert> artistListByAlbumIds1 = qukuService.getArtistByAlbumIds(id);
        ArtistConvert tbArtistPojo = CollUtil.isEmpty(artistListByAlbumIds1) ? new ArtistConvert() : artistListByAlbumIds1.get(0);
        Map<Long, List<TbResourcePojo>> musicMapUrl = qukuService.getMusicPathMap(musicListByAlbumId.stream()
                                                                                                    .map(TbMusicPojo::getId)
                                                                                                    .collect(Collectors.toSet()));
        int duration = 0;
        for (TbMusicPojo musicPojo : musicListByAlbumId) {
            SongItem e = new SongItem();
            e.setId(String.valueOf(musicPojo.getId()));
            e.setParent(String.valueOf(albumPojo.getId()));
            e.setIsDir(false);
            e.setTitle(musicPojo.getMusicName());
            e.setAlbum(albumPojo.getAlbumName());
            e.setAlbumId(String.valueOf(albumPojo.getId()));
            e.setArtist(tbArtistPojo.getArtistName());
            e.setArtistId(String.valueOf(tbArtistPojo.getId()));
            e.setTrack(1);
            e.setYear(albumPojo.getPublishTime().getYear());
            e.setCoverArt(String.valueOf(musicPojo.getId()));
            List<TbResourcePojo> musicPath = musicMapUrl.get(musicPojo.getId());
            TbResourcePojo tbResourcePojo = resourceReturnStrategyUtil.handleResource(musicPath);
            if (Objects.isNull(tbResourcePojo)) {
                continue;
            }
            e.setSize(Math.toIntExact(tbResourcePojo.getSize() == null ? 0 : tbResourcePojo.getSize()));
            e.setSuffix(tbResourcePojo.getEncodeType());
            e.setBitRate(tbResourcePojo.getRate());
            e.setPath(tbResourcePojo.getPath());
            e.setContentType(URLConnection.guessContentTypeFromName(tbResourcePojo.getPath()));
            
            e.setStarred(musicPojo.getUpdateTime().toString());
            e.setDuration(Optional.ofNullable(musicPojo.getTimeLength()).orElse(0) / 1000);
            e.setPlayCount(0);
            e.setPlayed(musicPojo.getCreateTime().toString());
            e.setType("music");
            e.setIsVideo(false);
            e.setCreated(albumPojo.getPublishTime().toString());
            song.add(e);
            duration += musicPojo.getTimeLength();
        }
        album.setDuration(duration / 1000);
        album.setSong(song);
        
        
        AlbumRes albumRes = new AlbumRes();
        albumRes.setAlbum(album);
        return albumRes;
    }
    
    /**
     * 专辑信息
     *
     * @param req 通用请求
     * @param id  专辑或歌曲ID
     * @return 专辑信息
     */
    public AlbumInfoRes getAlbumInfo(SubsonicCommonReq req, Long id) {
        AlbumInfoRes res = new AlbumInfoRes();
        AlbumInfoRes.AlbumInfo albumInfo = new AlbumInfoRes.AlbumInfo();
        TbAlbumPojo albumPojo = albumService.getById(id);
        if (Objects.nonNull(albumPojo)) {
            albumInfo.setNotes(albumPojo.getDescription());
            albumInfo.setLastFmUrl(URL);
            String albumPicUrl = remoteStorePicService.getAlbumPicUrl(albumPojo.getId());
            albumInfo.setLargeImageUrl(albumPicUrl);
            albumInfo.setSmallImageUrl(albumPicUrl);
            albumInfo.setMediumImageUrl(albumPicUrl);
            res.setAlbumInfo(albumInfo);
            return res;
        }
        TbMusicPojo byId = musicService.getById(id);
        if (Objects.nonNull(byId)) {
            Long albumId = byId.getAlbumId();
            if (Objects.nonNull(albumId)) {
                TbAlbumPojo albumPojo1 = albumService.getById(albumId);
                albumInfo.setNotes(albumPojo1.getDescription());
                albumInfo.setLastFmUrl(URL);
                String albumPicUrl = remoteStorePicService.getAlbumPicUrl(albumPojo1.getId());
                albumInfo.setLargeImageUrl(albumPicUrl);
                albumInfo.setSmallImageUrl(albumPicUrl);
                albumInfo.setMediumImageUrl(albumPicUrl);
                res.setAlbumInfo(albumInfo);
                return res;
            }
        }
        return res;
    }
    
    public AlbumInfo2Res getAlbumInfo2(SubsonicCommonReq req, Long id) {
        AlbumInfo2Res res = new AlbumInfo2Res();
        AlbumInfo2Res.AlbumInfo albumInfo = new AlbumInfo2Res.AlbumInfo();
        TbAlbumPojo albumPojo = albumService.getById(id);
        if (Objects.nonNull(albumPojo)) {
            albumInfo.setNotes(albumPojo.getDescription());
            albumInfo.setLastFmUrl("http://www.last.fm");
            String albumPicUrl = remoteStorePicService.getAlbumPicUrl(albumPojo.getId());
            albumInfo.setLargeImageUrl(albumPicUrl);
            albumInfo.setSmallImageUrl(albumPicUrl);
            albumInfo.setMediumImageUrl(albumPicUrl);
            res.setAlbumInfo(albumInfo);
            return res;
        }
        TbMusicPojo byId = musicService.getById(id);
        if (Objects.nonNull(byId)) {
            Long albumId = byId.getAlbumId();
            if (Objects.nonNull(albumId)) {
                TbAlbumPojo albumPojo1 = albumService.getById(albumId);
                albumInfo.setNotes(albumPojo1.getDescription());
                albumInfo.setLastFmUrl("http://www.last.fm");
                String albumPicUrl = remoteStorePicService.getAlbumPicUrl(albumPojo1.getId());
                albumInfo.setLargeImageUrl(albumPicUrl);
                albumInfo.setSmallImageUrl(albumPicUrl);
                albumInfo.setMediumImageUrl(albumPicUrl);
                res.setAlbumInfo(albumInfo);
                return res;
            }
        }
        return res;
    }
    
    public SimilarSongsRes getSimilarSongs(SubsonicCommonReq req, Long id, Integer count) {
        SimilarSongsRes res = new SimilarSongsRes();
        SimilarSongsRes.SimilarSongs similarSongs = new SimilarSongsRes.SimilarSongs();
        List<MusicConvert> musicConverts = qukuService.randomMusicList(count);
        List<SimilarSongsRes.Song> songs = new ArrayList<>();
        
        List<Long> musicIds = musicConverts.parallelStream()
                                           .map(MusicConvert::getId)
                                           .toList();
        List<Long> albumIds = musicConverts.parallelStream()
                                           .map(MusicConvert::getAlbumId)
                                           .toList();
        Map<Long, AlbumConvert> musicAlbumByMusicIdToMap = qukuService.getMusicAlbumByAlbumIdToMap(albumIds);
        Map<Long, List<ArtistConvert>> musicArtistByMusicIdToMap = qukuService.getArtistByMusicIdToMap(musicIds);
        Map<Long, List<TbResourcePojo>> resourceMap = tbResourceService.getResourceMap(musicIds);
        for (MusicConvert musicConvert : musicConverts) {
            SimilarSongsRes.Song e = new SimilarSongsRes.Song();
            e.setId(Math.toIntExact(musicConvert.getId()));
            e.setTitle(musicConvert.getMusicName());
            e.setDir(false);
            e.setTitle(musicConvert.getMusicName());
            Optional<AlbumConvert> albumConvertOpt = Optional.ofNullable(musicAlbumByMusicIdToMap.get(musicConvert.getId()));
            if (albumConvertOpt.isPresent()) {
                e.setParent(String.valueOf(albumConvertOpt.get().getId()));
                e.setAlbum(albumConvertOpt.get().getAlbumName());
                e.setAlbumId(String.valueOf(albumConvertOpt.get().getId()));
                e.setYear(albumConvertOpt.get().getPublishTime().getYear());
            }
            Optional<List<ArtistConvert>> artistConverts = Optional.ofNullable(musicArtistByMusicIdToMap.get(musicConvert.getId()));
            if (artistConverts.isPresent()) {
                e.setArtist(artistConverts.get().get(0).getArtistName());
                e.setArtistId(String.valueOf(artistConverts.get().get(0).getId()));
            }
            e.setTrack(0);
            e.setCoverArt(musicConvert.getPicUrl());
            TbResourcePojo tbResourcePojo = resourceReturnStrategyUtil.handleResource(resourceMap.get(musicConvert.getId()));
            if (Objects.isNull(tbResourcePojo)) {
                continue;
            }
            e.setSize(tbResourcePojo.getSize());
            e.setContentType(URLConnection.guessContentTypeFromName(tbResourcePojo.getPath()));
            e.setSuffix(tbResourcePojo.getEncodeType());
            e.setBitRate(tbResourcePojo.getRate());
            e.setPath(tbResourcePojo.getPath());
            
            e.setDuration(musicConvert.getTimeLength() / 1000);
            e.setPlayCount(0);
            e.setPlayed(new Date());
            e.setDiscNumber(0);
            e.setCreated(LocalDateTimeUtil.format(musicConvert.getCreateTime(), DatePattern.UTC_PATTERN));
            e.setType("music");
            e.setVideo(false);
            
            
            songs.add(e);
        }
        
        similarSongs.setSongs(songs);
        res.setSimilarSongs(similarSongs);
        return res;
    }
    
    public SimilarSongs2Res getSimilarSongs2(SubsonicCommonReq req, Long id, Integer count) {
        SimilarSongs2Res res = new SimilarSongs2Res();
        SimilarSongs2Res.SimilarSongs similarSongs = new SimilarSongs2Res.SimilarSongs();
        List<MusicConvert> musicConverts = qukuService.randomMusicList(count);
        List<SimilarSongs2Res.Song> songs = new ArrayList<>();
        
        List<Long> musicIds = musicConverts.parallelStream()
                                           .map(MusicConvert::getId)
                                           .toList();
        Collection<Long> albumIds = musicConverts.parallelStream()
                                                 .map(MusicConvert::getAlbumId)
                                                 .collect(Collectors.toSet());
        Map<Long, AlbumConvert> musicAlbumByMusicIdToMap = qukuService.getMusicAlbumByAlbumIdToMap(albumIds);
        Map<Long, List<ArtistConvert>> musicArtistByMusicIdToMap = qukuService.getArtistByMusicIdToMap(musicIds);
        Map<Long, List<TbResourcePojo>> resourceMap = tbResourceService.getResourceMap(musicIds);
        for (MusicConvert musicConvert : musicConverts) {
            SimilarSongs2Res.Song e = new SimilarSongs2Res.Song();
            e.setId(String.valueOf(musicConvert.getId()));
            e.setTitle(musicConvert.getMusicName());
            e.setIsDir(false);
            e.setTitle(musicConvert.getMusicName());
            Optional<AlbumConvert> albumConvertOpt = Optional.ofNullable(musicAlbumByMusicIdToMap.get(musicConvert.getId()));
            if (albumConvertOpt.isPresent()) {
                e.setParent(String.valueOf(albumConvertOpt.get().getId()));
                e.setAlbum(albumConvertOpt.get().getAlbumName());
                e.setAlbumId(String.valueOf(albumConvertOpt.get().getId()));
                e.setYear(albumConvertOpt.get().getPublishTime().getYear());
            }
            Optional<List<ArtistConvert>> artistConverts = Optional.ofNullable(musicArtistByMusicIdToMap.get(musicConvert.getId()));
            if (artistConverts.isPresent()) {
                e.setArtist(artistConverts.get().get(0).getArtistName());
                e.setArtistId(String.valueOf(artistConverts.get().get(0).getId()));
            }
            e.setTrack(0);
            e.setCoverArt(musicConvert.getPicUrl());
            TbResourcePojo tbResourcePojo = resourceReturnStrategyUtil.handleResource(resourceMap.get(musicConvert.getId()));
            if (Objects.isNull(tbResourcePojo)) {
                continue;
            }
            e.setSize(tbResourcePojo.getSize());
            e.setContentType(URLConnection.guessContentTypeFromName(tbResourcePojo.getPath()));
            e.setSuffix(tbResourcePojo.getEncodeType());
            e.setBitRate(tbResourcePojo.getRate());
            e.setPath(tbResourcePojo.getPath());
            
            e.setDuration(musicConvert.getTimeLength() / 1000);
            e.setPlayCount(0);
            e.setPlayed(new Date());
            e.setDiscNumber(0);
            e.setCreated(LocalDateTimeUtil.format(musicConvert.getCreateTime(), DatePattern.UTC_PATTERN));
            e.setType("music");
            e.setIsVideo(false);
            
            
            songs.add(e);
        }
        
        similarSongs.setSongs(songs);
        res.setSimilarSongs(similarSongs);
        return res;
    }
    
    public TopSongsRes getTopSongs(SubsonicCommonReq req, String artistName, Integer count) {
        TopSongsRes res = new TopSongsRes();
        Page<TbArtistPojo> page = new Page<>(1, count);
        tbArtistService.page(page,
                Wrappers.<TbArtistPojo>lambdaQuery().orderByDesc(TbArtistPojo::getCreateTime).like(TbArtistPojo::getArtistName, artistName));
        Map<Long, List<TbMusicPojo>> musicMap = qukuService.getMusicMapByArtistId(page.getRecords().parallelStream().map(TbArtistPojo::getId).toList());
        Map<Long, TbArtistPojo> artistMap = page.getRecords()
                                                .parallelStream()
                                                .collect(Collectors.toMap(TbArtistPojo::getId, tbArtistPojo -> tbArtistPojo));
        
        ArrayList<TopSongsRes.TopSongs> list = new ArrayList<>();
        for (Map.Entry<Long, List<TbMusicPojo>> longListEntry : musicMap.entrySet()) {
            List<TbMusicPojo> value = longListEntry.getValue();
            Map<Long, AlbumConvert> musicAlbumByMusicIdToMap = qukuService.getMusicAlbumByAlbumIdToMap(value.parallelStream()
                                                                                                            .map(TbMusicPojo::getAlbumId)
                                                                                                            .toList());
            Map<Long, List<TbResourcePojo>> resourceMap = tbResourceService.getResourceMap(value.parallelStream().map(TbMusicPojo::getId).toList());
            
            for (TbMusicPojo tbMusicPojo : value) {
                TopSongsRes.TopSongs topSongs = new TopSongsRes.TopSongs();
                topSongs.setDiscNumber(0);
                topSongs.setType("music");
                topSongs.setIsDir(false);
                topSongs.setTitle(tbMusicPojo.getMusicName());
                topSongs.setTrack(0);
                topSongs.setCoverArt(String.valueOf(tbMusicPojo.getId()));
                topSongs.setDuration(tbMusicPojo.getTimeLength());
                topSongs.setCreated(LocalDateTimeUtil.format(tbMusicPojo.getCreateTime(), DatePattern.UTC_PATTERN));
                topSongs.setIsVideo(false);
                
                TbResourcePojo tbResourcePojo = resourceReturnStrategyUtil.handleResource(resourceMap.get(tbMusicPojo.getId()));
                if (Objects.isNull(tbResourcePojo)) {
                    continue;
                }
                topSongs.setBitRate(tbResourcePojo.getRate());
                topSongs.setPath(tbResourcePojo.getPath());
                topSongs.setSize(tbResourcePojo.getSize());
                topSongs.setContentType(URLConnection.guessContentTypeFromName(tbResourcePojo.getPath()));
                topSongs.setSuffix(tbResourcePojo.getEncodeType());
                topSongs.setPlayCount(0);
                topSongs.setPlayed(new Date());
                
                AlbumConvert albumConvert = musicAlbumByMusicIdToMap.get(tbMusicPojo.getAlbumId());
                if (Objects.nonNull(albumConvert)) {
                    topSongs.setAlbum(albumConvert.getAlbumName());
                    String albumId = String.valueOf(albumConvert.getId());
                    topSongs.setAlbumId(albumId);
                    topSongs.setParent(albumId);
                    topSongs.setYear(albumConvert.getPublishTime().getYear());
                }
                
                TbArtistPojo tbArtistPojo = artistMap.get(longListEntry.getKey());
                if (Objects.nonNull(tbArtistPojo)) {
                    topSongs.setArtist(tbArtistPojo.getArtistName());
                    topSongs.setArtistId(String.valueOf(tbArtistPojo.getId()));
                }
                
                list.add(topSongs);
            }
        }
        
        res.setTopSongs(list);
        return res;
    }
}
