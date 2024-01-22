package org.api.subsonic.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.PageUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.api.common.service.QukuAPI;
import org.api.subsonic.common.SubsonicCommonReq;
import org.api.subsonic.config.SubsonicConfig;
import org.api.subsonic.model.res.albumlist.AlbumListRes;
import org.api.subsonic.model.res.albumlist2.AlbumItem;
import org.api.subsonic.model.res.albumlist2.AlbumList2;
import org.api.subsonic.model.res.albumlist2.AlbumList2Res;
import org.api.subsonic.model.res.nowplaying.NowPlayingRes;
import org.api.subsonic.model.res.randomsongs.RandomSongsRes;
import org.api.subsonic.model.res.songsbygenre.SongsByGenreRes;
import org.api.subsonic.model.res.starred.StarredRes;
import org.api.subsonic.model.res.starred2.Starred2Res;
import org.api.subsonic.utils.spring.SubsonicResourceReturnStrategyUtil;
import org.core.common.constant.HistoryConstant;
import org.core.common.constant.PlayListTypeConstant;
import org.core.common.constant.TargetTagConstant;
import org.core.mybatis.iservice.*;
import org.core.mybatis.model.convert.AlbumConvert;
import org.core.mybatis.model.convert.ArtistConvert;
import org.core.mybatis.model.convert.CollectConvert;
import org.core.mybatis.model.convert.MusicConvert;
import org.core.mybatis.pojo.*;
import org.core.service.AccountService;
import org.core.service.RemoteStorePicService;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service(SubsonicConfig.SUBSONIC + "SongListsApi")
public class SongListsApi {
    
    private final QukuAPI qukuService;
    
    private final TbCollectMusicService tbCollectMusicService;
    
    private final TbMusicService tbMusicService;
    
    private final TbAlbumService albumService;
    
    private final TbArtistService tbArtistService;
    
    private final TbCollectService tbCollectService;
    
    private final TbMvService tbMvService;
    
    private final TbHistoryService tbHistoryService;
    
    private final AccountService accountService;
    
    private final SubsonicResourceReturnStrategyUtil subsonicResourceReturnStrategyUtil;
    
    private final TbMiddleTagService tbMiddleTagService;
    
    private final TbTagService tbTagService;
    
    private final TbUserAlbumService tbUserAlbumService;
    
    private final TbUserArtistService tbUserArtistService;
    
    private final RemoteStorePicService remoteStorePicService;
    
    public SongListsApi(TbCollectMusicService tbCollectMusicService, QukuAPI qukuService, TbMusicService tbMusicService, TbUserAlbumService tbUserAlbumService, TbUserArtistService tbUserArtistService, TbAlbumService albumService, TbTagService tbTagService, TbArtistService tbArtistService, TbCollectService tbCollectService, TbMvService tbMvService, TbHistoryService tbHistoryService, AccountService accountService, SubsonicResourceReturnStrategyUtil subsonicResourceReturnStrategyUtil, TbMiddleTagService tbMiddleTagService, RemoteStorePicService remoteStorePicService) {
        this.tbCollectMusicService = tbCollectMusicService;
        this.qukuService = qukuService;
        this.tbMusicService = tbMusicService;
        this.tbUserAlbumService = tbUserAlbumService;
        this.tbUserArtistService = tbUserArtistService;
        this.albumService = albumService;
        this.tbTagService = tbTagService;
        this.tbArtistService = tbArtistService;
        this.tbCollectService = tbCollectService;
        this.tbMvService = tbMvService;
        this.tbHistoryService = tbHistoryService;
        this.accountService = accountService;
        this.subsonicResourceReturnStrategyUtil = subsonicResourceReturnStrategyUtil;
        this.tbMiddleTagService = tbMiddleTagService;
        this.remoteStorePicService = remoteStorePicService;
    }
    
    
    /**
     * 返回一个随机的，最新的，最高评级等列表
     *
     * @param req           通用请求
     * @param type          随机的，最新的，最高评级等
     * @param size          分页返回的数量
     * @param offset        分页偏移量
     * @param fromYear      范围内的第一年。如果 fromYear > toYear ，则返回一个倒序列表。
     * @param toYear        结束年份
     * @param genre         流派的名称，例如，“摇滚”
     * @param musicFolderId （自1.11.0起）仅返回音乐文件夹中具有给定ID的专辑。参见 getMusicFolders 。
     * @return 返回一个随机的，最新的，最高评级等列表
     */
    public AlbumList2Res getAlbumList2(SubsonicCommonReq req, String type, Long size, Long offset, Long fromYear, Long toYear, Long genre, Long musicFolderId) {
        List<TbAlbumPojo> albumList = handleAlbums(req, type, offset, size, fromYear, toYear);
        if (CollUtil.isEmpty(albumList)) {
            return new AlbumList2Res();
        }
        List<AlbumItem> albumArrayList = new ArrayList<>();
        Set<Long> albumIds = albumList.stream().map(TbAlbumPojo::getId).collect(Collectors.toSet());
        Map<Long, List<ArtistConvert>> artistMapByAlbumIds = qukuService.getAlbumArtistMapByAlbumIds(albumIds);
        Map<Long, Integer> albumMusicCountByMapAlbumId = qukuService.getAlbumMusicCountByMapAlbumId(albumIds);
        for (TbAlbumPojo albumPojo : albumList) {
            AlbumItem e = new AlbumItem();
            e.setId(String.valueOf(albumPojo.getId()));
            e.setAlbum(albumPojo.getAlbumName());
            e.setTitle(albumPojo.getAlbumName());
            e.setName(albumPojo.getAlbumName());
            e.setYear(albumPojo.getPublishTime().getYear());
            e.setSongCount(albumMusicCountByMapAlbumId.get(albumPojo.getId()));
            List<ArtistConvert> artistListByAlbumIds = artistMapByAlbumIds.get(albumPojo.getId());
            TbArtistPojo pojo = CollUtil.isNotEmpty(artistListByAlbumIds) ? artistListByAlbumIds.get(0) : new TbArtistPojo();
            e.setArtist(pojo.getArtistName());
            e.setArtistId(String.valueOf(pojo.getId()));
            e.setCoverArt(String.valueOf(albumPojo.getId()));
            albumArrayList.add(e);
        }
        // 歌手排序
        if (StringUtils.equalsIgnoreCase(type, "alphabeticalByArtist")) {
            albumArrayList = albumArrayList.parallelStream().sorted((o1, o2) -> StringUtils.compare(o1.getArtist(), o2.getArtist())).toList();
        }
        
        AlbumList2Res albumRes = new AlbumList2Res();
        AlbumList2 albumList2 = new AlbumList2();
        albumList2.setAlbum(albumArrayList);
        albumRes.setAlbumList2(albumList2);
        return albumRes;
    }
    
