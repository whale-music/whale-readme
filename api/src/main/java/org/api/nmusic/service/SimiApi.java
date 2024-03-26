package org.api.nmusic.service;

import lombok.extern.slf4j.Slf4j;
import org.api.common.service.QukuAPI;
import org.api.nmusic.config.NeteaseCloudConfig;
import org.api.nmusic.model.vo.simi.SimiArtistRes;
import org.core.mybatis.model.convert.ArtistConvert;
import org.core.utils.AliasUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service(NeteaseCloudConfig.NETEASECLOUD + "SimiApi")
public class SimiApi {
    
    private final QukuAPI qukuService;
    
    public SimiApi(QukuAPI qukuService) {
        this.qukuService = qukuService;
    }
    
    /**
     * 获取相似歌手
     *
     * @param id 歌手 id
     */
    public List<SimiArtistRes> simiArtist(Long id) {
        ArrayList<SimiArtistRes> simiArtistRes = new ArrayList<>();
        List<ArtistConvert> tbArtistPojos = qukuService.randomSinger(20);
        // todo: 需要优化
        for (ArtistConvert tbArtistPojo : tbArtistPojos) {
            SimiArtistRes artistRes = new SimiArtistRes();
            artistRes.setId(tbArtistPojo.getId());
            artistRes.setPicUrl(tbArtistPojo.getPicUrl());
            artistRes.setName(tbArtistPojo.getArtistName());
            artistRes.setAlias(AliasUtil.getAliasList(tbArtistPojo.getAliasName()));
            artistRes.setAlbumSize(qukuService.getArtistAlbumCountByArtistId(tbArtistPojo.getId()));
            artistRes.setMusicSize(qukuService.getMusicCountByArtistId(tbArtistPojo.getId()));
            artistRes.setBriefDesc(tbArtistPojo.getIntroduction());
            simiArtistRes.add(artistRes);
        }
        return simiArtistRes;
    }
}
