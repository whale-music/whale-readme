package org.core.repository;

import org.core.common.mapper.BaseMapper;
import org.core.pojo.SysDictTypePojo;
import org.springframework.stereotype.Repository;

@Repository
public interface SysDictTypeRepository extends  BaseMapper<SysDictTypePojo,Long> {
    
}