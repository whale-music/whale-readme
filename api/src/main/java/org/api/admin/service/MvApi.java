package org.api.admin.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.api.admin.config.AdminConfig;
import org.api.admin.model.common.SimpleArtist;
import org.api.admin.model.req.SaveMvReq;
import org.api.admin.model.res.MvInfoRes;
import org.api.admin.model.res.MvPageRes;
import org.api.admin.utils.VideoUtil;
import org.api.common.service.QukuAPI;
import org.core.common.constant.PicTypeConstant;
import org.core.config.HttpRequestConfig;
import org.core.mybatis.iservice.TbMvArtistService;
import org.core.mybatis.iservice.TbMvService;
import org.core.mybatis.model.convert.ArtistConvert;
import org.core.mybatis.pojo.TbMvArtistPojo;
import org.core.mybatis.pojo.TbMvPojo;
import org.core.mybatis.pojo.TbTagPojo;
import org.core.service.RemoteStorageService;
import org.core.service.RemoteStorePicService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service(AdminConfig.ADMIN + "MvApi")
public class MvApi {
    
    private final TbMvService tbMvService;
    
    private final TbMvArtistService tbMvArtistService;
    
    private final QukuAPI qukuApi;
    
    private final HttpRequestConfig httpRequestConfig;
    
    private final RemoteStorageService remoteStorageService;
    
    private final RemoteStorePicService remoteStorePicService;
    
    
    public MvApi(TbMvService tbMvService, QukuAPI qukuApi, HttpRequestConfig httpRequestConfig, TbMvArtistService tbMvArtistService, RemoteStorageService remoteStorageService, RemoteStorePicService remoteStorePicService) {
        this.tbMvService = tbMvService;
        this.qukuApi = qukuApi;
        this.httpRequestConfig = httpRequestConfig;
        this.tbMvArtistService = tbMvArtistService;
        this.remoteStorageService = remoteStorageService;
        this.remoteStorePicService = remoteStorePicService;
    }
    
    @Transactional(rollbackFor = Exception.class)
    public void saveMvInfo(SaveMvReq request) throws IOException {
        TbMvPojo one = tbMvService.getOne(Wrappers.<TbMvPojo>lambdaQuery().eq(TbMvPojo::getTitle, request.getTitle()));
        if (Objects.nonNull(one)) {
            request.setId(one.getId());
        }
        request.setPath("undefined path");
        request.setMd5("undefined md5");
        tbMvService.save(request);
        
        // artist
        tbMvArtistService.remove(Wrappers.<TbMvArtistPojo>lambdaQuery().in(TbMvArtistPojo::getMvId, request.getId()));
        if (CollUtil.isNotEmpty(request.getArtistIds())) {
            List<TbMvArtistPojo> list = request.getArtistIds().parallelStream().map(aLong -> new TbMvArtistPojo(request.getId(), aLong)).toList();
            tbMvArtistService.saveBatch(list);
        }
        
        // upload pic
        remoteStorePicService.saveOrUpdateMvPicFile(request.getId(), new File(httpRequestConfig.getTempPath(), request.getPicTempPath()));
        // 上传文件
        if (StringUtils.isNotBlank(request.getMvTempPath())) {
            File file = new File(httpRequestConfig.getTempPath(), request.getMvTempPath());
            long videoDuration = VideoUtil.getVideoDuration(file);
            request.setDuration(videoDuration);
            String path = remoteStorageService.uploadMvFile(file);
            // path
            request.setPath(path);
        }
        tbMvService.updateById(request);
        // tag
        Long id = request.getId();
        // 移除重新添加
        qukuApi.removeLabelMv(id);
        qukuApi.addMvGenreLabel(request.getId(), request.getTags());
    }
    
