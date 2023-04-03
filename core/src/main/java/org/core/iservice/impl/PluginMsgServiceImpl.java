package org.core.iservice.impl;

import org.core.common.service.ServiceImpl;
import org.core.iservice.PluginMsgService;
import org.core.pojo.PluginMsgPojo;
import org.core.repository.PluginMsgRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 插件消息表 服务实现类
 * </p>
 *
 * @author Sakura
 * @since 2023-04-02
 */
@Service
public class PluginMsgServiceImpl extends ServiceImpl<PluginMsgRepository, PluginMsgPojo> implements PluginMsgService {
    
    /**
     * @param baseJpaRepository
     */
    @Autowired
    @Override
    public void setBaseJpaRepository(PluginMsgRepository baseJpaRepository) {
        super.setBaseJpaRepository(baseJpaRepository);
    }

}
