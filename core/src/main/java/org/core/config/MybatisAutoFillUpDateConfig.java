package org.core.config;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.func.LambdaUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.github.benmanes.caffeine.cache.Cache;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.reflection.MetaObject;
import org.core.common.constant.UserCacheKeyFieldConstant;
import org.core.mybatis.pojo.SysUserPojo;
import org.core.mybatis.pojo.TbPicPojo;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Component
public class MybatisAutoFillUpDateConfig implements MetaObjectHandler {
    
    private final Cache<Long, TbPicPojo> picCache;
    
    private final CacheManager cacheManager;
    
    private final String createTimeField;
    
    private final String updateTimeField;
    
    private final String[] passwordFields;
    
    private final String[] userCacheKeys;
    
    private final String picIdField;
    
    public MybatisAutoFillUpDateConfig(Cache<Long, TbPicPojo> picCache, CacheManager cacheManager) {
        this.picCache = picCache;
        this.cacheManager = cacheManager;
        
        // 更新字段与保存字段
        this.updateTimeField = LambdaUtil.getFieldName(SysUserPojo::getUpdateTime);
        this.createTimeField = LambdaUtil.getFieldName(SysUserPojo::getCreateTime);
        
        // 密码字段
        this.passwordFields = new String[]{
                LambdaUtil.getFieldName(SysUserPojo::getPassword),
                LambdaUtil.getFieldName(SysUserPojo::getSubAccountPassword)
        };
        
        // 用户缓存key
        this.userCacheKeys = new String[]{
                UserCacheKeyFieldConstant.WEBDAV_USER_POJO,
                UserCacheKeyFieldConstant.SUBSONIC_USER_OR_SUB_ACCOUNT
        };
        // 封面数据ID
        this.picIdField = LambdaUtil.getFieldName(TbPicPojo::getId);
    }
    
    /**
     * 使用mp做添加操作时候，这个方法执行
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        // 设置属性值
        this.setFieldValByName(createTimeField, LocalDateTime.now(), metaObject);
        this.setFieldValByName(updateTimeField, LocalDateTime.now(), metaObject);
    }
    
    /**
     * 使用mp做修改操作时候，这个方法执行
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName(updateTimeField, LocalDateTime.now(), metaObject);
        
        // 封面图片表更新时自动删除缓存
        List<String> list = Arrays.asList(metaObject.getSetterNames());
        if (metaObject.getOriginalObject() instanceof TbPicPojo
                && CollUtil.contains(list, this.picIdField)
                && StringUtils.isNotBlank(String.valueOf(metaObject.getValue(this.picIdField)))
        ) {
            picCache.invalidate((Long) metaObject.getValue(this.picIdField));
        }
        
        // 用户更新密码，刷新用户缓存
        for (String field : this.passwordFields) {
            if (list.contains(field)) {
                for (String s : this.userCacheKeys) {
                    org.springframework.cache.Cache userOrSubAccount = cacheManager.getCache(s);
                    if (Objects.nonNull(userOrSubAccount)) {
                        userOrSubAccount.clear();
                    }
                }
            }
        }
    }
}
