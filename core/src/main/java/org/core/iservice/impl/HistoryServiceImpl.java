package org.core.iservice.impl;

import org.core.common.service.ServiceImpl;
import org.core.iservice.HistoryService;
import org.core.pojo.HistoryPojo;
import org.core.repository.HistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 音乐播放历史(包括歌单，音乐，专辑） 服务实现类
 * </p>
 *
 * @author Sakura
 * @since 2023-04-02
 */
@Service
public class HistoryServiceImpl extends ServiceImpl<HistoryRepository, HistoryPojo> implements HistoryService {
    
    /**
     * @param baseJpaRepository
     */
    @Autowired
    @Override
    public void setBaseJpaRepository(HistoryRepository baseJpaRepository) {
        super.setBaseJpaRepository(baseJpaRepository);
    }

}
