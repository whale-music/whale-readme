package org.core.iservice.impl;

import org.core.common.service.ServiceImpl;
import org.core.iservice.UserAlbumService;
import org.core.pojo.UserAlbumPojo;
import org.core.repository.UserAlbumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户收藏专辑表 服务实现类
 * </p>
 *
 * @author Sakura
 * @since 2023-04-02
 */
@Service
public class UserAlbumServiceImpl extends ServiceImpl<UserAlbumRepository, UserAlbumPojo> implements UserAlbumService {
    /**
     * @param baseJpaRepository
     */
    @Autowired
    @Override
    public void setBaseJpaRepository(UserAlbumRepository baseJpaRepository) {
        super.setBaseJpaRepository(baseJpaRepository);
    }
}
