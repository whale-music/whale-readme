package org.api.neteasecloudmusic.service;

import cn.hutool.core.collection.CollUtil;
import lombok.extern.slf4j.Slf4j;
import org.api.common.service.MusicCommonApi;
import org.api.neteasecloudmusic.config.NeteaseCloudConfig;
import org.api.neteasecloudmusic.model.vo.song.lyric.Klyric;
import org.api.neteasecloudmusic.model.vo.song.lyric.Lrc;
import org.api.neteasecloudmusic.model.vo.song.lyric.SongLyricRes;
import org.api.neteasecloudmusic.model.vo.songdetail.*;
import org.api.neteasecloudmusic.model.vo.songurl.DataItem;
import org.api.neteasecloudmusic.model.vo.songurl.SongUrlRes;
import org.core.common.page.LambdaQueryWrapper;
import org.core.common.page.Wrappers;
import org.core.iservice.AlbumService;
import org.core.iservice.CollectService;
import org.core.iservice.MusicService;
import org.core.iservice.RankService;
import org.core.pojo.*;
import org.core.service.QukuService;
import org.core.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service(NeteaseCloudConfig.NETEASECLOUD + "MusicApi")
public class MusicApi {
    
    @Autowired
    private MusicService musicService;
    
    @Autowired
    private MusicCommonApi musicCommonApi;
    
    @Autowired
    private QukuService qukuService;
    
    @Autowired
    private RankService rankService;
    
    @Autowired
    private AlbumService albumService;
    
    @Autowired
    private CollectService collectService;
    
