package org.api.nmusic.service;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.api.common.service.QukuAPI;
import org.api.nmusic.config.NeteaseCloudConfig;
import org.api.nmusic.model.vo.playlistdetail.PlayListDetailRes;
import org.core.common.constant.PlayListTypeConstant;
import org.core.common.exception.BaseException;
import org.core.common.result.NeteaseResult;
import org.core.common.result.ResultCode;
import org.core.mybatis.iservice.*;
import org.core.mybatis.model.convert.AlbumConvert;
import org.core.mybatis.model.convert.ArtistConvert;
import org.core.mybatis.model.convert.CollectConvert;
import org.core.mybatis.model.convert.MusicConvert;
import org.core.mybatis.pojo.*;
import org.core.service.AccountService;
import org.core.service.PlayListService;
import org.core.service.RemoteStorePicService;
import org.core.service.impl.QukuServiceImpl;
import org.core.utils.AliasUtil;
import org.core.utils.ExceptionUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 歌单中间层
 */
@Slf4j
@Service(NeteaseCloudConfig.NETEASECLOUD + "CollectApi")
public class CollectApi {
    
    private final TbCollectService collectService;
    
    private final TbUserCollectService userCollectService;
    
    private final TbTagService tagService;
    
    private final TbMiddleTagService collectMusicTagService;
    
    private final TbCollectMusicService collectMusicService;
    
    private final TbMusicService musicService;
    
    private final TbResourceService musicUrlService;
    
    private final PlayListService playListService;
    
    private final AccountService accountService;
    
    private final QukuAPI qukuService;
    
    private final RemoteStorePicService remoteStorePicService;
    
    public CollectApi(TbMiddleTagService collectMusicTagService, TbCollectService collectService, TbUserCollectService userCollectService, TbTagService tagService, TbCollectMusicService collectMusicService, TbMusicService musicService, TbResourceService musicUrlService, PlayListService playListService, AccountService accountService, QukuAPI qukuService, RemoteStorePicService remoteStorePicService) {
        this.collectMusicTagService = collectMusicTagService;
        this.collectService = collectService;
        this.userCollectService = userCollectService;
        this.tagService = tagService;
        this.collectMusicService = collectMusicService;
        this.musicService = musicService;
        this.musicUrlService = musicUrlService;
        this.playListService = playListService;
        this.accountService = accountService;
        this.qukuService = qukuService;
        this.remoteStorePicService = remoteStorePicService;
    }
    
    /**
     * 是否包含tag
     *
     * @param pojoList tag表
     * @param tagName  tag name
     */
    private static TbTagPojo containsTag(List<TbTagPojo> pojoList, String tagName) {
        for (TbTagPojo tbTagPojo : pojoList) {
            if (tagName.equals(tbTagPojo.getTagName())) {
                return tbTagPojo;
            }
        }
        return null;
    }
    
    
    /**
     * 查询tag表
     * 根据tag id列表返回歌单tag信息
     *
     * @param tagIds tag ID
     */
    public List<TbTagPojo> getTagPojoList(List<Long> tagIds) {
        if (tagIds == null || tagIds.isEmpty()) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<TbTagPojo> lambdaQueryWrapper = Wrappers.<TbTagPojo>lambdaQuery().in(TbTagPojo::getId, tagIds);
        return tagService.list(lambdaQueryWrapper);
    }
    
    /**
     * 根据歌单和tag中间表
     * 获取歌单对各的tag id
     *
     * @param collectId 歌单ID
     * @return 返回中间表tag id列表
     */
    public List<TbMiddleTagPojo> getCollectTagIdList(List<Long> collectId) {
        if (collectId == null || collectId.isEmpty()) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<TbMiddleTagPojo> lambdaQueryWrapper = Wrappers.<TbMiddleTagPojo>lambdaQuery()
                                                                         .in(TbMiddleTagPojo::getId, collectId);
        return collectMusicTagService.list(lambdaQueryWrapper);
    }
    
    /**
     * 创建歌单
     *
     * @param userId 用户ID
     * @param name   歌单名
     * @return 歌单信息
     */
    public CollectConvert createPlayList(Long userId, String name) {
        return qukuService.createPlayList(userId, name, PlayListTypeConstant.ORDINARY);
    }
    
