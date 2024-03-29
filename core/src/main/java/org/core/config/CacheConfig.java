package org.core.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.core.model.PicMiddleTypeModel;
import org.core.model.UserLoginCacheModel;
import org.core.model.WebDavResource;
import org.core.mybatis.pojo.TbPicPojo;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class CacheConfig {
    
    /**
     * 配置缓存管理器, ${@link Cacheable}注解缓存管理器
     *
     * @return 缓存管理器
     */
    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(Caffeine.newBuilder()
                                         // 设置最后一次写入或访问后经过固定时间过期, 一天
                                         .expireAfterAccess(3, TimeUnit.HOURS)
                                         // 初始的缓存空间大小
                                         .initialCapacity(100)
                                         // 缓存的最大条数
                                         .maximumSize(1000));
        return cacheManager;
    }
    
    /**
     * 图片缓存
     * key long
     * value pic
     */
    @Bean
    public Cache<Long, TbPicPojo> caffeineCache() {
        return Caffeine.newBuilder()
                       // 设置最后一次写入或访问后经过固定时间过期
                       .expireAfterWrite(1, TimeUnit.DAYS)
                       // 初始的缓存空间大小
                       .initialCapacity(1024 * 1024)
                       // 缓存的最大条数
                       .maximumSize(1000_0).build();
    }
    
    /**
     * 图片关联数据, 例如: key: 音乐ID, 音乐类型
     * key: middle type
     * value: pic id
     */
    @Bean
    public Cache<PicMiddleTypeModel, Long> picMiddleCaffeineCache() {
        return Caffeine.newBuilder()
                       // 设置最后一次写入或访问后经过固定时间过期
                       .expireAfterWrite(1, TimeUnit.DAYS)
                       // 初始的缓存空间大小
                       .initialCapacity(1024 * 1024)
                       // 缓存的最大条数
                       .maximumSize(1000_0).build();
    }
    
    /**
     * 设置用户登录缓存
     */
    @Bean
    public Cache<String, UserLoginCacheModel> userCacheCaffeineCache() {
        return Caffeine.newBuilder()
                       // 设置最后一次写入或访问后经过固定时间过期
                       .expireAfterWrite(1, TimeUnit.DAYS)
                       // 初始的缓存空间大小
                       .initialCapacity(1024 * 1024)
                       // 缓存的最大条数
                       .maximumSize(1000_0).build();
    }
    
    /**
     * 设置Webdav Resource URL 缓存
     */
    @Bean
    public Cache<String, WebDavResource> userWebdavPlayListResourceCacheCaffeineCache() {
        return Caffeine.newBuilder()
                       // 设置最后一次写入或访问后经过固定时间过期
                       .expireAfterWrite(1, TimeUnit.DAYS)
                       // 初始的缓存空间大小
                       .initialCapacity(1024 * 1024)
                       // 缓存的最大条数
                       .maximumSize(1000_0).build();
    }
    
}