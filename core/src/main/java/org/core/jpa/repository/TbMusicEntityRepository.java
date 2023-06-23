package org.core.jpa.repository;

import org.core.jpa.entity.TbMusicEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TbMusicEntityRepository extends JpaRepository<TbMusicEntity, Long>, JpaSpecificationExecutor<TbMusicEntity> {
}