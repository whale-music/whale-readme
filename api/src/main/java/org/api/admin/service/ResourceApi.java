package org.api.admin.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.api.admin.config.AdminConfig;
import org.api.admin.model.common.LinkResourceCommon;
import org.api.admin.model.req.*;
import org.api.admin.model.res.*;
import org.api.admin.utils.FileTypeUtil;
import org.api.admin.utils.VideoUtil;
import org.core.common.constant.PicTypeConstant;
import org.core.common.exception.BaseException;
import org.core.common.result.ResultCode;
import org.core.config.HttpRequestConfig;
import org.core.mybatis.iservice.*;
import org.core.mybatis.pojo.*;
import org.core.oss.model.Resource;
import org.core.service.AccountService;
import org.core.service.RemoteStorageService;
import org.core.service.RemoteStorePicService;
import org.core.utils.AudioUtil;
import org.core.utils.ExceptionUtil;
import org.core.utils.UserUtil;
import org.jaudiotagger.audio.AudioFile;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service(AdminConfig.ADMIN + "ResourceApi")
@Slf4j
public class ResourceApi {
    public static final Object lock = new Object();
    
    private final TbResourceService tbResourceService;
    
    private final TbMusicService tbMusicService;
    
    private final TbMiddlePicService tbMiddlePicService;
    
    private final TbPicService tbPicService;
    
    private final TbArtistService tbArtistService;
    
    private final TbAlbumService tbAlbumService;
    
    private final TbMvService tbMvService;
    
    private final TbMvInfoService tbMvInfoService;
    
    private final AccountService accountService;
    
    private final TbCollectService tbCollectService;
    
    private final HttpRequestConfig httpRequestConfig;
    
    private final RemoteStorageService remoteStorageService;
    
    private final RemoteStorePicService remoteStorePicService;
    
    public ResourceApi(TbResourceService tbResourceService, TbMiddlePicService tbMiddlePicService, TbMusicService tbMusicService, TbPicService tbPicService, TbArtistService tbArtistService, TbAlbumService tbAlbumService, TbMvService tbMvService, AccountService accountService, TbCollectService tbCollectService, HttpRequestConfig httpRequestConfig, MusicFlowApi musicFlowApi, RemoteStorageService remoteStorageService, TbMvInfoService tbMvInfoService, RemoteStorePicService remoteStorePicService) {
        this.tbResourceService = tbResourceService;
        this.tbMiddlePicService = tbMiddlePicService;
        this.tbMusicService = tbMusicService;
        this.tbPicService = tbPicService;
        this.tbArtistService = tbArtistService;
        this.tbAlbumService = tbAlbumService;
        this.tbMvService = tbMvService;
        this.accountService = accountService;
        this.tbCollectService = tbCollectService;
        this.httpRequestConfig = httpRequestConfig;
        this.remoteStorageService = remoteStorageService;
        this.tbMvInfoService = tbMvInfoService;
        this.remoteStorePicService = remoteStorePicService;
    }
    
    private static void fullAudioResource(File file, int rate, String md5, String upload, TbResourcePojo entity, Long userId, String encodeType) {
        entity.setRate(rate);
        String level = AudioUtil.getLevel(rate);
        entity.setLevel(level);
        entity.setPath(upload);
        entity.setUserId(userId);
        entity.setEncodeType(encodeType);
        entity.setSize(FileUtil.size(file));
        entity.setMd5(md5);
    }
    
    public Collection<FilterTermsRes> getFilterType(String type) {
        boolean b = StringUtils.equalsIgnoreCase(type, "type");
        Set<Resource> resources = remoteStorageService.listResource(false);
        Stream<String> stream = resources.parallelStream().map(Resource::getFileExtension);
        if (Boolean.TRUE.equals(b)) {
            Map<String, Long> collect = stream.filter(Objects::nonNull).map(FileTypeUtil::getTypeCategorization).collect(Collectors.groupingBy(
                    // 分组的键是元素的映射值，即字符串的长度
                    s -> s,
                    // 计数每个分组的元素个数
                    Collectors.counting()
            ));
            return getFilterTermsRes(collect);
        } else {
            Map<String, Long> collect = stream.collect(Collectors.groupingBy(
                    // 分组的键是元素的映射值，即字符串的长度
                    s -> s,
                    // 计数每个分组的元素个数
                    Collectors.counting()
            ));
            return getFilterTermsRes(collect);
        }
    }
    
