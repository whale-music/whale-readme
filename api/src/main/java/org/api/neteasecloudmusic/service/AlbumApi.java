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
import org.core.pojo.AlbumPojo;
import org.core.pojo.ArtistPojo;
import org.core.pojo.MusicPojo;
import org.core.pojo.SysUserPojo;
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
        List<AlbumPojo> userCollectAlbum = qukuService.getUserCollectAlbum(user, limit, offset);
        if (CollUtil.isEmpty(userCollectAlbum)) {
            return res;
        }
        ArrayList<DataItem> data = new ArrayList<>();
        for (AlbumPojo albumPojo : userCollectAlbum) {
            DataItem e = new DataItem();
            e.setId(albumPojo.getId());
            e.setName(albumPojo.getAlbumName());
            Integer albumSize = qukuService.getAlbumMusicCountByAlbumId(albumPojo.getId());
            e.setSize(albumSize);
            e.setPicUrl(albumPojo.getPic());
            ArrayList<ArtistsItem> artists = new ArrayList<>();
            List<ArtistPojo> singerListByAlbumIds = qukuService.getArtistListByAlbumIds(albumPojo.getId());
            for (ArtistPojo singerListByAlbumId : singerListByAlbumIds) {
                ArtistsItem e1 = new ArtistsItem();
                // 艺术家下专辑数量专辑
                e1.setAlbumSize(0);
                e1.setName(singerListByAlbumId.getArtistName());
                e1.setId(singerListByAlbumId.getId());
                String alias = Optional.ofNullable(singerListByAlbumId.getAliasName()).orElse("");
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
        AlbumPojo albumByAlbumId = qukuService.getAlbumByAlbumId(id);
        Album album = new Album();
        album.setAlbumName(albumByAlbumId.getAlbumName());
        album.setAlbumId(albumByAlbumId.getId());
        album.setCoverUrl(albumByAlbumId.getPic());
        album.setBlurImgUrl(albumByAlbumId.getPic());
        
        List<ArtistPojo> singerListByAlbumIds = qukuService.getArtistListByAlbumIds(id);
        ArtistPojo artistPojo = CollUtil.isEmpty(singerListByAlbumIds) ? new ArtistPojo() : singerListByAlbumIds.get(0);
        album.setArtistId(artistPojo.getId());
        album.setArtistName(artistPojo.getArtistName());
        album.setArtistNames(artistPojo.getArtistName());
        album.setArtistAvatarUrl(artistPojo.getPic());
        
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
        
        List<MusicPojo> tbMusicPojo = qukuService.getMusicListByAlbumId(id);
        
        ArrayList<SongsItem> songs = new ArrayList<>();
        AlbumPojo albumPojo = qukuService.getAlbumByAlbumId(id);
        for (MusicPojo musicPojo : tbMusicPojo) {
            SongsItem e = new SongsItem();
            e.setId(musicPojo.getId());
            e.setName(musicPojo.getMusicName());
            Al al = new Al();
            al.setId(albumPojo.getId());
            al.setPicUrl(albumPojo.getPic());
            al.setName(albumPojo.getAlbumName());
            e.setAl(al);
            
            ArrayList<ArItem> ar = new ArrayList<>();
            List<ArtistPojo> singerByMusicId = qukuService.getSingerByMusicId(musicPojo.getId());
            for (ArtistPojo artistPojo : singerByMusicId) {
                ArItem e1 = new ArItem();
                e1.setAlia(AliasUtil.getAliasList(artistPojo.getAliasName()));
                e1.setName(artistPojo.getArtistName());
                e1.setId(artistPojo.getId());
                ar.add(e1);
            }
            e.setAr(ar);
            
            songs.add(e);
        }
        AlbumRes res = new AlbumRes();
        res.setSongs(songs);
        
        Integer albumMusicCountByAlbumId = qukuService.getAlbumMusicCountByAlbumId(id);
        
        org.api.neteasecloudmusic.model.vo.album.album.Album album = new org.api.neteasecloudmusic.model.vo.album.album.Album();
        album.setName(albumPojo.getAlbumName());
        album.setId(albumPojo.getId());
        album.setDescription(albumPojo.getDescription());
        album.setCompany(albumPojo.getCompany());
        album.setSubType(albumPojo.getSubType());
        album.setPicUrl(albumPojo.getPic());
        album.setPublishTime(albumPojo.getPublishTime().getNano());
        album.setSize(albumMusicCountByAlbumId);
        
        
        List<ArtistPojo> singerListByAlbumIds = qukuService.getArtistListByAlbumIds(id);
        ArrayList<org.api.neteasecloudmusic.model.vo.album.album.ArtistsItem> artists = new ArrayList<>();
        for (ArtistPojo singerListByAlbumId : singerListByAlbumIds) {
            org.api.neteasecloudmusic.model.vo.album.album.ArtistsItem e = new org.api.neteasecloudmusic.model.vo.album.album.ArtistsItem();
            e.setName(singerListByAlbumId.getArtistName());
            e.setId(singerListByAlbumId.getId());
            e.setAlbumSize(albumMusicCountByAlbumId);
            e.setPicUrl(singerListByAlbumId.getPic());
            
            artists.add(e);
        }
        album.setArtists(artists);
        
        ArtistPojo artistPojo = CollUtil.isEmpty(singerListByAlbumIds) ? new ArtistPojo() : singerListByAlbumIds.get(0);
        Artist artist = new Artist();
        artist.setId(artistPojo.getId());
        artist.setName(albumPojo.getAlbumName());
        artist.setAlbumSize(qukuService.getAlbumCountBySingerId(albumPojo.getId()));
        artist.setPicUrl(artistPojo.getPic());
        artist.setImg1v1Url(artistPojo.getPic());
        album.setArtist(artist);
        
        res.setAlbum(album);
        return res;
    }
}
