package org.core.jpa.repository;

import org.core.jpa.entity.TbMvArtistEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TbMvArtistEntityRepository extends JpaRepository<TbMvArtistEntity, Long>, JpaSpecificationExecutor<TbMvArtistEntity> {
    
}