package org.core.oss.service;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.LFUCache;
import cn.hutool.cache.impl.TimedCache;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import org.apache.commons.collections4.IteratorUtils;
import org.apache.commons.lang3.StringUtils;
import org.core.common.properties.SaveConfig;
import org.core.mybatis.iservice.TbResourceService;
import org.core.mybatis.pojo.TbResourcePojo;
import org.core.oss.model.Resource;
import org.core.oss.service.impl.alist.enums.ResourceEnum;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public abstract class OSSServiceAbs {
    
    protected final Map<ResourceEnum, TimedCache<String, Resource>> cache = new EnumMap<>(ResourceEnum.class);
    protected final Map<ResourceEnum, TimedCache<String, String>> cacheMd5 = new EnumMap<>(ResourceEnum.class);
    // db 数据 key: path
    protected final LFUCache<String, TbResourcePojo> dbCache = CacheUtil.newLFUCache(5000);
    // 音乐地址创建缓存
    protected TimedCache<String, Resource> musicUrlTimedCache;
    protected TimedCache<String, Resource> picUrlTimedCache;
    protected TimedCache<String, Resource> mvUrlTimedCache;
    protected TimedCache<String, String> musicMd5TimedCache;
    protected TimedCache<String, String> picMd5TimedCache;
    protected TimedCache<String, String> mvMd5TimedCache;
    
    protected SaveConfig config;
    protected TbResourceService tbResourceService;
    
    protected OSSServiceAbs(SaveConfig config, TbResourceService tbResourceService) {
        this.config = config;
        this.tbResourceService = tbResourceService;
        
        long timeout = this.config.getBufferTime();
        musicUrlTimedCache = CacheUtil.newTimedCache(timeout);
        picUrlTimedCache = CacheUtil.newTimedCache(timeout);
        mvUrlTimedCache = CacheUtil.newTimedCache(timeout);
        musicMd5TimedCache = CacheUtil.newTimedCache(timeout);
        picMd5TimedCache = CacheUtil.newTimedCache(timeout);
        mvMd5TimedCache = CacheUtil.newTimedCache(timeout);
        
        int delay = 60 * 1000;
        musicUrlTimedCache.schedulePrune(delay);
        picUrlTimedCache.schedulePrune(delay);
        mvUrlTimedCache.schedulePrune(delay);
        
        cache.put(ResourceEnum.MUSIC, musicUrlTimedCache);
        cache.put(ResourceEnum.PIC, picUrlTimedCache);
        cache.put(ResourceEnum.MV, mvUrlTimedCache);
        
        cacheMd5.put(ResourceEnum.MUSIC, musicMd5TimedCache);
        cacheMd5.put(ResourceEnum.PIC, picMd5TimedCache);
        cacheMd5.put(ResourceEnum.MV, mvMd5TimedCache);
    }
    
    @NotNull
    protected static String getReName(String path, String newName) {
        Path parent = Path.of(path).getParent();
        String replace = StringUtils.replace(String.valueOf(parent), "\\", "/");
        return CharSequenceUtil.addSuffixIfNot(replace, "/") + newName;
    }
    
    protected static void conditionGetItemByPath(Collection<String> paths, TimedCache<String, Resource> cacheResources, Map<String, Resource> resourceMap) {
        if (CollUtil.isEmpty(paths)) {
            Iterator<Resource> iterator = cacheResources.iterator();
            resourceMap.putAll(IteratorUtils.toList(iterator)
                                            .parallelStream()
                                            .filter(Objects::nonNull)
                                            .collect(Collectors.toMap(Resource::getPath, resource -> resource)));
        } else {
            Set<Resource> set = paths.parallelStream().map(cacheResources::get).filter(Objects::nonNull).collect(Collectors.toSet());
            resourceMap.putAll(set.parallelStream().filter(Objects::nonNull).collect(Collectors.toMap(Resource::getPath, resource -> resource)));
        }
    }
    
    protected static void conditionGetItemByMd5(Collection<String> md5s, TimedCache<String, Resource> cacheResources, TimedCache<String, String> md5Mapping, Map<String, Resource> resourceMap) {
        if (CollUtil.isEmpty(md5s)) {
            Iterator<String> iterator = md5Mapping.iterator();
            if (CollUtil.isNotEmpty(iterator)) {
                Map<String, Resource> collect = IteratorUtils.toList(iterator).parallelStream()
                                                             .map(cacheResources::get)
                                                             .collect(Collectors.toMap(Resource::getPath, resource -> resource));
                resourceMap.putAll(collect);
            }
        } else {
            Set<String> paths = md5s.parallelStream().map(md5Mapping::get).filter(Objects::nonNull).collect(Collectors.toSet());
            if (CollUtil.isNotEmpty(paths)) {
                Set<Resource> set = paths.parallelStream().map(cacheResources::get).collect(Collectors.toSet());
                resourceMap.putAll(set.parallelStream().collect(Collectors.toMap(Resource::getPath, resource -> resource)));
            }
        }
    }
    
    @Nullable
    protected static String stringSlashConversion(String pathname, final String fileSeparator) {
        pathname = StringUtils.replace(pathname, "/", fileSeparator);
        pathname = StringUtils.replace(pathname, "\\", fileSeparator);
        return pathname;
    }
    
    protected void correspondingData(ResourceEnum type, Set<String> col) {
        switch (type) {
            case MV -> col.addAll(config.getMvSave());
            case PIC -> col.addAll(config.getImgSave());
            case MUSIC -> col.addAll(config.getObjectSave());
        }
    }
    
}
