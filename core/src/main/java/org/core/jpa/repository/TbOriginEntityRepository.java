package org.core.jpa.repository;

import org.core.jpa.entity.TbOriginEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TbOriginEntityRepository extends JpaRepository<TbOriginEntity, Long>, JpaSpecificationExecutor<TbOriginEntity> {
    
}