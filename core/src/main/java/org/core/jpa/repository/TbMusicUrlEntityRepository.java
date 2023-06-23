package org.core.jpa.repository;

import org.core.jpa.entity.TbMusicUrlEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TbMusicUrlEntityRepository extends JpaRepository<TbMusicUrlEntity, Long>, JpaSpecificationExecutor<TbMusicUrlEntity> {
    
}