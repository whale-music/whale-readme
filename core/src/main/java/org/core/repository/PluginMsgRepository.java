package org.core.repository;

import org.core.common.mapper.BaseMapper;
import org.core.pojo.PluginMsgPojo;
import org.springframework.stereotype.Repository;

@Repository
public interface PluginMsgRepository extends  BaseMapper<PluginMsgPojo,Long> {
    
}