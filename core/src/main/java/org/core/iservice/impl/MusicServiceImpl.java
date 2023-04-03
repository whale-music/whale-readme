package org.core.iservice.impl;

import org.core.common.service.ServiceImpl;
import org.core.iservice.MusicService;
import org.core.pojo.MusicPojo;
import org.core.repository.MusicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 所有音乐列表 服务实现类
 * </p>
 *
 * @author Sakura
 * @since 2023-04-02
 */
@Service
public class MusicServiceImpl extends ServiceImpl<MusicRepository, MusicPojo> implements MusicService {
    
    /**
     * @param baseJpaRepository
     */
    @Autowired
    @Override
    public void setBaseJpaRepository(MusicRepository baseJpaRepository) {
        super.setBaseJpaRepository(baseJpaRepository);
    }

}