    @NotNull
    private List<TbAlbumPojo> handleAlbums(SubsonicCommonReq req, String type, Long offset, Long size, Long fromYear, Long toYear) {
        Page<TbAlbumPojo> page = new Page<>(offset, size);
        String userName = req.getU();
        boolean yearFlag = Objects.nonNull(fromYear) && Objects.nonNull(toYear);
        SysUserPojo userByName = accountService.getUserByName(userName);
        switch (type) {
            // 最新添加
            case "newest":
                LambdaQueryWrapper<TbAlbumPojo> between = Wrappers.<TbAlbumPojo>lambdaQuery()
                                                                  .between(yearFlag, TbAlbumPojo::getPublishTime, new Date(fromYear), new Date(fromYear));
                if (fromYear > toYear) {
                    between.orderByDesc(TbAlbumPojo::getCreateTime);
                } else {
                    between.orderByAsc(TbAlbumPojo::getCreateTime);
                }
                albumService.page(page, between);
                break;
            // 播放最多
            case "frequent":
                List<TbHistoryPojo> frequentAlbum = tbHistoryService.getFrequentAlbum(userByName.getId(), offset, size);
                if (CollUtil.isEmpty(frequentAlbum)) {
                    return new ArrayList<>();
                }
                page.setRecords(albumService.listByIds(frequentAlbum.parallelStream().map(TbHistoryPojo::getMiddleId).toList()));
                break;
            // 最近播放
            case "recent":
                List<TbHistoryPojo> recentAlbum = tbHistoryService.getRecentAlbum(userByName.getId(), offset, size);
                if (CollUtil.isEmpty(recentAlbum)) {
                    return new ArrayList<>();
                }
                page.setRecords(albumService.listByIds(recentAlbum.parallelStream().map(TbHistoryPojo::getMiddleId).toList()));
                break;
            // 收藏
            case "starred":
                List<CollectConvert> userPlayList = qukuService.getUserPlayList(userByName.getId(), Collections.singleton(PlayListTypeConstant.LIKE));
                List<TbCollectMusicPojo> tbCollectMusicPojos = tbCollectMusicService.getCollectIds(userPlayList.stream()
                                                                                                               .map(TbCollectPojo::getId)
                                                                                                               .toList());
                if (CollUtil.isEmpty(tbCollectMusicPojos)) {
                    return new ArrayList<>();
                }
                List<Long> musicIds = tbCollectMusicPojos
                        .parallelStream()
                        .map(TbCollectMusicPojo::getMusicId)
                        .toList();
                if (CollUtil.isEmpty(musicIds)) {
                    return new ArrayList<>();
                }
                List<TbMusicPojo> tbMusicPojos = tbMusicService.listByIds(musicIds);
                List<Long> albumIds = tbMusicPojos.parallelStream().map(TbMusicPojo::getAlbumId).toList();
                page.setRecords(albumService.listByIds(albumIds));
                break;
            // 按专辑字母顺序排列
            case "alphabeticalByName":
                LambdaQueryWrapper<TbAlbumPojo> queryWrapper = Wrappers.<TbAlbumPojo>lambdaQuery().orderByDesc(TbAlbumPojo::getAlbumName);
                albumService.page(page, queryWrapper);
                break;
            // 按艺术家字母排序, 这个功能后面循环中实现实现
            case "alphabeticalByArtist":
                albumService.page(page);
                break;
            // 随机播放
            case "random":
            default:
                long count = albumService.count();
                int pageCount = PageUtil.totalPage(Math.toIntExact(count), Math.toIntExact(size));
                long randomOffset = RandomUtils.nextLong(0, pageCount);
                LambdaQueryWrapper<TbAlbumPojo> randomQueryWrapper = Wrappers.<TbAlbumPojo>lambdaQuery()
                                                                             .between(yearFlag,
                                                                                     TbAlbumPojo::getPublishTime,
                                                                                     new Date(fromYear),
                                                                                     new Date(fromYear));
                if (fromYear > toYear) {
                    randomQueryWrapper.orderByDesc(TbAlbumPojo::getPublishTime);
                } else {
                    randomQueryWrapper.orderByAsc(TbAlbumPojo::getPublishTime);
                }
                page = albumService.page(new Page<>(randomOffset, size), randomQueryWrapper);
                Collections.shuffle(page.getRecords());
        }
        return page.getRecords();
    }
    
    public void scrobble(SubsonicCommonReq req, Long id, Long timeStamp, Boolean submission) {
        String userName = req.getU();
        SysUserPojo userByName = accountService.getUserByName(userName);
        if (updateHistory(tbMusicService.count(Wrappers.<TbMusicPojo>lambdaQuery().eq(TbMusicPojo::getId, id)),
                userByName,
                id,
                HistoryConstant.MUSIC,
                timeStamp)) {
            return;
        }
        
        // 专辑
        if (updateHistory(albumService.count(Wrappers.<TbAlbumPojo>lambdaQuery().eq(TbAlbumPojo::getId, id)),
                userByName,
                id,
                HistoryConstant.ALBUM,
                timeStamp)) {
            return;
        }
        
        // 歌手
        if (updateHistory(tbArtistService.count(Wrappers.<TbArtistPojo>lambdaQuery().eq(TbArtistPojo::getId, id)),
                userByName,
                id,
                HistoryConstant.ARTIST,
                timeStamp)) {
            return;
        }
        
        // 歌单
        if (updateHistory(tbCollectService.count(Wrappers.<TbCollectPojo>lambdaQuery().eq(TbCollectPojo::getId, id)),
                userByName,
                id,
                HistoryConstant.PLAYLIST,
                timeStamp)) {
            return;
        }
        
        // MV
        if (updateHistory(tbMvService.count(Wrappers.<TbMvPojo>lambdaQuery().eq(TbMvPojo::getId, id)),
                userByName,
                id,
                HistoryConstant.MV,
                timeStamp)) {
            // TODO MV 后续处理
            return;
        }
    }
    
