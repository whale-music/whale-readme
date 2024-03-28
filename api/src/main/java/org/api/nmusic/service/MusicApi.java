package org.api.nmusic.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.api.common.service.QukuAPI;
import org.api.nmusic.config.NeteaseCloudConfig;
import org.api.nmusic.model.vo.song.lyric.Klyric;
import org.api.nmusic.model.vo.song.lyric.Lrc;
import org.api.nmusic.model.vo.song.lyric.SongLyricRes;
import org.api.nmusic.model.vo.song.lyric.Tlyric;
import org.api.nmusic.model.vo.songdetail.*;
import org.api.nmusic.model.vo.songurl.DataItem;
import org.api.nmusic.model.vo.songurl.SongUrlRes;
import org.api.nmusic.model.vo.songurlv1.SongUrlV1;
import org.core.common.constant.HistoryConstant;
import org.core.common.constant.LyricConstant;
import org.core.mybatis.iservice.*;
import org.core.mybatis.model.convert.AlbumConvert;
import org.core.mybatis.model.convert.ArtistConvert;
import org.core.mybatis.pojo.*;
import org.core.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service(NeteaseCloudConfig.NETEASECLOUD + "MusicApi")
public class MusicApi {
    
    @Autowired
    private TbMusicService musicService;
    
    
    @Autowired
    private QukuAPI qukuService;
    
    @Autowired
    private TbHistoryService historyService;
    
    @Autowired
    private TbAlbumService albumService;
    
    @Autowired
    private TbCollectService collectService;
    
    @Autowired
    private TbArtistService tbArtistService;
    
    @Autowired
    private TbMvService tbMvService;
    
    
    public SongUrlRes songUrl(List<Long> id, Integer br) {
        List<TbResourcePojo> musicUrlByMusicId = qukuService.getMusicUrlByMusicId(new HashSet<>(id), false);
        List<TbMusicPojo> musicPojos = musicService.listByIds(id);
        Map<Long, TbMusicPojo> musicPojoMap = musicPojos.stream().collect(Collectors.toMap(TbMusicPojo::getId, tbMusicPojo -> tbMusicPojo));
        SongUrlRes songUrlRes = new SongUrlRes();
        ArrayList<DataItem> data = new ArrayList<>();
        for (TbResourcePojo tbMusicUrlPojo : musicUrlByMusicId) {
            DataItem e = new DataItem();
            e.setId(tbMusicUrlPojo.getId());
            e.setUrl(tbMusicUrlPojo.getPath());
            e.setBr(tbMusicUrlPojo.getRate());
            e.setSize(tbMusicUrlPojo.getSize());
            e.setCode(200);
            e.setType(tbMusicUrlPojo.getEncodeType());
            e.setEncodeType(tbMusicUrlPojo.getEncodeType());
            e.setLevel(tbMusicUrlPojo.getLevel());
            e.setMd5(tbMusicUrlPojo.getMd5());
            e.setTime(Optional.ofNullable(musicPojoMap.get(tbMusicUrlPojo.getMusicId())).orElse(new TbMusicPojo()).getTimeLength());
            data.add(e);
            
            if (Objects.equals(e.getBr(), br)) {
                break;
            }
        }
        songUrlRes.setData(data);
        return songUrlRes;
    }
    
