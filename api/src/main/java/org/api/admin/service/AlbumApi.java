package org.api.admin.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.api.admin.config.AdminConfig;
import org.api.admin.model.req.AlbumPageReq;
import org.api.admin.model.req.SaveOrUpdateAlbumReq;
import org.api.admin.model.res.AlbumInfoRes;
import org.api.admin.model.res.AlbumPageRes;
import org.api.admin.utils.MyPageUtil;
import org.api.admin.utils.OrderByUtil;
import org.api.common.service.QukuAPI;
import org.core.common.exception.BaseException;
import org.core.common.result.ResultCode;
import org.core.config.HttpRequestConfig;
import org.core.mybatis.iservice.TbAlbumArtistService;
import org.core.mybatis.iservice.TbAlbumService;
import org.core.mybatis.iservice.TbArtistService;
import org.core.mybatis.model.convert.AlbumConvert;
import org.core.mybatis.model.convert.ArtistConvert;
import org.core.mybatis.model.convert.MusicConvert;
import org.core.mybatis.pojo.TbAlbumArtistPojo;
import org.core.mybatis.pojo.TbAlbumPojo;
import org.core.mybatis.pojo.TbArtistPojo;
import org.core.mybatis.pojo.TbTagPojo;
import org.core.service.RemoteStorePicService;
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
     * 专辑歌手中间表
     */
    private final TbAlbumArtistService albumSingerService;
    
    /**
     * 歌手表
     */
    private final TbArtistService singerService;
    
    private final QukuAPI qukuService;
    
    private final HttpRequestConfig httpRequestConfig;
    
    private final RemoteStorePicService remoteStorePicService;
    
    public AlbumApi(TbAlbumService albumService, TbAlbumArtistService albumSingerService, TbArtistService singerService, QukuAPI qukuService, HttpRequestConfig httpRequestConfig, RemoteStorePicService remoteStorePicService) {
        this.albumService = albumService;
        this.albumSingerService = albumSingerService;
        this.singerService = singerService;
        this.qukuService = qukuService;
        this.httpRequestConfig = httpRequestConfig;
        this.remoteStorePicService = remoteStorePicService;
    }
    
    
    public Page<AlbumPageRes> getAllAlbumList(AlbumPageReq req) {
        req.setAlbumName(StringUtils.trim(req.getAlbumName()));
        req.setArtistName(StringUtils.trim(req.getArtistName()));
        
        req.setPage(MyPageUtil.checkPage(req.getPage()));
        
        List<TbAlbumPojo> albumList = new ArrayList<>();
        if (StringUtils.isNotBlank(req.getAlbumName())) {
            LambdaQueryWrapper<TbAlbumPojo> albumWrapper = Wrappers.<TbAlbumPojo>lambdaQuery().like(TbAlbumPojo::getAlbumName, req.getAlbumName());
            albumList = albumService.list(albumWrapper);
        }
    
        List<Long> singerAlbumIdList = new ArrayList<>();
        if (StringUtils.isNotBlank(req.getArtistName())) {
            LambdaQueryWrapper<TbArtistPojo> singerWrapper = Wrappers.<TbArtistPojo>lambdaQuery().like(TbArtistPojo::getArtistName, req.getArtistName());
            List<TbArtistPojo> singerList = singerService.list(singerWrapper);
            // 查询歌手表
            if (CollUtil.isNotEmpty(singerList)) {
                List<Long> collect = singerList.stream().map(TbArtistPojo::getId).toList();
                List<TbAlbumArtistPojo> list = albumSingerService.list(Wrappers.<TbAlbumArtistPojo>lambdaQuery()
                                                                               .in(TbAlbumArtistPojo::getArtistId, collect));
                singerAlbumIdList = list.stream().map(TbAlbumArtistPojo::getAlbumId).toList();
            }
        }
        List<Long> albumListId = albumList.stream().map(TbAlbumPojo::getId).collect(Collectors.toList());
        albumListId.addAll(singerAlbumIdList);
    
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
            albumArtistMapByAlbumIds = qukuService.getAlbumArtistMapByAlbumIds(collect);
        }
        
        Page<AlbumPageRes> page = new Page<>();
        BeanUtils.copyProperties(albumPage, page, "records");
        page.setRecords(new ArrayList<>());
        for (AlbumConvert tbAlbumPojo : albumPage.getRecords()) {
            AlbumPageRes albumRes = new AlbumPageRes();
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
            List<ArtistConvert> albumArtistListByAlbumIds = qukuService.getAlbumArtistListByAlbumIds(albumPojo.getId());
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
        Integer albumCount = qukuService.getAlbumMusicCountByAlbumId(albumId);
        List<TbTagPojo> albumGenre = qukuService.getLabelAlbumGenre(albumId);
        List<MusicConvert> musicListByAlbumId = qukuService.getMusicListByAlbumId(albumId);
        List<ArtistConvert> artistListByAlbumIds = qukuService.getAlbumArtistListByAlbumIds(albumId);
        
        AlbumInfoRes albumRes = new AlbumInfoRes();
        albumRes.setAlbumGenre(albumGenre.parallelStream().map(TbTagPojo::getTagName).filter(StringUtils::isNotBlank).findFirst().orElse(""));
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
        if (StringUtils.isNotBlank(req.getAlbumGenre())) {
            qukuService.addAlbumGenreLabel(req.getId(), StringUtils.trim(req.getAlbumGenre()));
        }
        if (StringUtils.isNotBlank(req.getTempFile())) {
            File file = httpRequestConfig.getTempPathFile(req.getTempFile());
            remoteStorePicService.saveOrUpdateAlbumPicFile(req.getId(), file);
        }
        // 如果是更新专辑关联歌手数据则删除原来的，重新添加
        albumSingerService.remove(Wrappers.<TbAlbumArtistPojo>lambdaQuery().in(TbAlbumArtistPojo::getAlbumId, req.getId()));
        if (CollUtil.isNotEmpty(req.getArtistIds())) {
            Collection<TbAlbumArtistPojo> albumArtistList = req.getArtistIds()
                                                               .parallelStream()
                                                               .map(aLong -> new TbAlbumArtistPojo(req.getId(), aLong))
                                                               .collect(Collectors.toSet());
            albumSingerService.saveBatch(albumArtistList);
        }
    }
}