    public Page<MvPageRes> getMvPage(String title, List<String> artist, List<String> tags, String order, String orderBy, Long index, Long size) {
        // title
        List<TbMvPojo> tbMvPojoList = tbMvService.list(Wrappers.<TbMvPojo>lambdaQuery().like(StringUtils.isNotBlank(title), TbMvPojo::getTitle, title));
        List<Long> mvIds = new LinkedList<>(tbMvPojoList.parallelStream().map(TbMvPojo::getId).toList());
        if (CollUtil.isEmpty(mvIds)) {
            return new Page<>();
        }
        // artist
        if (CollUtil.isNotEmpty(artist)) {
            List<TbMvArtistPojo> list = tbMvArtistService.list(Wrappers.<TbMvArtistPojo>lambdaQuery().in(TbMvArtistPojo::getMvId, mvIds));
            mvIds.addAll(list.parallelStream().map(TbMvArtistPojo::getMvId).toList());
        }
        // tag
        if (CollUtil.isNotEmpty(mvIds) && CollUtil.isNotEmpty(tags)) {
            Map<Long, List<TbTagPojo>> labelMvTag = qukuApi.getLabelMvTag(mvIds, tags);
            mvIds.addAll(labelMvTag.keySet());
        }
        Page<TbMvPojo> page = new Page<>(index, size);
        LambdaQueryWrapper<TbMvPojo> tbMvPojoLambdaQueryWrapper = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(order) && StringUtils.isNotBlank(orderBy)) {
            boolean isAsc = StringUtils.equalsIgnoreCase(order, "asc");
            switch (orderBy) {
                case "publishTime" -> tbMvPojoLambdaQueryWrapper.orderBy(true, isAsc, TbMvPojo::getPublishTime);
                case "updateTime" -> tbMvPojoLambdaQueryWrapper.orderBy(true, isAsc, TbMvPojo::getUpdateTime);
                default -> tbMvPojoLambdaQueryWrapper.orderBy(true, isAsc, TbMvPojo::getCreateTime);
            }
        }
        tbMvService.page(page, tbMvPojoLambdaQueryWrapper.in(TbMvPojo::getId, mvIds));
        Page<MvPageRes> mvPageResPage = new Page<>();
        LinkedList<MvPageRes> records = new LinkedList<>();
        BeanUtils.copyProperties(page, mvPageResPage);
        Map<Long, List<TbTagPojo>> labelMusicTag = qukuApi.getLabelMvTag(mvIds);
        Map<Long, String> mvPicUrl = remoteStorePicService.getMvPicUrl(mvIds);
        Map<Long, List<ArtistConvert>> mvArtistByMvIdsToMap = qukuApi.getMvArtistByMvIdToMap(mvIds);
        for (TbMvPojo mvPojo : page.getRecords()) {
            MvPageRes e = new MvPageRes();
            BeanUtils.copyProperties(mvPojo, e);
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
            records.add(e);
        }
        mvPageResPage.setRecords(records);
        return mvPageResPage;
    }
    
    public void removeMv(List<Long> ids) {
        // remove mv artist
        tbMvArtistService.remove(Wrappers.<TbMvArtistPojo>lambdaQuery().in(TbMvArtistPojo::getMvId, ids));
        
        // remove mv pic
        remoteStorePicService.removePicIds(ids, Collections.singleton(PicTypeConstant.MV));
        
        // remove mv tag
        qukuApi.removeLabelMv(ids);
        
        // remove file
        remoteStorageService.removeMvStorageFiles(ids);
        
        // remove mv
        tbMvService.removeBatchByIds(ids);
    }
    
    public MvInfoRes getMvInfo(Long id) {
        TbMvPojo mvPojo = tbMvService.getById(id);
        
        MvInfoRes e = new MvInfoRes();
        BeanUtils.copyProperties(mvPojo, e);
        String mvPicUrl = remoteStorePicService.getMvPicUrl(mvPojo.getId());
        Map<Long, List<TbTagPojo>> labelMusicTag = qukuApi.getLabelMvTag(e.getId());
        List<TbTagPojo> tbTagPojos = labelMusicTag.get(mvPojo.getId());
        if (CollUtil.isNotEmpty(tbTagPojos)) {
            e.setTags(tbTagPojos.parallelStream().map(TbTagPojo::getTagName).toList());
        }
        e.setPicUrl(mvPicUrl);
        Map<Long, List<ArtistConvert>> mvArtistByMvIdsToMap = qukuApi.getMvArtistByMvIdToMap(id);
        List<ArtistConvert> artistConverts = mvArtistByMvIdsToMap.get(mvPojo.getId());
        if (CollUtil.isNotEmpty(artistConverts)) {
            e.setArtists(artistConverts.parallelStream()
                                       .map(artistConvert -> new SimpleArtist(artistConvert.getId(), artistConvert.getArtistName()))
                                       .toList());
        }
        String path = e.getPath();
        if (StringUtils.isNotBlank(path)) {
            e.setMvUrl(remoteStorageService.getAddressesNoRefresh(path));
        }
        return e;
    }
    
    public void updateMvInfo(SaveMvReq request) {
        // tag
        Long id = request.getId();
        // 移除重新添加
        qukuApi.removeLabelMv(id);
        qukuApi.addMvGenreLabel(request.getId(), request.getTags());
        // artist
        tbMvArtistService.remove(Wrappers.<TbMvArtistPojo>lambdaQuery().in(TbMvArtistPojo::getMvId, request.getId()));
        if (CollUtil.isNotEmpty(request.getArtistIds())) {
            List<TbMvArtistPojo> list = request.getArtistIds().parallelStream().map(aLong -> new TbMvArtistPojo(request.getId(), aLong)).toList();
            tbMvArtistService.saveBatch(list);
        }
        
        tbMvService.updateById(request);
    }
    
    public String updateMvFile(MultipartFile uploadFile, Long id) throws IOException {
        TbMvPojo mvPojo = tbMvService.getById(id);
        // 上传文件
        File dest = new File(httpRequestConfig.getTempPath(),
                LocalDateTime.now().getNano() + "-" + Objects.requireNonNull(uploadFile.getOriginalFilename()));
        FileUtil.writeBytes(uploadFile.getBytes(), dest);
        long videoDuration = VideoUtil.getVideoDuration(dest);
        String md5Str = DigestUtils.md5DigestAsHex(uploadFile.getBytes());
        String path = remoteStorageService.uploadMvFile(dest, md5Str);
        mvPojo.setPath(path);
        mvPojo.setMd5(md5Str);
        mvPojo.setDuration(videoDuration);
        tbMvService.updateById(mvPojo);
        return path;
    }
}
