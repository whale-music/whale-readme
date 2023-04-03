package org.core.repository;

import org.core.common.mapper.BaseMapper;
import org.core.pojo.PluginPojo;
import org.springframework.stereotype.Repository;

@Repository
public interface PluginRepository extends  BaseMapper<PluginPojo,Long> {
    
}