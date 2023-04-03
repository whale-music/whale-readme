package org.core.repository;

import org.core.common.mapper.BaseMapper;
import org.core.pojo.SysUserPojo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

@Repository
public interface SysUserRepository extends BaseMapper<SysUserPojo, Long>, JpaRepository<SysUserPojo, Long>, JpaSpecificationExecutor<SysUserPojo>, Serializable {
    
}