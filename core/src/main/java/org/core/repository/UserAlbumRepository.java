package org.core.repository;

import org.core.common.mapper.BaseMapper;
import org.core.pojo.UserAlbumPojo;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAlbumRepository extends  BaseMapper<UserAlbumPojo,Long> {
    
}