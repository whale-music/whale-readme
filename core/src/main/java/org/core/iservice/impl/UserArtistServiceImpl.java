package org.core.iservice.impl;

import org.core.common.service.ServiceImpl;
import org.core.iservice.UserArtistService;
import org.core.pojo.UserArtistPojo;
import org.core.repository.TbUserArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户关注歌曲家 服务实现类
 * </p>
 *
 * @author Sakura
 * @since 2023-04-02
 */
@Service
public class UserArtistServiceImpl extends ServiceImpl<TbUserArtistRepository, UserArtistPojo> implements UserArtistService {
    /**
     * @param baseJpaRepository
     */
    @Autowired
    @Override
    public void setBaseJpaRepository(TbUserArtistRepository baseJpaRepository) {
        super.setBaseJpaRepository(baseJpaRepository);
    }
}
