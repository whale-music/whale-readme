package org.core.oss.service;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.LFUCache;
import cn.hutool.cache.impl.TimedCache;
import org.core.mybatis.pojo.TbResourcePojo;
import org.core.oss.model.Resource;
import org.core.oss.service.impl.alist.enums.ResourceEnum;

import java.util.EnumMap;
import java.util.Map;

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
}
