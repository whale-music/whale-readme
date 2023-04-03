package org.core.repository;

import org.core.common.mapper.BaseMapper;
import org.core.pojo.MusicUrlPojo;
import org.springframework.stereotype.Repository;

@Repository
public interface MusicUrlRepository extends  BaseMapper<MusicUrlPojo,Long> {
    
}