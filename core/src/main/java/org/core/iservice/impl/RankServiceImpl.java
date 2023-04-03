package org.core.iservice.impl;

import org.core.common.service.ServiceImpl;
import org.core.iservice.RankService;
import org.core.pojo.RankPojo;
import org.core.repository.RankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 音乐播放排行榜 服务实现类
 * </p>
 *
 * @author Sakura
 * @since 2023-04-02
 */
@Service
public class RankServiceImpl extends ServiceImpl<RankRepository, RankPojo> implements RankService {
    
    /**
     * @param baseJpaRepository
     */
    @Autowired
    @Override
    public void setBaseJpaRepository(RankRepository baseJpaRepository) {
        super.setBaseJpaRepository(baseJpaRepository);
    }

}
