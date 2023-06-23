package org.core.jpa.repository;

import org.core.jpa.entity.TbMiddleTagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TbMiddleTagEntityRepository extends JpaRepository<TbMiddleTagEntity, Long>, JpaSpecificationExecutor<TbMiddleTagEntity> {
    
}