package org.api.subsonic.service;


import cn.hutool.core.collection.CollUtil;
import org.api.common.service.MusicCommonApi;
import org.api.subsonic.common.SubsonicCommonReq;
import org.api.subsonic.config.SubsonicConfig;
import org.core.iservice.TbAlbumService;
import org.core.iservice.TbCollectService;
import org.core.iservice.TbMusicService;
import org.core.pojo.TbAlbumPojo;
import org.core.pojo.TbCollectPojo;
import org.core.pojo.TbMusicPojo;
import org.core.pojo.TbMusicUrlPojo;
import org.core.service.QukuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(SubsonicConfig.SUBSONIC + "MediaRetrievalApi")
public class MediaRetrievalApi {
    
    @Autowired
    private QukuService qukuService;
    
    @Autowired
    private TbAlbumService albumService;
    
    @Autowired
    private TbMusicService musicService;
    
    @Autowired
    private TbCollectService collectService;
    
    @Autowired
    private MusicCommonApi musicCommonApi;
    
    public String getCoverArt(SubsonicCommonReq req, Long id) {
        TbAlbumPojo byId = albumService.getById(id);
        if (byId != null) {
            return byId.getPic();
        }
        TbMusicPojo byId1 = musicService.getById(id);
        if (byId1 != null) {
            return byId1.getPic();
        }
        TbCollectPojo byId2 = collectService.getById(id);
        if (byId2 != null) {
            return byId2.getPic();
        }
        
        return "http://p3.music.126.net/jWE3OEZUlwdz0ARvyQ9wWw==/109951165474121408.jpg";
    }
    
    public String stream(SubsonicCommonReq req, Long id) {
        List<TbMusicUrlPojo> musicUrlByMusicId = musicCommonApi.getMusicUrlByMusicId(id, false);
        return CollUtil.isEmpty(musicUrlByMusicId) ? "" : musicUrlByMusicId.get(0).getUrl();
    }
}