    /**
     * 获取歌曲详情
     *
     * @param ids 歌曲ID List
     */
    public SongDetailRes songDetail(List<Long> ids) {
        List<TbMusicPojo> musicPojoList = musicService.listByIds(ids);
        if (CollUtil.isEmpty(musicPojoList)) {
            return new SongDetailRes();
        }
        SongDetailRes songDetailRes = new SongDetailRes();
        ArrayList<SongsItem> songs = new ArrayList<>();
        
        ArrayList<PrivilegesItem> privileges = new ArrayList<>();
        
        Map<Long, List<ArtistConvert>> artistByMusicIdToMap = qukuService.getArtistByMusicIdToMap(ids);
        List<Long> albumIds = musicPojoList.parallelStream().map(TbMusicPojo::getAlbumId).toList();
        Map<Long, AlbumConvert> musicAlbumByAlbumIdToMap = qukuService.getMusicAlbumByAlbumIdToMap(albumIds);
        
        PrivilegesItem privilegesItem = new PrivilegesItem();
        privilegesItem.setId(30569280L);
        privilegesItem.setFee(0);
        privilegesItem.setPayed(0);
        privilegesItem.setSt(0);
        privilegesItem.setPl(320000);
        privilegesItem.setDl(320000);
        privilegesItem.setSp(7);
        privilegesItem.setCp(1);
        privilegesItem.setSubp(1);
        privilegesItem.setCs(false);
        privilegesItem.setMaxbr(320000);
        privilegesItem.setFl(320000);
        privilegesItem.setToast(false);
        privilegesItem.setFlag(128);
        privilegesItem.setPreSell(false);
        privilegesItem.setPlayMaxbr(320000);
        privilegesItem.setDownloadMaxbr(320000);
        privilegesItem.setMaxBrLevel("exhigh");
        privilegesItem.setPlayMaxBrLevel("exhigh");
        privilegesItem.setDownloadMaxBrLevel("exhigh");
        privilegesItem.setPlLevel("exhigh");
        privilegesItem.setDlLevel("exhigh");
        privilegesItem.setFlLevel("exhigh");
        privilegesItem.setRscl(null);
        
        // 设置 freeTrialPrivilege 对象
        FreeTrialPrivilege freeTrialPrivilege = new FreeTrialPrivilege();
        freeTrialPrivilege.setResConsumable(false);
        freeTrialPrivilege.setUserConsumable(false);
        freeTrialPrivilege.setListenType(0);
        freeTrialPrivilege.setCannotListenReason(1);
        freeTrialPrivilege.setPlayReason(null);
        privilegesItem.setFreeTrialPrivilege(freeTrialPrivilege);
        
        privilegesItem.setRightSource(0);
        
        // 设置 chargeInfoList 对象
        List<ChargeInfoListItem> chargeInfoList = new ArrayList<>();
        ChargeInfoListItem chargeInfo1 = new ChargeInfoListItem();
        chargeInfo1.setRate(128000);
        chargeInfo1.setChargeUrl(null);
        chargeInfo1.setChargeMessage(null);
        chargeInfo1.setChargeType(0);
        chargeInfoList.add(chargeInfo1);
        
        ChargeInfoListItem chargeInfo2 = new ChargeInfoListItem();
        chargeInfo2.setRate(192000);
        chargeInfo2.setChargeUrl(null);
        chargeInfo2.setChargeMessage(null);
        chargeInfo2.setChargeType(0);
        chargeInfoList.add(chargeInfo2);
        
        ChargeInfoListItem chargeInfo3 = new ChargeInfoListItem();
        chargeInfo3.setRate(320000);
        chargeInfo3.setChargeUrl(null);
        chargeInfo3.setChargeMessage(null);
        chargeInfo3.setChargeType(0);
        chargeInfoList.add(chargeInfo3);
        
        ChargeInfoListItem chargeInfo4 = new ChargeInfoListItem();
        chargeInfo4.setRate(999000);
        chargeInfo4.setChargeUrl(null);
        chargeInfo4.setChargeMessage(null);
        chargeInfo4.setChargeType(1);
        chargeInfoList.add(chargeInfo4);
        
        privilegesItem.setChargeInfoList(chargeInfoList);
        
        
        for (TbMusicPojo tbMusicPojo : musicPojoList) {
            SongsItem e = new SongsItem();
            e.setName(tbMusicPojo.getMusicName());
            e.setId(tbMusicPojo.getId());
            e.setPst(0);
            e.setT(0);
            e.setDt(tbMusicPojo.getTimeLength());
            e.setAlia(ListUtil.toList(StringUtils.split(tbMusicPojo.getAliasName(), ",")));
            e.setSq(null);
            e.setHr(null);
            e.setA(null);
            e.setCd("01");
            e.setNo(1);
            e.setRtUrl(null);
            e.setFtype(0);
            e.setRtUrls(new ArrayList<>());
            e.setDjId(0);
            e.setCopyright(2);
            e.setSId(0);
            e.setMark(0);
            e.setOriginCoverType(1);
            e.setOriginSongSimpleData(null);
            e.setTagPicList(null);
            e.setResourceState(true);
            e.setVersion(668);
            e.setSongJumpInfo(null);
            e.setEntertainmentTags(null);
            e.setAwardTags(null);
            e.setSingle(0);
            e.setNoCopyrightRcmd(null);
            e.setMv(0);
            e.setRtype(0);
            e.setRurl(null);
            e.setMst(9);
            e.setCp(0);
            e.setPublishTime(DateUtil.date(tbMusicPojo.getCreateTime()).getTime());
            
            List<ArtistConvert> singerByMusicId = artistByMusicIdToMap.get(tbMusicPojo.getId());
            if (CollUtil.isNotEmpty(singerByMusicId)) {
                ArrayList<ArItem> ar = new ArrayList<>();
                // 歌手
                for (ArtistConvert tbArtistPojo : singerByMusicId) {
                    ArItem e1 = new ArItem();
                    e1.setName(tbArtistPojo.getArtistName());
                    e1.setId(tbArtistPojo.getId());
                    e1.setAlias(Arrays.asList(Optional.ofNullable(tbArtistPojo.getAliasName()).orElse("").split(",")));
                    ar.add(e1);
                }
                e.setAr(ar);
            }
            
            // 专辑
            AlbumConvert albumByAlbumId = musicAlbumByAlbumIdToMap.get(tbMusicPojo.getAlbumId());
            if (Objects.nonNull(albumByAlbumId)) {
                Al al = new Al();
                al.setName(albumByAlbumId.getAlbumName());
                al.setPicUrl(albumByAlbumId.getPicUrl());
                al.setId(albumByAlbumId.getId());
                e.setAl(al);
            }
            
            songs.add(e);
            
            // PrivilegesItem privilegesItem = new PrivilegesItem();
            privilegesItem.setId(tbMusicPojo.getId());
            // 0 为无效歌曲，就是网易云没版权
            privilegesItem.setPl(1);
            // 0: 免费或无版权
            privilegesItem.setFee(0);
            privileges.add(privilegesItem);
        }
        songDetailRes.setSongs(songs);
        songDetailRes.setPrivileges(privileges);
        
        return songDetailRes;
    }
    
