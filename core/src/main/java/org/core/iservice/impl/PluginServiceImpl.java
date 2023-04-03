package org.core.iservice.impl;

import org.core.common.service.ServiceImpl;
import org.core.iservice.PluginService;
import org.core.pojo.PluginPojo;
import org.core.repository.PluginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 插件表 服务实现类
 * </p>
 *
 * @author Sakura
 * @since 2023-04-02
 */
@Service
public class PluginServiceImpl extends ServiceImpl<PluginRepository, PluginPojo> implements PluginService {
    
    /**
     * @param baseJpaRepository
     */
    @Autowired
    @Override
    public void setBaseJpaRepository(PluginRepository baseJpaRepository) {
        super.setBaseJpaRepository(baseJpaRepository);
    }

}