    public SongUrlRes songUrl(List<Long> id, Integer br) {
        List<MusicUrlPojo> musicUrlByMusicId = musicCommonApi.getMusicUrlByMusicId(Set.copyOf(id));
        List<MusicPojo> musicPojos = musicService.listByIds(id);
        Map<Long, MusicPojo> musicPojoMap = musicPojos.stream().collect(Collectors.toMap(MusicPojo::getId, tbMusicPojo -> tbMusicPojo));
        SongUrlRes songUrlRes = new SongUrlRes();
        ArrayList<DataItem> data = new ArrayList<>();
        for (MusicUrlPojo musicUrlPojo : musicUrlByMusicId) {
            DataItem e = new DataItem();
            e.setId(musicUrlPojo.getId());
            e.setUrl(musicUrlPojo.getUrl());
            e.setBr(musicUrlPojo.getRate());
            e.setSize(musicUrlPojo.getSize());
            e.setCode(200);
            e.setType(musicUrlPojo.getEncodeType());
            e.setEncodeType(musicUrlPojo.getEncodeType());
            e.setLevel(musicUrlPojo.getLevel());
            e.setMd5(musicUrlPojo.getMd5());
            e.setTime(Optional.ofNullable(musicPojoMap.get(musicUrlPojo.getMusicId())).orElse(new MusicPojo()).getTimeLength());
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
        List<MusicPojo> musicPojoList = musicService.listByIds(ids);
        if (CollUtil.isEmpty(musicPojoList)) {
            return new SongDetailRes();
        }
        SongDetailRes songDetailRes = new SongDetailRes();
        ArrayList<SongsItem> songs = new ArrayList<>();
        
        ArrayList<PrivilegesItem> privileges = new ArrayList<>();
        for (MusicPojo musicPojo : musicPojoList) {
            SongsItem e = new SongsItem();
            e.setId(musicPojo.getId());
            e.setName(musicPojo.getMusicName());
            e.setPublishTime(musicPojo.getCreateTime().getNano());
            e.setDt(musicPojo.getTimeLength());
            ArrayList<ArItem> ar = new ArrayList<>();
            List<ArtistPojo> singerByMusicId = qukuService.getSingerByMusicId(musicPojo.getId());
            
            // 歌手
            for (ArtistPojo artistPojo : singerByMusicId) {
                ArItem e1 = new ArItem();
                e1.setName(artistPojo.getArtistName());
                e1.setId(artistPojo.getId());
                e1.setAlias(Arrays.asList(Optional.ofNullable(artistPojo.getAliasName()).orElse("").split(",")));
                ar.add(e1);
            }
            e.setAr(ar);
            
            // 专辑
            AlbumPojo albumByAlbumId = qukuService.getAlbumByAlbumId(musicPojo.getAlbumId());
            Al al = new Al();
            al.setName(albumByAlbumId.getAlbumName());
            al.setPicUrl(albumByAlbumId.getPic());
            al.setId(albumByAlbumId.getId());
            e.setAl(al);
            
            songs.add(e);
            
            PrivilegesItem privilegesItem = new PrivilegesItem();
            privilegesItem.setId(musicPojo.getId());
            privileges.add(privilegesItem);
        }
        songDetailRes.setSongs(songs);
        songDetailRes.setPrivileges(privileges);
        
        return songDetailRes;
    }
    
    public SongLyricRes lyric(Long id) {
        SongLyricRes songLyricRes = new SongLyricRes();
        MusicPojo musicPojo = Optional.ofNullable(musicService.getById(id)).orElse(new MusicPojo());
        Lrc lrc = new Lrc();
        lrc.setLyric(musicPojo.getLyric());
        songLyricRes.setLrc(lrc);
        Klyric klyric = new Klyric();
        klyric.setLyric(musicPojo.getkLyric());
        songLyricRes.setKlyric(klyric);
        return songLyricRes;
    }
    
    /**
     * 听歌打卡
     *
     * @param id       歌曲ID
     * @param sourceid 歌单或者专辑ID
     * @param time     歌曲播放时间
     */
    public void scrobble(Long id, Long sourceid, Integer time) {
        Long userId = UserUtil.getUser().getId();
        
        LambdaQueryWrapper<RankPojo> musicWrapper = Wrappers.<RankPojo>lambdaQuery().eq(RankPojo::getUserId, userId).eq(RankPojo::getId, id);
        Optional<RankPojo> musicRank = rankService.getOne(musicWrapper);
        ArrayList<RankPojo> entityList = new ArrayList<>();
        // 没有数据则添加数据到表中
        if (musicRank.isEmpty()) {
            RankPojo musicRankPojo = new RankPojo();
            musicRankPojo.setId(id);
            musicRankPojo.setUserId(userId);
            musicRankPojo.setBroadcastCount(1);
            musicRankPojo.setBroadcastType(0);
            entityList.add(musicRankPojo);
        } else {
            Integer broadcastCount = musicRank.get().getBroadcastCount();
            broadcastCount = broadcastCount + 1;
            musicRank.get().setBroadcastCount(broadcastCount);
            rankService.update(musicRank.get(), musicWrapper);
        }
        
        // 专辑或歌单
        LambdaQueryWrapper<RankPojo> sourceWrapper = Wrappers.<RankPojo>lambdaQuery()
                                                             .eq(RankPojo::getUserId, userId)
                                                             .eq(RankPojo::getId, sourceid);
        Optional<RankPojo> sourcePojo = rankService.getOne(sourceWrapper);
        if (sourcePojo .isEmpty()) {
            // 专辑或歌单ID
            RankPojo rankPojo = new RankPojo();
            rankPojo.setUserId(userId);
            rankPojo.setId(sourceid);
            rankPojo.setBroadcastCount(1);
            
            CollectPojo collectPojo = collectService.getById(sourceid);
            if (collectPojo != null) {
                rankPojo.setBroadcastType(1);
            } else {
                AlbumPojo albumPojo = albumService.getById(sourceid);
                if (albumPojo != null) {
                    rankPojo.setBroadcastType(2);
                }
            }
            entityList.add(rankPojo);
        } else {
            Integer broadcastCount = sourcePojo.get().getBroadcastCount();
            broadcastCount = broadcastCount + 1;
            sourcePojo.get().setBroadcastCount(broadcastCount);
            rankService.update(sourcePojo.get(), sourceWrapper);
        }
        rankService.saveBatch(entityList);
    }
}
