package org.core.jpa.repository;

import org.core.jpa.entity.TbAlbumArtistEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TbAlbumArtistEntityRepository extends JpaRepository<TbAlbumArtistEntity, Long>, JpaSpecificationExecutor<TbAlbumArtistEntity> {

}