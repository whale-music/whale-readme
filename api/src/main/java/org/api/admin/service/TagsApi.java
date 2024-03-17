package org.api.admin.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.api.admin.config.AdminConfig;
import org.api.admin.model.common.PageResCommon;
import org.api.admin.model.req.PageTagsReq;
import org.api.admin.model.res.PageTagsRes;
import org.core.common.constant.PicTypeConstant;
import org.core.common.constant.TargetTagConstant;
import org.core.mybatis.iservice.*;
import org.core.mybatis.pojo.*;
import org.core.service.RemoteStorePicService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service(AdminConfig.ADMIN + "TagsApi")
public class TagsApi {
    private final RemoteStorePicService remoteStorePicService;
    
    private final TbTagService tbTagService;
    
    private final TbMiddleTagService tbMiddleTagService;
    
    private final TbMusicService tbMusicService;
    
    private final TbAlbumService tbAlbumService;
    
    private final TbCollectService tbCollectService;
    
    private final TbMvInfoService tbMvInfoService;
    
    public PageResCommon<PageTagsRes> getPageTags(PageTagsReq req) {
        if (CollUtil.isEmpty(req.getType())) {
            return new PageResCommon<>();
        }
        Page<TbMiddleTagPojo> page = new Page<>(req.getPageIndex(), req.getPageNum());
        
        // 查询Tag
        List<Long> selectTagNameIds = tbTagService.listObjs(Wrappers.<TbTagPojo>lambdaQuery()
                                                                    .select(TbTagPojo::getId)
                                                                    .in(TbTagPojo::getTagName, req.getFilterTagContents()));
        if (CollUtil.isEmpty(selectTagNameIds) && CollUtil.isNotEmpty(req.getFilterTagContents())) {
            return new PageResCommon<>();
        }
        // 分组查询数据关联的tag
        List<Byte> typeList = req.getType().parallelStream().filter(Objects::nonNull).map(TargetTagConstant.keyMap::get).toList();
        LambdaQueryWrapper<TbMiddleTagPojo> wrapper = Wrappers.<TbMiddleTagPojo>lambdaQuery()
                                                              .select(TbMiddleTagPojo::getMiddleId, TbMiddleTagPojo::getType)
                                                              .groupBy(TbMiddleTagPojo::getMiddleId, TbMiddleTagPojo::getType)
                                                              .in(CollUtil.isNotEmpty(selectTagNameIds), TbMiddleTagPojo::getTagId, selectTagNameIds)
                                                              .in(CollUtil.isNotEmpty(typeList), TbMiddleTagPojo::getType, typeList);
        tbMiddleTagService.page(page, wrapper);
        List<TbMiddleTagPojo> middleTagList = page.getRecords();
        if (CollUtil.isEmpty(middleTagList)) {
            return new PageResCommon<>();
        }
        // 查询关联数据的tag信息
        Set<Long> middleIds = middleTagList.parallelStream().map(TbMiddleTagPojo::getMiddleId).collect(Collectors.toSet());
        LambdaQueryWrapper<TbMiddleTagPojo> middleTagWrapper = Wrappers.lambdaQuery();
        middleTagWrapper.in(TbMiddleTagPojo::getMiddleId, middleIds).in(TbMiddleTagPojo::getType, typeList);
        List<TbMiddleTagPojo> middleTagPojoList = tbMiddleTagService.list(middleTagWrapper);
        Map<Long, List<TbMiddleTagPojo>> middleIdKeyValueMiddleMaps = middleTagPojoList.parallelStream()
                                                                                       .collect(Collectors.toMap(TbMiddleTagPojo::getMiddleId,
                                                                                               ListUtil::toList,
                                                                                               (o1, o2) -> {
                                                                                                   o2.addAll(o1);
                                                                                                   return o2;
                                                                                               }));
        
        // 关联数据查询Tag信息
        Set<Long> tagIds = middleTagPojoList.parallelStream().map(TbMiddleTagPojo::getTagId).collect(Collectors.toSet());
        List<TbTagPojo> tagList = tbTagService.listByIds(tagIds);
        if (CollUtil.isEmpty(tagList)) {
            return new PageResCommon<>();
        }
        Map<Long, TbTagPojo> tagMaps = tagList.parallelStream().collect(Collectors.toMap(TbTagPojo::getId, s -> s));
        
        Map<Byte, ArrayList<Long>> middleTagTypeMap = middleTagPojoList.parallelStream()
                                                                       .collect(Collectors.toMap(TbMiddleTagPojo::getType,
                                                                               tbMiddleTagPojo -> ListUtil.toList(tbMiddleTagPojo.getMiddleId()),
                                                                               (o1, o2) -> {
                                                                                   o2.addAll(o1);
                                                                                   return o2;
                                                                               }));
        Map<Byte, Map<Long, String>> typeMap = new HashMap<>();
        // 填充tag类型对应的信息
        fillTypeData(middleTagTypeMap, typeMap);
        
        ArrayList<PageTagsRes> content = new ArrayList<>(middleIds.size());
        for (Long middleId : middleIds) {
            PageTagsRes e = new PageTagsRes();
            e.setId(middleId);
            
            // 如果是音乐，会同时包含tag 和 流派
            Set<String> types = new HashSet<>();
            List<String> tags = new ArrayList<>();
            List<String> genres = new ArrayList<>();
            List<TbMiddleTagPojo> middleTag = middleIdKeyValueMiddleMaps.get(middleId);
            for (TbMiddleTagPojo tbMiddleTagPojo : middleTag) {
                // Tag 关联类型
                Byte type = tbMiddleTagPojo.getType();
                types.add(TargetTagConstant.valueMap.get(type));
                
                // 填充Tag关联信息
                Map<Long, String> longStringMap = typeMap.get(type);
                e.setName(longStringMap.get(middleId));
                e.setPicUrl(remoteStorePicService.getPicUrl(middleId, PicTypeConstant.tagToPic.get(type)));
                
                // 填充Tag
                if (TargetTagConstant.TAG.contains(type)) {
                    tags.add(tagMaps.get(tbMiddleTagPojo.getTagId()).getTagName());
                }
                // 填充genre
                if (TargetTagConstant.GENERIC.contains(type)) {
                    genres.add(tagMaps.get(tbMiddleTagPojo.getTagId()).getTagName());
                }
                
            }
            e.setTagType(types);
            e.setGenres(genres);
            e.setTagContent(tags);
            content.add(e);
        }
        
        PageResCommon<PageTagsRes> res = new PageResCommon<>();
        res.setSize(page.getSize());
        res.setCurrent(page.getCurrent());
        res.setTotal(page.getTotal());
        res.setContent(content);
        return res;
    }
    
