package org.api.admin.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.api.admin.config.AdminConfig;
import org.api.admin.model.common.PageResCommon;
import org.api.admin.model.common.SimpleArtist;
import org.api.admin.model.req.AlbumListPageReq;
import org.api.admin.model.req.ArtistPageReq;
import org.api.admin.model.req.SaveOrUpdateArtistReq;
import org.api.admin.model.res.ArtistInfoRes;
import org.api.admin.model.res.ArtistMvListRes;
import org.api.admin.model.res.ArtistPageRes;
import org.api.admin.model.res.ArtistRes;
import org.api.admin.utils.MyPageUtil;
import org.api.admin.utils.OrderByUtil;
import org.api.admin.utils.WrapperUtil;
import org.api.common.service.QukuAPI;
import org.core.common.constant.defaultinfo.DefaultInfo;
import org.core.common.exception.BaseException;
import org.core.common.result.ResultCode;
import org.core.config.HttpRequestConfig;
import org.core.mybatis.iservice.*;
import org.core.mybatis.model.convert.AlbumConvert;
import org.core.mybatis.model.convert.ArtistConvert;
import org.core.mybatis.model.convert.MusicConvert;
import org.core.mybatis.pojo.*;
import org.core.service.RemoteStorePicService;
import org.core.utils.AliasUtil;
import org.core.utils.ExceptionUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

@Service(AdminConfig.ADMIN + "ArtistApi")
public class ArtistApi {
    
    
    private final TbArtistService artistService;
    
    private final TbAlbumService tbAlbumService;
    
    private final TbMusicService tbMusicService;
    
    private final QukuAPI qukuService;
    
    private final DefaultInfo defaultInfo;
    
    private final HttpRequestConfig httpRequestConfig;
    
    private final TbMvArtistService tbMvArtistService;
    
    private final TbMvService tbMvService;
    
    private final RemoteStorePicService remoteStorePicService;
    
    public ArtistApi(TbArtistService artistService, QukuAPI qukuService, DefaultInfo defaultInfo, HttpRequestConfig httpRequestConfig, TbMvArtistService tbMvArtistService, TbMvService tbMvService, RemoteStorePicService remoteStorePicService, TbMusicService tbMusicService, TbAlbumService tbAlbumService) {
        this.artistService = artistService;
        this.qukuService = qukuService;
        this.defaultInfo = defaultInfo;
        this.httpRequestConfig = httpRequestConfig;
        this.tbMvArtistService = tbMvArtistService;
        this.tbMvService = tbMvService;
        this.remoteStorePicService = remoteStorePicService;
        this.tbMusicService = tbMusicService;
        this.tbAlbumService = tbAlbumService;
    }
    
    public Page<ArtistRes> getAllSingerList(AlbumListPageReq req) {
        req.setArtistName(StringUtils.trim(req.getArtistName()));
        req.setAlbumName(StringUtils.trim(req.getAlbumName()));
        
        req.setPage(MyPageUtil.checkPage(req.getPage()));
        
        Page<TbArtistPojo> page = new Page<>(req.getPage().getPageIndex(), req.getPage().getPageNum());
        LambdaQueryWrapper<TbArtistPojo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(req.getArtistName()), TbArtistPojo::getArtistName, req.getArtistName());
        OrderByUtil.pageOrderByArtist(req.getOrder(), req.getOrderBy(), queryWrapper);
        artistService.page(page, queryWrapper);
        