    @NotNull
    private Collection<FilterTermsRes> getFilterTermsRes(Map<String, Long> collect) {
        HashSet<FilterTermsRes> set = new HashSet<>();
        collect.forEach((s, aLong) -> {
            FilterTermsRes e = new FilterTermsRes();
            e.setName(s);
            e.setCount(aLong);
            e.setValue(true);
            set.add(e);
        });
        return set;
    }
    
    public List<ResourcePageRes> getResourcePage(ResourcePageReq req) {
        Set<Resource> resources = remoteStorageService.listResource(req.getRefresh());
        LinkedList<ResourcePageRes> res = new LinkedList<>();
        
        List<String> filter = req.getFilter();
        
        List<TbResourcePojo> resourceList = tbResourceService.list();
        Map<String, TbResourcePojo> resourceMap = resourceList.parallelStream()
                                                              .collect(Collectors.toMap(TbResourcePojo::getPath, tbResourcePojo -> tbResourcePojo));
        List<TbPicPojo> picList = tbPicService.list();
        Map<String, TbPicPojo> picMap = picList.parallelStream().collect(Collectors.toMap(TbPicPojo::getPath, tbPicPojo -> tbPicPojo));
        List<TbMvPojo> mvList = tbMvService.list();
        Map<String, TbMvPojo> mvMap = mvList.parallelStream().collect(Collectors.toMap(TbMvPojo::getPath, s -> s));
        for (Resource resource : resources) {
            String typeCategorization = FileTypeUtil.getTypeCategorization(resource.getFileExtension());
            // 过滤
            boolean b1 = CollUtil.isNotEmpty(filter)
                    && !CollUtil.contains(filter,
                    s -> StringUtils.equals(s,
                            Boolean.TRUE.equals(req.getFilterType()) ? typeCategorization : resource.getFileExtension()));
            if (b1
                    // 查询
                    || StringUtils.isNotBlank(req.getSelect()) && !StringUtils.equalsAnyIgnoreCase(resource.getName(), req.getSelect())
            ) {
                continue;
            }
            ResourcePageRes e = new ResourcePageRes();
            BeanUtils.copyProperties(resource, e);
            if (StringUtils.equals(typeCategorization, FileTypeUtil.IMAGE)) {
                TbPicPojo tbPicPojo = picMap.get(resource.getPath());
                if (Objects.isNull(tbPicPojo)) {
                    e.setStatus(false);
                } else {
                    e.setMd5(tbPicPojo.getMd5());
                    e.setStatus(true);
                }
            } else if (StringUtils.equals(typeCategorization, FileTypeUtil.VIDEO)) {
                TbMvPojo mvPojo = mvMap.get(resource.getPath());
                if (Objects.isNull(mvPojo)) {
                    e.setStatus(false);
                } else {
                    e.setMd5(mvPojo.getMd5());
                    e.setStatus(true);
                }
            } else if (StringUtils.equals(typeCategorization, FileTypeUtil.AUDIO)) {
                TbResourcePojo tbResourcePojo = resourceMap.get(resource.getPath());
                if (Objects.isNull(tbResourcePojo)) {
                    e.setStatus(false);
                } else {
                    e.setMd5(tbResourcePojo.getMd5());
                    e.setStatus(true);
                }
            }
            
            e.setType(typeCategorization);
            res.add(e);
        }
        String order = req.getOrder();
        boolean orderFlag = StringUtils.equals("asc", order);
        switch (req.getOrderBy()) {
            case "createTime" -> {
                Function<ResourcePageRes, Date> getCreationTime = ResourcePageRes::getCreationTime;
                if (res.parallelStream().map(getCreationTime).anyMatch(Objects::nonNull)) {
                    Comparator<ResourcePageRes> comparing = Comparator.comparing(getCreationTime);
                    CollUtil.sort(res, orderFlag ? comparing : comparing.reversed());
                }
            }
            case "updateTime" -> {
                Function<ResourcePageRes, Date> getModificationTime = ResourcePageRes::getModificationTime;
                if (res.parallelStream().map(getModificationTime).anyMatch(Objects::nonNull)) {
                    Comparator<ResourcePageRes> comparing = Comparator.comparing(getModificationTime);
                    CollUtil.sort(res, orderFlag ? comparing : comparing.reversed());
                }
            }
            case "size" -> {
                Function<ResourcePageRes, Long> getSize = ResourcePageRes::getSize;
                if (res.parallelStream().map(getSize).anyMatch(Objects::nonNull)) {
                    Comparator<ResourcePageRes> comparing = Comparator.comparing(getSize);
                    CollUtil.sort(res, orderFlag ? comparing : comparing.reversed());
                }
            }
            //  case "name"
            default -> {
                Function<ResourcePageRes, String> getName = ResourcePageRes::getName;
                if (res.parallelStream().map(getName).anyMatch(Objects::nonNull)) {
                    Comparator<ResourcePageRes> comparing = Comparator.comparing(getName);
                    CollUtil.sort(res, orderFlag ? comparing : comparing.reversed());
                }
            }
        }
        return res;
    }
    