    private void fillTypeData(Map<Byte, ArrayList<Long>> middleTagTypeMap, Map<Byte, Map<Long, String>> typeMap) {
        // 音乐Tag
        List<Long> musicTagIds = middleTagTypeMap.get(TargetTagConstant.TARGET_MUSIC_TAG);
        if (CollUtil.isNotEmpty(musicTagIds)) {
            Map<Long, String> musicMaps = tbMusicService.listByIds(musicTagIds)
                                                        .parallelStream()
                                                        .collect(Collectors.toMap(TbMusicPojo::getId, TbMusicPojo::getMusicName));
            typeMap.put(TargetTagConstant.TARGET_MUSIC_TAG, musicMaps);
        }
        
        // 音乐流派
        List<Long> musicGenreIds = middleTagTypeMap.get(TargetTagConstant.TARGET_MUSIC_GENRE);
        if (CollUtil.isNotEmpty(musicGenreIds)) {
            Map<Long, String> musicMaps = tbMusicService.listByIds(musicGenreIds)
                                                        .parallelStream()
                                                        .collect(Collectors.toMap(TbMusicPojo::getId, TbMusicPojo::getMusicName));
            typeMap.put(TargetTagConstant.TARGET_MUSIC_GENRE, musicMaps);
        }
        
        // 专辑
        List<Long> albumIds = middleTagTypeMap.get(TargetTagConstant.TARGET_ALBUM_GENRE);
        if (CollUtil.isNotEmpty(albumIds)) {
            Map<Long, String> albumMaps = tbAlbumService.listByIds(albumIds)
                                                        .parallelStream()
                                                        .collect(Collectors.toMap(TbAlbumPojo::getId, TbAlbumPojo::getAlbumName));
            typeMap.put(TargetTagConstant.TARGET_ALBUM_GENRE, albumMaps);
        }
        
        // 歌单
        List<Long> collectIds = middleTagTypeMap.get(TargetTagConstant.TARGET_COLLECT_TAG);
        if (CollUtil.isNotEmpty(collectIds)) {
            Map<Long, String> collectMaps = tbCollectService.listByIds(collectIds)
                                                            .parallelStream()
                                                            .collect(Collectors.toMap(TbCollectPojo::getId, TbCollectPojo::getPlayListName));
            typeMap.put(TargetTagConstant.TARGET_COLLECT_TAG, collectMaps);
        }
        
        // MV
        ArrayList<Long> mvIds = middleTagTypeMap.get(TargetTagConstant.TARGET_MV_TAG);
        if (CollUtil.isNotEmpty(mvIds)) {
            Map<Long, String> mvMaps = tbMvInfoService.listByIds(mvIds)
                                                      .parallelStream()
                                                      .collect(Collectors.toMap(TbMvInfoPojo::getId, TbMvInfoPojo::getTitle));
            typeMap.put(TargetTagConstant.TARGET_MV_TAG, mvMaps);
        }
    }
}
