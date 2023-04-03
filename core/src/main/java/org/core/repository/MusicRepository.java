package org.core.repository;

import org.core.common.mapper.BaseMapper;
import org.core.pojo.MusicPojo;
import org.springframework.stereotype.Repository;
@Repository
public interface MusicRepository extends  BaseMapper<MusicPojo,Long> {
    
}