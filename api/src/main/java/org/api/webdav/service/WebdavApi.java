package org.api.webdav.service;

import cn.hutool.core.collection.CollUtil;
import org.apache.commons.lang3.StringUtils;
import org.api.webdav.config.WebdavConfig;
import org.api.webdav.constant.WebdavCacheConstant;
import org.api.webdav.model.CollectTypeList;
import org.api.webdav.model.PlayListRes;
import org.api.webdav.utils.spring.WebdavResourceReturnStrategyUtil;
import org.core.common.constant.PlayListTypeConstant;
import org.core.mybatis.iservice.TbCollectService;
import org.core.mybatis.iservice.TbResourceService;
import org.core.mybatis.model.convert.ArtistConvert;
import org.core.mybatis.pojo.*;
import org.core.service.AccountService;
import org.core.service.PlayListService;
import org.core.service.QukuService;
import org.core.service.RemoteStorageService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service(WebdavConfig.WEBDAV + "WebdavApi")
public class WebdavApi {
    
    private final QukuService qukuService;
    
    private final PlayListService playListService;
    
    private final TbResourceService tbResourceService;
    
    private final TbCollectService tbCollectService;
    
    private final AccountService accountService;
    
    private final RemoteStorageService remoteStorageService;
    
    private final WebdavResourceReturnStrategyUtil resourceReturnStrategyUtil;
    
    public WebdavApi(TbCollectService tbCollectService, WebdavResourceReturnStrategyUtil resourceReturnStrategyUtil, PlayListService playListService, TbResourceService tbResourceService, RemoteStorageService remoteStorageService, AccountService accountService, QukuService qukuService) {
        this.tbCollectService = tbCollectService;
        this.resourceReturnStrategyUtil = resourceReturnStrategyUtil;
        this.playListService = playListService;
        this.tbResourceService = tbResourceService;
        this.remoteStorageService = remoteStorageService;
        this.accountService = accountService;
        this.qukuService = qukuService;
    }
    
    @Cacheable(value = WebdavCacheConstant.WEBDAV_COLLECT_TYPE_LIST, key = "#id")
    public CollectTypeList getUserPlayList(Long id) {
        List<TbCollectPojo> ordinaryCollect = tbCollectService.getUserCollect(id, PlayListTypeConstant.ORDINARY);
        List<TbCollectPojo> likeCollect = tbCollectService.getUserCollect(id, PlayListTypeConstant.LIKE);
        List<TbCollectPojo> recommendCollect = tbCollectService.getUserCollect(id, PlayListTypeConstant.RECOMMEND);
        CollectTypeList collectTypeList = new CollectTypeList();
        collectTypeList.setLikeCollect(likeCollect);
        collectTypeList.setRecommendCollect(recommendCollect);
        collectTypeList.setOrdinaryCollect(ordinaryCollect);
        return collectTypeList;
    }
    
    @Cacheable(value = WebdavCacheConstant.WEBDAV_PLAY_LIST, key = "#id")
    public List<PlayListRes> getPlayListMusic(Long id) {
        List<PlayListRes> res = new LinkedList<>();
        
        List<TbMusicPojo> playListAllMusic = playListService.getPlayListAllMusic(id);
        if (CollUtil.isEmpty(playListAllMusic)) {
            return Collections.emptyList();
        }
        
        List<Long> musicIds = playListAllMusic.parallelStream().map(TbMusicPojo::getId).toList();
        Map<Long, List<TbResourcePojo>> resourceList = tbResourceService.getResourceMap(musicIds);
        Map<Long, List<ArtistConvert>> artistByMusicIdToMap = qukuService.getArtistByMusicIdToMap(musicIds);
        for (TbMusicPojo likeMusicId : playListAllMusic) {
            PlayListRes e = new PlayListRes(likeMusicId);
            List<TbResourcePojo> tbResourcePoos = resourceList.get(likeMusicId.getId());
            if (CollUtil.isEmpty(tbResourcePoos)) {
                continue;
            }
            // 先过滤存储中不存在的音源，防止数据库中与文件不一致导致播放错误
            List<TbResourcePojo> list = tbResourcePoos.parallelStream()
                                                      .filter(tbResourcePojo -> StringUtils.isNotBlank(remoteStorageService.getMusicResourceUrl(
                                                              tbResourcePojo.getPath())))
                                                      .toList();
            if (CollUtil.isEmpty(list)) {
                continue;
            }
            TbResourcePojo tbResourcePojo = resourceReturnStrategyUtil.handleResource(list);
            List<ArtistConvert> artistConverts = artistByMusicIdToMap.get(likeMusicId.getId());
            if (CollUtil.isNotEmpty(artistConverts)) {
                List<String> artistNames = artistConverts.parallelStream().map(TbArtistPojo::getArtistName).toList();
                e.setArtistName(CollUtil.join(artistNames, "-"));
            }
            e.setRate(tbResourcePojo.getRate());
            e.setMd5(tbResourcePojo.getMd5());
            e.setPath(tbResourcePojo.getPath());
            e.setSize(tbResourcePojo.getSize());
            res.add(e);
        }
        return res;
    }
    
    @Cacheable(value = WebdavCacheConstant.WEBDAV_USER_POJO, key = "#userName")
    public SysUserPojo getUserByName(String userName) {
        return accountService.getUserOrSubAccount(userName);
    }
}
