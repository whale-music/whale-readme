package org.core.jpa.repository;

import org.core.jpa.entity.TbHistoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TbHistoryEntityRepository extends JpaRepository<TbHistoryEntity, Long>, JpaSpecificationExecutor<TbHistoryEntity> {
    
    Page<TbHistoryEntity> findByUserIdEqualsAndTypeEquals(Long userId, Byte type, Pageable pageable);
}