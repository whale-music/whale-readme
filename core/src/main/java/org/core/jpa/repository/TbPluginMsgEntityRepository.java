package org.core.jpa.repository;

import org.core.jpa.entity.TbPluginMsgEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TbPluginMsgEntityRepository extends JpaRepository<TbPluginMsgEntity, Long>, JpaSpecificationExecutor<TbPluginMsgEntity> {
    
}