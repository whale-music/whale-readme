package org.core.jpa.repository;

import org.core.jpa.entity.TbMusicArtistEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TbMusicArtistEntityRepository extends JpaRepository<TbMusicArtistEntity, Long>, JpaSpecificationExecutor<TbMusicArtistEntity> {
    
}