package org.api.webdav.service;

import cn.hutool.core.collection.CollUtil;
import org.api.webdav.config.WebdavConfig;
import org.api.webdav.model.CollectTypeList;
import org.api.webdav.model.PlayListRes;
import org.api.webdav.utils.spring.WebdavResourceReturnStrategyUtil;
import org.core.common.constant.PlayListTypeConstant;
import org.core.mybatis.iservice.TbCollectService;
import org.core.mybatis.iservice.TbResourceService;
import org.core.mybatis.pojo.TbCollectPojo;
import org.core.mybatis.pojo.TbMusicPojo;
import org.core.mybatis.pojo.TbResourcePojo;
import org.core.service.PlayListService;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service(WebdavConfig.WEBDAV + "WebdavApi")
public class WebdavApi {
    
    private final PlayListService playListService;
    
    private final TbResourceService tbResourceService;
    
    private final TbCollectService tbCollectService;
    
    private final WebdavResourceReturnStrategyUtil resourceReturnStrategyUtil;
    
    public static final String WEBDAV_COLLECT_TYPE_LIST = "webdav-collect-type-list";
    public static final String WEBDAV_PLAY_LIST = "webdav-play-list";
    
    public WebdavApi(TbCollectService tbCollectService, WebdavResourceReturnStrategyUtil resourceReturnStrategyUtil, PlayListService playListService, TbResourceService tbResourceService) {
        this.tbCollectService = tbCollectService;
        this.resourceReturnStrategyUtil = resourceReturnStrategyUtil;
        this.playListService = playListService;
        this.tbResourceService = tbResourceService;
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
        
        List<TbMusicPojo> playListAllMusic = playListService.getPlayListAllMusic(id);
        if (CollUtil.isEmpty(playListAllMusic)) {
            return Collections.emptyList();
        }
        
        List<Long> musicIds = playListAllMusic.parallelStream()
                                              .map(TbMusicPojo::getId)
                                              .toList();
        Map<Long, List<TbResourcePojo>> resourceList = tbResourceService.getResourceMap(musicIds);
        for (TbMusicPojo likeMusicId : playListAllMusic) {
            PlayListRes e = new PlayListRes();
            BeanUtils.copyProperties(likeMusicId, e);
            List<TbResourcePojo> tbResourcePoos = resourceList.get(likeMusicId.getId());
            
            if (CollUtil.isNotEmpty(tbResourcePoos)) {
                TbResourcePojo tbResourcePojo = resourceReturnStrategyUtil.handleResource(tbResourcePoos);
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
