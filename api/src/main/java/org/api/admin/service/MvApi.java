package org.api.admin.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.crypto.digest.DigestUtil;
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
import org.core.config.HttpRequestConfig;
import org.core.mybatis.iservice.TbMvArtistService;
import org.core.mybatis.iservice.TbMvInfoService;
import org.core.mybatis.iservice.TbMvService;
import org.core.mybatis.model.convert.ArtistConvert;
import org.core.mybatis.pojo.TbMvArtistPojo;
import org.core.mybatis.pojo.TbMvInfoPojo;
import org.core.mybatis.pojo.TbMvPojo;
import org.core.mybatis.pojo.TbTagPojo;
import org.core.service.RemoteStorageService;
import org.core.service.RemoteStorePicService;
import org.core.service.TagManagerService;
import org.core.utils.UserUtil;
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
    
    private final TbMvInfoService tbMvInfoService;
    
    private final TagManagerService tagManagerService;
    
    public MvApi(TbMvService tbMvService, QukuAPI qukuApi, HttpRequestConfig httpRequestConfig, TbMvArtistService tbMvArtistService, RemoteStorageService remoteStorageService, RemoteStorePicService remoteStorePicService, TbMvInfoService tbMvInfoService, TagManagerService tagManagerService) {
        this.tbMvService = tbMvService;
        this.qukuApi = qukuApi;
        this.httpRequestConfig = httpRequestConfig;
        this.tbMvArtistService = tbMvArtistService;
        this.remoteStorageService = remoteStorageService;
        this.remoteStorePicService = remoteStorePicService;
        this.tbMvInfoService = tbMvInfoService;
        this.tagManagerService = tagManagerService;
    }
    
    @Transactional(rollbackFor = Exception.class)
    public void saveMvInfo(SaveMvReq request) throws IOException {
        TbMvInfoPojo one = Optional.ofNullable(tbMvInfoService.getOne(Wrappers.<TbMvInfoPojo>lambdaQuery().eq(TbMvInfoPojo::getTitle, request.getTitle())))
                                   .orElse(new TbMvInfoPojo(request.getId(),
                                           request.getTitle(),
                                           request.getDescription(),
                                           request.getPublishTime(),
                                           null,
                                           null));
        tbMvInfoService.save(one);
        
        // artist
        tbMvArtistService.remove(Wrappers.<TbMvArtistPojo>lambdaQuery().in(TbMvArtistPojo::getMvId, request.getId()));
        if (CollUtil.isNotEmpty(request.getArtistIds())) {
            List<TbMvArtistPojo> list = request.getArtistIds().parallelStream().map(aLong -> new TbMvArtistPojo(request.getId(), aLong)).toList();
            tbMvArtistService.saveBatch(list);
        }
        // upload pic
        if (StringUtils.isNotBlank(request.getPicTempPath())) {
            remoteStorePicService.saveOrUpdateMvPicFile(request.getId(), httpRequestConfig.getTempPathFile(request.getPicTempPath()));
        }
        // 视频信息 上传文件
        TbMvPojo entity = new TbMvPojo();
        if (StringUtils.isNotBlank(request.getMvTempPath())) {
            File file = httpRequestConfig.getTempPathFile(request.getMvTempPath());
            Long duration = Optional.ofNullable(request.getDuration()).orElse(VideoUtil.getVideoDuration(file));
            String path = remoteStorageService.uploadMvFile(file);
            entity.setMd5(DigestUtil.md5Hex(file));
            entity.setPath(path);
            entity.setMvId(one.getId());
            entity.setDuration(duration);
            entity.setUserId(UserUtil.getUser().getId());
            tbMvService.updateById(entity);
        }
        // tag
        Long id = request.getId();
        // 移除重新添加
        tagManagerService.removeLabelMv(id);
        tagManagerService.addMvGenreLabel(request.getId(), request.getTags());
    }
    
    public Page<MvPageRes> getMvPage(String title, List<String> artist, List<String> tags, String order, String orderBy, Long index, Long size) {
        // title
        List<TbMvInfoPojo> tbMvPojoList = tbMvInfoService.list(Wrappers.<TbMvInfoPojo>lambdaQuery()
                                                                       .like(StringUtils.isNotBlank(title), TbMvInfoPojo::getTitle, title));
        List<Long> mvIds = new LinkedList<>(tbMvPojoList.parallelStream().map(TbMvInfoPojo::getId).toList());
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
            Map<Long, List<TbTagPojo>> labelMvTag = tagManagerService.getMvTag(tags.iterator());
            mvIds.addAll(labelMvTag.keySet());
        }
        Page<TbMvInfoPojo> page = new Page<>(index, size);
        LambdaQueryWrapper<TbMvInfoPojo> tbMvPojoLambdaQueryWrapper = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(order) && StringUtils.isNotBlank(orderBy)) {
            boolean isAsc = StringUtils.equalsIgnoreCase(order, "asc");
            switch (orderBy) {
                case "publishTime" -> tbMvPojoLambdaQueryWrapper.orderBy(true, isAsc, TbMvInfoPojo::getPublishTime);
                case "updateTime" -> tbMvPojoLambdaQueryWrapper.orderBy(true, isAsc, TbMvInfoPojo::getUpdateTime);
                default -> tbMvPojoLambdaQueryWrapper.orderBy(true, isAsc, TbMvInfoPojo::getCreateTime);
            }
        }
        tbMvInfoService.page(page, tbMvPojoLambdaQueryWrapper.in(TbMvInfoPojo::getId, mvIds));
        Page<MvPageRes> mvPageResPage = new Page<>();
        LinkedList<MvPageRes> records = new LinkedList<>();
        BeanUtils.copyProperties(page, mvPageResPage);
        Map<Long, List<TbTagPojo>> labelMusicTag = tagManagerService.getMvTag(mvIds);
        Map<Long, String> mvPicUrl = remoteStorePicService.getMvPicUrl(mvIds);
        Map<Long, List<ArtistConvert>> mvArtistByMvIdsToMap = qukuApi.getMvArtistByMvIdToMap(mvIds);
        for (TbMvInfoPojo mvPojo : page.getRecords()) {
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
        remoteStorePicService.removeMvPicMiddleIds(ids);
        
        // remove mv tag
        tagManagerService.removeLabelMv(ids);
        
        // remove file
        remoteStorageService.removeMvStorageFiles(ids);
        
        // remove mv
        tbMvInfoService.removeBatchByIds(ids);
        
        // 不移除mv资源表tb_mv
    }
    
    public MvInfoRes getMvInfo(Long id) {
        TbMvInfoPojo mvPojo = tbMvInfoService.getById(id);
        
        MvInfoRes e = new MvInfoRes();
        BeanUtils.copyProperties(mvPojo, e);
        String mvPicUrl = remoteStorePicService.getMvPicUrl(mvPojo.getId());
        List<TbTagPojo> tbTagPojos = tagManagerService.getMvTag(e.getId());
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
        TbMvPojo one = tbMvService.getOne(Wrappers.<TbMvPojo>lambdaQuery().eq(TbMvPojo::getMvId, mvPojo.getId()));
        if (Objects.nonNull(one)) {
            String path = one.getPath();
            if (StringUtils.isNotBlank(path)) {
                e.setMvUrl(remoteStorageService.getMvAddressesNoRefresh(path));
            }
        }
        return e;
    }
    
    public void updateMvInfo(SaveMvReq request) {
        // tag
        Long id = request.getId();
        // 移除重新添加
        tagManagerService.removeLabelMv(id);
        tagManagerService.addMvGenreLabel(request.getId(), request.getTags());
        // artist
        tbMvArtistService.remove(Wrappers.<TbMvArtistPojo>lambdaQuery().in(TbMvArtistPojo::getMvId, request.getId()));
        if (CollUtil.isNotEmpty(request.getArtistIds())) {
            List<TbMvArtistPojo> list = request.getArtistIds().parallelStream().map(aLong -> new TbMvArtistPojo(request.getId(), aLong)).toList();
            tbMvArtistService.saveBatch(list);
        }
        
        tbMvInfoService.updateById(new TbMvInfoPojo(request.getId(), request.getTitle(), request.getDescription(), request.getPublishTime(), null, null));
    }
    
    public String updateMvFile(MultipartFile uploadFile, Long id) throws IOException {
        TbMvPojo mvPojo = tbMvService.getById(id);
        // 上传文件
        File dest = httpRequestConfig.getTempPathFile(LocalDateTime.now().getNano() + "-" + Objects.requireNonNull(uploadFile.getOriginalFilename()));
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
