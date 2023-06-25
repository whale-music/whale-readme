package org.api.neteasecloudmusic.service;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.api.common.service.QukuAPI;
import org.api.neteasecloudmusic.config.NeteaseCloudConfig;
import org.api.neteasecloudmusic.model.vo.song.lyric.Klyric;
import org.api.neteasecloudmusic.model.vo.song.lyric.Lrc;
import org.api.neteasecloudmusic.model.vo.song.lyric.SongLyricRes;
import org.api.neteasecloudmusic.model.vo.song.lyric.Tlyric;
import org.api.neteasecloudmusic.model.vo.songdetail.*;
import org.api.neteasecloudmusic.model.vo.songurl.DataItem;
import org.api.neteasecloudmusic.model.vo.songurl.SongUrlRes;
import org.core.config.LyricConfig;
import org.core.mybatis.iservice.TbAlbumService;
import org.core.mybatis.iservice.TbCollectService;
import org.core.mybatis.iservice.TbHistoryService;
import org.core.mybatis.iservice.TbMusicService;
import org.core.mybatis.model.convert.AlbumConvert;
import org.core.mybatis.model.convert.ArtistConvert;
import org.core.mybatis.pojo.*;
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
    private QukuAPI qukuService;
    
    @Autowired
    private TbHistoryService historyService;
    
    @Autowired
    private TbAlbumService albumService;
    
    @Autowired
    private TbCollectService collectService;
    
    
    public SongUrlRes songUrl(List<Long> id, Integer br) {
        List<TbMusicUrlPojo> musicUrlByMusicId = qukuService.getMusicUrlByMusicId(new HashSet<>(id), false);
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
            List<ArtistConvert> singerByMusicId = qukuService.getAlbumArtistByMusicId(tbMusicPojo.getId());
            
            // 歌手
            for (ArtistConvert tbArtistPojo : singerByMusicId) {
                ArItem e1 = new ArItem();
                e1.setName(tbArtistPojo.getArtistName());
                e1.setId(tbArtistPojo.getId());
                e1.setAlias(Arrays.asList(Optional.ofNullable(tbArtistPojo.getAliasName()).orElse("").split(",")));
                ar.add(e1);
            }
            e.setAr(ar);
            
            // 专辑
            AlbumConvert albumByAlbumId = qukuService.getAlbumByAlbumId(tbMusicPojo.getAlbumId());
            Al al = new Al();
            al.setName(albumByAlbumId.getAlbumName());
            al.setPicUrl(albumByAlbumId.getPicUrl());
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
        
        LambdaQueryWrapper<TbHistoryPojo> musicWrapper = Wrappers.<TbHistoryPojo>lambdaQuery().eq(TbHistoryPojo::getUserId, userId).eq(TbHistoryPojo::getId, id);
        TbHistoryPojo musicRank = historyService.getOne(musicWrapper);
        ArrayList<TbHistoryPojo> entityList = new ArrayList<>();
        // 没有数据则添加数据到表中
        if (musicRank == null) {
            TbHistoryPojo musicRankPojo = new TbHistoryPojo();
            musicRankPojo.setId(id);
            musicRankPojo.setUserId(userId);
            musicRankPojo.setCount(1);
            musicRankPojo.setCount(0);
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
                                                               .eq(TbHistoryPojo::getId, sourceid);
        TbHistoryPojo sourcePojo = historyService.getOne(sourceWrapper);
        if (sourcePojo == null) {
            // 专辑或歌单ID
            TbHistoryPojo rankPojo = new TbHistoryPojo();
            rankPojo.setUserId(userId);
            rankPojo.setId(sourceid);
            rankPojo.setCount(1);
    
            TbCollectPojo collectPojo = collectService.getById(sourceid);
            if (collectPojo != null) {
                rankPojo.setCount(1);
            } else {
                TbAlbumPojo albumPojo = albumService.getById(sourceid);
                if (albumPojo != null) {
                    rankPojo.setType(2);
                }
            }
            entityList.add(rankPojo);
        } else {
            Integer broadcastCount = sourcePojo.getCount();
            broadcastCount = broadcastCount + 1;
            sourcePojo.setType(broadcastCount);
            historyService.update(sourcePojo, sourceWrapper);
        }
        historyService.saveBatch(entityList);
    }
}
