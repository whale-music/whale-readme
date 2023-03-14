package org.api.neteasecloudmusic.service;

import cn.hutool.core.collection.CollUtil;
import lombok.extern.slf4j.Slf4j;
import org.api.common.service.MusicCommonApi;
import org.api.neteasecloudmusic.model.vo.songdetail.*;
import org.api.neteasecloudmusic.model.vo.songurl.DataItem;
import org.api.neteasecloudmusic.model.vo.songurl.SongUrlRes;
import org.core.pojo.TbAlbumPojo;
import org.core.pojo.TbMusicPojo;
import org.core.pojo.TbMusicUrlPojo;
import org.core.pojo.TbSingerPojo;
import org.core.service.QukuService;
import org.core.service.TbMusicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service("MusicApi")
public class MusicApi {
    
    @Autowired
    private TbMusicService musicService;
    
    @Autowired
    private MusicCommonApi musicCommonApi;
    
    @Autowired
    private QukuService qukuService;
    
    
    public SongUrlRes songUrl(List<Long> id, Integer br) {
        List<TbMusicUrlPojo> musicUrlByMusicId = musicCommonApi.getMusicUrlByMusicId(Set.copyOf(id));
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
            ArrayList<ArItem> ar = new ArrayList<>();
            List<TbSingerPojo> singerByMusicId = qukuService.getSingerByMusicId(tbMusicPojo.getId());
            
            // 歌手
            for (TbSingerPojo tbSingerPojo : singerByMusicId) {
                ArItem e1 = new ArItem();
                e1.setName(tbSingerPojo.getSingerName());
                e1.setId(tbSingerPojo.getId());
                e1.setAlias(Arrays.asList(Optional.ofNullable(tbSingerPojo.getAlias()).orElse("").split(",")));
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
}