    public ResourcePicInfoRes getPicResourceInfo(String path) {
        Resource resource = remoteStorageService.getPicResource(path);
        ResourcePicInfoRes res = new ResourcePicInfoRes();
        BeanUtils.copyProperties(resource, res);
        TbPicPojo picResourceByName = tbPicService.getPicResourceByPath(path);
        // artist album music playlist user userBackground mv
        if (Objects.nonNull(picResourceByName)) {
            res.setPicResource(picResourceByName);
            LinkedList<ResourcePicInfoRes.LinkData> linkData = new LinkedList<>();
            
            List<TbMiddlePicPojo> list = tbMiddlePicService.list(Wrappers.<TbMiddlePicPojo>lambdaQuery()
                                                                         .eq(TbMiddlePicPojo::getPicId, picResourceByName.getId()));
            for (TbMiddlePicPojo tbMiddlePicPojo : list) {
                ResourcePicInfoRes.LinkData e = new ResourcePicInfoRes.LinkData();
                Long middleId = tbMiddlePicPojo.getMiddleId();
                e.setId(middleId);
                e.setType(PicTypeConstant.PIC_TYPE_KEY.get(tbMiddlePicPojo.getType()));
                switch (tbMiddlePicPojo.getType()) {
                    case PicTypeConstant.MUSIC -> {
                        // music pic
                        TbMusicPojo byId = Optional.ofNullable(tbMusicService.getById(middleId)).orElse(new TbMusicPojo());
                        e.setName(byId.getMusicName());
                    }
                    case PicTypeConstant.ARTIST -> {
                        // artist pic
                        TbArtistPojo artistPojo = Optional.ofNullable(tbArtistService.getById(middleId)).orElse(new TbArtistPojo());
                        e.setName(artistPojo.getArtistName());
                    }
                    case PicTypeConstant.ALBUM -> {
                        // album pic
                        TbAlbumPojo albumPojo = Optional.ofNullable(tbAlbumService.getById(middleId)).orElse(new TbAlbumPojo());
                        e.setName(albumPojo.getAlbumName());
                    }
                    case PicTypeConstant.USER_BACKGROUND, PicTypeConstant.USER_AVATAR -> {
                        // userBackground pic
                        // userAvatar pic
                        SysUserPojo user = Optional.ofNullable(accountService.getById(middleId)).orElse(new SysUserPojo());
                        e.setName(user.getUsername());
                    }
                    case PicTypeConstant.PLAYLIST -> {
                        // playlist pic
                        TbCollectPojo collect = Optional.ofNullable(tbCollectService.getById(middleId)).orElse(new TbCollectPojo());
                        e.setName(collect.getPlayListName());
                    }
                    case PicTypeConstant.MV -> {
                        TbMvInfoPojo mvPojo = Optional.ofNullable(tbMvInfoService.getById(middleId)).orElse(new TbMvInfoPojo());
                        e.setName(mvPojo.getTitle());
                    }
                    default -> throw new BaseException(ResultCode.PARAM_IS_INVALID);
                }
                linkData.add(e);
            }
            res.setLinkData(linkData);
        }
        return res;
    }
    
