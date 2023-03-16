package org.api.neteasecloudmusic.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.api.neteasecloudmusic.model.vo.artist.album.Artist;
import org.api.neteasecloudmusic.model.vo.artist.album.ArtistAlbumRes;
import org.api.neteasecloudmusic.model.vo.artist.album.ArtistsItem;
import org.api.neteasecloudmusic.model.vo.artist.album.HotAlbumsItem;
import org.api.neteasecloudmusic.model.vo.artist.sublist.ArtistSubListRes;
import org.api.neteasecloudmusic.model.vo.artist.sublist.DataItem;
import org.core.pojo.SysUserPojo;
import org.core.pojo.TbAlbumPojo;
import org.core.pojo.TbAlbumSingerPojo;
import org.core.pojo.TbSingerPojo;
import org.core.service.QukuService;
import org.core.service.TbAlbumService;
import org.core.service.TbAlbumSingerService;
import org.core.service.TbSingerService;
import org.core.utils.AliasUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service("NeteaseCloudArtistApi")
public class ArtistApi {
    @Autowired
    private QukuService qukuService;
    
    @Autowired
    private TbAlbumSingerService albumSingerService;
    
    @Autowired
    private TbAlbumService albumService;
    
    @Autowired
    private TbSingerService singerService;
    
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
    
    public ArtistAlbumRes artistAlbum(Long id, Long limit, Long offset) {
        Page<TbAlbumSingerPojo> page = new Page<>(offset, limit);
        albumSingerService.page(page, Wrappers.<TbAlbumSingerPojo>lambdaQuery().eq(TbAlbumSingerPojo::getSingerId, id));
        List<Long> albumIds = page.getRecords().stream().map(TbAlbumSingerPojo::getAlbumId).collect(Collectors.toList());
        List<TbAlbumPojo> albumPojoList = albumService.listByIds(albumIds);
    
        Artist artist = new Artist();
        TbSingerPojo singerPojo = singerService.getById(id);
        artist.setImg1v1Url(singerPojo.getPic());
        artist.setId(singerPojo.getId());
        artist.setName(singerPojo.getSingerName());
        artist.setAlias(AliasUtil.getAliasList(singerPojo.getSingerName()));
        artist.setPicIdStr(singerPojo.getPic());
        artist.setMusicSize(qukuService.getMusicCountBySingerId(singerPojo.getId()));
        artist.setAlbumSize(qukuService.getAlbumCountBySingerId(singerPojo.getId()));
    
        ArtistsItem e1 = new ArtistsItem();
        e1.setPicUrl(singerPojo.getPic());
        e1.setId(singerPojo.getId());
        e1.setName(singerPojo.getSingerName());
        e1.setImg1v1Url(singerPojo.getPic());
        
        ArrayList<ArtistsItem> artists = new ArrayList<>();
        artists.add(e1);
        
    
        ArrayList<HotAlbumsItem> hotAlbums = new ArrayList<>();
        for (TbAlbumPojo tbAlbumPojo : albumPojoList) {
            HotAlbumsItem e = new HotAlbumsItem();
            e.setId(tbAlbumPojo.getId());
            e.setName(tbAlbumPojo.getAlbumName());
            e.setAlias(AliasUtil.getAliasList(tbAlbumPojo.getAliasName()));
            e.setCompany(tbAlbumPojo.getCompany());
            e.setPicUrl(tbAlbumPojo.getPic());
            e.setBlurPicUrl(tbAlbumPojo.getPic());
            e.setArtist(artist);
    
            e.setArtists(artists);
            hotAlbums.add(e);
        }
        ArtistAlbumRes res = new ArtistAlbumRes();
        res.setArtist(artist);
        res.setHotAlbums(hotAlbums);
        return res;
    }
}
