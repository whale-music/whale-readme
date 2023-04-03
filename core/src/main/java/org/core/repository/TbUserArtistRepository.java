package org.core.repository;

import org.core.common.mapper.BaseMapper;
import org.core.pojo.UserArtistPojo;
import org.springframework.stereotype.Repository;

@Repository
public interface TbUserArtistRepository extends  BaseMapper<UserArtistPojo,Long> {
    
}