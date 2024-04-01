package org.api.admin.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.api.admin.config.AdminConfig;
import org.api.admin.model.common.PageResCommon;
import org.api.admin.model.req.AlbumListPageReq;
import org.api.admin.model.req.AlbumPageReq;
import org.api.admin.model.req.SaveOrUpdateAlbumReq;
import org.api.admin.model.res.AlbumInfoRes;
import org.api.admin.model.res.AlbumListPageRes;
import org.api.admin.model.res.AlbumPageRes;
import org.api.admin.utils.MyPageUtil;
import org.api.admin.utils.OrderByUtil;
import org.api.admin.utils.WrapperUtil;
import org.api.common.service.QukuAPI;
import org.core.common.exception.BaseException;
import org.core.common.result.ResultCode;
import org.core.config.HttpRequestConfig;
import org.core.mybatis.iservice.TbAlbumService;
import org.core.mybatis.iservice.TbArtistService;
import org.core.mybatis.iservice.TbMusicService;
import org.core.mybatis.model.convert.AlbumConvert;
import org.core.mybatis.model.convert.ArtistConvert;
import org.core.mybatis.model.convert.MusicConvert;
import org.core.mybatis.pojo.TbAlbumPojo;
import org.core.mybatis.pojo.TbArtistPojo;
import org.core.mybatis.pojo.TbMusicPojo;
import org.core.mybatis.pojo.TbTagPojo;
import org.core.service.RemoteStorePicService;
import org.core.service.TagManagerService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

@Service(AdminConfig.ADMIN + "AlbumApi")
public class AlbumApi {
    
    /**
     * 专辑表
     */
    private final TbAlbumService albumService;
    
    /**
     * 歌手表
     */
    private final TbArtistService singerService;
    
    private final TbMusicService tbMusicService;
    
    private final QukuAPI qukuService;
    
    private final HttpRequestConfig httpRequestConfig;
    
    private final RemoteStorePicService remoteStorePicService;
    
    private final TagManagerService tagManagerService;
    
    public AlbumApi(TbAlbumService albumService, TbArtistService singerService, QukuAPI qukuService, HttpRequestConfig httpRequestConfig, RemoteStorePicService remoteStorePicService, TbMusicService tbMusicService, TagManagerService tagManagerService) {
        this.albumService = albumService;
        this.singerService = singerService;
        this.qukuService = qukuService;
        this.httpRequestConfig = httpRequestConfig;
        this.remoteStorePicService = remoteStorePicService;
        this.tbMusicService = tbMusicService;
        this.tagManagerService = tagManagerService;
    }
    
    
    public Page<AlbumListPageRes> getAllAlbumList(AlbumListPageReq req) {
        req.setAlbumName(StringUtils.trim(req.getAlbumName()));
        req.setArtistName(StringUtils.trim(req.getArtistName()));
        req.setPage(MyPageUtil.checkPage(req.getPage()));
        
        List<Long> albumListId = new ArrayList<>();
        
        if (StringUtils.isNotBlank(req.getAlbumName())) {
            LambdaQueryWrapper<TbAlbumPojo> albumWrapper = Wrappers.<TbAlbumPojo>lambdaQuery()
                                                                   .select(TbAlbumPojo::getId)
                                                                   .like(TbAlbumPojo::getAlbumName, req.getAlbumName());
            albumListId.addAll(albumService.listObjs(albumWrapper));
        }
        
        if (StringUtils.isNotBlank(req.getArtistName())) {
            // 查找歌手关联的专辑
            LambdaQueryWrapper<TbArtistPojo> singerWrapper = Wrappers.<TbArtistPojo>lambdaQuery()
                                                                     .select(TbArtistPojo::getId)
                                                                     .like(TbArtistPojo::getArtistName, req.getArtistName());
            List<Long> artistIds = singerService.listObjs(singerWrapper);
            if (CollUtil.isNotEmpty(artistIds)) {
                albumListId.addAll(qukuService.getAlbumIdsByArtistIds(artistIds));
            }
        }
    
        LambdaQueryWrapper<TbAlbumPojo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        OrderByUtil.pageOrderByAlbum(req.getOrder(), req.getOrderBy(), lambdaQueryWrapper);
        lambdaQueryWrapper.in(CollUtil.isNotEmpty(albumListId), TbAlbumPojo::getId, albumListId);
        
        // 查询全部专辑数据
        Page<TbAlbumPojo> albumPojoPage = new Page<>(req.getPage().getPageIndex(), req.getPage().getPageNum());
        Page<AlbumConvert> albumPage = qukuService.getAlbumPage(albumPojoPage, lambdaQueryWrapper);
        
        // 获取专辑ID，以供查询歌手信息
        Map<Long, List<ArtistConvert>> albumArtistMapByAlbumIds = new HashMap<>();
        if (CollUtil.isNotEmpty(albumPage.getRecords())) {
            List<Long> collect = albumPage.getRecords().stream().map(TbAlbumPojo::getId).toList();
            albumArtistMapByAlbumIds = qukuService.getArtistByAlbumIdsToMap(collect);
        }
        
        Page<AlbumListPageRes> page = new Page<>();
        BeanUtils.copyProperties(albumPage, page, "records");
        page.setRecords(new ArrayList<>());
        for (AlbumConvert tbAlbumPojo : albumPage.getRecords()) {
            AlbumListPageRes albumRes = new AlbumListPageRes();
            albumRes.setArtistList(new ArrayList<>());
            BeanUtils.copyProperties(tbAlbumPojo, albumRes);
            albumRes.setPicUrl(tbAlbumPojo.getPicUrl());
            
            // 获取专辑中所有歌手
            List<ArtistConvert> artistConverts = albumArtistMapByAlbumIds.get(tbAlbumPojo.getId());
            albumRes.setArtistList(artistConverts);
    
            // 获取专辑下歌曲数量
            albumRes.setAlbumSize(qukuService.getAlbumMusicCountByAlbumId(tbAlbumPojo.getId()).longValue());
    
            albumRes.setOrderBy(req.getOrderBy());
            albumRes.setOrder(req.getOrder());
    
            page.getRecords().add(albumRes);
        }
    
        return page;
    }
    
