package org.core.iservice.impl;

import org.core.common.service.ServiceImpl;
import org.core.iservice.AlbumArtistService;
import org.core.pojo.AlbumArtistPojo;
import org.core.repository.AlbumArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 歌手和专辑中间表 服务实现类
 * </p>
 *
 * @author Sakura
 * @since 2023-04-02
 */
@Service
public class AlbumArtistServiceImpl extends ServiceImpl<AlbumArtistRepository, AlbumArtistPojo> implements AlbumArtistService {
    
    /**
     * @param baseJpaRepository
     */
    @Autowired
    @Override
    public void setBaseJpaRepository(AlbumArtistRepository baseJpaRepository) {
        super.setBaseJpaRepository(baseJpaRepository);
    }
}
