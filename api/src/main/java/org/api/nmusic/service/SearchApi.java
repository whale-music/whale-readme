package org.api.nmusic.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.api.nmusic.config.NeteaseCloudConfig;
import org.api.nmusic.model.vo.cloudsearch.CloudSearchRes;
import org.api.nmusic.model.vo.search.SearchRes;
import org.core.mybatis.iservice.TbMusicService;
import org.core.mybatis.model.convert.AlbumConvert;
import org.core.mybatis.model.convert.ArtistConvert;
import org.core.mybatis.pojo.TbMusicPojo;
import org.core.service.QukuService;
import org.core.service.RemoteStorePicService;
import org.core.utils.AliasUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service(NeteaseCloudConfig.NETEASECLOUD + "SearchApi")
public class SearchApi {
    
    private final TbMusicService tbMusicService;
    
    private final QukuService qukuService;
    
    private final RemoteStorePicService remoteStorePicService;
    
    public SearchApi(TbMusicService tbMusicService, QukuService qukuService, RemoteStorePicService remoteStorePicService) {
        this.tbMusicService = tbMusicService;
        this.qukuService = qukuService;
        this.remoteStorePicService = remoteStorePicService;
    }
    
    @NotNull
    private static CloudSearchRes.Result.SongsItem.Privilege fillPrivilege() {
        CloudSearchRes.Result.SongsItem.Privilege privilege = new CloudSearchRes.Result.SongsItem.Privilege();
        privilege.setId(1357375695);
        privilege.setFee(0);
        privilege.setPayed(0);
        privilege.setSt(0);
        privilege.setPl(0);
        privilege.setDl(0);
        privilege.setSp(7);
        privilege.setCp(1);
        privilege.setSubp(1);
        privilege.setCs(false);
        privilege.setMaxbr(999000);
        privilege.setFl(0);
        privilege.setToast(false);
        privilege.setFlag(1028);
        privilege.setPreSell(false);
        privilege.setPlayMaxbr(999000);
        privilege.setDownloadMaxbr(999000);
        privilege.setMaxBrLevel("lossless");
        privilege.setPlayMaxBrLevel("lossless");
        privilege.setDownloadMaxBrLevel("lossless");
        privilege.setPlLevel("none");
        privilege.setDlLevel("none");
        privilege.setFlLevel("none");
        privilege.setRscl(null);
        privilege.setFreeTrialPrivilege(new CloudSearchRes.Result.SongsItem.Privilege.FreeTrialPrivilege(true, false, 0, 0));
        privilege.setRightSource(0);
        privilege.setChargeInfoList(null);
        return privilege;
    }
    
    @NotNull
    private static SearchRes.Result.SongsItem.Privilege fillSearchPrivilege() {
        SearchRes.Result.SongsItem.Privilege privilege = new SearchRes.Result.SongsItem.Privilege();
        privilege.setId(1357375695);
        privilege.setFee(0);
        privilege.setPayed(0);
        privilege.setSt(0);
        privilege.setPl(0);
        privilege.setDl(0);
        privilege.setSp(7);
        privilege.setCp(1);
        privilege.setSubp(1);
        privilege.setCs(false);
        privilege.setMaxbr(999000);
        privilege.setFl(0);
        privilege.setToast(false);
        privilege.setFlag(1028);
        privilege.setPreSell(false);
        privilege.setPlayMaxbr(999000);
        privilege.setDownloadMaxbr(999000);
        privilege.setMaxBrLevel("lossless");
        privilege.setPlayMaxBrLevel("lossless");
        privilege.setDownloadMaxBrLevel("lossless");
        privilege.setPlLevel("none");
        privilege.setDlLevel("none");
        privilege.setFlLevel("none");
        privilege.setRscl(null);
        privilege.setFreeTrialPrivilege(new SearchRes.Result.SongsItem.Privilege.FreeTrialPrivilege(true, false, 0, 0));
        privilege.setRightSource(0);
        privilege.setChargeInfoList(null);
        return privilege;
    }
    
    private static long getPageCurrent(Long limit, Long offset) {
        return offset / limit + 1;
    }
    
