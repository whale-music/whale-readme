package org.core.jpa.repository;

import org.core.jpa.entity.TbUserAlbumEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TbUserAlbumEntityRepository extends JpaRepository<TbUserAlbumEntity, Long>, JpaSpecificationExecutor<TbUserAlbumEntity> {
    
}