    /**
     * 获取视频信息
     *
     * @param path 路径
     * @return Video信息
     */
    public ResourceVideoInfoRes getVideoResourceInfo(String path) {
        Resource resource = remoteStorageService.getMvResource(path);
        ResourceVideoInfoRes res = new ResourceVideoInfoRes();
        BeanUtils.copyProperties(resource, res);
        TbMvPojo mv = tbMvService.getMvByPath(path);
        ResourceVideoInfoRes.LinkData linkData = new ResourceVideoInfoRes.LinkData();
        if (Objects.nonNull(mv)) {
            ResourceVideoInfoRes.MvResource mvResource = new ResourceVideoInfoRes.MvResource();
            BeanUtils.copyProperties(mv, mvResource);
            mvResource.setFileExtension(FileUtil.getSuffix(mvResource.getPath()));
            res.setMvResource(mvResource);
            
            if (Objects.nonNull(mv.getMvId())) {
                TbMvInfoPojo byId = tbMvInfoService.getById(mv.getMvId());
                linkData.setId(byId.getId());
                linkData.setName(byId.getTitle());
            }
            linkData.setValue("");
            linkData.setType("video");
        }
        res.setLinkData(linkData);
        return res;
    }
    
    public List<AutocompleteMusicRes> getMusicAutocomplete(String music) {
        LambdaQueryWrapper<TbMusicPojo> like = Wrappers.<TbMusicPojo>lambdaQuery()
                                                       .like(StringUtils.isNotBlank(music), TbMusicPojo::getMusicName, music)
                                                       .or()
                                                       .like(StringUtils.isNotBlank(music), TbMusicPojo::getAliasName, music);
        Page<TbMusicPojo> page = tbMusicService.page(new Page<>(0, 10), like);
        if (CollUtil.isEmpty(page.getRecords())) {
            return Collections.emptyList();
        }
        return page.getRecords().parallelStream().map(tbMusicPojo -> {
            AutocompleteMusicRes autocompleteMusicRes = new AutocompleteMusicRes();
            autocompleteMusicRes.setLink(tbMusicPojo.getId());
            autocompleteMusicRes.setValue(tbMusicPojo.getMusicName());
            return autocompleteMusicRes;
        }).toList();
    }
    
    public List<AutocompleteMvRes> getMvAutocomplete(String name) {
        LambdaQueryWrapper<TbMvInfoPojo> like = Wrappers.<TbMvInfoPojo>lambdaQuery()
                                                        .like(StringUtils.isNotBlank(name), TbMvInfoPojo::getTitle, name);
        Page<TbMvInfoPojo> page = tbMvInfoService.page(new Page<>(0, 10), like);
        if (CollUtil.isEmpty(page.getRecords())) {
            return Collections.emptyList();
        }
        return page.getRecords().parallelStream().map(tbMusicPojo -> {
            AutocompleteMvRes autocompleteMusicRes = new AutocompleteMvRes();
            autocompleteMusicRes.setLink(tbMusicPojo.getId());
            autocompleteMusicRes.setValue(tbMusicPojo.getTitle());
            return autocompleteMusicRes;
        }).toList();
    }
    
