package org.core.jpa.repository;

import org.core.jpa.entity.TbScheduleTaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TbScheduleTaskEntityRepository extends JpaRepository<TbScheduleTaskEntity, Long>, JpaSpecificationExecutor<TbScheduleTaskEntity> {
    
}