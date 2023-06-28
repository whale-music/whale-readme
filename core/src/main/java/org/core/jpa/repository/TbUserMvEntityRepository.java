package org.core.jpa.repository;

import org.core.jpa.entity.TbUserMvEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TbUserMvEntityRepository extends JpaRepository<TbUserMvEntity, Long>, JpaSpecificationExecutor<TbUserMvEntity> {
    
}