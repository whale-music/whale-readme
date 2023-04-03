package org.core.iservice.impl;

import org.core.common.service.ServiceImpl;
import org.core.iservice.TagService;
import org.core.pojo.TagPojo;
import org.core.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 标签表（风格） 服务实现类
 * </p>
 *
 * @author Sakura
 * @since 2023-04-02
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagRepository, TagPojo> implements TagService {
    
    /**
     * @param baseJpaRepository
     */
    @Autowired
    @Override
    public void setBaseJpaRepository(TagRepository baseJpaRepository) {
        super.setBaseJpaRepository(baseJpaRepository);
    }
}
