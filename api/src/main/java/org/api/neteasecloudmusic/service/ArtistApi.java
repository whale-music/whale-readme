package org.api.neteasecloudmusic.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.api.common.service.QukuAPI;
import org.api.neteasecloudmusic.config.NeteaseCloudConfig;
import org.api.neteasecloudmusic.model.vo.artist.album.Artist;
import org.api.neteasecloudmusic.model.vo.artist.album.ArtistAlbumRes;
import org.api.neteasecloudmusic.model.vo.artist.album.ArtistsItem;
import org.api.neteasecloudmusic.model.vo.artist.album.HotAlbumsItem;
import org.api.neteasecloudmusic.model.vo.artist.artist.Al;
import org.api.neteasecloudmusic.model.vo.artist.artist.ArItem;
import org.api.neteasecloudmusic.model.vo.artist.artist.ArtistRes;
import org.api.neteasecloudmusic.model.vo.artist.artist.HotSongsItem;
import org.api.neteasecloudmusic.model.vo.artist.sublist.ArtistSubListRes;
import org.api.neteasecloudmusic.model.vo.artist.sublist.DataItem;
import org.core.mybatis.iservice.TbAlbumArtistService;
import org.core.mybatis.iservice.TbAlbumService;
import org.core.mybatis.iservice.TbArtistService;
import org.core.mybatis.model.convert.AlbumConvert;
import org.core.mybatis.model.convert.ArtistConvert;
import org.core.mybatis.model.convert.MusicConvert;
import org.core.mybatis.pojo.SysUserPojo;
import org.core.mybatis.pojo.TbAlbumArtistPojo;
import org.core.mybatis.pojo.TbAlbumPojo;
import org.core.mybatis.pojo.TbArtistPojo;
import org.core.utils.AliasUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service(NeteaseCloudConfig.NETEASECLOUD + "ArtistApi")
public class ArtistApi {
    @Autowired
    private QukuAPI qukuService;
    
    @Autowired
    private TbAlbumArtistService albumSingerService;
    
    @Autowired
    private TbAlbumService albumService;
    
    @Autowired
    private TbArtistService singerService;
    
    public ArtistSubListRes artistSublist(SysUserPojo user) {
        user = Optional.ofNullable(user).orElse(new SysUserPojo());
        ArtistSubListRes res = new ArtistSubListRes();
        List<ArtistConvert> userPojoList = qukuService.getUserLikeSingerList(user.getId());
        ArrayList<DataItem> data = new ArrayList<>();
        for (ArtistConvert tbArtistPojo : userPojoList) {
            DataItem e = new DataItem();
            e.setName(tbArtistPojo.getArtistName());
            e.setId(tbArtistPojo.getId());
            String singerAlias = Optional.ofNullable(tbArtistPojo.getAliasName()).orElse("");
            e.setAlias(Arrays.asList(singerAlias.split(",")));
            e.setPicUrl(tbArtistPojo.getPicUrl());
            e.setAlbumSize(qukuService.getArtistAlbumCountBySingerId(e.getId()));
            e.setMvSize(0);
            data.add(e);
        }
        res.setData(data);
        return res;
    }
    
