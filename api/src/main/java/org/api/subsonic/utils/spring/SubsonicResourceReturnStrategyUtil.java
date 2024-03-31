package org.api.subsonic.utils.spring;

import cn.hutool.core.collection.CollUtil;
import org.api.subsonic.config.SubsonicResourceReturnStrategyConfig;
import org.core.mybatis.pojo.TbResourcePojo;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Component
public class SubsonicResourceReturnStrategyUtil {
    
    private final SubsonicResourceReturnStrategyConfig resourceReturnStrategyConfig;
    
    public SubsonicResourceReturnStrategyUtil(SubsonicResourceReturnStrategyConfig resourceReturnStrategyConfig) {
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
            case FIRST -> {
                return Optional.ofNullable(resources.get(0)).orElse(new TbResourcePojo());
            }
            case LAST -> {
                return Optional.ofNullable(resources.get(resources.size() - 1)).orElse(new TbResourcePojo());
                
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
            default -> {
                return Optional.ofNullable(resources.get(0)).orElse(new TbResourcePojo());
            }
        }
    }
}
