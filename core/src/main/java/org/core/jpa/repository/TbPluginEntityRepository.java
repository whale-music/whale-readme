package org.core.jpa.repository;

import org.core.jpa.entity.TbPluginEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TbPluginEntityRepository extends JpaRepository<TbPluginEntity, Long>, JpaSpecificationExecutor<TbPluginEntity> {
    
}