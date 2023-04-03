package org.core.repository;

import org.core.common.mapper.BaseMapper;
import org.core.pojo.SysDictDataPojo;
import org.springframework.stereotype.Repository;

@Repository
public interface SysDictDataRepository extends  BaseMapper<SysDictDataPojo,Long> {
    
}