    /**
     * 修改用户歌单信息,值为null则不修改
     *
     * @param userId      用户ID
     * @param collectPojo 歌单信息
     */
    public void updatePlayList(Long userId, TbCollectPojo collectPojo) {
        TbCollectPojo tbCollectPojo = collectService.getById(collectPojo.getId());
        QukuServiceImpl.checkUserAuth(userId, tbCollectPojo);
        
        collectService.updateById(collectPojo);
    }
    
    /**
     * 修改歌单tag
     *
     * @param userId    用户ID
     * @param collectId 歌单ID
     * @param split     tag
     */
    public void updatePlayListTag(Long userId, Long collectId, String[] split) {
        TbCollectPojo collectPojo = new TbCollectPojo();
        collectPojo.setId(collectId);
        collectPojo.setUserId(userId);
        QukuServiceImpl.checkUserAuth(userId, collectPojo);
        
        
        // 如果为0则直接不需要保存操作
        if (split.length == 0) {
            return;
        }
        
        // 先删除歌单的关联tag然后在重新添加
        LambdaQueryWrapper<TbMiddleTagPojo> collectLambdaQueryWrapper = Wrappers.<TbMiddleTagPojo>lambdaQuery()
                                                                                .eq(TbMiddleTagPojo::getId, collectId);
        collectMusicTagService.remove(collectLambdaQueryWrapper);
        
        // tag为空字符串跳过
        if (split.length == 1 && StringUtils.isBlank(split[0])) {
            return;
        }
        
        List<String> splitList = Arrays.asList(split);
        
        // 需要存入的tag list
        List<TbMiddleTagPojo> saveCollectTagPojoList = new ArrayList<>(split.length);
        // 用来批量存放tb tag
        List<TbTagPojo> saveTbTagList = new ArrayList<>();
        
        LambdaQueryWrapper<TbTagPojo> tagPojoLambdaQueryWrapper = Wrappers.<TbTagPojo>lambdaQuery().in(TbTagPojo::getTagName, splitList);
        // 获取tag表中已经记录的tag id
        List<TbTagPojo> tbTagPojos = tagService.list(tagPojoLambdaQueryWrapper);
        
        for (String tagName : split) {
            // 如果不包含tag 则新建tag ,否则直接写入到歌单andtag中间表
            TbTagPojo tagPojo = containsTag(tbTagPojos, tagName);
            if (tagPojo == null) {
                // 新建tag
                TbTagPojo tbTagPojo = new TbTagPojo();
                tbTagPojo.setId(IdWorker.getId());
                tbTagPojo.setTagName(tagName);
                saveTbTagList.add(tbTagPojo);
                // 添加到中间表
                TbMiddleTagPojo e = new TbMiddleTagPojo();
                e.setTagId(tbTagPojo.getId());
                e.setMiddleId(collectId);
                saveCollectTagPojoList.add(e);
            } else {
                // 已有tag信息, 直接使用tag id
                TbMiddleTagPojo e = new TbMiddleTagPojo();
                e.setTagId(tagPojo.getId());
                e.setMiddleId(collectId);
                saveCollectTagPojoList.add(e);
            }
        }
        
        // 保存tag表
        tagService.saveBatch(saveTbTagList);
        // 保存tag and 歌单中间表
        collectMusicTagService.saveBatch(saveCollectTagPojoList);
    }
    
    /**
     * 删除歌单
     *
     * @param userId     用户ID
     * @param collectIds 歌单ID
     */
    public void removePlayList(Long userId, List<Long> collectIds) {
        qukuService.removePlayList(userId, collectIds);
    }
    
    /**
     * 收藏/取消歌单
     *
     * @param userId    用户ID
     * @param collectId 歌单ID
     * @param flag      取消/收藏 true:收藏,false:取消收藏
     */
    public void subscribePlayList(Long userId, Long collectId, boolean flag) {
        TbCollectPojo tbCollectPojo = collectService.getById(collectId);
        ExceptionUtil.isNull(tbCollectPojo == null, ResultCode.PLAY_LIST_NO_EXIST);
        // 需要收藏歌单存在，并且用户不一样, 不等于为true
        boolean userFlag = !Objects.equals(tbCollectPojo.getUserId(), userId);
        
        LambdaQueryWrapper<TbUserCollectPojo> eq = Wrappers.<TbUserCollectPojo>lambdaQuery()
                                                           .eq(TbUserCollectPojo::getCollectId, collectId)
                                                           .eq(TbUserCollectPojo::getUserId, userId);
        TbUserCollectPojo one = userCollectService.getOne(eq);
        // 防止重复收藏
        ExceptionUtil.isNull(one == null, ResultCode.PLAY_LIST_LIKE);
        // 收藏
        if (userFlag && flag) {
            TbUserCollectPojo entity = new TbUserCollectPojo();
            entity.setCollectId(collectId);
            entity.setUserId(userId);
            userCollectService.save(entity);
        } else {
            userCollectService.remove(eq);
        }
    }
    
