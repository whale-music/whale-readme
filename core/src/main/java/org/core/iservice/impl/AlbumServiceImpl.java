package org.core.iservice.impl;

import org.core.common.service.ServiceImpl;
import org.core.iservice.AlbumService;
import org.core.pojo.AlbumPojo;
import org.core.repository.AlbumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 歌曲专辑表 服务实现类
 * </p>
 *
 * @author Sakura
 * @since 2023-04-02
 */
@Service
public class AlbumServiceImpl extends ServiceImpl<AlbumRepository, AlbumPojo> implements AlbumService {
    /**
     * @param baseJpaRepository
     */
    @Autowired
    @Override
    public void setBaseJpaRepository(AlbumRepository baseJpaRepository) {
        super.setBaseJpaRepository(baseJpaRepository);
    }
}