    public List<AutocompletePicRes> getPicAutocomplete(final String name, final String type) {
        // artist album music playlist user userBackground mv
        final String music = "music";
        final String album = "album";
        final String playlist = "playlist";
        final String artist = "artist";
        final String userBackground = "userBackground";
        final String userAvatar = "userAvatar";
        switch (type) {
            case music -> {
                LambdaQueryWrapper<TbMusicPojo> like = Wrappers.<TbMusicPojo>lambdaQuery()
                                                               .like(StringUtils.isNotBlank(name), TbMusicPojo::getMusicName, name)
                                                               .or()
                                                               .like(StringUtils.isNotBlank(name), TbMusicPojo::getAliasName, name);
                Page<TbMusicPojo> page = tbMusicService.page(new Page<>(0, 10), like);
                if (CollUtil.isEmpty(page.getRecords())) {
                    return Collections.emptyList();
                }
                return page.getRecords().parallelStream().map(tbMusicPojo -> {
                    AutocompletePicRes autocompletePicRes = new AutocompletePicRes();
                    autocompletePicRes.setLink(tbMusicPojo.getId());
                    autocompletePicRes.setType(music);
                    autocompletePicRes.setValue(tbMusicPojo.getMusicName());
                    return autocompletePicRes;
                }).toList();
            }
            case album -> {
                LambdaQueryWrapper<TbAlbumPojo> albumWrapper = Wrappers.<TbAlbumPojo>lambdaQuery().like(TbAlbumPojo::getAlbumName, name);
                Page<TbAlbumPojo> albumPage = tbAlbumService.page(new Page<>(0, 10), albumWrapper);
                if (CollUtil.isEmpty(albumPage.getRecords())) {
                    return Collections.emptyList();
                }
                return albumPage.getRecords().parallelStream().map(tbAlbumPojo -> {
                    AutocompletePicRes autocompletePicRes = new AutocompletePicRes();
                    autocompletePicRes.setValue(tbAlbumPojo.getAlbumName());
                    autocompletePicRes.setLink(tbAlbumPojo.getId());
                    autocompletePicRes.setType(album);
                    return autocompletePicRes;
                }).toList();
            }
            case artist -> {
                LambdaQueryWrapper<TbArtistPojo> artistWrapper = Wrappers.<TbArtistPojo>lambdaQuery().like(TbArtistPojo::getArtistName, name);
                Page<TbArtistPojo> artistPage = tbArtistService.page(new Page<>(0, 10), artistWrapper);
                if (CollUtil.isEmpty(artistPage.getRecords())) {
                    return Collections.emptyList();
                }
                return artistPage.getRecords().parallelStream().map(tbAlbumPojo -> {
                    AutocompletePicRes autocompletePicRes = new AutocompletePicRes();
                    autocompletePicRes.setValue(tbAlbumPojo.getArtistName());
                    autocompletePicRes.setLink(tbAlbumPojo.getId());
                    autocompletePicRes.setType(artist);
                    return autocompletePicRes;
                }).toList();
            }
            case playlist -> {
                LambdaQueryWrapper<TbCollectPojo> playlistWrapper = Wrappers.<TbCollectPojo>lambdaQuery().like(TbCollectPojo::getPlayListName, name);
                Page<TbCollectPojo> playlistPage = tbCollectService.page(new Page<>(0, 10), playlistWrapper);
                if (CollUtil.isEmpty(playlistPage.getRecords())) {
                    return Collections.emptyList();
                }
                return playlistPage.getRecords().parallelStream().map(tbAlbumPojo -> {
                    AutocompletePicRes autocompletePicRes = new AutocompletePicRes();
                    autocompletePicRes.setValue(tbAlbumPojo.getPlayListName());
                    autocompletePicRes.setLink(tbAlbumPojo.getId());
                    autocompletePicRes.setType(playlist);
                    return autocompletePicRes;
                }).toList();
            }
            case userAvatar, userBackground -> {
                LambdaQueryWrapper<SysUserPojo> userWrapper = Wrappers.<SysUserPojo>lambdaQuery().like(SysUserPojo::getUsername, name);
                Page<SysUserPojo> userPage = accountService.page(new Page<>(0, 10), userWrapper);
                if (CollUtil.isEmpty(userPage.getRecords())) {
                    return Collections.emptyList();
                }
                return userPage.getRecords().parallelStream().map(tbAlbumPojo -> {
                    AutocompletePicRes autocompletePicRes = new AutocompletePicRes();
                    autocompletePicRes.setValue(tbAlbumPojo.getUsername());
                    autocompletePicRes.setLink(tbAlbumPojo.getId());
                    String s = StringUtils.equals(type, userAvatar) ? userAvatar : userBackground;
                    autocompletePicRes.setType(s);
                    return autocompletePicRes;
                }).toList();
            }
            default -> throw new BaseException(ResultCode.PARAM_IS_INVALID);
        }
    }
    
