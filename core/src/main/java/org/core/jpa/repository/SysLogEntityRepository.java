package org.core.jpa.repository;

import org.core.jpa.entity.SysLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SysLogEntityRepository extends JpaRepository<SysLogEntity, Long>, JpaSpecificationExecutor<SysLogEntity> {
    
}