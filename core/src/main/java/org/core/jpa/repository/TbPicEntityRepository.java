package org.core.jpa.repository;

import org.core.jpa.entity.TbPicEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TbPicEntityRepository extends JpaRepository<TbPicEntity, Long>, JpaSpecificationExecutor<TbPicEntity> {
    
}