    private boolean updateHistory(long count, SysUserPojo userByName, Long id, Byte type, Long playTime) {
        // 音乐
        if (count > 0) {
            TbHistoryPojo historyPojo = tbHistoryService.getOne(Wrappers.<TbHistoryPojo>lambdaQuery()
                                                                        .eq(TbHistoryPojo::getUserId, userByName.getId())
                                                                        .eq(TbHistoryPojo::getMiddleId, id)
                                                                        .eq(TbHistoryPojo::getType, type));
            if (Objects.isNull(historyPojo)) {
                TbHistoryPojo entity = new TbHistoryPojo();
                entity.setCount(1);
                entity.setMiddleId(id);
                entity.setUserId(userByName.getId());
                entity.setPlayedTime(playTime);
                entity.setType(type);
                tbHistoryService.save(entity);
            } else {
                historyPojo.setCount(historyPojo.getCount() + 1);
                tbHistoryService.updateById(historyPojo);
            }
            return true;
        }
        return false;
    }
    
    /**
     * 返回一个随机的，最新的，最高评级等列表
     *
     * @param req           通用请求
     * @param type          随机的，最新的，最高评级等
     * @param size          分页返回的数量
     * @param offset        分页偏移量
     * @param fromYear      范围内的第一年。如果 fromYear > toYear ，则返回一个倒序列表。
     * @param toYear        结束年份
     * @param genre         流派的名称，例如，“摇滚”
     * @param musicFolderId （自1.11.0起）仅返回音乐文件夹中具有给定ID的专辑。参见 getMusicFolders 。
     * @return 返回一个随机的，最新的，最高评级等列表
     */
    public AlbumListRes getAlbumList(SubsonicCommonReq req, String type, Long size, Long offset, Long fromYear, Long toYear, Long genre, Long musicFolderId) {
        List<TbAlbumPojo> albumList = handleAlbums(req, type, offset, size, fromYear, toYear);
        if (CollUtil.isEmpty(albumList)) {
            return new AlbumListRes();
        }
        List<AlbumListRes.Album> albumArrayList = new ArrayList<>();
        Set<Long> albumIds = albumList.stream().map(TbAlbumPojo::getId).collect(Collectors.toSet());
        Map<Long, List<ArtistConvert>> artistMapByAlbumIds = qukuService.getAlbumArtistMapByAlbumIds(albumIds);
        Map<Long, Integer> albumMusicCountByMapAlbumId = qukuService.getAlbumMusicCountByMapAlbumId(albumIds);
        for (TbAlbumPojo albumPojo : albumList) {
            AlbumListRes.Album e = new AlbumListRes.Album();
            e.setId(String.valueOf(albumPojo.getId()));
            e.setAlbum(albumPojo.getAlbumName());
            e.setTitle(albumPojo.getAlbumName());
            e.setName(albumPojo.getAlbumName());
            e.setYear(albumPojo.getPublishTime().getYear());
            e.setSongCount(albumMusicCountByMapAlbumId.get(albumPojo.getId()));
            List<ArtistConvert> artistListByAlbumIds = artistMapByAlbumIds.get(albumPojo.getId());
            TbArtistPojo pojo = CollUtil.isNotEmpty(artistListByAlbumIds) ? artistListByAlbumIds.get(0) : new TbArtistPojo();
            e.setArtist(pojo.getArtistName());
            e.setArtistId(String.valueOf(pojo.getId()));
            e.setCoverArt(String.valueOf(albumPojo.getId()));
            albumArrayList.add(e);
        }
        // 歌手排序
        if (StringUtils.equalsIgnoreCase(type, "alphabeticalByArtist")) {
            albumArrayList = albumArrayList.parallelStream().sorted((o1, o2) -> StringUtils.compare(o1.getArtist(), o2.getArtist())).toList();
        }
        
        AlbumListRes albumRes = new AlbumListRes();
        AlbumListRes.AlbumList albumList2 = new AlbumListRes.AlbumList();
        albumList2.setAlbum(albumArrayList);
        albumRes.setAlbumList(albumList2);
        return albumRes;
    }
    
