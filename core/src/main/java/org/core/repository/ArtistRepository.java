package org.core.repository;

import org.core.common.mapper.BaseMapper;
import org.core.pojo.ArtistPojo;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtistRepository extends  BaseMapper<ArtistPojo,Long> {
    
}