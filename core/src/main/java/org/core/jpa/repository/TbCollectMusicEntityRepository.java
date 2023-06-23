package org.core.jpa.repository;

import org.core.jpa.entity.TbCollectMusicEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TbCollectMusicEntityRepository extends JpaRepository<TbCollectMusicEntity, Long>, JpaSpecificationExecutor<TbCollectMusicEntity> {
    
}