package org.api.neteasecloudmusic.service;

import lombok.extern.slf4j.Slf4j;
import org.api.common.service.QukuAPI;
import org.api.neteasecloudmusic.config.NeteaseCloudConfig;
import org.api.neteasecloudmusic.model.vo.simi.SimiArtistRes;
import org.core.mybatis.model.convert.ArtistConvert;
import org.core.utils.AliasUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service(NeteaseCloudConfig.NETEASECLOUD + "SimiApi")
public class SimiApi {
    
    @Autowired
    private QukuAPI qukuService;
    
    /**
     * 获取相似歌手
     *
     * @param id 歌手 id
     */
    public List<SimiArtistRes> simiArtist(Long id) {
        ArrayList<SimiArtistRes> simiArtistRes = new ArrayList<>();
        List<ArtistConvert> tbArtistPojos = qukuService.randomSinger(20);
        for (ArtistConvert tbArtistPojo : tbArtistPojos) {
            SimiArtistRes artistRes = new SimiArtistRes();
            artistRes.setId(tbArtistPojo.getId());
            artistRes.setPicUrl(tbArtistPojo.getPicUrl());
            artistRes.setName(tbArtistPojo.getArtistName());
            artistRes.setAlias(AliasUtil.getAliasList(tbArtistPojo.getAliasName()));
            artistRes.setAlbumSize(qukuService.getAlbumCountBySingerId(tbArtistPojo.getId()));
            artistRes.setMusicSize(qukuService.getMusicCountBySingerId(tbArtistPojo.getId()));
            artistRes.setBriefDesc(tbArtistPojo.getIntroduction());
            simiArtistRes.add(artistRes);
        }
        return simiArtistRes;
    }
}