        Page<ArtistRes> singerResPage = new Page<>();
        BeanUtils.copyProperties(page, singerResPage);
        singerResPage.setRecords(new ArrayList<>());
        for (TbArtistPojo singerPojo : page.getRecords()) {
            long albumSize = qukuService.getArtistAlbumCountBySingerId(singerPojo.getId());
            long musicSize = qukuService.getMusicCountBySingerId(singerPojo.getId());
            
            ArtistRes artistRes = new ArtistRes();
            BeanUtils.copyProperties(singerPojo, artistRes);
            artistRes.setAlbumSize(String.valueOf(albumSize));
            artistRes.setMusicSize(String.valueOf(musicSize));
            artistRes.setPicUrl(remoteStorePicService.getArtistPicUrl(singerPojo.getId()));
            singerResPage.getRecords().add(artistRes);
        }
        
        
        return singerResPage;
    }
    
    public List<Map<String, Object>> getSelectedSinger(String name) {
        LambdaQueryWrapper<TbArtistPojo> desc = Wrappers.<TbArtistPojo>lambdaQuery()
                                                        .like(StringUtils.isNotBlank(name), TbArtistPojo::getArtistName, name)
                                                        .orderByDesc(TbArtistPojo::getUpdateTime);
        
        Page<TbArtistPojo> page = artistService.page(new Page<>(0, 10), desc);
        
        ArrayList<Map<String, Object>> maps = new ArrayList<>();
        for (TbArtistPojo albumPojo : page.getRecords()) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("value", albumPojo.getArtistName());
            map.put("link", String.valueOf(albumPojo.getId()));
            map.putAll(BeanUtil.beanToMap(albumPojo));
            maps.add(map);
        }
        return maps;
    }
    
    public List<ArtistConvert> getSingerListByAlbumId(Long albumId) {
        return qukuService.getAlbumArtistListByAlbumIds(albumId);
    }
    
    public ArtistInfoRes getArtistById(Long id) {
        ArtistInfoRes artistInfoRes = new ArtistInfoRes();
        
        TbArtistPojo pojo = artistService.getById(id);
        BeanUtils.copyProperties(pojo, artistInfoRes);
        artistInfoRes.setArtistNames(AliasUtil.getAliasList(pojo.getAliasName()));
        String picUrl = remoteStorePicService.getArtistPicUrl(pojo.getId());
        artistInfoRes.setPicUrl(StringUtils.isBlank(picUrl) ? defaultInfo.getPic().getDefaultPic() : picUrl);
        List<AlbumConvert> albumListByArtistIds = qukuService.getAlbumListByArtistIds(Collections.singletonList(id));
        List<MusicConvert> musicListByArtistId = qukuService.getMusicListByArtistId(id);
        artistInfoRes.setAlbumList(albumListByArtistIds);
        artistInfoRes.setMusicList(musicListByArtistId);
        return artistInfoRes;
    }
    
    public void deleteArtist(List<Long> id) {
        qukuService.deleteArtist(id);
    }
    
    public void saveOrUpdateArtist(SaveOrUpdateArtistReq req) {
        if (req.getId() == null && StringUtils.isEmpty(req.getArtistName())) {
            throw new BaseException(ResultCode.PARAM_NOT_COMPLETE);
        }
        artistService.saveOrUpdate(req);
        if (StringUtils.isNotBlank(req.getTempFile())) {
            File file = httpRequestConfig.getTempPathFile(req.getTempFile());
            ExceptionUtil.isNull(FileUtil.isEmpty(file), ResultCode.DATA_NONE_FOUND);
            remoteStorePicService.saveOrUpdateArtistPicFile(req.getId(), file);
        }
    }
    
    public List<ArtistMvListRes> getMvList(Long id) {
        List<TbMvArtistPojo> list = tbMvArtistService.list(Wrappers.<TbMvArtistPojo>lambdaQuery().eq(TbMvArtistPojo::getArtistId, id));
        if (CollUtil.isEmpty(list)) {
            return Collections.emptyList();
        }
        List<TbMvPojo> tbMvPojos = tbMvService.listByIds(list.parallelStream().map(TbMvArtistPojo::getMvId).toList());
        
        List<Long> mvIds = tbMvPojos.parallelStream().map(TbMvPojo::getId).toList();
        Map<Long, List<TbTagPojo>> labelMusicTag = qukuService.getLabelMvTag(mvIds);
        Map<Long, String> mvPicUrl = remoteStorePicService.getMvPicUrl(mvIds);
        Map<Long, List<ArtistConvert>> mvArtistByMvIdsToMap = qukuService.getMvArtistByMvIdToMap(mvIds);
        
        List<ArtistMvListRes> listRes = new ArrayList<>();
        for (TbMvPojo mvPojo : tbMvPojos) {
            ArtistMvListRes e = new ArtistMvListRes(mvPojo);
            List<TbTagPojo> tbTagPojos = labelMusicTag.get(mvPojo.getId());
            if (CollUtil.isNotEmpty(tbTagPojos)) {
                e.setTags(tbTagPojos.parallelStream().map(TbTagPojo::getTagName).toList());
            }
            e.setPicUrl(mvPicUrl.get(mvPojo.getId()));
            List<ArtistConvert> artistConverts = mvArtistByMvIdsToMap.get(mvPojo.getId());
            if (CollUtil.isNotEmpty(artistConverts)) {
                e.setArtists(artistConverts.parallelStream()
                                           .map(artistConvert -> new SimpleArtist(artistConvert.getId(), artistConvert.getArtistName()))
                                           .toList());
            }
            listRes.add(e);
        }
        return listRes;
    }
    
    public PageResCommon<ArtistPageRes> getArtistPage(ArtistPageReq req) {
        Page<TbArtistPojo> reqPage = req.getPage();
        Page<TbArtistPojo> page = new Page<>(reqPage.getCurrent(), reqPage.getSize());
        final String name = req.getName();
        final String albumName = req.getAlbumName();
        final String musicName = req.getMusicName();
        final String artistName = StringUtils.trim(req.getArtistName());
        List<Long> artistIds = new ArrayList<>();
        // 音乐
        boolean nameNotBlank = StringUtils.isNotBlank(name);
        boolean musicNotBlank = StringUtils.isNotBlank(musicName);
        if (nameNotBlank || musicNotBlank) {
            List<TbMusicPojo> list = tbMusicService.list(WrapperUtil.musicWrapper(nameNotBlank, musicNotBlank, name, musicName));
            List<ArtistConvert> musicArtistByMusicId = qukuService.getMusicArtistByMusicId(list.parallelStream()
                                                                                               .map(TbMusicPojo::getId)
                                                                                               .collect(Collectors.toSet()));
            artistIds.addAll(musicArtistByMusicId.parallelStream().map(TbArtistPojo::getId).collect(Collectors.toSet()));
        }
        // 专辑
        boolean albumNotBlank = StringUtils.isNotBlank(albumName);
        if (nameNotBlank || albumNotBlank) {
            List<TbAlbumPojo> list = tbAlbumService.list(WrapperUtil.albumWrapper(nameNotBlank, albumNotBlank, name, albumName));
            List<ArtistConvert> artistConverts = qukuService.getAlbumArtistListByAlbumIds(list.parallelStream()
                                                                                              .map(TbAlbumPojo::getId)
                                                                                              .toList());
            artistIds.addAll(artistConverts.stream().map(TbArtistPojo::getId).toList());
        }
        // 歌手
        boolean artistNotBlank = StringUtils.isNotBlank(artistName);
        if (nameNotBlank || artistNotBlank) {
            List<TbArtistPojo> list = artistService.list(WrapperUtil.artistWrapper(nameNotBlank, artistNotBlank, name, artistName));
            artistIds.addAll(list.parallelStream().map(TbArtistPojo::getId).toList());
        }
        
        // 如果前端传入搜索参数，并且没有查询到数据则直接返回空数据库。防止搜索结果混乱
        if (StringUtils.isNotBlank(name)
                || (StringUtils.isNotBlank(musicName)
                || StringUtils.isNotBlank(albumName)
                || StringUtils.isNotBlank(artistName))
                && CollUtil.isEmpty(artistIds)) {
            return new PageResCommon<>();
        }
        
        LambdaQueryWrapper<TbArtistPojo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(CollUtil.isNotEmpty(artistIds), TbArtistPojo::getId, artistIds);
        artistService.page(page, queryWrapper);
        
        PageResCommon<ArtistPageRes> res = new PageResCommon<>();
        
        res.setTotal(page.getTotal());
        res.setSize(page.getSize());
        res.setCurrent(page.getCurrent());
        
        ArrayList<ArtistPageRes> content = new ArrayList<>();
        for (TbArtistPojo artistPojo : page.getRecords()) {
            long albumSize = qukuService.getArtistAlbumCountBySingerId(artistPojo.getId());
            long musicSize = qukuService.getMusicCountBySingerId(artistPojo.getId());
            
            ArtistPageRes artistRes = new ArtistPageRes();
            artistRes.setId(artistPojo.getId());
            artistRes.setArtistName(artistPojo.getArtistName());
            artistRes.setAliasName(artistPojo.getAliasName());
            artistRes.setUserId(artistPojo.getUserId());
            artistRes.setPicUrl(remoteStorePicService.getArtistPicUrl(artistPojo.getId()));
            artistRes.setAlbumSize(String.valueOf(albumSize));
            artistRes.setMusicSize(String.valueOf(musicSize));
            
            content.add(artistRes);
        }
        res.setContent(content);
        
        return res;
    }
}
