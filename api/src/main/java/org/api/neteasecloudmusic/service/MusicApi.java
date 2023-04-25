package org.api.neteasecloudmusic.service;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.api.common.service.MusicCommonApi;
import org.api.neteasecloudmusic.config.NeteaseCloudConfig;
import org.api.neteasecloudmusic.model.vo.song.lyric.Klyric;
import org.api.neteasecloudmusic.model.vo.song.lyric.Lrc;
import org.api.neteasecloudmusic.model.vo.song.lyric.SongLyricRes;
import org.api.neteasecloudmusic.model.vo.song.lyric.Tlyric;
import org.api.neteasecloudmusic.model.vo.songdetail.*;
import org.api.neteasecloudmusic.model.vo.songurl.DataItem;
import org.api.neteasecloudmusic.model.vo.songurl.SongUrlRes;
import org.core.config.LyricConfig;
import org.core.iservice.TbAlbumService;
import org.core.iservice.TbCollectService;
import org.core.iservice.TbMusicService;
import org.core.iservice.TbRankService;
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
    private TbMusicService musicService;
    
    @Autowired
    private MusicCommonApi musicCommonApi;
    
    @Autowired
    private QukuService qukuService;
    
    @Autowired
    private TbRankService rankService;
    
    @Autowired
    private TbAlbumService albumService;
    
    @Autowired
    private TbCollectService collectService;
    
    
    public SongUrlRes songUrl(List<Long> id, Integer br) {
        List<TbMusicUrlPojo> musicUrlByMusicId = musicCommonApi.getMusicUrlByMusicId(new HashSet<>(id), false);
        List<TbMusicPojo> musicPojos = musicService.listByIds(id);
        Map<Long, TbMusicPojo> musicPojoMap = musicPojos.stream().collect(Collectors.toMap(TbMusicPojo::getId, tbMusicPojo -> tbMusicPojo));
        SongUrlRes songUrlRes = new SongUrlRes();
        ArrayList<DataItem> data = new ArrayList<>();
        for (TbMusicUrlPojo tbMusicUrlPojo : musicUrlByMusicId) {
            DataItem e = new DataItem();
            e.setId(tbMusicUrlPojo.getId());
            e.setUrl(tbMusicUrlPojo.getUrl());
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
        for (TbMusicPojo tbMusicPojo : musicPojoList) {
            SongsItem e = new SongsItem();
            e.setId(tbMusicPojo.getId());
            e.setName(tbMusicPojo.getMusicName());
            e.setPublishTime(tbMusicPojo.getCreateTime().getNano());
            e.setDt(tbMusicPojo.getTimeLength());
            ArrayList<ArItem> ar = new ArrayList<>();
            List<TbArtistPojo> singerByMusicId = qukuService.getArtistByMusicId(tbMusicPojo.getId());
            
            // 歌手
            for (TbArtistPojo tbArtistPojo : singerByMusicId) {
                ArItem e1 = new ArItem();
                e1.setName(tbArtistPojo.getArtistName());
                e1.setId(tbArtistPojo.getId());
                e1.setAlias(Arrays.asList(Optional.ofNullable(tbArtistPojo.getAliasName()).orElse("").split(",")));
                ar.add(e1);
            }
            e.setAr(ar);
            
            // 专辑
            TbAlbumPojo albumByAlbumId = qukuService.getAlbumByAlbumId(tbMusicPojo.getAlbumId());
            Al al = new Al();
            al.setName(albumByAlbumId.getAlbumName());
            al.setPicUrl(albumByAlbumId.getPic());
            al.setId(albumByAlbumId.getId());
            e.setAl(al);
            
            songs.add(e);
            
            PrivilegesItem privilegesItem = new PrivilegesItem();
            privilegesItem.setId(tbMusicPojo.getId());
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
                                            .filter(tbLyricPojo -> StringUtils.equals(tbLyricPojo.getType(), LyricConfig.LYRIC))
                                            .findFirst()
                                            .orElse(new TbLyricPojo());
    
        TbLyricPojo kLyricPojo = tbLyricPojos.stream()
                                             .filter(tbLyricPojo -> StringUtils.equals(tbLyricPojo.getType(), LyricConfig.K_LYRIC))
                                             .findFirst()
                                             .orElse(new TbLyricPojo());
    
        TbLyricPojo tLyricPojo = tbLyricPojos.stream()
                                             .filter(tbLyricPojo -> StringUtils.equals(tbLyricPojo.getType(), LyricConfig.K_LYRIC))
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
    public void scrobble(Long id, Long sourceid, Integer time) {
        Long userId = UserUtil.getUser().getId();
        
        LambdaQueryWrapper<TbRankPojo> musicWrapper = Wrappers.<TbRankPojo>lambdaQuery().eq(TbRankPojo::getUserId, userId).eq(TbRankPojo::getId, id);
        TbRankPojo musicRank = rankService.getOne(musicWrapper);
        ArrayList<TbRankPojo> entityList = new ArrayList<>();
        // 没有数据则添加数据到表中
        if (musicRank == null) {
            TbRankPojo musicRankPojo = new TbRankPojo();
            musicRankPojo.setId(id);
            musicRankPojo.setUserId(userId);
            musicRankPojo.setBroadcastCount(1);
            musicRankPojo.setBroadcastType(0);
            entityList.add(musicRankPojo);
        } else {
            Integer broadcastCount = musicRank.getBroadcastCount();
            broadcastCount = broadcastCount + 1;
            musicRank.setBroadcastCount(broadcastCount);
            rankService.update(musicRank, musicWrapper);
        }
        
        // 专辑或歌单
        LambdaQueryWrapper<TbRankPojo> sourceWrapper = Wrappers.<TbRankPojo>lambdaQuery()
                                                               .eq(TbRankPojo::getUserId, userId)
                                                               .eq(TbRankPojo::getId, sourceid);
        TbRankPojo sourcePojo = rankService.getOne(sourceWrapper);
        if (sourcePojo == null) {
            // 专辑或歌单ID
            TbRankPojo rankPojo = new TbRankPojo();
            rankPojo.setUserId(userId);
            rankPojo.setId(sourceid);
            rankPojo.setBroadcastCount(1);
    
            TbCollectPojo collectPojo = collectService.getById(sourceid);
            if (collectPojo != null) {
                rankPojo.setBroadcastType(1);
            } else {
                TbAlbumPojo albumPojo = albumService.getById(sourceid);
                if (albumPojo != null) {
                    rankPojo.setBroadcastType(2);
                }
            }
            entityList.add(rankPojo);
        } else {
            Integer broadcastCount = sourcePojo.getBroadcastCount();
            broadcastCount = broadcastCount + 1;
            sourcePojo.setBroadcastCount(broadcastCount);
            rankService.update(sourcePojo, sourceWrapper);
        }
        rankService.saveBatch(entityList);
    }
}
