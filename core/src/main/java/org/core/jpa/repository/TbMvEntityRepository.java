package org.core.jpa.repository;

import org.core.jpa.entity.TbMvEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TbMvEntityRepository extends JpaRepository<TbMvEntity, Long>, JpaSpecificationExecutor<TbMvEntity> {
    
}