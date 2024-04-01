package org.core.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.map.DefaultedMap;
import org.apache.commons.lang3.StringUtils;
import org.core.model.TagMiddleTypeModel;
import org.core.mybatis.iservice.TbMiddleTagService;
import org.core.mybatis.iservice.TbTagService;
import org.core.mybatis.pojo.TbMiddleTagPojo;
import org.core.mybatis.pojo.TbTagPojo;
import org.core.service.TagManagerService;
import org.core.utils.CollectUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service("TagManagerServiceImpl")
public class TagManagerServiceImpl implements TagManagerService {
    public static final Object lock = new Object();
    
    private final TbMiddleTagService middleTagService;
    
    private final TbTagService tagService;
    
    /**
     * 获取tag
     *
     * @param type tag类型 0流派 1歌曲 2歌单
     * @param ids  歌单，音乐，专辑
     * @return tag列表
     */
    @Override
    public Map<Long, List<TbTagPojo>> getTag(Byte type, Collection<Long> ids) {
        if (CollectUtil.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        LambdaQueryWrapper<TbMiddleTagPojo> wrapper = Wrappers.<TbMiddleTagPojo>lambdaQuery()
                                                              .in(TbMiddleTagPojo::getMiddleId, ids)
                                                              .eq(TbMiddleTagPojo::getType, type);
        List<TbMiddleTagPojo> middleTagList = middleTagService.list(wrapper);
        if (CollUtil.isEmpty(middleTagList)) {
            return Collections.emptyMap();
        }
        List<Long> tagIds = middleTagList.parallelStream().map(TbMiddleTagPojo::getTagId).toList();
        List<TbTagPojo> tagList = tagService.list(Wrappers.<TbTagPojo>lambdaQuery()
                                                          .in(TbTagPojo::getId, tagIds));
        Map<Long, TbTagPojo> collect = tagList.parallelStream()
                                              .collect(Collectors.toMap(TbTagPojo::getId, o -> o));
        Map<Long, List<TbTagPojo>> resultMap = middleTagList.parallelStream()
                                                            .collect(Collectors.toMap(TbMiddleTagPojo::getMiddleId,
                                                                    tbTagPojo ->
                                                                            ListUtil.toList(collect.get(tbTagPojo.getTagId()))
                                                                    ,
                                                                    (tbTagPojos1, tbTagPojos2) -> {
                                                                        tbTagPojos2.addAll(tbTagPojos1);
                                                                        return tbTagPojos2;
                                                                    }));
        return DefaultedMap.defaultedMap(resultMap, Collections.emptyList());
    }
    
    /**
     * 根据tag名获取 Tag
     *
     * @param type tag类型
     * @param tags tag名
     * @return tag列表
     */
    @Override
    public Map<Long, List<TbTagPojo>> getTag(Byte type, Iterator<String> tags) {
        if (CollectUtil.isEmpty(tags)) {
            return Collections.emptyMap();
        }
        List<TbTagPojo> tagList = tagService.list(Wrappers.<TbTagPojo>lambdaQuery().in(TbTagPojo::getTagName, tags));
        if (CollectUtil.isEmpty(tagList)) {
            return Collections.emptyMap();
        }
        List<Long> tagIds = tagList.parallelStream().map(TbTagPojo::getId).toList();
        LambdaQueryWrapper<TbMiddleTagPojo> wrapper = Wrappers.<TbMiddleTagPojo>lambdaQuery()
                                                              .in(TbMiddleTagPojo::getTagId, tagIds)
                                                              .eq(TbMiddleTagPojo::getType, type);
        List<TbMiddleTagPojo> middleTagList = middleTagService.list(wrapper);
        if (CollUtil.isEmpty(middleTagList)) {
            return Collections.emptyMap();
        }
        Map<Long, TbTagPojo> collect = tagList.parallelStream().collect(Collectors.toMap(TbTagPojo::getId, o -> o));
        Map<Long, List<TbTagPojo>> resultMap = middleTagList.parallelStream()
                                                            .collect(Collectors.toMap(TbMiddleTagPojo::getMiddleId,
                                                                    middleTagPojo -> ListUtil.toList(collect.get(middleTagPojo.getTagId())),
                                                                    (tbTagPojos1, tbTagPojos2) -> {
                                                                        tbTagPojos2.addAll(tbTagPojos1);
                                                                        return tbTagPojos2;
                                                                    }));
        return DefaultedMap.defaultedMap(resultMap, Collections.emptyList());
    }
    
    /**
     * 批量添加tag
     *
     * @param target 指定歌单tag，或者音乐tag，音乐流派 0流派 1歌曲 2歌单
     * @param id     歌单或歌曲前ID
     * @param labels 标签名
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addTag(Byte target, Long id, List<String> labels) {
        // 新增tag
        synchronized (lock) {
            Set<Long> tagIds = null;
            if (CollUtil.isNotEmpty(labels)) {
                List<TbTagPojo> list = tagService.list(Wrappers.<TbTagPojo>lambdaQuery().in(TbTagPojo::getTagName, labels));
                Map<String, TbTagPojo> collect = list.parallelStream().collect(Collectors.toMap(TbTagPojo::getTagName, tbTagPojo -> tbTagPojo));
                for (String label : labels) {
                    if (StringUtils.isNotBlank(label) && collect.get(label) == null) {
                        TbTagPojo entity = new TbTagPojo();
                        entity.setCount(0);
                        entity.setTagName(label);
                        list.add(entity);
                    }
                }
                tagService.saveOrUpdateBatch(list);
                tagIds = list.parallelStream().map(TbTagPojo::getId).collect(Collectors.toSet());
            }
            // 关联到对应ID
            this.addTag(target, id, tagIds);
        }
    }
    
    /**
     * 批量添加tag
     *
     * @param target 指定歌单tag，或者音乐tag，音乐流派 0流派 1歌曲 2歌单
     * @param id     歌单或歌曲前ID
     * @param tagIds 标签ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addTag(Byte target, Long id, Set<Long> tagIds) {
        LambdaQueryWrapper<TbMiddleTagPojo> eq = Wrappers.<TbMiddleTagPojo>lambdaQuery()
                                                         .eq(TbMiddleTagPojo::getMiddleId, id)
                                                         .eq(TbMiddleTagPojo::getType, target);
        // 删除重新添加
        List<TbMiddleTagPojo> middleTagPojoList = middleTagService.list(eq);
        if (CollUtil.isNotEmpty(middleTagPojoList)) {
            List<Long> removeTagIds = new ArrayList<>();
            List<Long> middleTagIds = middleTagPojoList.parallelStream().map(TbMiddleTagPojo::getId).toList();
            // 删除关联tag后，tag标签自动减一。并且到零时自动删除
            for (TbTagPojo tbTagPojo : tagService.listByIds(middleTagIds)) {
                tbTagPojo.setCount(tbTagPojo.getCount() - 1);
                if (tbTagPojo.getCount() == 0) {
                    removeTagIds.add(tbTagPojo.getId());
                }
            }
            if (CollUtil.isNotEmpty(middleTagIds)) {
                middleTagService.removeBatchByIds(middleTagIds);
            }
            if (CollUtil.isNotEmpty(removeTagIds)) {
                tagService.removeBatchByIds(removeTagIds);
            }
        }
        
        if (CollUtil.isNotEmpty(tagIds)) {
            // 添加tag关联
            List<TbMiddleTagPojo> middleTagPojos = tagIds.parallelStream().map(aLong -> new TbMiddleTagPojo(null, id, aLong, target)).toList();
            middleTagService.saveOrUpdateBatch(middleTagPojos);
            
            // 关联后tag关联数自动加1
            List<TbTagPojo> tagPojoList = tagService.listByIds(tagIds);
            for (TbTagPojo tbTagPojo : tagPojoList) {
                tbTagPojo.setCount(tbTagPojo.getCount() + 1);
            }
            tagService.updateBatchById(tagPojoList);
        }
        
    }
    
    /**
     * 根据类型ID, 删除tag
     *
     * @param list tag 数据
     */
    @Override
    public void removeLabel(Collection<TagMiddleTypeModel> list) {
        synchronized (lock) {
            LambdaQueryWrapper<TbMiddleTagPojo> eq = Wrappers.lambdaQuery();
            for (TagMiddleTypeModel middleTypeModel : list) {
                eq.in(TbMiddleTagPojo::getMiddleId, middleTypeModel.getMiddleId()).in(TbMiddleTagPojo::getType, middleTypeModel.getType());
            }
            // 查询出所有tag关联数据
            List<TbMiddleTagPojo> tbMiddleTagPojoList = middleTagService.list(eq);
            if (CollUtil.isEmpty(tbMiddleTagPojoList)) {
                return;
            }
            // 根据相同tag分组, 然后查询出对于的tag表, 然后根据每个要删除的数量进行计算, 等于或小于0时删除tag
            voteToRemoveTag(tbMiddleTagPojoList);
        }
    }
    
    
    /**
     * 删除歌单或音乐中的tag
     *
     * @param target       指定歌单tag，或者音乐tag，音乐流派 0流派 1歌曲 2歌单
     * @param id           歌单或歌曲前ID
     * @param labelBatchId 需要删除的label ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeLabelById(Byte target, Long id, Collection<Long> labelBatchId) {
        synchronized (lock) {
            LambdaQueryWrapper<TbMiddleTagPojo> eq = Wrappers.<TbMiddleTagPojo>lambdaQuery()
                                                             .eq(TbMiddleTagPojo::getType, target)
                                                             .eq(TbMiddleTagPojo::getMiddleId, id)
                                                             .in(TbMiddleTagPojo::getTagId, labelBatchId);
            List<TbMiddleTagPojo> list = middleTagService.list(eq);
            if (CollUtil.isEmpty(list)) {
                return;
            }
            voteToRemoveTag(list);
        }
    }
    
    /**
     * 删除歌单或音乐中的tag
     *
     * @param target         指定歌单tag，或者音乐tag，音乐流派 0流派 1歌曲 2歌单
     * @param id             歌单或歌曲前ID
     * @param labelBatchName 需要删除的label ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeLabelByName(Byte target, Long id, Collection<String> labelBatchName) {
        LambdaQueryWrapper<TbTagPojo> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.in(TbTagPojo::getTagName, labelBatchName);
        List<TbTagPojo> list = tagService.list(queryWrapper);
        Set<Long> collect = list.parallelStream().map(TbTagPojo::getId).collect(Collectors.toSet());
        removeLabelById(target, id, collect);
    }
    
    
    /**
     * 删除关联tag数据，根据投票制
     * 如果关联数据为0时，删除tag
     *
     * @param tbMiddleTagPojoList 关联数据
     */
    private void voteToRemoveTag(List<TbMiddleTagPojo> tbMiddleTagPojoList) {
        Map<Long, Integer> map = tbMiddleTagPojoList.parallelStream()
                                                    .collect(Collectors.toMap(TbMiddleTagPojo::getTagId,
                                                            tbMiddleTagPojo -> 1,
                                                            Integer::sum));
        List<TbTagPojo> tagList = tagService.listByIds(map.keySet());
        ArrayList<Long> tagIds = new ArrayList<>();
        for (TbTagPojo tbTagPojo : tagList) {
            Integer integer = map.get(tbTagPojo.getId());
            int count = tbTagPojo.getCount() - integer;
            // 关联tag数量小于等于0时删除
            if (count <= 0) {
                tagIds.add(tbTagPojo.getId());
            }
        }
        middleTagService.removeBatchByIds(tbMiddleTagPojoList);
        if (CollUtil.isNotEmpty(tagIds)) {
            tagService.removeByIds(tagIds);
        }
    }
}
