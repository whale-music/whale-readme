package org.core.iservice.impl;

import org.core.common.service.ServiceImpl;
import org.core.iservice.PluginTaskService;
import org.core.pojo.PluginTaskPojo;
import org.core.repository.PluginTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 插件任务表 服务实现类
 * </p>
 *
 * @author Sakura
 * @since 2023-04-02
 */
@Service
public class PluginTaskServiceImpl extends ServiceImpl<PluginTaskRepository, PluginTaskPojo> implements PluginTaskService {
    
    /**
     * @param baseJpaRepository
     */
    @Autowired
    @Override
    public void setBaseJpaRepository(PluginTaskRepository baseJpaRepository) {
        super.setBaseJpaRepository(baseJpaRepository);
    }

}
