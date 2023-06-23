package org.core.jpa.repository;

import org.core.jpa.entity.TbLyricEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TbLyricEntityRepository extends JpaRepository<TbLyricEntity, Long>, JpaSpecificationExecutor<TbLyricEntity> {
    
}