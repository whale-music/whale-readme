package org.core.jpa.repository;

import org.core.jpa.entity.TbResourceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.NonNull;

public interface TbResourceEntityRepository extends JpaRepository<TbResourceEntity, Long>, JpaSpecificationExecutor<TbResourceEntity> {
    long countByMd5EqualsIgnoreCase(@NonNull String md5);
    
}