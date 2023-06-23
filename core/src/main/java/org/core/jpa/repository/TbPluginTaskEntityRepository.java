package org.core.jpa.repository;

import org.core.jpa.entity.TbPluginTaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TbPluginTaskEntityRepository extends JpaRepository<TbPluginTaskEntity, Long>, JpaSpecificationExecutor<TbPluginTaskEntity> {
    
}