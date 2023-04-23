package org.api.subsonic.service;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.api.subsonic.common.SubsonicCommonReq;
import org.api.subsonic.config.SubsonicConfig;
import org.api.subsonic.model.req.album.albumlist2.AlbumReq;
import org.api.subsonic.model.res.album.albumlist2.Album;
import org.api.subsonic.model.res.album.albumlist2.AlbumList2;
import org.api.subsonic.model.res.album.albumlist2.AlbumRes;
import org.core.iservice.TbAlbumService;
import org.core.pojo.TbAlbumPojo;
import org.core.pojo.TbArtistPojo;
import org.core.service.QukuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service(SubsonicConfig.SUBSONIC + "SongListsApi")
public class SongListsApi {
    
    @Autowired
    private QukuService qukuService;
    
    @Autowired
    private TbAlbumService albumService;
    
    public AlbumRes getAlbumList2(SubsonicCommonReq req, AlbumReq albumReq) {
        Page<TbAlbumPojo> page = new Page<>(albumReq.getOffset(),albumReq.getSize());
        albumService.page(page);
        ArrayList<Album> albumArrayList = new ArrayList<>();
        for (TbAlbumPojo albumPojo : page.getRecords()) {
            Album e = new Album();
            e.setId(String.valueOf(albumPojo.getId()));
            e.setAlbum(albumPojo.getAlbumName());
            e.setTitle(albumPojo.getAlbumName());
            e.setName(albumPojo.getAlbumName());
            e.setYear(albumPojo.getPublishTime().getYear());
            e.setSongCount(qukuService.getAlbumMusicCountByAlbumId(albumPojo.getId()));
            List<TbArtistPojo> artistListByAlbumIds = qukuService.getArtistListByAlbumIds(albumPojo.getId());
            TbArtistPojo pojo = CollUtil.isNotEmpty(artistListByAlbumIds) ? artistListByAlbumIds.get(0) : new TbArtistPojo();
            e.setArtist(pojo.getArtistName());
            e.setArtistId(String.valueOf(pojo.getId()));
            e.setCoverArt(String.valueOf(albumPojo.getId()));
            albumArrayList.add(e);
        }
        
        AlbumRes albumRes = new AlbumRes();
        AlbumList2 albumList2 = new AlbumList2();
        albumList2.setAlbum(albumArrayList);
        albumRes.setAlbumList2(albumList2);
        return albumRes;
    }
}
