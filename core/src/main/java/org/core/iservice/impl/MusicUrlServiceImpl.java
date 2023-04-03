package org.core.iservice.impl;

import org.core.common.service.ServiceImpl;
import org.core.iservice.MusicUrlService;
import org.core.pojo.MusicUrlPojo;
import org.core.repository.MusicUrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 音乐下载地址 服务实现类
 * </p>
 *
 * @author Sakura
 * @since 2023-04-02
 */
@Service
public class MusicUrlServiceImpl extends ServiceImpl<MusicUrlRepository, MusicUrlPojo> implements MusicUrlService {
    
    /**
     * @param baseJpaRepository
     */
    @Autowired
    @Override
    public void setBaseJpaRepository(MusicUrlRepository baseJpaRepository) {
        super.setBaseJpaRepository(baseJpaRepository);
    }

}