    /**
     * 获取歌单所有歌曲
     *
     * @param collectId 歌单ID
     * @param pageIndex 当前多少页
     * @param pageSize  每页多少条
     * @return 歌单内所有歌曲
     */
    public Page<MusicConvert> getPlayListAllSong(Long collectId, Long pageIndex, Long pageSize) {
        LambdaQueryWrapper<TbCollectMusicPojo> wrapper = Wrappers.<TbCollectMusicPojo>lambdaQuery().in(TbCollectMusicPojo::getCollectId, collectId);
        Page<TbCollectMusicPojo> page = collectMusicService.page(new Page<>(pageIndex, pageSize), wrapper);
        if (page.getTotal() == 0) {
            throw new BaseException(ResultCode.SONG_NOT_EXIST);
        }
        List<Long> musicIds = page.getRecords().stream().map(TbCollectMusicPojo::getMusicId).toList();
        List<TbMusicPojo> tbMusicPojoList = musicService.listByIds(musicIds);
        
        List<MusicConvert> collect = tbMusicPojoList.stream().map(tbMusicPojo -> {
            MusicConvert convert = new MusicConvert();
            BeanUtils.copyProperties(tbMusicPojo, convert);
            convert.setPicUrl(remoteStorePicService.getMusicPicUrl(tbMusicPojo.getId()));
            return convert;
        }).toList();
        
        Page<MusicConvert> musicPojoPage = new Page<>();
        musicPojoPage.setRecords(collect);
        musicPojoPage.setCurrent(page.getCurrent());
        musicPojoPage.setTotal(page.getTotal());
        musicPojoPage.setSize(page.getSize());
        return musicPojoPage;
    }
    
    /**
     * 获取音乐信息
     *
     * @param musicIds 音乐id list
     * @return 音乐信息列表
     */
    public List<TbResourcePojo> getMusicInfo(List<Long> musicIds) {
        return musicUrlService.list(Wrappers.<TbResourcePojo>lambdaQuery().in(TbResourcePojo::getMusicId, musicIds));
    }
    
    /**
     * 添加歌曲到歌单
     *
     * @param userID    用户ID
     * @param collectId 歌单ID
     * @param songIds   歌曲ID
     * @param flag      添加还是删除
     * @return 返回添加歌单后歌单数量
     */
    public NeteaseResult addSongToCollect(Long userID, Long collectId, List<Long> songIds, boolean flag) {
        TbCollectPojo tbCollectPojo = collectService.getById(collectId);
        QukuServiceImpl.checkUserAuth(userID, tbCollectPojo);
        qukuService.addOrRemoveMusicToCollect(userID, tbCollectPojo.getId(), songIds, flag);
        long count = collectMusicService.count(Wrappers.<TbCollectMusicPojo>lambdaQuery().eq(TbCollectMusicPojo::getCollectId, collectId));
        NeteaseResult map = new NeteaseResult();
        map.put("trackIds", songIds.toArray());
        map.put("count", count);
        map.put("cloudCount", 0);
        return map.success();
    }
    
    /**
     * 添加喜爱歌曲
     *
     * @param userId          用户ID
     * @param id              歌曲ID
     * @param isAddAndDelLike true添加歌曲，false删除歌曲
     */
    public void like(Long userId, Long id, Boolean isAddAndDelLike) {
        qukuService.collectLike(userId, id, isAddAndDelLike);
    }
    
