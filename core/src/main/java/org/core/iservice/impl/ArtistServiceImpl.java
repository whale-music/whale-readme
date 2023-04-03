package org.core.iservice.impl;

import org.core.common.service.ServiceImpl;
import org.core.iservice.ArtistService;
import org.core.pojo.ArtistPojo;
import org.core.repository.ArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 歌手表 服务实现类
 * </p>
 *
 * @author Sakura
 * @since 2023-04-02
 */
@Service
public class ArtistServiceImpl extends ServiceImpl<ArtistRepository, ArtistPojo> implements ArtistService {
    /**
     * @param baseJpaRepository
     */
    @Autowired
    @Override
    public void setBaseJpaRepository(ArtistRepository baseJpaRepository) {
        super.setBaseJpaRepository(baseJpaRepository);
    }

}