    public SongLyricRes lyric(Long id) {
        SongLyricRes songLyricRes = new SongLyricRes();
        List<TbLyricPojo> tbLyricPojos = Optional.ofNullable(qukuService.getMusicLyric(id))
                                                 .orElse(new ArrayList<>());
        TbLyricPojo lyricPojo = tbLyricPojos.stream()
                                            .filter(tbLyricPojo -> StringUtils.equals(tbLyricPojo.getType(), LyricConstant.LYRIC))
                                            .findFirst()
                                            .orElse(new TbLyricPojo());
    
        TbLyricPojo kLyricPojo = tbLyricPojos.stream()
                                             .filter(tbLyricPojo -> StringUtils.equals(tbLyricPojo.getType(), LyricConstant.K_LYRIC))
                                             .findFirst()
                                             .orElse(new TbLyricPojo());
    
        TbLyricPojo tLyricPojo = tbLyricPojos.stream()
                                             .filter(tbLyricPojo -> StringUtils.equals(tbLyricPojo.getType(), LyricConstant.K_LYRIC))
                                             .findFirst()
                                             .orElse(new TbLyricPojo());
        Lrc lrc = new Lrc();
        // 普通歌词
        lrc.setLyric(lyricPojo.getLyric());
        songLyricRes.setLrc(lrc);
        Klyric klyric = new Klyric();
        // 逐词歌词
        klyric.setLyric(kLyricPojo.getLyric());
        songLyricRes.setKlyric(klyric);
        // 未知歌词
        Tlyric tlyric = new Tlyric();
        tlyric.setLyric(tLyricPojo.getLyric());
        songLyricRes.setTlyric(tlyric);
        return songLyricRes;
    }
    
