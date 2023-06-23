package org.core.jpa.repository;

import org.core.jpa.entity.TbTagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TbTagEntityRepository extends JpaRepository<TbTagEntity, Long>, JpaSpecificationExecutor<TbTagEntity> {
    
}