    @Transactional(rollbackFor = Exception.class)
    public void linkPicture(LinkPicResourceReq picResource) {
        synchronized (lock) {
            // 如果图片关联的数据了其他的图片，则无法关联
            for (LinkResourceCommon linkResourceCommon : picResource.getLinkList()) {
                long count = tbMiddlePicService.count(Wrappers.<TbMiddlePicPojo>lambdaQuery()
                                                              .ne(TbMiddlePicPojo::getPicId, picResource.getId())
                                                              .eq(TbMiddlePicPojo::getMiddleId, linkResourceCommon.getId())
                                                              .eq(TbMiddlePicPojo::getType,
                                                                      PicTypeConstant.PIC_TYPE_VALUE.get(linkResourceCommon.getType())));
                if (count > 0) {
                    throw new BaseException(ResultCode.LINK_PIC_ERROR.getCode(),
                            String.format(ResultCode.LINK_PIC_ERROR.getResultMsg(), linkResourceCommon.getId()));
                }
            }
            tbMiddlePicService.remove(Wrappers.<TbMiddlePicPojo>lambdaQuery()
                                              .eq(TbMiddlePicPojo::getPicId, picResource.getId()));
            if (CollUtil.isNotEmpty(picResource.getLinkList())) {
                final List<TbMiddlePicPojo> entityList = picResource.getLinkList().parallelStream().map(linkResourceCommon -> new TbMiddlePicPojo(null,
                        linkResourceCommon.getId(),
                        picResource.getId(),
                        PicTypeConstant.PIC_TYPE_VALUE.get(linkResourceCommon.getType()))).toList();
                tbMiddlePicService.saveBatch(entityList);
                TbPicPojo picPojo = tbPicService.getById(picResource.getId());
                int count = Optional.ofNullable(picPojo.getCount()).orElse(0) + entityList.size();
                picPojo.setCount(count);
                tbPicService.updateById(picPojo);
            }
        }
    }
    
    @Transactional(rollbackFor = Exception.class)
    public void linkVideo(LinkVideoResourceReq videoResourceReq) {
        // 检测mv id是否已经绑定， 已绑定则抛出
        long count = tbMvService.count(Wrappers.<TbMvPojo>lambdaQuery()
                                               .ne(TbMvPojo::getMvId, videoResourceReq.getMvId())
                                               .eq(TbMvPojo::getPath, videoResourceReq.getPath()));
        if (count > 0) {
            throw new BaseException(ResultCode.LINK_PIC_ERROR.getCode(),
                    String.format(ResultCode.LINK_PIC_ERROR.getResultMsg(), videoResourceReq.getMvId()));
        }
        // 如果没有ID则表示删除关联的path
        // 关联文件和MV的DB数据
        LambdaUpdateWrapper<TbMvPojo> update = Wrappers.<TbMvPojo>lambdaUpdate()
                                                       .eq(TbMvPojo::getId, videoResourceReq.getId())
                                                       .set(TbMvPojo::getMvId, videoResourceReq.getMvId());
        tbMvService.update(update);
    }
    
