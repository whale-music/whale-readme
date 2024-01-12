package org.api.neteasecloudmusic.service;

import lombok.extern.slf4j.Slf4j;
import org.api.common.service.QukuAPI;
import org.api.neteasecloudmusic.config.NeteaseCloudConfig;
import org.core.mybatis.model.convert.ArtistConvert;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service(NeteaseCloudConfig.NETEASECLOUD + "SongApi")
public class SongApi {
    /**
     * 曲库服务
     */
    private final QukuAPI qukuService;
    
    public SongApi(QukuAPI qukuService) {
        this.qukuService = qukuService;
    }
    
    /**
     * 获取歌曲下艺术家信息
     */
    public List<ArtistConvert> getSingerByMusicId(String musicId) {
        return qukuService.getAlbumArtistByMusicId(Long.valueOf(musicId));
    }
}