    /**
     * 查询用户喜爱歌单
     *
     * @param uid 用户ID
     * @return 返回歌曲数组
     */
    public List<Long> likelist(Long uid) {
        List<TbCollectPojo> userCollect = collectService.getUserCollect(uid, PlayListTypeConstant.LIKE);
        List<Long> collectIds = userCollect.parallelStream().map(TbCollectPojo::getId).toList();
        List<TbCollectMusicPojo> list = collectMusicService.list(Wrappers.<TbCollectMusicPojo>lambdaQuery()
                                                                         .in(TbCollectMusicPojo::getCollectId, collectIds));
        return list.stream().map(TbCollectMusicPojo::getMusicId).toList();
    }
    
    
    /**
     * 查询歌单详细信息
     *
     * @param id 歌单信息
     */
    public PlayListDetailRes playlistDetail(Long id) {
        TbCollectPojo byId = collectService.getById(id);
        if (byId == null) {
            return new PlayListDetailRes();
        }
        List<TbMusicPojo> playListAllMusic = playListService.getPlayListAllMusic(id);
        SysUserPojo userPojo = Optional.ofNullable(accountService.getById(byId.getUserId())).orElse(new SysUserPojo());
        
        PlayListDetailRes.Playlist playlist = fillPlayUser(byId, playListAllMusic.size());
        // 歌单创建者
        PlayListDetailRes.Playlist.Creator creator = fillCreatorUser(userPojo);
        playlist.setCreator(creator);
        
        ArrayList<PlayListDetailRes.PrivilegesItem> privileges = new ArrayList<>();
        ArrayList<PlayListDetailRes.Playlist.TracksItem> tracks = new ArrayList<>();
        ArrayList<PlayListDetailRes.Playlist.TrackIdsItem> trackIds = new ArrayList<>();
        // 获取歌曲map
        List<Long> musicIds = playListAllMusic.parallelStream().map(TbMusicPojo::getId).toList();
        Map<Long, List<ArtistConvert>> artistByMusicIdToMap = qukuService.getArtistByMusicIdToMap(musicIds);
        // 获取专辑map
        List<Long> albumIds = playListAllMusic.parallelStream().map(TbMusicPojo::getAlbumId).toList();
        Map<Long, AlbumConvert> musicAlbumByAlbumIdToMap = qukuService.getMusicAlbumByAlbumIdToMap(albumIds);
        for (TbMusicPojo tbMusicPojo : playListAllMusic) {
            // 艺术家数据
            List<ArtistConvert> singerByMusicId = Optional.ofNullable(artistByMusicIdToMap.get(tbMusicPojo.getId())).orElse(new ArrayList<>());
            // 专辑数据
            AlbumConvert albumByAlbumId = Optional.ofNullable(musicAlbumByAlbumIdToMap.get(tbMusicPojo.getAlbumId())).orElse(new AlbumConvert());
            
            // tracks 歌单内音乐
            tracks.add(fillTracks(tbMusicPojo, albumByAlbumId, singerByMusicId));
            // 歌单音乐ID
            trackIds.add(fillTrackIds(tbMusicPojo, userPojo));
            // 歌单中歌曲简略信息
            privileges.add(fillPrivileges(tbMusicPojo));
        }
        
        playlist.setTracks(tracks);
        playlist.setTrackIds(trackIds);
        playlist.setTrackCount(playListAllMusic.size());
        
        PlayListDetailRes playListRes = new PlayListDetailRes();
        playListRes.setPrivileges(privileges);
        playListRes.setPlaylist(playlist);
        return playListRes;
    }
    
    private PlayListDetailRes.Playlist fillPlayUser(TbCollectPojo byId, int size) {
        PlayListDetailRes.Playlist playlist = new PlayListDetailRes.Playlist();
        playlist.setId(byId.getId());
        playlist.setName(byId.getPlayListName());
        // playlist.setCoverImgId(109951165443127620);
        playlist.setCoverImgUrl(remoteStorePicService.getCollectPicUrl(byId.getId()));
        // playlist.setCoverImgIdStr("109951165443127612");
        playlist.setAdType(0);
        playlist.setUserId(byId.getUserId());
        playlist.setCreateTime(byId.getCreateTimeToTime());
        playlist.setStatus(0);
        playlist.setOpRecommend(false);
        playlist.setHighQuality(false);
        playlist.setNewImported(false);
        playlist.setUpdateTime(byId.getUpdateTimeToTime());
        playlist.setTrackCount(1713);
        playlist.setSpecialType(5);
        playlist.setPrivacy(0);
        playlist.setTrackUpdateTime(byId.getUpdateTimeToTime());
        playlist.setCommentThreadId("A_PL_0_000000");
        playlist.setPlayCount(size);
        playlist.setTrackNumberUpdateTime(byId.getUpdateTimeToTime());
        playlist.setSubscribedCount(0);
        playlist.setCloudTrackCount(0);
        playlist.setOrdered(true);
        playlist.setDescription(playlist.getDescription());
        playlist.setTags(new ArrayList<>());
        playlist.setUpdateFrequency(null);
        playlist.setBackgroundCoverId(0);
        playlist.setBackgroundCoverUrl(null);
        playlist.setTitleImage(0);
        playlist.setTitleImageUrl(null);
        playlist.setEnglishTitle(null);
        playlist.setOfficialPlaylistType(null);
        playlist.setCopied(false);
        playlist.setRelateResType(null);
        return playlist;
    }
    