    public ResourceAudioInfoRes getAudioResourceInfo(String path) {
        Resource resource = remoteStorageService.getMusicResource(path);
        ResourceAudioInfoRes res = new ResourceAudioInfoRes();
        BeanUtils.copyProperties(resource, res);
        
        ResourceAudioInfoRes.LinkData linkData = new ResourceAudioInfoRes.LinkData();
        TbResourcePojo dbResource = tbResourceService.getResourceByPath(resource.getPath());
        if (Objects.nonNull(dbResource)) {
            res.setDbResource(dbResource);
            linkData.setId(dbResource.getMusicId());
            TbMusicPojo byId = tbMusicService.getById(dbResource.getMusicId());
            linkData.setName(Optional.ofNullable(byId).orElse(new TbMusicPojo()).getMusicName());
        }
        linkData.setType("music");
        linkData.setValue("");
        res.setLinkData(linkData);
        return res;
    }
    
    /**
     * <p>绑定</p>
     * <p>解除绑定</p>
     *
     * @param audioResourceReq
     */
    @Transactional(rollbackFor = Exception.class)
    public void linkAudio(LinkAudioResourceReq audioResourceReq) {
        ExceptionUtil.isNull(Objects.isNull(audioResourceReq.getId()), ResultCode.PLEASE_SYNCHRONIZE_THE_DATA_FIRST);
        // 关联数据必须是没有已关联的数据
        long count = tbResourceService.count(Wrappers.<TbResourcePojo>lambdaQuery()
                                                     .eq(TbResourcePojo::getMusicId, audioResourceReq.getMusicId())
                                                     .ne(TbResourcePojo::getId, audioResourceReq.getId()));
        if (count > 0) {
            throw new BaseException(ResultCode.LINK_AUDIO_ERROR.getCode(),
                    String.format(ResultCode.LINK_AUDIO_ERROR.getResultMsg(), audioResourceReq.getMusicId()));
        }
        // 更新
        LambdaUpdateWrapper<TbResourcePojo> set = Wrappers.<TbResourcePojo>lambdaUpdate()
                                                          .eq(TbResourcePojo::getId, audioResourceReq.getId())
                                                          .set(TbResourcePojo::getMusicId, audioResourceReq.getMusicId());
        tbResourceService.update(set);
    }
    
    @Transactional(rollbackFor = Exception.class)
    public void syncResource(SyncResourceReq picResource) throws IOException {
        String path = picResource.getPath();
        switch (picResource.getType()) {
            case "audio" -> {
                Resource musicResource = remoteStorageService.getMusicResource(path);
                File file = org.core.utils.http.HttpUtil.downloadResource(musicResource, httpRequestConfig);
                AudioFile audioInfo = AudioUtil.getAudioInfo(file);
                int rate = Math.toIntExact(audioInfo.getAudioHeader().getBitRateAsNumber()) * 1000;
                String md5 = DigestUtil.md5Hex(file);
                TbResourcePojo entity = tbResourceService.getOne(Wrappers.<TbResourcePojo>lambdaQuery().eq(TbResourcePojo::getMd5, md5));
                Long userId = UserUtil.getUser().getId();
                String encodeType = FileUtil.extName(file.getName());
                if (Objects.isNull(entity)) {
                    entity = new TbResourcePojo();
                    fullAudioResource(file, rate, md5, path, entity, userId, encodeType);
                    tbResourceService.save(entity);
                    log.info("文件保存到数据库{}", entity);
                } else {
                    fullAudioResource(file, rate, md5, path, entity, userId, encodeType);
                    tbResourceService.updateById(entity);
                    log.info("更新成功{}", entity);
                }
                FileUtil.del(file);
                log.info("music");
            }
            case "video" -> {
                Resource musicResource = remoteStorageService.getMvResource(path);
                File file = org.core.utils.http.HttpUtil.downloadResource(musicResource, httpRequestConfig);
                String md5 = DigestUtil.md5Hex(file);
                long videoDuration = VideoUtil.getVideoDuration(file);
                TbMvPojo one = tbMvService.getOne(Wrappers.<TbMvPojo>lambdaQuery().eq(TbMvPojo::getMd5, md5));
                
                Long userId = UserUtil.getUser().getId();
                if (Objects.isNull(one)) {
                    TbMvPojo entity = new TbMvPojo();
                    entity.setDuration(videoDuration);
                    entity.setMd5(md5);
                    entity.setPath(path);
                    entity.setMvId(null);
                    entity.setUserId(userId);
                    tbMvService.save(entity);
                } else {
                    one.setDuration(videoDuration);
                    one.setMd5(md5);
                    one.setPath(path);
                    one.setMvId(null);
                    one.setUserId(userId);
                    tbMvService.updateById(one);
                }
                FileUtil.del(file);
                log.info("video");
            }
            case "pic" -> {
                synchronized (lock) {
                    Resource musicResource = remoteStorageService.getPicResource(path);
                    File file = org.core.utils.http.HttpUtil.downloadResource(musicResource, httpRequestConfig);
                    String md5 = DigestUtil.md5Hex(file);
                    TbPicPojo one = tbPicService.getOne(Wrappers.<TbPicPojo>lambdaQuery().eq(TbPicPojo::getMd5, md5));
                    if (Objects.isNull(one)) {
                        TbPicPojo entity = new TbPicPojo();
                        entity.setCount(0);
                        entity.setMd5(md5);
                        entity.setPath(path);
                        tbPicService.save(entity);
                    } else {
                        int count = one.getCount();
                        count++;
                        one.setCount(count);
                        one.setMd5(md5);
                        one.setPath(path);
                        tbPicService.updateById(one);
                    }
                }
                log.info("pic");
            }
            default -> throw new BaseException(ResultCode.PARAM_IS_INVALID);
        }
    }
    
