package org.core.jpa.repository;

import org.core.jpa.entity.TbUserCollectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TbUserCollectEntityRepository extends JpaRepository<TbUserCollectEntity, Long>, JpaSpecificationExecutor<TbUserCollectEntity> {
    
}