    private PlayListDetailRes.PrivilegesItem fillPrivileges(TbMusicPojo tbMusicPojo) {
        PlayListDetailRes.PrivilegesItem songDetail = new PlayListDetailRes.PrivilegesItem();
        songDetail.setId(tbMusicPojo.getId());
        songDetail.setFee(0);
        songDetail.setPayed(0);
        songDetail.setRealPayed(0);
        songDetail.setSt(0);
        songDetail.setPl(320000);
        songDetail.setDl(999000);
        songDetail.setSp(7);
        songDetail.setCp(1);
        songDetail.setSubp(1);
        songDetail.setCs(false);
        songDetail.setMaxbr(999000);
        songDetail.setFl(320000);
        songDetail.setPc(null);
        songDetail.setToast(false);
        songDetail.setFlag(256);
        songDetail.setPaidBigBang(false);
        songDetail.setPreSell(false);
        songDetail.setPlayMaxbr(999000);
        songDetail.setDownloadMaxbr(999000);
        songDetail.setMaxBrLevel("lossless");
        songDetail.setPlayMaxBrLevel("lossless");
        songDetail.setDownloadMaxBrLevel("lossless");
        songDetail.setPlLevel("exhigh");
        songDetail.setDlLevel("lossless");
        songDetail.setFlLevel("exhigh");
        songDetail.setRscl(null);
        
        List<PlayListDetailRes.PrivilegesItem.ChargeInfoListItem> chargeInfoList = new ArrayList<>();
        chargeInfoList.add(new PlayListDetailRes.PrivilegesItem.ChargeInfoListItem(128000, null, null, 0));
        chargeInfoList.add(new PlayListDetailRes.PrivilegesItem.ChargeInfoListItem(192000, null, null, 0));
        chargeInfoList.add(new PlayListDetailRes.PrivilegesItem.ChargeInfoListItem(320000, null, null, 0));
        chargeInfoList.add(new PlayListDetailRes.PrivilegesItem.ChargeInfoListItem(999000, null, null, 0));
        songDetail.setChargeInfoList(chargeInfoList);
        
        PlayListDetailRes.PrivilegesItem.FreeTrialPrivilege freeTrialPrivilege = new PlayListDetailRes.PrivilegesItem.FreeTrialPrivilege();
        freeTrialPrivilege.setResConsumable(false);
        freeTrialPrivilege.setUserConsumable(false);
        freeTrialPrivilege.setListenType(0);
        freeTrialPrivilege.setCannotListenReason(1);
        freeTrialPrivilege.setPlayReason(null);
        songDetail.setFreeTrialPrivilege(freeTrialPrivilege);
        return songDetail;
    }
    
    private PlayListDetailRes.Playlist.Creator fillCreatorUser(SysUserPojo userPojo) {
        PlayListDetailRes.Playlist.Creator creator = new PlayListDetailRes.Playlist.Creator();
        creator.setDefaultAvatar(false);
        creator.setProvince(420000);
        creator.setAuthStatus(0);
        creator.setFollowed(false);
        creator.setAvatarUrl(remoteStorePicService.getUserAvatarPicUrl(userPojo.getId()));
        creator.setAccountStatus(0);
        creator.setGender(1);
        creator.setCity(1010000);
        creator.setBirthday(0);
        creator.setUserId(userPojo.getId());
        creator.setUserType(0);
        creator.setNickname(userPojo.getNickname());
        creator.setSignature(userPojo.getSignature());
        creator.setDescription("");
        creator.setDetailDescription("");
        // creator.setAvatarImgId(0L);
        // creator.setBackgroundImgId(0L);
        creator.setBackgroundUrl(remoteStorePicService.getUserBackgroundPicUrl(userPojo.getId()));
        creator.setAuthority(0);
        creator.setMutual(false);
        creator.setDjStatus(0);
        creator.setVipType(2);
        creator.setAuthenticationTypes(0);
        // creator.setAvatarImgIdStr("");
        // creator.setBackgroundImgIdStr("");
        creator.setAnchor(false);
        return creator;
    }
    
