package org.core.repository;

import org.core.common.mapper.BaseMapper;
import org.core.pojo.RankPojo;
import org.springframework.stereotype.Repository;

@Repository
public interface RankRepository extends  BaseMapper<RankPojo,Long> {
    
}