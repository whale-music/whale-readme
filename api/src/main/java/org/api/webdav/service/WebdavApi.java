package org.api.webdav.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import org.api.webdav.config.WebdavConfig;
import org.api.webdav.model.CollectTypeList;
import org.api.webdav.model.PlayListRes;
import org.api.webdav.utils.spring.WebdavResourceReturnStrategyUtil;
import org.core.common.constant.PlayListTypeConstant;
import org.core.jpa.entity.TbMusicEntity;
import org.core.jpa.entity.TbResourceEntity;
import org.core.jpa.repository.TbMusicEntityRepository;
import org.core.mybatis.iservice.TbCollectMusicService;
import org.core.mybatis.iservice.TbCollectService;
import org.core.mybatis.pojo.TbCollectMusicPojo;
import org.core.mybatis.pojo.TbCollectPojo;
import org.core.service.AccountService;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service(WebdavConfig.WEBDAV + "WebdavApi")
public class WebdavApi {
    private final TbCollectMusicService collectMusicService;
    
    private final TbMusicEntityRepository tbMusicEntityRepository;
    
    private final TbCollectService tbCollectService;
    
    private final WebdavResourceReturnStrategyUtil resourceReturnStrategyUtil;
    
    public static final String WEBDAV_COLLECT_TYPE_LIST = "webdav-collect-type-list";
    public static final String WEBDAV_PLAY_LIST = "webdav-play-list";
    
    public WebdavApi(TbCollectMusicService collectMusicService, TbMusicEntityRepository tbMusicEntityRepository, TbCollectService tbCollectService, WebdavResourceReturnStrategyUtil resourceReturnStrategyUtil, AccountService accountService) {
        this.collectMusicService = collectMusicService;
        this.tbMusicEntityRepository = tbMusicEntityRepository;
        this.tbCollectService = tbCollectService;
        this.resourceReturnStrategyUtil = resourceReturnStrategyUtil;
    }
    
    @Cacheable(value = WEBDAV_COLLECT_TYPE_LIST, key = "#id")
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
    
    @Cacheable(value = WEBDAV_PLAY_LIST, key = "#id")
    public List<PlayListRes> getPlayListMusic(Long id) {
        List<PlayListRes> res = new LinkedList<>();
        List<TbCollectMusicPojo> collectIds = collectMusicService.getCollectIds(Collections.singleton(id));
        List<Long> allMusicIds = collectIds.parallelStream().map(TbCollectMusicPojo::getMusicId).toList();
        if (CollUtil.isEmpty(allMusicIds)) {
            return Collections.emptyList();
        }
        
        Set<TbMusicEntity> allMusicEntityList = tbMusicEntityRepository.findByIdIn(allMusicIds);
        Map<Long, TbMusicEntity> musicMaps = allMusicEntityList.parallelStream()
                                                               .collect(Collectors.toMap(TbMusicEntity::getId, tbMusicEntity -> tbMusicEntity));
        for (Long likeMusicId : allMusicIds) {
            PlayListRes e = new PlayListRes();
            TbMusicEntity tbMusicEntity = musicMaps.get(likeMusicId);
            BeanUtils.copyProperties(tbMusicEntity, e);
            
            if (CollUtil.isNotEmpty(tbMusicEntity.getTbResourcesById())) {
                TbResourceEntity tbResourcePojo = resourceReturnStrategyUtil.handleResourceEntity(ListUtil.toList(tbMusicEntity.getTbResourcesById()));
                e.setMd5(tbResourcePojo.getMd5());
                e.setPath(tbResourcePojo.getPath());
                e.setSize(tbResourcePojo.getSize());
                res.add(e);
            }
        }
        return res;
    }
    
    @CacheEvict(value = {WEBDAV_COLLECT_TYPE_LIST, WEBDAV_PLAY_LIST}, allEntries = true)
    public void refreshAllCache() {
        // refresh webdav all cache
    }
}
