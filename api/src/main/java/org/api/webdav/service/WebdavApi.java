package org.api.webdav.service;

import cn.hutool.core.collection.CollUtil;
import org.api.common.service.QukuAPI;
import org.api.webdav.config.WebdavConfig;
import org.api.webdav.model.CollectTypeList;
import org.api.webdav.model.PlayListRes;
import org.core.config.PlayListTypeConfig;
import org.core.jpa.entity.TbMusicEntity;
import org.core.jpa.repository.TbMusicEntityRepository;
import org.core.mybatis.iservice.TbCollectMusicService;
import org.core.mybatis.iservice.TbCollectService;
import org.core.mybatis.iservice.TbResourceService;
import org.core.mybatis.model.convert.CollectConvert;
import org.core.mybatis.pojo.TbCollectMusicPojo;
import org.core.mybatis.pojo.TbCollectPojo;
import org.core.mybatis.pojo.TbResourcePojo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service(WebdavConfig.WEBDAV + "WebdavApi")
public class WebdavApi {
    @Autowired
    private TbCollectMusicService collectMusicService;
    
    @Autowired
    private QukuAPI qukuApi;
    
    @Autowired
    private TbMusicEntityRepository tbMusicEntityRepository;
    
    @Autowired
    private TbCollectService tbCollectService;
    
    @Autowired
    private TbResourceService tbResourceService;
    
    
    public CollectTypeList getUserPlayList(Long id) {
        List<TbCollectPojo> ordinaryCollect = tbCollectService.getUserCollect(id, PlayListTypeConfig.ORDINARY);
        List<TbCollectPojo> likeCollect = tbCollectService.getUserCollect(id, PlayListTypeConfig.LIKE);
        List<TbCollectPojo> recommendCollect = tbCollectService.getUserCollect(id, PlayListTypeConfig.RECOMMEND);
        CollectTypeList collectTypeList = new CollectTypeList();
        collectTypeList.setLikeCollect(likeCollect);
        collectTypeList.setRecommendCollect(recommendCollect);
        collectTypeList.setOrdinaryCollect(ordinaryCollect);
        return collectTypeList;
    }
    
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
        Map<Long, List<TbResourcePojo>> resourceListMap = tbResourceService.getResourceList(allMusicIds);
        for (Long likeMusicId : allMusicIds) {
            PlayListRes e = new PlayListRes();
            TbMusicEntity tbMusicEntity = musicMaps.get(likeMusicId);
            BeanUtils.copyProperties(tbMusicEntity, e);
            
            List<TbResourcePojo> tbResourcePojos = resourceListMap.get(likeMusicId);
            if (CollUtil.isNotEmpty(tbResourcePojos)) {
                TbResourcePojo tbResourcePojo = tbResourcePojos.get(0);
                e.setMd5(tbResourcePojo.getMd5());
                e.setPath(tbResourcePojo.getPath());
                e.setSize(tbResourcePojo.getSize());
                e.setResourceList(tbResourcePojos);
                res.add(e);
            }
        }
        return res;
    }
    
    private List<Long> getPlayListMusicIds(Long id, byte type) {
        List<CollectConvert> likeUserPlayList = qukuApi.getUserPlayList(id, Collections.singleton(type));
        if (CollUtil.isEmpty(likeUserPlayList)) {
            return Collections.emptyList();
        }
        List<Long> likeUserIds = likeUserPlayList.parallelStream().map(TbCollectPojo::getId).toList();
        List<TbCollectMusicPojo> likeCollectPojoList = collectMusicService.getCollectIds(likeUserIds);
        return likeCollectPojoList.parallelStream().map(TbCollectMusicPojo::getMusicId).toList();
    }
}
