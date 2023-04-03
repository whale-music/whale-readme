package org.core.iservice.impl;


import org.core.common.service.ServiceImpl;
import org.core.iservice.SysUserService;
import org.core.pojo.SysUserPojo;
import org.core.repository.SysUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统用户表 服务实现类
 * </p>
 *
 * @author Sakura
 * @since 2023-04-02
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserRepository, SysUserPojo> implements SysUserService {
    
    /**
     * @param baseJpaRepository
     */
    @Autowired
    @Override
    public void setBaseJpaRepository(SysUserRepository baseJpaRepository) {
        super.setBaseJpaRepository(baseJpaRepository);
    }
}
