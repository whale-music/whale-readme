package org.api.admin.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.api.admin.config.AdminConfig;
import org.api.admin.model.common.SimpleArtist;
import org.api.admin.model.req.AlbumPageReq;
import org.api.admin.model.req.SaveOrUpdateArtistReq;
import org.api.admin.model.res.ArtistInfoRes;
import org.api.admin.model.res.ArtistMvListRes;
import org.api.admin.model.res.ArtistRes;
import org.api.admin.utils.MyPageUtil;
import org.api.common.service.QukuAPI;
import org.core.common.constant.defaultinfo.DefaultInfo;
import org.core.common.exception.BaseException;
import org.core.common.result.ResultCode;
import org.core.config.HttpRequestConfig;
import org.core.mybatis.iservice.TbArtistService;
import org.core.mybatis.iservice.TbMvArtistService;
import org.core.mybatis.iservice.TbMvService;
import org.core.mybatis.model.convert.AlbumConvert;
import org.core.mybatis.model.convert.ArtistConvert;
import org.core.mybatis.model.convert.MusicConvert;
import org.core.mybatis.pojo.TbArtistPojo;
import org.core.mybatis.pojo.TbMvArtistPojo;
import org.core.mybatis.pojo.TbMvPojo;
import org.core.mybatis.pojo.TbTagPojo;
import org.core.service.RemoteStorePicService;
import org.core.utils.AliasUtil;
import org.core.utils.ExceptionUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;

@Service(AdminConfig.ADMIN + "ArtistApi")
public class ArtistApi {
    
    
    private final TbArtistService artistService;
    
    private final QukuAPI qukuService;
    
    private final DefaultInfo defaultInfo;
    
    private final HttpRequestConfig httpRequestConfig;
    
    private final TbMvArtistService tbMvArtistService;
    
    private final TbMvService tbMvService;
    
    private final RemoteStorePicService remoteStorePicService;
    
    public ArtistApi(TbArtistService artistService, QukuAPI qukuService, DefaultInfo defaultInfo, HttpRequestConfig httpRequestConfig, TbMvArtistService tbMvArtistService, TbMvService tbMvService, RemoteStorePicService remoteStorePicService) {
        this.artistService = artistService;
        this.qukuService = qukuService;
        this.defaultInfo = defaultInfo;
        this.httpRequestConfig = httpRequestConfig;
        this.tbMvArtistService = tbMvArtistService;
        this.tbMvService = tbMvService;
        this.remoteStorePicService = remoteStorePicService;
    }
    
    /**
     * 设置分页查询排序
     */
    private static void pageOrderBy(boolean order, String orderBy, LambdaQueryWrapper<TbArtistPojo> musicWrapper) {
        // sort歌曲添加顺序, createTime创建日期顺序,updateTime修改日期顺序, id歌曲ID顺序
        switch (Optional.ofNullable(orderBy).orElse("")) {
            case "id":
                musicWrapper.orderBy(true, order, TbArtistPojo::getId);
                break;
            case "updateTime":
                musicWrapper.orderBy(true, order, TbArtistPojo::getUpdateTime);
                break;
            case "createTime":
            default:
                musicWrapper.orderBy(true, order, TbArtistPojo::getCreateTime);
                break;
        }
    }
    
    public Page<ArtistRes> getAllSingerList(AlbumPageReq req) {
        req.setArtistName(StringUtils.trim(req.getArtistName()));
        req.setAlbumName(StringUtils.trim(req.getAlbumName()));
        
        req.setPage(MyPageUtil.checkPage(req.getPage()));
        
        Page<TbArtistPojo> page = new Page<>(req.getPage().getPageIndex(), req.getPage().getPageNum());
        LambdaQueryWrapper<TbArtistPojo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(req.getArtistName()), TbArtistPojo::getArtistName, req.getArtistName());
        pageOrderBy(req.getOrder(), req.getOrderBy(), queryWrapper);
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
}