    public RandomSongsRes getRandomSongs(SubsonicCommonReq req, Long size, String genre, Long fromYear, Long toYear, Long musicFolderId) {
        RandomSongsRes res = new RandomSongsRes();
        
        List<MusicConvert> musicListByAlbumId = qukuService.randomMusicList(size.intValue(), genre, fromYear, toYear);
        
        List<Long> musicIds = musicListByAlbumId.parallelStream()
                                                .map(TbMusicPojo::getId)
                                                .toList();
        List<Long> albumIds = musicListByAlbumId.parallelStream()
                                                .map(TbMusicPojo::getId)
                                                .toList();
        Map<Long, List<ArtistConvert>> musicArtistByMusicIdToMap = qukuService.getMusicArtistByMusicIdToMap(musicIds);
        Map<Long, AlbumConvert> albumByMusicIdToMap = qukuService.getMusicAlbumByAlbumIdToMap(albumIds);
        Map<Long, List<TbResourcePojo>> musicMapUrl = qukuService.getMusicPathMap(musicListByAlbumId.stream()
                                                                                                    .map(TbMusicPojo::getId)
                                                                                                    .collect(Collectors.toSet()));
        Map<Long, List<TbTagPojo>> labelMusicGenre = qukuService.getLabelMusicGenre(musicIds);
        ArrayList<RandomSongsRes.Song> song = new ArrayList<>();
        for (TbMusicPojo musicPojo : musicListByAlbumId) {
            RandomSongsRes.Song e = new RandomSongsRes.Song();
            e.setId(String.valueOf(musicPojo.getId()));
            e.setParent(String.valueOf(musicPojo.getAlbumId()));
            e.setIsDir(false);
            e.setTitle(musicPojo.getMusicName());
            List<TbTagPojo> tbTagPojos = labelMusicGenre.get(musicPojo.getId());
            if (CollUtil.isNotEmpty(tbTagPojos)) {
                e.setGenre(tbTagPojos.get(0).getTagName());
            }
            AlbumConvert albumConvert = albumByMusicIdToMap.get(musicPojo.getId());
            if (Objects.nonNull(albumConvert)) {
                e.setAlbum(albumConvert.getAlbumName());
                e.setAlbumId(String.valueOf(albumConvert.getId()));
                e.setYear(String.valueOf(albumConvert.getPublishTime().getYear()));
            }
            List<ArtistConvert> artistConverts = musicArtistByMusicIdToMap.get(musicPojo.getId());
            if (CollUtil.isNotEmpty(artistConverts)) {
                ArtistConvert artistConvert = artistConverts.get(0);
                e.setArtist(artistConvert.getArtistName());
                e.setArtistId(String.valueOf(artistConvert.getId()));
            }
            e.setCoverArt(String.valueOf(musicPojo.getId()));
            e.setTrack(String.valueOf(0));
            List<TbResourcePojo> musicUrl = musicMapUrl.get(musicPojo.getId());
            TbResourcePojo tbResourcePojo = subsonicResourceReturnStrategyUtil.handleResource(musicUrl);
            if (Objects.nonNull(tbResourcePojo)) {
                e.setSize(String.valueOf(tbResourcePojo.getSize()));
                e.setSuffix(tbResourcePojo.getEncodeType());
                e.setTranscodedSuffix(tbResourcePojo.getEncodeType());
                e.setBitRate(String.valueOf(tbResourcePojo.getRate()));
                e.setPath(tbResourcePojo.getPath());
                if (StringUtils.equalsIgnoreCase(tbResourcePojo.getEncodeType(), "mp3")) {
                    e.setContentType("audio/mpeg");
                    e.setTranscodedContentType("audio/mpeg");
                } else {
                    e.setContentType("audio/" + tbResourcePojo.getEncodeType());
                    e.setTranscodedContentType("audio/" + tbResourcePojo.getEncodeType());
                }
            }
            e.setDuration(String.valueOf(Optional.ofNullable(musicPojo.getTimeLength()).orElse(0) / 1000));
            e.setPlayCount(0);
            e.setPlayed(new Date());
            e.setType("music");
            e.setIsVideo(false);
            e.setCreated(Date.from(musicPojo.getCreateTime().atZone(ZoneId.systemDefault()).toInstant()));
            song.add(e);
        }
        RandomSongsRes.RandomSongs randomSongs = new RandomSongsRes.RandomSongs();
        randomSongs.setSong(song);
        res.setRandomSongs(randomSongs);
        return res;
    }
    
    public SongsByGenreRes getSongsByGenre(SubsonicCommonReq req, Long genre, Long musicFolderId, Long count, Long offset) {
        SongsByGenreRes res = new SongsByGenreRes();
        Page<TbTagPojo> page = tbTagService.page(new Page<>(offset, count), Wrappers.<TbTagPojo>lambdaQuery().eq(TbTagPojo::getTagName, genre));
        if (CollUtil.isEmpty(page.getRecords())) {
            return new SongsByGenreRes();
        }
        Map<Long, TbTagPojo> tagMap = page.getRecords().parallelStream().collect(Collectors.toMap(TbTagPojo::getId, tbTagPojo -> tbTagPojo));
        
        LambdaQueryWrapper<TbMiddleTagPojo> in = Wrappers.<TbMiddleTagPojo>lambdaQuery()
                                                         .eq(TbMiddleTagPojo::getTagId, TargetTagConstant.TARGET_MUSIC_GENRE)
                                                         .in(TbMiddleTagPojo::getTagId, page.getRecords().parallelStream().map(TbTagPojo::getId).toList());
        List<TbMiddleTagPojo> middleTagList = tbMiddleTagService.list(in);
        Map<Long, List<TbTagPojo>> labelMusicGenre = middleTagList.parallelStream()
                                                                  .collect(Collectors.toMap(TbMiddleTagPojo::getMiddleId,
                                                                          tbMiddleTagPojo -> ListUtil.toList(tagMap.get(tbMiddleTagPojo.getTagId())),
                                                                          (o1, o2) -> {
                                                                              o2.addAll(o1);
                                                                              return o2;
                                                                          }));
        
        List<Long> musicIds = middleTagList.parallelStream().map(TbMiddleTagPojo::getMiddleId).toList();
        List<TbMusicPojo> musicListByAlbumId = tbMusicService.listByIds(musicIds);
        List<Long> albumIds = musicListByAlbumId.parallelStream().map(TbMusicPojo::getAlbumId).toList();
        
        Map<Long, List<ArtistConvert>> musicArtistByMusicIdToMap = qukuService.getMusicArtistByMusicIdToMap(musicIds);
        Map<Long, AlbumConvert> albumByMusicIdToMap = qukuService.getMusicAlbumByAlbumIdToMap(albumIds);
        Map<Long, List<TbResourcePojo>> musicMapUrl = qukuService.getMusicPathMap(musicIds);
        ArrayList<SongsByGenreRes.Song> song = new ArrayList<>();
        for (TbMusicPojo musicPojo : musicListByAlbumId) {
            SongsByGenreRes.Song e = new SongsByGenreRes.Song();
            e.setId(String.valueOf(musicPojo.getId()));
            e.setParent(String.valueOf(musicPojo.getAlbumId()));
            e.setIsDir(false);
            e.setTitle(musicPojo.getMusicName());
            List<TbTagPojo> tbTagPojos = labelMusicGenre.get(musicPojo.getId());
            if (CollUtil.isNotEmpty(tbTagPojos)) {
                e.setGenre(tbTagPojos.get(0).getTagName());
            }
            AlbumConvert albumConvert = albumByMusicIdToMap.get(musicPojo.getId());
            if (Objects.nonNull(albumConvert)) {
                e.setAlbum(albumConvert.getAlbumName());
                e.setAlbumId(String.valueOf(albumConvert.getId()));
                e.setYear(String.valueOf(albumConvert.getPublishTime().getYear()));
            }
            List<ArtistConvert> artistConverts = musicArtistByMusicIdToMap.get(musicPojo.getId());
            if (CollUtil.isNotEmpty(artistConverts)) {
                ArtistConvert artistConvert = artistConverts.get(0);
                e.setArtist(artistConvert.getArtistName());
                e.setArtistId(String.valueOf(artistConvert.getId()));
            }
            e.setCoverArt(String.valueOf(musicPojo.getId()));
            e.setTrack(String.valueOf(0));
            List<TbResourcePojo> musicUrl = musicMapUrl.get(musicPojo.getId());
            TbResourcePojo tbResourcePojo = subsonicResourceReturnStrategyUtil.handleResource(musicUrl);
            if (Objects.nonNull(tbResourcePojo)) {
                e.setSize(String.valueOf(tbResourcePojo.getSize()));
                e.setSuffix(tbResourcePojo.getEncodeType());
                e.setBitRate(String.valueOf(tbResourcePojo.getRate()));
                e.setPath(tbResourcePojo.getPath());
                if (StringUtils.equalsIgnoreCase(tbResourcePojo.getEncodeType(), "mp3")) {
                    e.setContentType("audio/mpeg");
                } else {
                    e.setContentType("audio/" + tbResourcePojo.getEncodeType());
                }
            }
            e.setDuration(String.valueOf(Optional.ofNullable(musicPojo.getTimeLength()).orElse(0) / 1000));
            e.setPlayCount(0);
            e.setPlayed(new Date());
            e.setType("music");
            e.setIsVideo(false);
            e.setCreated(Date.from(musicPojo.getCreateTime().atZone(ZoneId.systemDefault()).toInstant()));
            song.add(e);
        }
        SongsByGenreRes.SongsByGenre songsByGenre = new SongsByGenreRes.SongsByGenre();
        songsByGenre.setSong(song);
        res.setSongsByGenre(songsByGenre);
        
        return res;
    }
    
