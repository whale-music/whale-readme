package org.api.neteasecloudmusic.service;

import lombok.extern.slf4j.Slf4j;
import org.core.pojo.TbSingerPojo;
import org.core.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service("SongApi")
public class SongApi {
    
    @Autowired
    private TbMusicService musicService;
    
    @Autowired
    private TbAlbumService albumService;
    
    @Autowired
    private TbMusicSingerService musicSingerService;
    
    @Autowired
    private TbSingerService singerService;
    
    /**
     * 曲库服务
     */
    @Autowired
    private QukuService qukuService;
    
    /**
     * 获取歌曲下艺术家信息
     */
    public List<TbSingerPojo> getSingerByMusicId(String musicId) {
        return qukuService.getSingerByMusicId(Long.valueOf(musicId));
    }
}