    /**
     * 添加音乐时选择专辑接口
     *
     * @param name 专辑名
     */
    public List<Map<String, Object>> getSelectAlbumList(String name) {
        LambdaQueryWrapper<TbAlbumPojo> desc = Wrappers.<TbAlbumPojo>lambdaQuery()
                                                       .like(StringUtils.isNotBlank(name), TbAlbumPojo::getAlbumName, name)
                                                       .orderByDesc(TbAlbumPojo::getUpdateTime);
        
        Page<TbAlbumPojo> page = albumService.page(new Page<>(0, 10), desc);
    
        ArrayList<Map<String, Object>> maps = new ArrayList<>();
        for (TbAlbumPojo albumPojo : page.getRecords()) {
            HashMap<String, Object> map = new HashMap<>();
            List<ArtistConvert> albumArtistListByAlbumIds = qukuService.getArtistByAlbumIds(albumPojo.getId());
            List<String> artistName = albumArtistListByAlbumIds
                    .parallelStream()
                    .map(TbArtistPojo::getArtistName)
                    .toList();
            String join = CollUtil.join(artistName, ",");
            String albumName = albumPojo.getAlbumName();
            String format = String.format("%s <b style='color: var(--el-color-primary);'>#%s#</b>", albumName, join);
            map.put("display", format);
            map.put("value", albumName);
            map.put("link", String.valueOf(albumPojo.getId()));
            map.put("artists", albumArtistListByAlbumIds);
            map.putAll(BeanUtil.beanToMap(albumPojo));
            maps.add(map);
        }
        return maps;
    }
    
    public AlbumInfoRes getAlbumInfo(Long albumId) {
        TbAlbumPojo byId = albumService.getById(albumId);
        if (Objects.isNull(byId)) {
            throw new BaseException(ResultCode.ALBUM_NO_EXIST_ERROR);
        }
        Integer albumCount = qukuService.getAlbumMusicCountByAlbumId(albumId);
        List<TbTagPojo> albumGenre = tagManagerService.getAlbumGenre(albumId);
        List<MusicConvert> musicListByAlbumId = qukuService.getMusicListByAlbumId(albumId);
        List<ArtistConvert> artistListByAlbumIds = qukuService.getArtistByAlbumIds(albumId);
        
        AlbumInfoRes albumRes = new AlbumInfoRes();
        albumRes.setAlbumGenre(albumGenre.parallelStream().map(TbTagPojo::getTagName).filter(StringUtils::isNotBlank).toList());
        albumRes.setArtistList(artistListByAlbumIds);
        albumRes.setMusicList(musicListByAlbumId);
        BeanUtils.copyProperties(byId, albumRes);
        albumRes.setPicUrl(remoteStorePicService.getAlbumPicUrl(byId.getId()));
        albumRes.setAlbumSize(Long.valueOf(albumCount));
        return albumRes;
    }
    
    public void deleteAlbum(List<Long> id, Boolean compel) {
        qukuService.deleteAlbum(id, compel);
    }
    
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdateAlbum(SaveOrUpdateAlbumReq req) {
        if (req.getId() == null && StringUtils.isBlank(req.getAlbumName())) {
            throw new BaseException(ResultCode.PARAM_NOT_COMPLETE);
        }
        albumService.saveOrUpdate(req);
        // 保存或更新专辑流派
        if (CollUtil.isNotEmpty(req.getAlbumGenre())) {
            List<String> list = req.getAlbumGenre().parallelStream().map(StringUtils::trim).toList();
            tagManagerService.addAlbumGenreLabel(req.getId(), list);
        }
        if (StringUtils.isNotBlank(req.getTempFile())) {
            File file = httpRequestConfig.getTempPathFile(req.getTempFile());
            remoteStorePicService.saveOrUpdateAlbumPicFile(req.getId(), file);
        }
    }
    
