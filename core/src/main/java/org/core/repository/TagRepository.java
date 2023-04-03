package org.core.repository;

import org.core.common.mapper.BaseMapper;
import org.core.pojo.TagPojo;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends  BaseMapper<TagPojo,Long> {
    
}