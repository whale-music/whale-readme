package org.core.jpa.repository;

import org.core.jpa.entity.SysDictDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SysDictDataEntityRepository extends JpaRepository<SysDictDataEntity, Long>, JpaSpecificationExecutor<SysDictDataEntity> {
    
}