package org.api.neteasecloudmusic.service;

import cn.hutool.core.collection.CollUtil;
import lombok.extern.slf4j.Slf4j;
import org.api.neteasecloudmusic.config.NeteaseCloudConfig;
import org.api.neteasecloudmusic.model.vo.album.album.*;
import org.api.neteasecloudmusic.model.vo.album.detail.Album;
import org.api.neteasecloudmusic.model.vo.album.detail.AlbumDetailRes;
import org.api.neteasecloudmusic.model.vo.album.detail.DescrItem;
import org.api.neteasecloudmusic.model.vo.album.detail.Product;
import org.api.neteasecloudmusic.model.vo.album.sublist.AlbumSubListRes;
import org.api.neteasecloudmusic.model.vo.album.sublist.ArtistsItem;
import org.api.neteasecloudmusic.model.vo.album.sublist.DataItem;
import org.core.pojo.SysUserPojo;
import org.core.pojo.TbAlbumPojo;
import org.core.pojo.TbMusicPojo;
import org.core.pojo.TbSingerPojo;
import org.core.service.QukuService;
import org.core.utils.AliasUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service(NeteaseCloudConfig.NETEASECLOUD + "AlbumApi")
public class AlbumApi {
    
    @Autowired
    private QukuService qukuService;
    
    /**
     * 返回专辑数据和歌手数据(没有歌手数据)
     *
     * @param user   用户数据
     * @param limit  每页数据
     * @param offset 当前多少页
     */
    public AlbumSubListRes albumSubList(SysUserPojo user, Long limit, Long offset) {
        AlbumSubListRes res = new AlbumSubListRes();
        List<TbAlbumPojo> userCollectAlbum = qukuService.getUserCollectAlbum(user, limit, offset);
        if (CollUtil.isEmpty(userCollectAlbum)) {
            return res;
        }
        ArrayList<DataItem> data = new ArrayList<>();
        for (TbAlbumPojo tbAlbumPojo : userCollectAlbum) {
            DataItem e = new DataItem();
            e.setId(tbAlbumPojo.getId());
            e.setName(tbAlbumPojo.getAlbumName());
            Integer albumSize = qukuService.getAlbumMusicCountByAlbumId(tbAlbumPojo.getId());
            e.setSize(albumSize);
            e.setPicUrl(tbAlbumPojo.getPic());
            ArrayList<ArtistsItem> artists = new ArrayList<>();
            List<TbSingerPojo> singerListByAlbumIds = qukuService.getSingerListByAlbumIds(tbAlbumPojo.getId());
            for (TbSingerPojo singerListByAlbumId : singerListByAlbumIds) {
                ArtistsItem e1 = new ArtistsItem();
                // 艺术家下专辑数量专辑
                e1.setAlbumSize(0);
                e1.setName(singerListByAlbumId.getSingerName());
                e1.setId(singerListByAlbumId.getId());
                String alias = Optional.ofNullable(singerListByAlbumId.getAlias()).orElse("");
                e1.setAlias(Arrays.asList(alias.split(",")));
                e1.setPicUrl(singerListByAlbumId.getPic());
                artists.add(e1);
            }
            e.setArtists(artists);
            data.add(e);
        }
        res.setData(data);
        res.setCount(data.size());
        return res;
    }
    
    public AlbumDetailRes albumDetail(Long id) {
        AlbumDetailRes res = new AlbumDetailRes();
        TbAlbumPojo albumByAlbumId = qukuService.getAlbumByAlbumId(id);
        Album album = new Album();
        album.setAlbumName(albumByAlbumId.getAlbumName());
        album.setAlbumId(albumByAlbumId.getId());
        album.setCoverUrl(albumByAlbumId.getPic());
        album.setBlurImgUrl(albumByAlbumId.getPic());
        
        List<TbSingerPojo> singerListByAlbumIds = qukuService.getSingerListByAlbumIds(id);
        TbSingerPojo tbSingerPojo = CollUtil.isEmpty(singerListByAlbumIds) ? new TbSingerPojo() : singerListByAlbumIds.get(0);
        album.setArtistId(tbSingerPojo.getId());
        album.setArtistName(tbSingerPojo.getSingerName());
        album.setArtistNames(tbSingerPojo.getSingerName());
        album.setArtistAvatarUrl(tbSingerPojo.getPic());
        
        Product product = new Product();
        String description = albumByAlbumId.getDescription();
        ArrayList<DescrItem> descr = new ArrayList<>();
        DescrItem e = new DescrItem();
        e.setResource(description);
        descr.add(e);
        product.setDescr(descr);
        res.setProduct(product);
        res.setAlbum(album);
        return res;
    }
    
