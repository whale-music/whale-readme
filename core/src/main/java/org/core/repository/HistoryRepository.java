package org.core.repository;

import org.core.common.mapper.BaseMapper;
import org.core.pojo.HistoryPojo;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryRepository extends  BaseMapper<HistoryPojo,Long> {
    
}