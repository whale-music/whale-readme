package org.core.jpa.repository;

import org.core.jpa.entity.TbAlbumEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TbAlbumEntityRepository extends JpaRepository<TbAlbumEntity, Long>, JpaSpecificationExecutor<TbAlbumEntity> {
    
}