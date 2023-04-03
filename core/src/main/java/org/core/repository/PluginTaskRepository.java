package org.core.repository;

import org.core.common.mapper.BaseMapper;
import org.core.pojo.PluginTaskPojo;
import org.springframework.stereotype.Repository;

@Repository
public interface PluginTaskRepository extends  BaseMapper<PluginTaskPojo,Long> {
    
}