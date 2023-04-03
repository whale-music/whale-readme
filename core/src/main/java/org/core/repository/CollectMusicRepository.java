package org.core.repository;

import org.core.common.mapper.BaseMapper;
import org.core.pojo.CollectMusicPojo;
import org.springframework.stereotype.Repository;

@Repository
public interface CollectMusicRepository extends  BaseMapper<CollectMusicPojo,Long> {
    
}