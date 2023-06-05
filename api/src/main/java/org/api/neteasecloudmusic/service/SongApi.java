package org.api.neteasecloudmusic.service;

import lombok.extern.slf4j.Slf4j;
import org.api.common.service.QukuAPI;
import org.api.neteasecloudmusic.config.NeteaseCloudConfig;
import org.core.model.convert.ArtistConvert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service(NeteaseCloudConfig.NETEASECLOUD + "SongApi")
public class SongApi {
    /**
     * 曲库服务
     */
    @Autowired
    private QukuAPI qukuService;
    
    /**
     * 获取歌曲下艺术家信息
     */
    public List<ArtistConvert> getSingerByMusicId(String musicId) {
        return qukuService.getAlbumArtistByMusicId(Long.valueOf(musicId));
    }
}