    private PlayListDetailRes.Playlist.TrackIdsItem fillTrackIds(TbMusicPojo tbMusicPojo, SysUserPojo userPojo) {
        PlayListDetailRes.Playlist.TrackIdsItem song = new PlayListDetailRes.Playlist.TrackIdsItem();
        song.setId(tbMusicPojo.getId());
        song.setV(0);
        song.setT(0);
        song.setAt(1711525625685L);
        song.setUid(userPojo.getId());
        song.setRcmdReason("");
        return song;
    }
    
    private PlayListDetailRes.Playlist.TracksItem fillTracks(TbMusicPojo tbMusicPojo, AlbumConvert albumByAlbumId, List<ArtistConvert> artistConverts) {
        PlayListDetailRes.Playlist.TracksItem e = new PlayListDetailRes.Playlist.TracksItem();
        e.setName(tbMusicPojo.getMusicName());
        e.setId(tbMusicPojo.getId());
        e.setPst(0);
        e.setT(0);
        
        List<PlayListDetailRes.Playlist.TracksItem.ArItem> artists = new ArrayList<>();
        if (CollUtil.isNotEmpty(artistConverts)) {
            for (ArtistConvert artistConvert : artistConverts) {
                PlayListDetailRes.Playlist.TracksItem.ArItem artist = new PlayListDetailRes.Playlist.TracksItem.ArItem();
                artist.setId(artistConvert.getId());
                artist.setName(artistConvert.getArtistName());
                artist.setAlias(AliasUtil.getAliasList(artistConvert.getAliasName()));
                artists.add(artist);
            }
        }
        e.setAr(artists);
        e.setAlia(AliasUtil.getAliasList(tbMusicPojo.getAliasName()));
        // e.setPop(100);
        e.setSt(0);
        e.setRt("");
        e.setFee(0);
        e.setV(3);
        e.setCrbt(null);
        e.setCf("");
        
        PlayListDetailRes.Playlist.TracksItem.Al album = new PlayListDetailRes.Playlist.TracksItem.Al();
        album.setId(albumByAlbumId.getId());
        album.setName(albumByAlbumId.getAlbumName());
        album.setPicUrl(remoteStorePicService.getAlbumPicUrl(albumByAlbumId.getId()));
        album.setTns(new ArrayList<>());
        // album.setPicStr("");
        // album.setPic(0L);
        e.setAl(album);
        e.setDt(tbMusicPojo.getTimeLength());
        PlayListDetailRes.Playlist.TracksItem.H h = new PlayListDetailRes.Playlist.TracksItem.H();
        h.setBr(320001);
        h.setFid(0);
        h.setSize(10202427);
        h.setVd(-40588);
        h.setSr(44100);
        e.setH(h);
        
        PlayListDetailRes.Playlist.TracksItem.M m = new PlayListDetailRes.Playlist.TracksItem.M();
        m.setBr(192001);
        m.setFid(0);
        m.setSize(6121474);
        m.setVd(-38058);
        m.setSr(44100);
        e.setM(m);
        
        
        PlayListDetailRes.Playlist.TracksItem.L l = new PlayListDetailRes.Playlist.TracksItem.L();
        l.setBr(128001);
        l.setFid(0);
        l.setSize(4080997);
        l.setVd(-36535);
        l.setSr(44100);
        e.setL(l);
        
        PlayListDetailRes.Playlist.TracksItem.Sq sq = new PlayListDetailRes.Playlist.TracksItem.Sq();
        sq.setBr(1652269);
        sq.setFid(0);
        sq.setSize(52666081);
        sq.setVd(-40625);
        sq.setSr(44100);
        e.setSq(sq);
        
        e.setHr(null);
        e.setA(null);
        e.setCd("01");
        e.setNo(1);
        e.setRtUrl(null);
        e.setFtype(0);
        e.setRtUrls(new ArrayList<>());
        e.setDjId(0);
        e.setCopyright(0);
        e.setSId(0);
        e.setMark(128);
        e.setOriginCoverType(1);
        e.setOriginSongSimpleData(null);
        e.setTagPicList(null);
        e.setResourceState(true);
        e.setVersion(3);
        e.setSongJumpInfo(null);
        e.setEntertainmentTags(null);
        e.setAwardTags(null);
        e.setSingle(0);
        e.setNoCopyrightRcmd(null);
        e.setRtype(0);
        e.setRurl(null);
        e.setMst(9);
        e.setCp(0);
        e.setMv(0);
        e.setPublishTime(tbMusicPojo.getPublishTimeToTime());
        return e;
    }
}
