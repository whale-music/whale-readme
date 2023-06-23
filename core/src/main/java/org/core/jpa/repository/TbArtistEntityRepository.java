package org.core.jpa.repository;

import org.core.jpa.entity.TbArtistEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TbArtistEntityRepository extends JpaRepository<TbArtistEntity, Long>, JpaSpecificationExecutor<TbArtistEntity> {
    
}