    public NowPlayingRes getNowPlaying(SubsonicCommonReq req) {
        String userName = req.getU();
        SysUserPojo userByName = accountService.getUserByName(userName);
        if (Objects.isNull(userByName)) {
            return new NowPlayingRes();
        }
        List<TbHistoryPojo> recentMusic = tbHistoryService.getRecentMusic(userByName.getId(), 0L, 20L);
        if (CollUtil.isEmpty(recentMusic)) {
            return new NowPlayingRes();
        }
        List<Long> musicIds = recentMusic.parallelStream().map(TbHistoryPojo::getMiddleId).toList();
        NowPlayingRes res = new NowPlayingRes();
        NowPlayingRes.NowPlaying nowPlaying = new NowPlayingRes.NowPlaying();
        ArrayList<NowPlayingRes.Entry> entries = new ArrayList<>();
        List<TbMusicPojo> musicListByAlbumId = tbMusicService.listByIds(musicIds);
        List<Long> albumIds = musicListByAlbumId.parallelStream().map(TbMusicPojo::getAlbumId).toList();
        
        Map<Long, List<ArtistConvert>> musicArtistByMusicIdToMap = qukuService.getMusicArtistByMusicIdToMap(musicIds);
        Map<Long, AlbumConvert> albumByMusicIdToMap = qukuService.getMusicAlbumByAlbumIdToMap(albumIds);
        Map<Long, List<TbResourcePojo>> musicMapUrl = qukuService.getMusicPathMap(musicIds);
        Map<Long, List<TbTagPojo>> labelMusicGenre = qukuService.getLabelMusicGenre(musicIds);
        for (TbMusicPojo musicPojo : musicListByAlbumId) {
            NowPlayingRes.Entry e = new NowPlayingRes.Entry();
            e.setId(String.valueOf(musicPojo.getId()));
            e.setParent(String.valueOf(musicPojo.getAlbumId()));
            e.setIsDir(false);
            e.setTitle(musicPojo.getMusicName());
            e.setUsername(userByName.getUsername());
            List<TbTagPojo> tbTagPojos = labelMusicGenre.get(musicPojo.getId());
            if (CollUtil.isNotEmpty(tbTagPojos)) {
                e.setGenre(tbTagPojos.get(0).getTagName());
            }
            AlbumConvert albumConvert = albumByMusicIdToMap.get(musicPojo.getId());
            if (Objects.nonNull(albumConvert)) {
                e.setAlbum(albumConvert.getAlbumName());
                e.setYear(String.valueOf(albumConvert.getPublishTime().getYear()));
            }
            List<ArtistConvert> artistConverts = musicArtistByMusicIdToMap.get(musicPojo.getId());
            if (CollUtil.isNotEmpty(artistConverts)) {
                ArtistConvert artistConvert = artistConverts.get(0);
                e.setArtist(artistConvert.getArtistName());
            }
            e.setCoverArt(String.valueOf(musicPojo.getId()));
            e.setTrack(String.valueOf(0));
            List<TbResourcePojo> musicUrl = musicMapUrl.get(musicPojo.getId());
            TbResourcePojo tbResourcePojo = subsonicResourceReturnStrategyUtil.handleResource(musicUrl);
            if (Objects.nonNull(tbResourcePojo)) {
                e.setSize(String.valueOf(tbResourcePojo.getSize()));
                e.setSuffix(tbResourcePojo.getEncodeType());
                e.setBitRate(String.valueOf(tbResourcePojo.getRate()));
                e.setPath(tbResourcePojo.getPath());
                if (StringUtils.equalsIgnoreCase(tbResourcePojo.getEncodeType(), "mp3")) {
                    e.setContentType("audio/mpeg");
                } else {
                    e.setContentType("audio/" + tbResourcePojo.getEncodeType());
                }
            }
            entries.add(e);
        }
        
        nowPlaying.setEntries(entries);
        res.setNowPlaying(nowPlaying);
        return res;
    }
    