    public CloudSearchRes cloudSearch(String keywords, Long limit, Long offset, Integer type) {
        long current = getPageCurrent(limit, offset);
        LambdaQueryWrapper<TbMusicPojo> wrapper = Wrappers.<TbMusicPojo>lambdaQuery()
                                                          .like(TbMusicPojo::getMusicName, keywords)
                                                          .or()
                                                          .like(TbMusicPojo::getAliasName, keywords);
        Page<TbMusicPojo> page = tbMusicService.page(Page.of(current, limit), wrapper);
        CloudSearchRes res = new CloudSearchRes();
        CloudSearchRes.Result result = new CloudSearchRes.Result();
        ArrayList<CloudSearchRes.Result.SongsItem> songs = new ArrayList<>();
        
        List<Long> musicIds = page.getRecords().parallelStream().map(TbMusicPojo::getId).toList();
        List<Long> albumMaps = page.getRecords().parallelStream().map(TbMusicPojo::getAlbumId).toList();
        Map<Long, List<ArtistConvert>> artistByMusicIdToMap = qukuService.getArtistByMusicIdToMap(musicIds);
        Map<Long, AlbumConvert> musicAlbumByAlbumIdToMap = qukuService.getMusicAlbumByAlbumIdToMap(albumMaps);
        for (TbMusicPojo musicPojo : page.getRecords()) {
            List<ArtistConvert> artistConverts = Optional.ofNullable(artistByMusicIdToMap.get(musicPojo.getId())).orElse(new ArrayList<>());
            AlbumConvert albumConvert = Optional.ofNullable(musicAlbumByAlbumIdToMap.get(musicPojo.getAlbumId())).orElse(new AlbumConvert());
            CloudSearchRes.Result.SongsItem song = new CloudSearchRes.Result.SongsItem();
            song.setName(musicPojo.getMusicName());
            song.setId(musicPojo.getId());
            
            // 设置 歌手
            List<CloudSearchRes.Result.SongsItem.ArItem> arList = new ArrayList<>();
            for (ArtistConvert artistConvert : artistConverts) {
                CloudSearchRes.Result.SongsItem.ArItem artist = new CloudSearchRes.Result.SongsItem.ArItem();
                artist.setId(artistConvert.getId());
                artist.setName(artistConvert.getArtistName());
                artist.setTns(AliasUtil.getAliasList(artistConvert.getAliasName()));
                artist.setAlias(AliasUtil.getAliasList(artistConvert.getAliasName()));
                arList.add(artist);
            }
            song.setAr(arList);
            
            // 设置 专辑
            CloudSearchRes.Result.SongsItem.Al album = new CloudSearchRes.Result.SongsItem.Al();
            album.setId(albumConvert.getId());
            album.setName(albumConvert.getAlbumName());
            album.setPicUrl(remoteStorePicService.getAlbumPicUrl(albumConvert.getId()));
            album.setTns(new ArrayList<>());
            album.setPicStr("");
            album.setPic(0L);
            song.setAl(album);
            
            // 设置其他字段...
            song.setPop(100);
            song.setSt(0);
            song.setRt("");
            song.setFee(0);
            song.setV(12);
            song.setCrbt(null);
            song.setCf("");
            song.setDt(239560);
            song.setH(new CloudSearchRes.Result.SongsItem.H(320000, 0, 9584893, 3991, 44100));
            song.setM(new CloudSearchRes.Result.SongsItem.M(192000, 0, 5750953, 6570, 44100));
            song.setL(new CloudSearchRes.Result.SongsItem.L(128000, 0, 3833983, 8220, 44100));
            song.setSq(new CloudSearchRes.Result.SongsItem.Sq(1508256, 0, 45164780, 4001, 44100));
            song.setHr(null);
            song.setA(null);
            song.setCd("01");
            song.setNo(1);
            song.setRtUrl(null);
            song.setFtype(0);
            song.setRtUrls(new ArrayList<>());
            song.setDjId(0);
            song.setCopyright(1);
            song.setSId(0);
            song.setMark(17179877376L);
            song.setOriginCoverType(1);
            song.setOriginSongSimpleData(null);
            song.setTagPicList(null);
            song.setResourceState(true);
            song.setVersion(0);
            song.setSongJumpInfo(null);
            song.setEntertainmentTags(null);
            song.setSingle(0);
            song.setNoCopyrightRcmd(null);
            song.setMst(9);
            song.setCp(7002);
            song.setMv(0);
            song.setRtype(0);
            song.setRurl(null);
            song.setPublishTime(737308800000L);
            
            CloudSearchRes.Result.SongsItem.Privilege privilege = fillPrivilege();
            song.setPrivilege(privilege);
            
            songs.add(song);
        }
        result.setSongs(songs);
        
        result.setSongCount(page.getTotal());
        res.setResult(result);
        return res;
    }
    
