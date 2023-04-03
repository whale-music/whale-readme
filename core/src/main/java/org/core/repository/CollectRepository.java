package org.core.repository;

import org.core.common.mapper.BaseMapper;
import org.core.pojo.CollectPojo;
import org.springframework.stereotype.Repository;

@Repository
public interface CollectRepository extends  BaseMapper<CollectPojo,Long> {
    
}