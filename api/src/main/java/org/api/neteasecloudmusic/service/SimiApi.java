package org.api.neteasecloudmusic.service;

import lombok.extern.slf4j.Slf4j;
import org.api.neteasecloudmusic.config.NeteaseCloudConfig;
import org.api.neteasecloudmusic.model.vo.simi.SimiArtistRes;
import org.core.pojo.ArtistPojo;
import org.core.service.QukuService;
import org.core.utils.AliasUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service(NeteaseCloudConfig.NETEASECLOUD + "SimiApi")
public class SimiApi {
    
    @Autowired
    private QukuService qukuService;
    
    /**
     * 获取相似歌手
     *
     * @param id 歌手 id
     */
    public List<SimiArtistRes> simiArtist(Long id) {
        ArrayList<SimiArtistRes> simiArtistRes = new ArrayList<>();
        List<ArtistPojo> artistPojos = qukuService.randomSinger(20);
        for (ArtistPojo artistPojo : artistPojos) {
            SimiArtistRes artistRes = new SimiArtistRes();
            artistRes.setId(artistPojo.getId());
            artistRes.setPicUrl(artistPojo.getPic());
            artistRes.setName(artistPojo.getArtistName());
            artistRes.setAlias(AliasUtil.getAliasList(artistPojo.getAliasName()));
            artistRes.setAlbumSize(qukuService.getAlbumCountBySingerId(artistPojo.getId()));
            artistRes.setMusicSize(qukuService.getMusicCountBySingerId(artistPojo.getId()));
            artistRes.setBriefDesc(artistPojo.getIntroduction());
            simiArtistRes.add(artistRes);
        }
        return simiArtistRes;
    }
}
