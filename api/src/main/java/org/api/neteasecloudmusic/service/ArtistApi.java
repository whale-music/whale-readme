package org.api.neteasecloudmusic.service;

import lombok.extern.slf4j.Slf4j;
import org.api.neteasecloudmusic.model.vo.artist.sublist.ArtistSubListRes;
import org.api.neteasecloudmusic.model.vo.artist.sublist.DataItem;
import org.core.pojo.SysUserPojo;
import org.core.pojo.TbSingerPojo;
import org.core.service.QukuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service("NeteaseCloudArtistApi")
public class ArtistApi {
    @Autowired
    private QukuService qukuService;
    
    
    public ArtistSubListRes artistSublist(SysUserPojo user) {
        ArtistSubListRes res = new ArtistSubListRes();
        List<TbSingerPojo> userPojoList = qukuService.getUserLikeSingerList(user);
        ArrayList<DataItem> data = new ArrayList<>();
        for (TbSingerPojo tbSingerPojo : userPojoList) {
            DataItem e = new DataItem();
            e.setName(tbSingerPojo.getSingerName());
            e.setId(tbSingerPojo.getId());
            String singerAlias = Optional.ofNullable(tbSingerPojo.getAlias()).orElse("");
            e.setAlias(Arrays.asList(singerAlias.split(",")));
            e.setPicUrl(tbSingerPojo.getPic());
            e.setAlbumSize(qukuService.getAlbumCountBySingerId(e.getId()));
            e.setMvSize(0);
            data.add(e);
        }
        res.setData(data);
        return res;
    }
}
