package org.core.repository;

import org.core.common.mapper.BaseMapper;
import org.core.pojo.CollectTagPojo;
import org.springframework.stereotype.Repository;

@Repository
public interface CollectTagRepository extends  BaseMapper<CollectTagPojo,Long> {
    
}