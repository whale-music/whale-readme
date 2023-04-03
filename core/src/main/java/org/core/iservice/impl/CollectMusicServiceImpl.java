package org.core.iservice.impl;

import org.core.common.service.ServiceImpl;
import org.core.iservice.CollectMusicService;
import org.core.pojo.CollectMusicPojo;
import org.core.repository.CollectMusicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 歌单和音乐的中间表，用于记录歌单中的每一个音乐 服务实现类
 * </p>
 *
 * @author Sakura
 * @since 2023-04-02
 */
@Service
public class CollectMusicServiceImpl extends ServiceImpl<CollectMusicRepository, CollectMusicPojo> implements CollectMusicService {
    
    /**
     * @param baseJpaRepository
     */
    @Autowired
    @Override
    public void setBaseJpaRepository(CollectMusicRepository baseJpaRepository) {
        super.setBaseJpaRepository(baseJpaRepository);
    }

}