    public SearchRes search(String keywords, Long limit, Long offset, Integer type) {
        long current = getPageCurrent(limit, offset);
        LambdaQueryWrapper<TbMusicPojo> wrapper = Wrappers.<TbMusicPojo>lambdaQuery()
                                                          .like(TbMusicPojo::getMusicName, keywords)
                                                          .or()
                                                          .like(TbMusicPojo::getAliasName, keywords);
        Page<TbMusicPojo> page = tbMusicService.page(Page.of(current, limit), wrapper);
        SearchRes res = new SearchRes();
        SearchRes.Result result = new SearchRes.Result();
        ArrayList<SearchRes.Result.SongsItem> songs = new ArrayList<>();
        
        List<Long> musicIds = page.getRecords().parallelStream().map(TbMusicPojo::getId).toList();
        List<Long> albumMaps = page.getRecords().parallelStream().map(TbMusicPojo::getAlbumId).toList();
        Map<Long, List<ArtistConvert>> artistByMusicIdToMap = qukuService.getArtistByMusicIdToMap(musicIds);
        Map<Long, AlbumConvert> musicAlbumByAlbumIdToMap = qukuService.getMusicAlbumByAlbumIdToMap(albumMaps);
        for (TbMusicPojo musicPojo : page.getRecords()) {
            List<ArtistConvert> artistConverts = Optional.ofNullable(artistByMusicIdToMap.get(musicPojo.getId())).orElse(new ArrayList<>());
            AlbumConvert albumConvert = Optional.ofNullable(musicAlbumByAlbumIdToMap.get(musicPojo.getAlbumId())).orElse(new AlbumConvert());
            SearchRes.Result.SongsItem song = new SearchRes.Result.SongsItem();
            song.setName(musicPojo.getMusicName());
            song.setId(musicPojo.getId());
            
            // 设置 歌手
            List<SearchRes.Result.SongsItem.ArItem> arList = new ArrayList<>();
            for (ArtistConvert artistConvert : artistConverts) {
                SearchRes.Result.SongsItem.ArItem artist = new SearchRes.Result.SongsItem.ArItem();
                artist.setId(artistConvert.getId());
                artist.setName(artistConvert.getArtistName());
                artist.setTns(AliasUtil.getAliasList(artistConvert.getAliasName()));
                artist.setAlias(AliasUtil.getAliasList(artistConvert.getAliasName()));
                arList.add(artist);
            }
            song.setAr(arList);
            
            // 设置 专辑
            SearchRes.Result.SongsItem.Al album = new SearchRes.Result.SongsItem.Al();
            album.setId(albumConvert.getId());
            album.setName(albumConvert.getAlbumName());
            album.setPicUrl(remoteStorePicService.getAlbumPicUrl(albumConvert.getId()));
            album.setTns(new ArrayList<>());
            album.setPicStr("");
            album.setPic(0L);
            song.setAl(album);
            
            // 设置其他字段...
            song.setPop(100);
            song.setSt(0);
            song.setRt("");
            song.setFee(0);
            song.setV(12);
            song.setCrbt(null);
            song.setCf("");
            song.setDt(239560);
            song.setH(new SearchRes.Result.SongsItem.H(320000, 0, 9584893, 3991, 44100));
            song.setM(new SearchRes.Result.SongsItem.M(192000, 0, 5750953, 6570, 44100));
            song.setL(new SearchRes.Result.SongsItem.L(128000, 0, 3833983, 8220, 44100));
            song.setSq(new SearchRes.Result.SongsItem.Sq(1508256, 0, 45164780, 4001, 44100));
            song.setHr(null);
            song.setA(null);
            song.setCd("01");
            song.setNo(1);
            song.setRtUrl(null);
            song.setFtype(0);
            song.setRtUrls(new ArrayList<>());
            song.setDjId(0);
            song.setCopyright(1);
            song.setSId(0);
            song.setMark(17179877376L);
            song.setOriginCoverType(1);
            song.setOriginSongSimpleData(null);
            song.setTagPicList(null);
            song.setResourceState(true);
            song.setVersion(0);
            song.setSongJumpInfo(null);
            song.setEntertainmentTags(null);
            song.setSingle(0);
            song.setNoCopyrightRcmd(null);
            song.setMst(9);
            song.setCp(7002);
            song.setMv(0);
            song.setRtype(0);
            song.setRurl(null);
            song.setPublishTime(737308800000L);
            
            SearchRes.Result.SongsItem.Privilege privilege = fillSearchPrivilege();
            song.setPrivilege(privilege);
            
            songs.add(song);
        }
        result.setSongs(songs);
        
        result.setSongCount(page.getTotal());
        res.setResult(result);
        return res;
    }
}
