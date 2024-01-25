package org.core.jpa.repository;

import org.core.jpa.entity.TbMvInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TbMvInfoEntityRepository extends JpaRepository<TbMvInfoEntity, Long>, JpaSpecificationExecutor<TbMvInfoEntity> {
    
}