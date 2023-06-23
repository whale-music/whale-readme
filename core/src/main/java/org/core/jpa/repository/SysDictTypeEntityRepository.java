package org.core.jpa.repository;

import org.core.jpa.entity.SysDictTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SysDictTypeEntityRepository extends JpaRepository<SysDictTypeEntity, Long>, JpaSpecificationExecutor<SysDictTypeEntity> {
    
}