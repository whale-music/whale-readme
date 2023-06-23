package org.core.jpa.repository;

import org.core.jpa.entity.TbUserArtistEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TbUserArtistEntityRepository extends JpaRepository<TbUserArtistEntity, Long>, JpaSpecificationExecutor<TbUserArtistEntity> {
    
}