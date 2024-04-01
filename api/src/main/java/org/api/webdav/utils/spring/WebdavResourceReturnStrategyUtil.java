package org.api.webdav.utils.spring;

import cn.hutool.core.collection.CollUtil;
import org.api.webdav.config.WebdavResourceReturnStrategyConfig;
import org.core.jpa.entity.TbResourceEntity;
import org.core.mybatis.pojo.TbResourcePojo;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Component
public class WebdavResourceReturnStrategyUtil {
    
    private final WebdavResourceReturnStrategyConfig resourceReturnStrategyConfig;
    
    public WebdavResourceReturnStrategyUtil(WebdavResourceReturnStrategyConfig resourceReturnStrategyConfig) {
        this.resourceReturnStrategyConfig = resourceReturnStrategyConfig;
    }
    
    /**
     * 根据配置选择返回资源文件
     *
     * @param resources 资源文件
     * @return 文件数据
     */
    public TbResourcePojo handleResource(List<TbResourcePojo> resources) {
        if (CollUtil.isEmpty(resources)) {
            return null;
        }
        if (resources.size() == 1) {
            return resources.getFirst();
        }
        switch (resourceReturnStrategyConfig.getResource().getReturnPlan()) {
            case LAST -> {
                return Optional.ofNullable(resources.getLast()).orElse(new TbResourcePojo());
            }
            case BITRATEMAX -> {
                return resources.parallelStream().max(Comparator.comparingInt(TbResourcePojo::getRate)).orElse(new TbResourcePojo());
            }
            case BITRATEMIN -> {
                return resources.parallelStream().min(Comparator.comparingInt(TbResourcePojo::getRate)).orElse(new TbResourcePojo());
            }
            case SIZEMAX -> {
                return resources.parallelStream().max(Comparator.comparingLong(TbResourcePojo::getSize)).orElse(new TbResourcePojo());
            }
            case SIZEMIN -> {
                return resources.parallelStream().min(Comparator.comparingLong(TbResourcePojo::getSize)).orElse(new TbResourcePojo());
            }
            // case FIRST -> {
            //     return Optional.ofNullable(resources.get(0)).orElse(new TbResourcePojo());
            // }
            default -> {
                return Optional.ofNullable(resources.getFirst()).orElse(new TbResourcePojo());
            }
        }
    }
    
    /**
     * 根据配置选择返回资源文件
     *
     * @param resources 资源文件
     * @return 文件数据
     */
    public TbResourceEntity handleResourceEntity(List<TbResourceEntity> resources) {
        if (CollUtil.isEmpty(resources)) {
            return null;
        }
        if (resources.size() == 1) {
            return resources.getFirst();
        }
        switch (resourceReturnStrategyConfig.getResource().getReturnPlan()) {
            case LAST -> {
                return Optional.ofNullable(resources.getLast()).orElse(new TbResourceEntity());
            }
            case BITRATEMAX -> {
                return resources.parallelStream().max(Comparator.comparingInt(TbResourceEntity::getRate)).orElse(new TbResourceEntity());
            }
            case BITRATEMIN -> {
                return resources.parallelStream().min(Comparator.comparingInt(TbResourceEntity::getRate)).orElse(new TbResourceEntity());
            }
            case SIZEMAX -> {
                return resources.parallelStream().max(Comparator.comparingLong(TbResourceEntity::getSize)).orElse(new TbResourceEntity());
            }
            case SIZEMIN -> {
                return resources.parallelStream().min(Comparator.comparingLong(TbResourceEntity::getSize)).orElse(new TbResourceEntity());
            }
            // case FIRST -> {
            //     return Optional.ofNullable(resources.get(0)).orElse(new TbResourceEntity());
            // }
            default -> {
                return Optional.ofNullable(resources.getFirst()).orElse(new TbResourceEntity());
            }
        }
    }
}
