package org.core.iservice.impl;

import org.core.common.service.ServiceImpl;
import org.core.iservice.CollectService;
import org.core.pojo.CollectPojo;
import org.core.repository.CollectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 歌单列表 服务实现类
 * </p>
 *
 * @author Sakura
 * @since 2023-04-02
 */
@Service
public class CollectServiceImpl extends ServiceImpl<CollectRepository, CollectPojo> implements CollectService {
    
    /**
     * @param baseJpaRepository
     */
    @Autowired
    @Override
    public void setBaseJpaRepository(CollectRepository baseJpaRepository) {
        super.setBaseJpaRepository(baseJpaRepository);
    }

}
