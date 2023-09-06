package org.core.jpa.repository;

import org.core.jpa.entity.TbMusicEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Collection;
import java.util.Set;

public interface TbMusicEntityRepository extends JpaRepository<TbMusicEntity, Long>, JpaSpecificationExecutor<TbMusicEntity> {
    Set<TbMusicEntity> findByIdIn(Collection<Long> ids);
    
}