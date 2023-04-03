package org.core.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.RandomUtil;
import org.apache.commons.lang3.StringUtils;
import org.core.common.page.Page;
import org.core.common.reflection.ReflectionFieldName;
import org.core.iservice.CollectMusicService;
import org.core.iservice.CollectService;
import org.core.iservice.MusicService;
import org.core.pojo.CollectMusicPojo;
import org.core.pojo.CollectPojo;
import org.core.pojo.MusicPojo;
import org.core.service.PlayListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.*;
import java.util.stream.Collectors;

@Service("PlayListServiceImpl")
public class PlayListServiceImpl implements PlayListService {
    
    @Autowired
    private CollectService collectService;
    
    @Autowired
    private CollectMusicService collectMusicService;
    
    @Autowired
    private MusicService musicService;
    
    public Page<CollectPojo> getPlayList(CollectPojo collectPojo, Long current, Long size) {
        collectPojo = Optional.ofNullable(collectPojo).orElse(new CollectPojo());
        Pageable pageable = PageRequest.of(Math.toIntExact(current), Math.toIntExact(size));
        
        CollectPojo finalCollectPojo = collectPojo;
        Specification<CollectPojo> tbCollectPojoSpecification = (root, query, criteriaBuilder) -> {
            
            List<Predicate> list = new ArrayList<>();
            if (finalCollectPojo.getId() != null) {
                list.add(criteriaBuilder.equal(root.get(ReflectionFieldName.getFieldName(CollectPojo::getId)), finalCollectPojo.getId()));
            }
            if (StringUtils.isNotBlank(finalCollectPojo.getPlayListName())) {
                list.add(criteriaBuilder.equal(root.get(ReflectionFieldName.getFieldName(CollectPojo::getPlayListName)),
                        finalCollectPojo.getPlayListName()));
            }
            // 只查询普通歌单
            list.add(criteriaBuilder.equal(root.get(ReflectionFieldName.getFieldName(CollectPojo::getType)), "0"));
            
            Predicate[] arr = new Predicate[list.size()];
            return query.where(list.toArray(arr)).getRestriction();
        };
        Specification<CollectPojo> where = Specification.where(tbCollectPojoSpecification);
        return collectService.findAll(where, pageable);
    }
    
    
    public List<CollectPojo> randomPlayList(Long limit) {
        List<CollectPojo> collectPojos = new ArrayList<>();
        long count = collectService.count();
        if (count == 0) {
            return new ArrayList<>();
        }
        for (int i = 0; i < limit; i++) {
            long randomNum = RandomUtil.randomLong(count);
            Page<CollectPojo> playList = getPlayList(null, randomNum, 1L);
            collectPojos.addAll(playList.getRecords());
        }
        Set<CollectPojo> playerSet = new TreeSet<>(Comparator.comparing(CollectPojo::getId));
        playerSet.addAll(collectPojos);
        return new ArrayList<>(playerSet);
    }
    
    /**
     * 获取歌单下所有音乐
     *
     * @param id 歌单ID
     */
    public List<MusicPojo> getPlayListAllMusic(Long id) {
        List<CollectMusicPojo> list =   collectMusicService.listLambdaEq(CollectMusicPojo::getCollectId, id);
        if (CollUtil.isEmpty(list)) {
            return Collections.emptyList();
        }
        List<Long> musicIds = list.stream().map(CollectMusicPojo::getMusicId).collect(Collectors.toList());
        return musicService.listByIds(musicIds);
    }
    
}