    public ArtistAlbumRes artistAlbum(Long id, Long limit, Long offset) {
        Page<TbAlbumArtistPojo> page = new Page<>(offset, limit);
        albumSingerService.page(page, Wrappers.<TbAlbumArtistPojo>lambdaQuery().eq(TbAlbumArtistPojo::getArtistId, id));
        List<Long> albumIds = page.getRecords().stream().map(TbAlbumArtistPojo::getAlbumId).toList();
        List<TbAlbumPojo> albumPojoList = albumService.listByIds(albumIds);
    
        Artist artist = new Artist();
        TbArtistPojo singerPojo = singerService.getById(id);
        String picUrl = qukuService.getArtistPicUrl(singerPojo.getId());
        artist.setImg1v1Url(picUrl);
        artist.setId(singerPojo.getId());
        artist.setName(singerPojo.getArtistName());
        artist.setAlias(AliasUtil.getAliasList(singerPojo.getArtistName()));
        artist.setPicIdStr(picUrl);
        artist.setPicUrl(picUrl);
        artist.setMusicSize(qukuService.getMusicCountBySingerId(singerPojo.getId()));
        artist.setAlbumSize(qukuService.getArtistAlbumCountBySingerId(singerPojo.getId()));
    
        ArtistsItem e1 = new ArtistsItem();
        e1.setPicUrl(picUrl);
        e1.setId(singerPojo.getId());
        e1.setName(singerPojo.getArtistName());
        e1.setImg1v1Url(picUrl);
    
        ArrayList<ArtistsItem> artists = new ArrayList<>();
        artists.add(e1);
    
    
        ArrayList<HotAlbumsItem> hotAlbums = new ArrayList<>();
        for (TbAlbumPojo tbAlbumPojo : albumPojoList) {
            HotAlbumsItem e = new HotAlbumsItem();
            e.setId(tbAlbumPojo.getId());
            e.setName(tbAlbumPojo.getAlbumName());
            e.setCompany(tbAlbumPojo.getCompany());
            String url = qukuService.getAlbumPicUrl(tbAlbumPojo.getId());
            e.setPicUrl(url);
            e.setBlurPicUrl(url);
            e.setArtist(artist);
        
            e.setArtists(artists);
            hotAlbums.add(e);
        }
        ArtistAlbumRes res = new ArtistAlbumRes();
        res.setArtist(artist);
        res.setHotAlbums(hotAlbums);
        return res;
    }
    
    /**
     * 获取歌手(信息)单曲
     *
     * @param id 歌手ID
     */
    public ArtistRes artists(Long id) {
        ArtistRes artistRes = new ArtistRes();
        TbArtistPojo singerPojo = singerService.getById(id);
        List<MusicConvert> musicPojoList = qukuService.getMusicListByArtistId(id);
        org.api.neteasecloudmusic.model.vo.artist.artist.Artist artist = new org.api.neteasecloudmusic.model.vo.artist.artist.Artist();
        artist.setName(singerPojo.getArtistName());
        artist.setId(singerPojo.getId());
        String picUrl = qukuService.getArtistPicUrl(singerPojo.getId());
        artist.setPicUrl(picUrl);
        artist.setAlias(AliasUtil.getAliasList(singerPojo.getAliasName()));
        artist.setImg1v1IdStr(picUrl);
        artist.setMusicSize(qukuService.getMusicCountBySingerId(singerPojo.getId()));
        artist.setBriefDesc(singerPojo.getIntroduction());
        artist.setImg1v1Url(picUrl);
        artist.setAlbumSize(qukuService.getArtistAlbumCountBySingerId(id));
        artistRes.setArtist(artist);
    
        ArrayList<HotSongsItem> hotSongs = new ArrayList<>();
        for (MusicConvert tbMusicPojo : musicPojoList) {
            HotSongsItem hotSongsItem = new HotSongsItem();
            hotSongsItem.setName(tbMusicPojo.getMusicName());
            hotSongsItem.setAlia(AliasUtil.getAliasList(tbMusicPojo.getAliasName()));
            hotSongsItem.setId(tbMusicPojo.getId());
            
            AlbumConvert albumByMusicId = qukuService.getAlbumByMusicId(tbMusicPojo.getId());
            Al al = new Al();
            al.setId(albumByMusicId.getId());
            al.setName(albumByMusicId.getAlbumName());
            al.setPicUrl(albumByMusicId.getPicUrl());
            hotSongsItem.setAl(al);
            hotSongs.add(hotSongsItem);
            
            ArrayList<ArItem> ar = new ArrayList<>();
            List<ArtistConvert> singerByMusicId = qukuService.getAlbumArtistByMusicId(tbMusicPojo.getId());
            for (ArtistConvert tbArtistPojo : singerByMusicId) {
                ArItem arItem = new ArItem();
                arItem.setId(tbArtistPojo.getId());
                arItem.setAlia(AliasUtil.getAliasList(tbArtistPojo.getAliasName()));
                arItem.setName(tbArtistPojo.getArtistName());
                ar.add(arItem);
            }
            hotSongsItem.setAr(ar);
        }
        artistRes.setHotSongs(hotSongs);
        
        
        artistRes.setMore(true);
        return artistRes;
    }
}
