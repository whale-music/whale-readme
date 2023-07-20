package org.core.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.core.model.MiddleTypeModel;
import org.core.mybatis.pojo.SysUserPojo;
import org.core.mybatis.pojo.TbPicPojo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class CacheConfig {
    
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
    
    @Bean
    public Cache<MiddleTypeModel, Long> picMiddleCaffeineCache() {
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
    public Cache<String, SysUserPojo> userCacheCaffeineCache() {
        return Caffeine.newBuilder()
                       // 设置最后一次写入或访问后经过固定时间过期
                       .expireAfterWrite(1, TimeUnit.DAYS)
                       // 初始的缓存空间大小
                       .initialCapacity(1024 * 1024)
                       // 缓存的最大条数
                       .maximumSize(1000_0).build();
    }
}