    public StarredRes getStarred(SubsonicCommonReq req, Long musicFolderId) {
        StarredRes res = new StarredRes();
        StarredRes.Starred starred = new StarredRes.Starred();
        res.setStarred(starred);
        String userName = req.getU();
        SysUserPojo userByName = accountService.getUserByName(userName);
        
        // 专辑
        List<TbUserAlbumPojo> userAlbumList = tbUserAlbumService.list(Wrappers.<TbUserAlbumPojo>lambdaQuery()
                                                                              .eq(TbUserAlbumPojo::getUserId, userByName.getId()));
        if (CollUtil.isNotEmpty(userAlbumList)) {
            List<Long> albumIds = userAlbumList.parallelStream().map(TbUserAlbumPojo::getAlbumId).toList();
            List<TbAlbumPojo> tbAlbumPojos = albumService.listByIds(albumIds);
            
            Map<Long, List<ArtistConvert>> albumArtistMapByAlbumIds = qukuService.getAlbumArtistMapByAlbumIds(albumIds);
            Map<Long, Integer> albumMusicCountByMapAlbumId = qukuService.getAlbumMusicCountByMapAlbumId(albumIds);
            Map<Long, Integer> albumDurationCount = qukuService.getAlbumDurationCount(albumIds);
            List<StarredRes.Album> albums = new ArrayList<>();
            for (TbAlbumPojo tbAlbumPojo : tbAlbumPojos) {
                StarredRes.Album e = new StarredRes.Album();
                e.setId(String.valueOf(tbAlbumPojo.getId()));
                e.setAlbum(tbAlbumPojo.getAlbumName());
                e.setTitle(tbAlbumPojo.getAlbumName());
                e.setName(tbAlbumPojo.getAlbumName());
                e.setCoverArt(String.valueOf(tbAlbumPojo.getId()));
                e.setAlbumTitle(tbAlbumPojo.getAlbumName());
                
                List<ArtistConvert> artistConverts = albumArtistMapByAlbumIds.get(tbAlbumPojo.getId());
                if (CollUtil.isNotEmpty(artistConverts)) {
                    ArtistConvert artistConvert = artistConverts.get(0);
                    e.setArtist(artistConvert.getArtistName());
                    e.setArtistId(String.valueOf(artistConvert.getId()));
                    e.setParent(String.valueOf(artistConvert.getId()));
                }
                e.setCoverArt(String.valueOf(tbAlbumPojo.getId()));
                e.setCreated(Date.from(tbAlbumPojo.getCreateTime().atZone(ZoneId.systemDefault()).toInstant()));
                e.setDuration(albumDurationCount.get(tbAlbumPojo.getId()));
                e.setIsDir(true);
                e.setIsVideo(false);
                e.setPlayCount(0);
                e.setPlayed(new Date());
                e.setSongCount(albumMusicCountByMapAlbumId.get(tbAlbumPojo.getId()));
                e.setStarred(LocalDateTimeUtil.format(tbAlbumPojo.getPublishTime(), DatePattern.UTC_MS_PATTERN));
                e.setYear(tbAlbumPojo.getPublishTime().getYear());
                
                albums.add(e);
            }
            starred.setAlbums(albums);
        }
        // 歌手
        List<TbUserArtistPojo> userArtistList = tbUserArtistService.list(Wrappers.<TbUserArtistPojo>lambdaQuery()
                                                                                 .eq(TbUserArtistPojo::getUserId, userByName.getId()));
        if (CollUtil.isNotEmpty(userArtistList)) {
            List<Long> artistIds = userArtistList.parallelStream().map(TbUserArtistPojo::getArtistId).toList();
            List<TbArtistPojo> tbArtistPojos = tbArtistService.listByIds(artistIds);
            List<StarredRes.Artist> artists = new ArrayList<>();
            for (TbArtistPojo tbArtistPojo : tbArtistPojos) {
                StarredRes.Artist e = new StarredRes.Artist();
                e.setId(String.valueOf(tbArtistPojo.getId()));
                e.setName(tbArtistPojo.getArtistName());
                e.setCoverArt(String.valueOf(tbArtistPojo.getId()));
                e.setStarred(LocalDateTimeUtil.format(tbArtistPojo.getCreateTime(), DatePattern.UTC_MS_PATTERN));
                e.setAlbumCount(tbArtistPojo.getCreateTime().getYear());
                e.setUserRating(0);
                e.setCoverArt(String.valueOf(tbArtistPojo.getId()));
                e.setArtistImageUrl(remoteStorePicService.getArtistPicUrl(tbArtistPojo.getId()));
                artists.add(e);
            }
            starred.setArtists(artists);
        }
        
        // 音乐
        List<CollectConvert> userPlayList = qukuService.getUserPlayList(userByName.getId(), Collections.singleton(PlayListTypeConstant.LIKE));
        if (CollUtil.isNotEmpty(userPlayList)) {
            List<TbCollectMusicPojo> tbCollectMusicPojos = tbCollectMusicService.getCollectIds(userPlayList.stream().map(TbCollectPojo::getId).toList());
            List<Long> musicIds = tbCollectMusicPojos.parallelStream().map(TbCollectMusicPojo::getMusicId).toList();
            List<TbMusicPojo> tbMusicPojos = tbMusicService.listByIds(musicIds);
            List<Long> albumIds = tbMusicPojos.parallelStream().map(TbMusicPojo::getAlbumId).toList();
            Map<Long, List<ArtistConvert>> musicArtistByMusicIdToMap = qukuService.getMusicArtistByMusicIdToMap(musicIds);
            Map<Long, AlbumConvert> albumByMusicIdToMap = qukuService.getMusicAlbumByAlbumIdToMap(albumIds);
            Map<Long, List<TbResourcePojo>> musicMapUrl = qukuService.getMusicPathMap(musicIds);
            Map<Long, List<TbTagPojo>> labelMusicGenre = qukuService.getLabelMusicGenre(musicIds);
            List<StarredRes.Song> songs = new ArrayList<>();
            for (TbMusicPojo musicPojo : tbMusicPojos) {
                StarredRes.Song e = new StarredRes.Song();
                e.setId(String.valueOf(musicPojo.getId()));
                e.setParent(String.valueOf(musicPojo.getAlbumId()));
                e.setIsDir(false);
                e.setTitle(musicPojo.getMusicName());
                e.setUserRating(0);
                List<TbTagPojo> tbTagPojos = labelMusicGenre.get(musicPojo.getId());
                if (CollUtil.isNotEmpty(tbTagPojos)) {
                    TbTagPojo tbTagPojo = tbTagPojos.get(0);
                    e.setGenre(tbTagPojo.getTagName());
                }
                AlbumConvert albumConvert = albumByMusicIdToMap.get(musicPojo.getId());
                if (Objects.nonNull(albumConvert)) {
                    e.setAlbum(albumConvert.getAlbumName());
                    e.setAlbumId(String.valueOf(albumConvert.getId()));
                    e.setYear(String.valueOf(albumConvert.getPublishTime().getYear()));
                }
                List<ArtistConvert> artistConverts = musicArtistByMusicIdToMap.get(musicPojo.getId());
                if (CollUtil.isNotEmpty(artistConverts)) {
                    ArtistConvert artistConvert = artistConverts.get(0);
                    e.setArtistId(String.valueOf(artistConvert.getId()));
                    e.setArtist(artistConvert.getArtistName());
                }
                e.setCoverArt(String.valueOf(musicPojo.getId()));
                e.setTrack(String.valueOf(0));
                List<TbResourcePojo> musicUrl = musicMapUrl.get(musicPojo.getId());
                TbResourcePojo tbResourcePojo = subsonicResourceReturnStrategyUtil.handleResource(musicUrl);
                if (Objects.nonNull(tbResourcePojo)) {
                    e.setSize(String.valueOf(tbResourcePojo.getSize()));
                    e.setSuffix(tbResourcePojo.getEncodeType());
                    e.setBitRate(String.valueOf(tbResourcePojo.getRate()));
                    e.setPath(tbResourcePojo.getPath());
                    if (StringUtils.equalsIgnoreCase(tbResourcePojo.getEncodeType(), "mp3")) {
                        e.setContentType("audio/mpeg");
                    } else {
                        e.setContentType("audio/" + tbResourcePojo.getEncodeType());
                    }
                }
                e.setDuration(String.valueOf(Optional.ofNullable(musicPojo.getTimeLength()).orElse(0) / 1000));
                e.setPlayCount(0);
                e.setPlayed(new Date());
                e.setType("music");
                e.setIsVideo(false);
                e.setCreated(Date.from(musicPojo.getCreateTime().atZone(ZoneId.systemDefault()).toInstant()));
                e.setStarred(Date.from(musicPojo.getCreateTime().atZone(ZoneId.systemDefault()).toInstant()));
                
                songs.add(e);
            }
            starred.setSongs(songs);
        }
        
        return res;
    }
    
