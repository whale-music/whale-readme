/*
 * Copyright (c) 2011-2022, baomidou (jobob@qq.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.core.common.service;

import org.core.common.page.LambdaQueryWrapper;
import org.core.common.page.Page;
import org.core.common.reflection.SFunction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * 顶级 Service
 *
 * @author hubin
 * @since 2018-06-23
 */
public interface IService<T> {
    
    /**
     * 默认批次提交数量
     */
    int DEFAULT_BATCH_SIZE = 1000;
    
    /**
     * 插入一条记录（选择字段，策略插入）
     *
     * @param entity 实体对象
     */
    T save(T entity);
    
    /**
     * 插入（批量）
     *
     * @param entityList 实体对象集合
     */
    @Transactional(rollbackFor = Exception.class)
    default boolean saveBatch(Collection<T> entityList) {
        return saveBatch(entityList, DEFAULT_BATCH_SIZE);
    }
    
    /**
     * 插入（批量）
     *
     * @param entityList 实体对象集合
     * @param batchSize  插入批次数量
     */
    boolean saveBatch(Collection<T> entityList, int batchSize);
    
    
    /**
     * 根据 ID 删除
     *
     * @param id 主键ID
     */
    boolean removeById(Long id);
    
    /**
     * 根据实体(ID)删除
     *
     * @param entity 实体
     * @since 3.4.4
     */
    boolean removeById(T entity);
    
    
    /**
     * 删除（根据ID 批量删除）
     *
     * @param list 主键ID或实体列表
     */
    boolean removeByIds(Collection<Long> list);
    
    /**
     * 批量删除
     *
     * @param list    主键ID或实体列表
     * @param useFill 是否填充(为true的情况,会将入参转换实体进行delete删除)
     * @return 删除结果
     * @since 3.5.0
     */
    @Transactional(rollbackFor = Exception.class)
    boolean removeByIds(Collection<?> list, boolean useFill);
    
    /**
     * 根据 ID 选择修改
     *
     * @param entity 实体对象
     */
    T updateById(T entity);
    
    /**
     * 根据ID 批量更新
     *
     * @param entityList 实体对象集合
     */
    @Transactional(rollbackFor = Exception.class)
    default boolean updateBatchById(Collection<T> entityList) {
        return updateBatchById(entityList, DEFAULT_BATCH_SIZE);
    }
    
    /**
     * 根据ID 批量更新
     *
     * @param entityList 实体对象集合
     * @param batchSize  更新批次数量
     */
    boolean updateBatchById(Collection<T> entityList, int batchSize);
    
    /**
     * TableId 注解存在更新记录，否插入一条记录
     *
     * @param entity 实体对象
     */
    T saveOrUpdate(T entity);
    
    /**
     * 根据 ID 查询
     *
     * @param id 主键ID
     */
    default T getById(Long id) {
        return getBaseJpaRepository().getReferenceById(id);
    }
    
    /**
     * 查询总记录数
     */
    long count();
    
    Long countById(Long id);
    
    default Long countById(Boolean condition, Long id) {
        if (Boolean.TRUE.equals(condition)) {
            return countById(id);
        }
        return null;
    }
    
    Long countById(Collection<Long> id);
    
    default Long countById(Boolean condition, Collection<Long> id) {
        if (Boolean.TRUE.equals(condition)) {
            return countById(id);
        }
        return null;
    }
    
    Long countLambda(SFunction<T, ?> func, Object id);
    
    default Long countLambda(Boolean condition, SFunction<T, ?> func, Object id) {
        if (Boolean.TRUE.equals(condition)) {
            return countLambda(func, id);
        }
        return null;
    }
    
    Long countLambda(SFunction<T, ?> func, Collection<Object> id);
    
    default Long countLambda(Boolean condition, SFunction<T, ?> func, Collection<Object> id) {
        if (Boolean.TRUE.equals(condition)) {
            return countLambda(func, id);
        }
        return null;
    }
    
    Long count(Specification<T> id);
    
    /**
     * 查询所有
     */
    List<T> list();
    
    /**
     * 查询集合中所有数据
     */
    List<T> listLambdaIn(SFunction<T, ?> func, Collection<?> id);
    
    /**
     * 查询相同字段
     */
    List<T> listLambdaEq(SFunction<T, ?> func, Object id);
    
    List<T> listLike(SFunction<T, ?> func, Object id);
    
    /**
     * 查询所有
     */
    List<T> listByIds(Collection<Long> ids);
    
    List<T> findAll(Specification<T> specification);
    
    Page<T> findAll(Specification<T> specification, Pageable pageable);
    
    /**
     * 获取对应 entity 的 BaseMapper
     *
     * @return BaseMapper
     */
    JpaRepository<T, Long> getBaseJpaRepository();
    
    Optional<T> getOne(Specification<T> specification);
    
    Optional<T> getOne(SFunction<T, ?> func, Object id);
    
    Optional<T> getOne(LambdaQueryWrapper<T> queryWrapper);
    
    List<T> list(LambdaQueryWrapper<T> queryWrapper);
    
    long count(LambdaQueryWrapper<T> queryWrapper);
    
    List<T> saveOrUpdateBatch(Collection<T> saveBatch);
    
    Page<T> page(Page<T> page, LambdaQueryWrapper<T> queryWrapper);
    
    Page<T> page(Page<T> page);
    
    void remove(LambdaQueryWrapper<T> queryWrapper);
    
    void update(T entity, LambdaQueryWrapper<T> sourceWrapper);
}