    public void cleanResource(CleanResourceReq cleanResource) {
        switch (cleanResource.getType()) {
            case "audio" -> {
                if (Boolean.TRUE.equals(cleanResource.getIsForceDelete())) {
                    LambdaQueryWrapper<TbResourcePojo> eq = Wrappers.<TbResourcePojo>lambdaQuery().eq(TbResourcePojo::getId, cleanResource.getId());
                    tbResourceService.remove(eq);
                } else {
                    LambdaUpdateWrapper<TbResourcePojo> set = Wrappers.<TbResourcePojo>lambdaUpdate()
                                                                      .eq(TbResourcePojo::getId, cleanResource.getId())
                                                                      .eq(TbResourcePojo::getMusicId, cleanResource.getMiddleId())
                                                                      .set(TbResourcePojo::getMusicId, null);
                    tbResourceService.update(set);
                }
            }
            case "video" -> {
                if (Boolean.TRUE.equals(cleanResource.getIsForceDelete())) {
                    tbMvService.remove(Wrappers.<TbMvPojo>lambdaUpdate()
                                               .eq(TbMvPojo::getId, cleanResource.getId())
                                               .eq(TbMvPojo::getMvId, cleanResource.getMiddleId()));
                } else {
                    tbMvService.update(Wrappers.<TbMvPojo>lambdaUpdate()
                                               .eq(TbMvPojo::getId, cleanResource.getId())
                                               .eq(TbMvPojo::getMvId, cleanResource.getMiddleId())
                                               .set(TbMvPojo::getMvId, null));
                }
            }
            case "pic" -> {
                if (Boolean.TRUE.equals(cleanResource.getIsForceDelete())) {
                    remoteStorePicService.removePicById(cleanResource.getId(), false);
                } else {
                    Byte aByte = PicTypeConstant.PIC_TYPE_VALUE.get(cleanResource.getPicType());
                    ExceptionUtil.isNull(Objects.isNull(aByte) || Objects.isNull(cleanResource.getMiddleId()), ResultCode.PARAM_IS_INVALID);
                    remoteStorePicService.removePicMiddle(cleanResource.getMiddleId(), aByte, false);
                }
            }
            default -> throw new BaseException(ResultCode.PARAM_IS_INVALID);
        }
    }
}
