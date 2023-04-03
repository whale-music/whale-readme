package org.core.iservice.impl;

import org.core.common.service.ServiceImpl;
import org.core.iservice.SysDictTypeService;
import org.core.pojo.SysDictTypePojo;
import org.core.repository.SysDictTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 字典类型表 服务实现类
 * </p>
 *
 * @author Sakura
 * @since 2023-04-02
 */
@Service
public class SysDictTypeServiceImpl extends ServiceImpl<SysDictTypeRepository, SysDictTypePojo> implements SysDictTypeService {
    
    /**
     * @param baseJpaRepository
     */
    @Autowired
    @Override
    public void setBaseJpaRepository(SysDictTypeRepository baseJpaRepository) {
        super.setBaseJpaRepository(baseJpaRepository);
    }

}
