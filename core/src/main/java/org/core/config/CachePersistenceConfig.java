package org.core.config;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.PrimitiveArrayUtil;
import cn.hutool.core.util.SerializeUtil;
import cn.hutool.json.JSONUtil;
import com.github.benmanes.caffeine.cache.Cache;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.core.model.UserLoginCacheModel;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

@Slf4j
@Component
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class CachePersistenceConfig implements DisposableBean {
    public static final String CACHE_SER = "cache.ser";
    private final Cache<String, UserLoginCacheModel> cacheStringUserLoginCacheModel;
    @Value("${cache.path}")
    private String cachePath;
    
    public CachePersistenceConfig(Cache<String, UserLoginCacheModel> cacheStringUserLoginCacheModel) {
        this.cacheStringUserLoginCacheModel = cacheStringUserLoginCacheModel;
    }
    
    /**
     * 持久化caffeine中的数据， 暂时只保存登录信息.
     * 目前因为使用SpringApplicationBuilder导致同一个类会初始5次。所以这个类会重复执行5次，且每次读取的缓存可能不会一致，所以需要对缓存进行合并操作
     */
    @Override
    public void destroy() {
        log.info("Start saving login cache");
        // 读取之前的缓存，防止spring boot多个容器中缓存不互通，导致缓存不一致
        Map<String, UserLoginCacheModel> stringUserLoginCacheModelMap = readLoginCache();
        // 加载当前缓存
        ConcurrentMap<String, UserLoginCacheModel> map = cacheStringUserLoginCacheModel.asMap();
        if (CollUtil.isNotEmpty(map)) {
            // 合并缓存
            map.putAll(stringUserLoginCacheModelMap);
            HashMap<String, UserLoginCacheModel> cacheStore = new HashMap<>(map);
            log.info("map cache: {}", JSONUtil.toJsonStr(map));
            // 序列化对象并保存到文件
            byte[] bytes = SerializeUtil.serialize(cacheStore);
            FileUtil.writeBytes(bytes, new File(cachePath, CACHE_SER));
        }
        log.info("Save cache succeeded");
    }
    
    @PostConstruct
    public void initLoginCache() {
        log.info("Initialize load cache");
        cacheStringUserLoginCacheModel.putAll(readLoginCache());
        log.info("load succeeded");
    }
    
    private Map<String, UserLoginCacheModel> readLoginCache() {
        // 从文件中读取并反序列化对象
        File file = new File(cachePath, CACHE_SER);
        if (!FileUtil.isFile(file)) {
            return Collections.emptyMap();
        }
        byte[] fileBytes = FileUtil.readBytes(file);
        if (PrimitiveArrayUtil.isNotEmpty(fileBytes)) {
            return SerializeUtil.deserialize(fileBytes);
        }
        return Collections.emptyMap();
    }
}
