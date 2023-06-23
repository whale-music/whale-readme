package org.core.jpa.repository;

import org.core.jpa.entity.TbCollectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TbCollectEntityRepository extends JpaRepository<TbCollectEntity, Long>, JpaSpecificationExecutor<TbCollectEntity> {
    
}