package org.core.repository;

import org.core.common.mapper.BaseMapper;
import org.core.pojo.AlbumPojo;
import org.springframework.stereotype.Repository;

@Repository
public interface AlbumRepository extends BaseMapper<AlbumPojo, Long> {
    
}