    public PageResCommon<AlbumPageRes> getAlbumPage(AlbumPageReq req) {
        final String name = StringUtils.trim(req.getName());
        final String albumName = StringUtils.trim(req.getAlbumName());
        final String musicName = StringUtils.trim(req.getMusicName());
        final String artistName = StringUtils.trim(req.getArtistName());
        
        List<Long> albumIds = new ArrayList<>();
        boolean nameNotBlank = StringUtils.isNotBlank(name);
        // 专辑
        boolean albumNotBlank = StringUtils.isNotBlank(albumName);
        if (nameNotBlank || albumNotBlank) {
            List<TbAlbumPojo> list = albumService.list(WrapperUtil.albumWrapper(nameNotBlank, albumNotBlank, name, albumName));
            albumIds.addAll(list.stream().map(TbAlbumPojo::getId).toList());
        }
        
        // 音乐
        boolean musicNotBlank = StringUtils.isNotBlank(musicName);
        if (nameNotBlank || musicNotBlank) {
            List<TbMusicPojo> list = tbMusicService.list(WrapperUtil.musicWrapper(nameNotBlank, musicNotBlank, name, musicName));
            Set<Long> collect = Optional.ofNullable(list).orElse(new ArrayList<>()).stream().map(TbMusicPojo::getAlbumId).collect(Collectors.toSet());
            albumIds.addAll(collect);
        }
        // 歌手
        boolean artistNotBlank = StringUtils.isNotBlank(artistName);
        if (nameNotBlank || artistNotBlank) {
            LambdaQueryWrapper<TbArtistPojo> singerWrapper = WrapperUtil.artistWrapper(
                    nameNotBlank, artistNotBlank, name,
                    artistName
            );
            // 查询歌手关联的专辑数据
            singerWrapper.select(TbArtistPojo::getId);
            List<Long> singerList = singerService.listObjs(singerWrapper);
            if (CollUtil.isNotEmpty(singerList)) {
                albumIds.addAll(qukuService.getAlbumIdsByArtistIds(singerList));
            }
        }
        
        // 如果前端传入搜索参数，并且没有查询到数据则直接返回空数据库。防止搜索结果混乱
        if ((StringUtils.isNotBlank(name)
                || StringUtils.isNotBlank(musicName)
                || StringUtils.isNotBlank(albumName)
                || StringUtils.isNotBlank(artistName))
                && CollUtil.isEmpty(albumIds)) {
            return new PageResCommon<>();
        }
        
        LambdaQueryWrapper<TbAlbumPojo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        OrderByUtil.pageOrderByAlbum(req.getOrder(), req.getOrderBy(), lambdaQueryWrapper);
        lambdaQueryWrapper.in(CollUtil.isNotEmpty(albumIds), TbAlbumPojo::getId, albumIds);
        
        // 查询全部专辑数据
        Page<TbAlbumPojo> albumPojoPage = req.getPageCommon().getPage();
        Page<AlbumConvert> albumPage = qukuService.getAlbumPage(albumPojoPage, lambdaQueryWrapper);
        
        // 获取专辑ID，以供查询歌手信息
        List<AlbumConvert> records = albumPage.getRecords();
        
        // 获取歌手信息
        Map<Long, List<ArtistConvert>> albumArtistMapByAlbumIds = new HashMap<>();
        if (CollUtil.isNotEmpty(records)) {
            List<Long> collect = records.stream().map(TbAlbumPojo::getId).toList();
            albumArtistMapByAlbumIds = qukuService.getArtistByAlbumIdsToMap(collect);
        }
        
        PageResCommon<AlbumPageRes> res = new PageResCommon<>();
        
        res.setCurrent(albumPage.getCurrent());
        res.setSize(albumPage.getSize());
        res.setTotal(albumPage.getTotal());
        ArrayList<AlbumPageRes> content = new ArrayList<>();
        for (AlbumConvert tbAlbumPojo : records) {
            AlbumPageRes albumRes = new AlbumPageRes();
            albumRes.setId(tbAlbumPojo.getId());
            albumRes.setAlbumName(tbAlbumPojo.getAlbumName());
            albumRes.setPublishTime(tbAlbumPojo.getPublishTime());
            albumRes.setPicUrl(tbAlbumPojo.getPicUrl());
            albumRes.setUserId(tbAlbumPojo.getUserId());
            albumRes.setUpdateTime(tbAlbumPojo.getUpdateTime());
            albumRes.setCreateTime(tbAlbumPojo.getCreateTime());
            
            // 获取专辑中所有歌手
            List<ArtistConvert> artistConverts = Optional.ofNullable(albumArtistMapByAlbumIds.get(tbAlbumPojo.getId())).orElse(new ArrayList<>());
            albumRes.setArtistList(artistConverts.stream().map(AlbumPageRes.AlbumArtistConvert::new).toList());
            
            // 获取专辑下歌曲数量
            albumRes.setAlbumSize(qukuService.getAlbumMusicCountByAlbumId(tbAlbumPojo.getId()).longValue());
            
            albumRes.setOrderBy(req.getOrderBy());
            albumRes.setOrder(req.getOrder());
            
            content.add(albumRes);
        }
        res.setContent(content);
        
        return res;
    }
}
