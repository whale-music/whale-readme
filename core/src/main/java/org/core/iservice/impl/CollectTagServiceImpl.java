package org.core.iservice.impl;

import org.core.common.service.ServiceImpl;
import org.core.iservice.CollectTagService;
import org.core.pojo.CollectTagPojo;
import org.core.repository.CollectTagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 歌单风格中间表 服务实现类
 * </p>
 *
 * @author Sakura
 * @since 2023-04-02
 */
@Service
public class CollectTagServiceImpl extends ServiceImpl<CollectTagRepository, CollectTagPojo> implements CollectTagService {
    
    /**
     * @param baseJpaRepository
     */
    @Autowired
    @Override
    public void setBaseJpaRepository(CollectTagRepository baseJpaRepository) {
        super.setBaseJpaRepository(baseJpaRepository);
    }

}
