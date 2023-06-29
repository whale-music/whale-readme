package org.core.jpa.repository;

import org.core.jpa.entity.TbUserCollectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.NonNull;

import java.util.stream.Stream;

public interface TbUserCollectEntityRepository extends JpaRepository<TbUserCollectEntity, Long>, JpaSpecificationExecutor<TbUserCollectEntity> {
    Stream<TbUserCollectEntity> findByUserIdEquals(@NonNull Long userId);
    
}