    /**
     * 听歌打卡
     *
     * @param id       歌曲ID
     * @param sourceid 歌单或者专辑ID
     * @param time     歌曲播放时间
     */
    @Transactional(rollbackFor = Exception.class)
    public void scrobble(Long id, Long sourceid, Long time) {
        Long userId = UserUtil.getUser().getId();
        
        LambdaQueryWrapper<TbHistoryPojo> musicWrapper = Wrappers.<TbHistoryPojo>lambdaQuery()
                                                                 .eq(TbHistoryPojo::getUserId, userId)
                                                                 .eq(TbHistoryPojo::getId, id);
        TbHistoryPojo musicRank = historyService.getOne(musicWrapper);
        ArrayList<TbHistoryPojo> entityList = new ArrayList<>();
        // 没有数据则添加数据到表中
        if (musicRank == null) {
            TbHistoryPojo musicRankPojo = new TbHistoryPojo();
            musicRankPojo.setUserId(userId);
            musicRankPojo.setCount(1);
            musicRankPojo.setMiddleId(id);
            musicRankPojo.setPlayedTime(time);
            musicRankPojo.setType(HistoryConstant.MUSIC);
            entityList.add(musicRankPojo);
        } else {
            Integer broadcastCount = musicRank.getCount();
            broadcastCount = broadcastCount + 1;
            musicRank.setCount(broadcastCount);
            historyService.update(musicRank, musicWrapper);
        }
        
        // 专辑或歌单
        LambdaQueryWrapper<TbHistoryPojo> sourceWrapper = Wrappers.<TbHistoryPojo>lambdaQuery()
                                                                  .eq(TbHistoryPojo::getUserId, userId)
                                                                  .eq(TbHistoryPojo::getMiddleId, sourceid);
        TbHistoryPojo sourcePojo = historyService.getOne(sourceWrapper);
        if (sourcePojo == null) {
            // 专辑或歌单ID
            TbHistoryPojo rankPojo = new TbHistoryPojo();
            rankPojo.setUserId(userId);
            rankPojo.setMiddleId(sourceid);
            rankPojo.setPlayedTime(time);
            rankPojo.setCount(1);
            
            TbCollectPojo collectPojo = collectService.getById(sourceid);
            if (collectPojo == null) {
                TbAlbumPojo albumPojo = albumService.getById(sourceid);
                if (albumPojo != null) {
                    rankPojo.setType(HistoryConstant.ALBUM);
                } else {
                    TbArtistPojo artistPojo = tbArtistService.getById(sourceid);
                    if (Objects.nonNull(artistPojo)) {
                        rankPojo.setType(HistoryConstant.ARTIST);
                    } else {
                        TbMvPojo mvPojo = tbMvService.getById(sourceid);
                        if (Objects.nonNull(mvPojo)) {
                            rankPojo.setType(HistoryConstant.MV);
                        }
                    }
                }
            } else {
                rankPojo.setType(HistoryConstant.PLAYLIST);
            }
            entityList.add(rankPojo);
        } else {
            // 歌单或专辑播放数量
            Integer broadcastCount = sourcePojo.getCount();
            broadcastCount = broadcastCount + 1;
            sourcePojo.setCount(broadcastCount);
            Long playedTime = sourcePojo.getPlayedTime();
            // 增加歌单歌曲播放总时间
            long sumPlaytime = playedTime + time;
            sourcePojo.setPlayedTime(sumPlaytime);
            historyService.update(sourcePojo, sourceWrapper);
        }
        if (CollUtil.isNotEmpty(entityList)) {
            historyService.saveBatch(entityList);
        }
    }
    
    public SongUrlV1 songUrlV1(List<Long> id, String level) {
        List<TbResourcePojo> musicUrlByMusicId = qukuService.getMusicUrlByMusicId(new HashSet<>(id), false);
        List<TbMusicPojo> musicPojos = musicService.listByIds(id);
        Map<Long, TbMusicPojo> musicPojoMap = musicPojos.stream().collect(Collectors.toMap(TbMusicPojo::getId, tbMusicPojo -> tbMusicPojo));
        SongUrlV1 songUrlRes = new SongUrlV1();
        ArrayList<org.api.nmusic.model.vo.songurlv1.DataItem> data = new ArrayList<>();
        for (TbResourcePojo tbMusicUrlPojo : musicUrlByMusicId) {
            org.api.nmusic.model.vo.songurlv1.DataItem e = new org.api.nmusic.model.vo.songurlv1.DataItem();
            e.setId(tbMusicUrlPojo.getId());
            e.setUrl(tbMusicUrlPojo.getPath());
            e.setExpi(60 * 60 * 1000);
            e.setBr(tbMusicUrlPojo.getRate());
            e.setFee(1);
            e.setSize(tbMusicUrlPojo.getSize());
            e.setCode(200);
            e.setType(tbMusicUrlPojo.getEncodeType());
            e.setEncodeType(tbMusicUrlPojo.getEncodeType());
            e.setLevel(tbMusicUrlPojo.getLevel());
            e.setMd5(tbMusicUrlPojo.getMd5());
            e.setUrlSource(0);
            e.setRightSource(0);
            e.setTime(Optional.ofNullable(musicPojoMap.get(tbMusicUrlPojo.getMusicId())).orElse(new TbMusicPojo()).getTimeLength());
            data.add(e);
            if (StringUtils.equals(tbMusicUrlPojo.getLevel(), level)) {
                break;
            }
        }
        songUrlRes.setData(data);
        return songUrlRes;
    }
}
