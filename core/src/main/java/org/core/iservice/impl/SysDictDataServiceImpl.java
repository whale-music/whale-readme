package org.core.iservice.impl;

import org.core.common.service.ServiceImpl;
import org.core.iservice.SysDictDataService;
import org.core.pojo.SysDictDataPojo;
import org.core.repository.SysDictDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 字典数据表 服务实现类
 * </p>
 *
 * @author Sakura
 * @since 2023-04-02
 */
@Service
public class SysDictDataServiceImpl extends ServiceImpl<SysDictDataRepository, SysDictDataPojo> implements SysDictDataService {
    
    /**
     * @param baseJpaRepository
     */
    @Autowired
    @Override
    public void setBaseJpaRepository(SysDictDataRepository baseJpaRepository) {
        super.setBaseJpaRepository(baseJpaRepository);
    }

}