    public Starred2Res getStarred2(SubsonicCommonReq req, Long musicFolderId) {
        Starred2Res starred2Res = new Starred2Res();
        Starred2Res.Starred2 starred2 = new Starred2Res.Starred2();
        starred2Res.setStarred2(starred2);
        String userName = req.getU();
        SysUserPojo userByName = accountService.getUserByName(userName);
        
        // 专辑
        List<TbUserAlbumPojo> userAlbumList = tbUserAlbumService.list(Wrappers.<TbUserAlbumPojo>lambdaQuery()
                                                                              .eq(TbUserAlbumPojo::getUserId, userByName.getId()));
        if (CollUtil.isNotEmpty(userAlbumList)) {
            List<Long> albumIds = userAlbumList.parallelStream().map(TbUserAlbumPojo::getAlbumId).toList();
            List<TbAlbumPojo> tbAlbumPojos = albumService.listByIds(albumIds);
            
            Map<Long, List<ArtistConvert>> albumArtistMapByAlbumIds = qukuService.getAlbumArtistMapByAlbumIds(albumIds);
            Map<Long, List<TbTagPojo>> labelAlbumGenre = qukuService.getLabelAlbumGenre(albumIds);
            Map<Long, Integer> albumMusicCountByMapAlbumId = qukuService.getAlbumMusicCountByMapAlbumId(albumIds);
            Map<Long, Integer> albumDurationCount = qukuService.getAlbumDurationCount(albumIds);
            List<Starred2Res.Album> albums = new ArrayList<>();
            for (TbAlbumPojo tbAlbumPojo : tbAlbumPojos) {
                Starred2Res.Album e = new Starred2Res.Album();
                e.setId(String.valueOf(tbAlbumPojo.getId()));
                e.setAlbum(tbAlbumPojo.getAlbumName());
                e.setTitle(tbAlbumPojo.getAlbumName());
                e.setName(tbAlbumPojo.getAlbumName());
                e.setCoverArt(String.valueOf(tbAlbumPojo.getId()));
                
                List<ArtistConvert> artistConverts = albumArtistMapByAlbumIds.get(tbAlbumPojo.getId());
                if (CollUtil.isNotEmpty(artistConverts)) {
                    ArtistConvert artistConvert = artistConverts.get(0);
                    e.setArtist(artistConvert.getArtistName());
                    e.setArtistId(String.valueOf(artistConvert.getId()));
                    e.setParent(String.valueOf(artistConvert.getId()));
                }
                List<TbTagPojo> tbTagPojos = labelAlbumGenre.get(tbAlbumPojo.getId());
                if (CollUtil.isNotEmpty(tbTagPojos)) {
                    TbTagPojo tbTagPojo = tbTagPojos.get(0);
                    e.setGenre(tbTagPojo.getTagName());
                }
                e.setCreated(Date.from(tbAlbumPojo.getCreateTime().atZone(ZoneId.systemDefault()).toInstant()));
                e.setDuration(albumDurationCount.get(tbAlbumPojo.getId()));
                e.setIsDir(true);
                e.setIsVideo(false);
                e.setPlayCount(0);
                e.setPlayed(new Date());
                e.setSongCount(albumMusicCountByMapAlbumId.get(tbAlbumPojo.getId()));
                e.setStarred(LocalDateTimeUtil.format(tbAlbumPojo.getPublishTime(), DatePattern.UTC_MS_PATTERN));
                e.setYear(tbAlbumPojo.getPublishTime().getYear());
                
                albums.add(e);
            }
            starred2.setAlbum(albums);
        }
        // 歌手
        List<TbUserArtistPojo> userArtistList = tbUserArtistService.list(Wrappers.<TbUserArtistPojo>lambdaQuery()
                                                                                 .eq(TbUserArtistPojo::getUserId, userByName.getId()));
        if (CollUtil.isNotEmpty(userArtistList)) {
            List<Long> artistIds = userArtistList.parallelStream().map(TbUserArtistPojo::getArtistId).toList();
            List<TbArtistPojo> tbArtistPojos = tbArtistService.listByIds(artistIds);
            List<Starred2Res.Artist> artists = new ArrayList<>();
            for (TbArtistPojo tbArtistPojo : tbArtistPojos) {
                Starred2Res.Artist e = new Starred2Res.Artist();
                e.setId(String.valueOf(tbArtistPojo.getId()));
                e.setName(tbArtistPojo.getArtistName());
                e.setStarred(LocalDateTimeUtil.format(tbArtistPojo.getCreateTime(), DatePattern.UTC_MS_PATTERN));
                e.setAlbumCount(tbArtistPojo.getCreateTime().getYear());
                e.setUserRating(0);
                e.setCoverArt(String.valueOf(tbArtistPojo.getId()));
                e.setArtistImageUrl(remoteStorePicService.getArtistPicUrl(tbArtistPojo.getId()));
                artists.add(e);
            }
            starred2.setArtist(artists);
        }
        
        // 音乐
        List<CollectConvert> userPlayList = qukuService.getUserPlayList(userByName.getId(), Collections.singleton(PlayListTypeConstant.LIKE));
        if (CollUtil.isNotEmpty(userPlayList)) {
            List<TbCollectMusicPojo> tbCollectMusicPojos = tbCollectMusicService.getCollectIds(userPlayList.stream().map(TbCollectPojo::getId).toList());
            List<Long> musicIds = tbCollectMusicPojos.parallelStream().map(TbCollectMusicPojo::getMusicId).toList();
            List<TbMusicPojo> tbMusicPojos = tbMusicService.listByIds(musicIds);
            List<Long> albumIds = tbMusicPojos.parallelStream().map(TbMusicPojo::getAlbumId).toList();
            Map<Long, List<ArtistConvert>> musicArtistByMusicIdToMap = qukuService.getMusicArtistByMusicIdToMap(musicIds);
            Map<Long, AlbumConvert> albumByMusicIdToMap = qukuService.getMusicAlbumByAlbumIdToMap(albumIds);
            Map<Long, List<TbResourcePojo>> musicMapUrl = qukuService.getMusicPathMap(musicIds);
            Map<Long, List<TbTagPojo>> labelMusicGenre = qukuService.getLabelMusicGenre(musicIds);
            List<Starred2Res.Song> songs = new ArrayList<>();
            for (TbMusicPojo musicPojo : tbMusicPojos) {
                Starred2Res.Song e = new Starred2Res.Song();
                e.setId(String.valueOf(musicPojo.getId()));
                e.setParent(String.valueOf(musicPojo.getAlbumId()));
                e.setIsDir(false);
                e.setTitle(musicPojo.getMusicName());
                e.setUserRating(0);
                List<TbTagPojo> tbTagPojos = labelMusicGenre.get(musicPojo.getId());
                if (CollUtil.isNotEmpty(tbTagPojos)) {
                    TbTagPojo tbTagPojo = tbTagPojos.get(0);
                    e.setGenre(tbTagPojo.getTagName());
                }
                AlbumConvert albumConvert = albumByMusicIdToMap.get(musicPojo.getId());
                if (Objects.nonNull(albumConvert)) {
                    e.setAlbum(albumConvert.getAlbumName());
                    e.setAlbumId(String.valueOf(albumConvert.getId()));
                    e.setYear(albumConvert.getPublishTime().getYear());
                }
                List<ArtistConvert> artistConverts = musicArtistByMusicIdToMap.get(musicPojo.getId());
                if (CollUtil.isNotEmpty(artistConverts)) {
                    ArtistConvert artistConvert = artistConverts.get(0);
                    e.setArtistId(String.valueOf(artistConvert.getId()));
                    e.setArtist(artistConvert.getArtistName());
                }
                e.setCoverArt(String.valueOf(musicPojo.getId()));
                e.setTrack(0);
                List<TbResourcePojo> musicUrl = musicMapUrl.get(musicPojo.getId());
                TbResourcePojo tbResourcePojo = subsonicResourceReturnStrategyUtil.handleResource(musicUrl);
                if (Objects.nonNull(tbResourcePojo)) {
                    e.setSize(tbResourcePojo.getSize());
                    e.setSuffix(tbResourcePojo.getEncodeType());
                    e.setBitRate(tbResourcePojo.getRate());
                    e.setPath(tbResourcePojo.getPath());
                    if (StringUtils.equalsIgnoreCase(tbResourcePojo.getEncodeType(), "mp3")) {
                        e.setContentType("audio/mpeg");
                    } else {
                        e.setContentType("audio/" + tbResourcePojo.getEncodeType());
                    }
                }
                e.setDuration(Optional.ofNullable(musicPojo.getTimeLength()).orElse(0) / 1000);
                e.setPlayCount(0);
                e.setPlayed(new Date());
                e.setType("music");
                e.setIsVideo(false);
                e.setCreated(Date.from(musicPojo.getCreateTime().atZone(ZoneId.systemDefault()).toInstant()));
                e.setStarred(Date.from(musicPojo.getCreateTime().atZone(ZoneId.systemDefault()).toInstant()));
                songs.add(e);
            }
            starred2.setSong(songs);
        }
        
        return starred2Res;
    }
}
