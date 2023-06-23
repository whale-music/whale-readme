package org.core.jpa.repository;

import org.core.jpa.entity.TbMiddlePicEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TbMiddlePicEntityRepository extends JpaRepository<TbMiddlePicEntity, Long>, JpaSpecificationExecutor<TbMiddlePicEntity> {
    
}