    public AlbumRes album(Long id) {
        
        List<TbMusicPojo> tbMusicPojo = qukuService.getMusicListByAlbumId(id);
        
        ArrayList<SongsItem> songs = new ArrayList<>();
        TbAlbumPojo tbAlbumPojo = qukuService.getAlbumByAlbumId(id);
        for (TbMusicPojo musicPojo : tbMusicPojo) {
            SongsItem e = new SongsItem();
            e.setId(musicPojo.getId());
            e.setName(musicPojo.getMusicName());
            Al al = new Al();
            al.setId(tbAlbumPojo.getId());
            al.setPicUrl(tbAlbumPojo.getPic());
            al.setName(tbAlbumPojo.getAlbumName());
            e.setAl(al);
    
            ArrayList<ArItem> ar = new ArrayList<>();
            List<TbSingerPojo> singerByMusicId = qukuService.getSingerByMusicId(musicPojo.getId());
            for (TbSingerPojo tbSingerPojo : singerByMusicId) {
                ArItem e1 = new ArItem();
                e1.setAlia(AliasUtil.getAliasList(tbSingerPojo.getAlias()));
                e1.setName(tbSingerPojo.getSingerName());
                e1.setId(tbSingerPojo.getId());
                ar.add(e1);
            }
            e.setAr(ar);
            
            songs.add(e);
        }
        AlbumRes res = new AlbumRes();
        res.setSongs(songs);
        
        Integer albumMusicCountByAlbumId = qukuService.getAlbumMusicCountByAlbumId(id);
    
        org.api.neteasecloudmusic.model.vo.album.album.Album album = new org.api.neteasecloudmusic.model.vo.album.album.Album();
        album.setName(tbAlbumPojo.getAlbumName());
        album.setId(tbAlbumPojo.getId());
        album.setDescription(tbAlbumPojo.getDescription());
        album.setCompany(tbAlbumPojo.getCompany());
        album.setSubType(tbAlbumPojo.getSubType());
        album.setPublishTime(tbAlbumPojo.getPublishTime().getNano());
        album.setSize(albumMusicCountByAlbumId);
    
    
        List<TbSingerPojo> singerListByAlbumIds = qukuService.getSingerListByAlbumIds(id);
        ArrayList<org.api.neteasecloudmusic.model.vo.album.album.ArtistsItem> artists = new ArrayList<>();
        for (TbSingerPojo singerListByAlbumId : singerListByAlbumIds) {
            org.api.neteasecloudmusic.model.vo.album.album.ArtistsItem e = new org.api.neteasecloudmusic.model.vo.album.album.ArtistsItem();
            e.setName(singerListByAlbumId.getSingerName());
            e.setId(singerListByAlbumId.getId());
            e.setAlbumSize(albumMusicCountByAlbumId);
            e.setPicUrl(singerListByAlbumId.getPic());
            
            artists.add(e);
        }
        album.setArtists(artists);
    
        TbSingerPojo tbSingerPojo = CollUtil.isEmpty(singerListByAlbumIds) ? new TbSingerPojo() : singerListByAlbumIds.get(0);
        Artist artist = new Artist();
        artist.setId(tbSingerPojo.getId());
        artist.setName(tbAlbumPojo.getAlbumName());
        artist.setAlbumSize(qukuService.getAlbumCountBySingerId(tbAlbumPojo.getId()));
        artist.setPicUrl(tbSingerPojo.getPic());
        artist.setImg1v1Url(tbSingerPojo.getPic());
        album.setArtist(artist);
        
        res.setAlbum(album);
        return res;
    }
}
