package org.api.neteasecloudmusic.service;

import cn.hutool.core.collection.CollUtil;
import lombok.extern.slf4j.Slf4j;
import org.api.common.service.QukuAPI;
import org.api.neteasecloudmusic.config.NeteaseCloudConfig;
import org.api.neteasecloudmusic.model.vo.album.album.*;
import org.api.neteasecloudmusic.model.vo.album.detail.Album;
import org.api.neteasecloudmusic.model.vo.album.detail.AlbumDetailRes;
import org.api.neteasecloudmusic.model.vo.album.detail.DescrItem;
import org.api.neteasecloudmusic.model.vo.album.detail.Product;
import org.api.neteasecloudmusic.model.vo.album.sublist.AlbumSubListRes;
import org.api.neteasecloudmusic.model.vo.album.sublist.ArtistsItem;
import org.api.neteasecloudmusic.model.vo.album.sublist.DataItem;
import org.core.mybatis.model.convert.AlbumConvert;
import org.core.mybatis.model.convert.ArtistConvert;
import org.core.mybatis.model.convert.MusicConvert;
import org.core.mybatis.pojo.SysUserPojo;
import org.core.utils.AliasUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service(NeteaseCloudConfig.NETEASECLOUD + "AlbumApi")
public class AlbumApi {
    
    private final QukuAPI qukuService;
    
    public AlbumApi(QukuAPI qukuService) {
        this.qukuService = qukuService;
    }
    
    /**
     * 返回专辑数据和歌手数据(没有歌手数据)
     *
     * @param user   用户数据
     * @param limit  每页数据
     * @param offset 当前多少页
     */
    public AlbumSubListRes albumSubList(SysUserPojo user, Long limit, Long offset) {
        AlbumSubListRes res = new AlbumSubListRes();
        List<AlbumConvert> userCollectAlbum = qukuService.getUserCollectAlbum(user, limit, offset);
        if (CollUtil.isEmpty(userCollectAlbum)) {
            return res;
        }
        ArrayList<DataItem> data = new ArrayList<>();
        for (AlbumConvert tbAlbumPojo : userCollectAlbum) {
            DataItem e = new DataItem();
            e.setId(tbAlbumPojo.getId());
            e.setName(tbAlbumPojo.getAlbumName());
            Integer albumSize = qukuService.getAlbumMusicCountByAlbumId(tbAlbumPojo.getId());
            e.setSize(albumSize);
        
            e.setPicUrl(tbAlbumPojo.getPicUrl());
            ArrayList<ArtistsItem> artists = new ArrayList<>();
            List<ArtistConvert> singerListByAlbumIds = qukuService.getArtistByAlbumIds(tbAlbumPojo.getId());
            for (ArtistConvert singerListByAlbumId : singerListByAlbumIds) {
                ArtistsItem e1 = new ArtistsItem();
                // 艺术家下专辑数量专辑
                e1.setAlbumSize(0);
                e1.setName(singerListByAlbumId.getArtistName());
                e1.setId(singerListByAlbumId.getId());
                String alias = Optional.ofNullable(singerListByAlbumId.getAliasName()).orElse("");
                e1.setAlias(Arrays.asList(alias.split(",")));
                e1.setPicUrl(singerListByAlbumId.getPicUrl());
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
        AlbumConvert albumByAlbumId = qukuService.getAlbumByAlbumId(id);
        Album album = new Album();
        album.setAlbumName(albumByAlbumId.getAlbumName());
        album.setAlbumId(albumByAlbumId.getId());
        album.setCoverUrl(albumByAlbumId.getPicUrl());
        album.setBlurImgUrl(albumByAlbumId.getPicUrl());
        
        List<ArtistConvert> singerListByAlbumIds = qukuService.getArtistByAlbumIds(id);
        ArtistConvert tbArtistPojo = CollUtil.isEmpty(singerListByAlbumIds) ? new ArtistConvert() : singerListByAlbumIds.get(0);
        album.setArtistId(tbArtistPojo.getId());
        album.setArtistName(tbArtistPojo.getArtistName());
        album.setArtistNames(tbArtistPojo.getArtistName());
        album.setArtistAvatarUrl(tbArtistPojo.getPicUrl());
    
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
        List<MusicConvert> tbMusicPojo = qukuService.getMusicListByAlbumId(id);
    
        ArrayList<SongsItem> songs = new ArrayList<>();
        AlbumConvert tbAlbumPojo = qukuService.getAlbumByAlbumId(id);
        for (MusicConvert musicPojo : tbMusicPojo) {
            SongsItem e = new SongsItem();
            e.setId(musicPojo.getId());
            e.setName(musicPojo.getMusicName());
            Al al = new Al();
            al.setId(tbAlbumPojo.getId());
            al.setPicUrl(musicPojo.getPicUrl());
            al.setName(tbAlbumPojo.getAlbumName());
            e.setAl(al);
        
            ArrayList<ArItem> ar = new ArrayList<>();
            List<ArtistConvert> singerByMusicId = qukuService.getArtistByMusicIds(musicPojo.getId());
            for (ArtistConvert tbArtistPojo : singerByMusicId) {
                ArItem e1 = new ArItem();
                e1.setAlia(AliasUtil.getAliasList(tbArtistPojo.getAliasName()));
                e1.setName(tbArtistPojo.getArtistName());
                e1.setId(tbArtistPojo.getId());
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
        album.setPicUrl(tbAlbumPojo.getPicUrl());
        album.setPublishTime(tbAlbumPojo.getPublishTime().getNano());
        album.setSize(albumMusicCountByAlbumId);
        
        
        List<ArtistConvert> singerListByAlbumIds = qukuService.getArtistByAlbumIds(id);
        ArrayList<org.api.neteasecloudmusic.model.vo.album.album.ArtistsItem> artists = new ArrayList<>();
        for (ArtistConvert singerListByAlbumId : singerListByAlbumIds) {
            org.api.neteasecloudmusic.model.vo.album.album.ArtistsItem e = new org.api.neteasecloudmusic.model.vo.album.album.ArtistsItem();
            e.setName(singerListByAlbumId.getArtistName());
            e.setId(singerListByAlbumId.getId());
            e.setAlbumSize(albumMusicCountByAlbumId);
            e.setPicUrl(singerListByAlbumId.getPicUrl());
        
            artists.add(e);
        }
        album.setArtists(artists);
    
        ArtistConvert tbArtistPojo = CollUtil.isEmpty(singerListByAlbumIds) ? new ArtistConvert() : singerListByAlbumIds.get(0);
        Artist artist = new Artist();
        artist.setId(tbArtistPojo.getId());
        artist.setName(tbAlbumPojo.getAlbumName());
        artist.setAlbumSize(qukuService.getArtistAlbumCountByArtistId(tbAlbumPojo.getId()));
        artist.setPicUrl(tbArtistPojo.getPicUrl());
        artist.setImg1v1Url(tbArtistPojo.getPicUrl());
        album.setArtist(artist);
    
        res.setAlbum(album);
        return res;
    }
}
