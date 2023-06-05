package org.core.config;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.github.benmanes.caffeine.cache.Cache;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.reflection.MetaObject;
import org.core.pojo.TbPicPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
public class MybatisAutoFillUpDateConfig implements MetaObjectHandler {
    
    @Autowired
    private Cache<Long, TbPicPojo> picCache;
    
    /**
     * 使用mp做添加操作时候，这个方法执行
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        // 设置属性值
        this.setFieldValByName("createTime", LocalDateTime.now(), metaObject);
        this.setFieldValByName("updateTime", LocalDateTime.now(), metaObject);
    }
    
    /**
     * 使用mp做修改操作时候，这个方法执行
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("updateTime", LocalDateTime.now(), metaObject);
    
        // 更新自动删除缓存
        List<String> list = Arrays.asList(metaObject.getSetterNames());
        if (CollUtil.contains(list, "url") && StringUtils.isNotBlank(String.valueOf(metaObject.getValue("url")))) {
            picCache.invalidate(metaObject.getValue("id"));
        }
    }
}
