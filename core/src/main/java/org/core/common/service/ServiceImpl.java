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

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.PageUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.core.common.mapper.BaseMapper;
import org.core.common.page.LambdaQueryWrapper;
import org.core.common.page.Page;
import org.core.common.reflection.ReflectionFieldName;
import org.core.common.reflection.SFunction;
import org.springframework.beans.BeanUtils;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import java.util.*;

/**
 * IService 实现类（ 泛型：M 是 mapper 对象，T 是实体 ）
 *
 * @author hubin
 * @since 2018-06-23
 */
public class ServiceImpl<M extends BaseMapper<T, Long>, T> implements IService<T> {
    
    private static final String ID = "id";
    protected Log log = LogFactory.getLog(getClass());
    
    protected M baseJpaRepository;
    
    public void setBaseJpaRepository(M baseJpaRepository) {
        this.baseJpaRepository = baseJpaRepository;
    }
    
    public ServiceImpl() {
        super();
    }
    
    private Long getBeanId(T entity) {
        BeanMap beanMap = BeanMap.create(entity);
        Object key = beanMap.get(ID);
        if (key instanceof Long) {
            Objects.requireNonNull(key);
            return (Long) key;
        }
        throw new IllegalArgumentException();
    }
    
    @Override
    public T save(T entity) {
        return baseJpaRepository.save(entity);
    }
    
    @Override
    public boolean saveBatch(Collection<T> entityList) {
        return IService.super.saveBatch(entityList);
    }
    
    @Override
    public boolean saveBatch(Collection<T> entityList, int batchSize) {
        int count = PageUtil.totalPage(entityList.size(), batchSize);
        List<T> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            List<T> page = ListUtil.page(i, batchSize, ListUtil.toList(entityList));
            list.addAll(baseJpaRepository.saveAll(page));
        }
        return list.size() == entityList.size();
    }
    
    
    @Override
    public boolean removeById(Long id) {
        baseJpaRepository.deleteById(id);
        return true;
    }
    
    @Override
    public boolean removeById(T entity) {
        baseJpaRepository.delete(entity);
        return true;
    }
    
    @Override
    public boolean removeByIds(Collection<Long> list) {
        baseJpaRepository.deleteAllById(list);
        return false;
    }
    
    @Override
    public boolean removeByIds(Collection<?> list, boolean useFill) {
        return false;
    }
    
    
    @Override
    public T updateById(T entity) {
        BeanMap beanMap = BeanMap.create(entity);
        Long id = (Long) beanMap.get("id");
        T referenceById = baseJpaRepository.getReferenceById(id);
        BeanUtils.copyProperties(entity, referenceById);
        return baseJpaRepository.save(referenceById);
    }
    
    @Override
    public boolean updateBatchById(Collection<T> entityList) {
        return IService.super.updateBatchById(entityList);
    }
    
    @Override
    public boolean updateBatchById(Collection<T> entityList, int batchSize) {
        List<Long> ids = new ArrayList<>(entityList.size());
        HashMap<String, T> map = new HashMap<>();
        for (T t : entityList) {
            Long key = getBeanId(t);
            map.put(String.valueOf(key), t);
        }
        List<T> allById = baseJpaRepository.findAllById(ids);
        // TODO 检查是否是值传递
        for (T t : allById) {
            Object key = getBeanId(t);
            BeanUtils.copyProperties(map.get(String.valueOf(key)), t);
        }
        baseJpaRepository.saveAll(allById);
        return true;
    }
    
    @Override
    public T saveOrUpdate(T entity) {
        return baseJpaRepository.save(entity);
    }
    
    @Override
    public T getById(Long id) {
        return IService.super.getById(id);
    }
    
    
    @Override
    public int hashCode() {
        return super.hashCode();
    }
    
    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
    
    @Override
    public String toString() {
        return baseJpaRepository.toString();
    }
    
    @Override
    public long count() {
        return baseJpaRepository.count();
    }
    
    /**
     * @param id
     * @return
     */
    @Override
    public Long countById(Long id) {
        Specification<T> where = Specification.where((root, query, criteriaBuilder) -> query.where(criteriaBuilder.in(root.get(ID)).value(id))
                                                                                            .getRestriction());
        return baseJpaRepository.count(where);
    }
    
    /**
     * @param id
     * @return
     */
    @Override
    public Long countById(Collection<Long> id) {
        Specification<T> where = Specification.where((root, query, criteriaBuilder) -> query.where(criteriaBuilder.in(root.get(ID)).in(id))
                                                                                            .getRestriction());
        return baseJpaRepository.count(where);
    }
    
    /**
     * @param func
     * @param id
     * @return
     */
    @Override
    public Long countLambda(SFunction<T, ?> func, Object id) {
        String fieldName = ReflectionFieldName.getFieldName(func);
        Specification<T> where = Specification.where((root, query, criteriaBuilder) -> query.where(criteriaBuilder.in(root.get(fieldName)).value(id))
                                                                                            .getRestriction());
        return baseJpaRepository.count(where);
    }
    
    /**
     * @param func
     * @param id
     * @return
     */
    @Override
    public Long countLambda(SFunction<T, ?> func, Collection<Object> id) {
        String fieldName = ReflectionFieldName.getFieldName(func);
        Specification<T> where = Specification.where((root, query, criteriaBuilder) -> query.where(criteriaBuilder.in(root.get(fieldName)).in(id))
                                                                                            .getRestriction());
        return baseJpaRepository.count(where);
    }
    
    /**
     * @param id
     * @return
     */
    @Override
    public Long count(Specification<T> id) {
        return baseJpaRepository.count(id);
    }
    
    @Override
    public List<T> list() {
        return baseJpaRepository.findAll();
    }
    
    /**
     * 查询集合中所有数据
     *
     * @param func
     * @param id
     */
    @Override
    public List<T> listLambdaIn(SFunction<T, ?> func, Collection<?> id) {
        String fieldName = ReflectionFieldName.getFieldName(func);
        Specification<T> where = Specification.where((root, query, criteriaBuilder) -> query.where(criteriaBuilder.in(root.get(fieldName)).in(id))
                                                                                            .getRestriction());
        return baseJpaRepository.findAll(where);
    }
    
    /**
     * 查询相同字段
     *
     * @param func
     * @param id
     */
    @Override
    public List<T> listLambdaEq(SFunction<T, ?> func, Object id) {
        String fieldName = ReflectionFieldName.getFieldName(func);
        Specification<T> where = Specification.where((root, query, criteriaBuilder) -> query.where(criteriaBuilder.equal(root.get(fieldName), id))
                                                                                            .getRestriction());
        return baseJpaRepository.findAll(where);
    }
    
    /**
     * @param func
     * @param id
     * @return
     */
    @Override
    public List<T> listLike(SFunction<T, ?> func, Object id) {
        String fieldName = ReflectionFieldName.getFieldName(func);
        Specification<T> where = Specification.where((root, query, criteriaBuilder) -> query.where(criteriaBuilder.like(root.get(fieldName),
                id.toString() + "%")).getRestriction());
        return baseJpaRepository.findAll(where);
    }
    
    /**
     * 查询所有
     */
    @Override
    public List<T> listByIds(Collection<Long> ids) {
        return baseJpaRepository.findAllById(ids);
    }
    
    /**
     * @param specification
     * @return
     */
    @Override
    public List<T> findAll(Specification<T> specification) {
        return baseJpaRepository.findAll(specification);
    }
    
    /**
     * @param specification
     * @param pageable
     * @return
     */
    @Override
    public Page<T> findAll(Specification<T> specification, Pageable pageable) {
        org.springframework.data.domain.Page<T> p = baseJpaRepository.findAll(specification, pageable);
        Page<T> tPage = new Page<>();
        tPage.setCurrent(p.getNumber());
        tPage.setRecords(p.getContent());
        tPage.setTotal(p.getSize());
        return tPage;
    }
    
    @Override
    public JpaRepository<T, Long> getBaseJpaRepository() {
        return baseJpaRepository;
    }
    
    /**
     * @param specification
     * @return
     */
    @Override
    public Optional<T> getOne(Specification<T> specification) {
        return baseJpaRepository.findOne(specification);
    }
    
    /**
     * @param func
     * @param id
     * @return
     */
    @Override
    public Optional<T> getOne(SFunction<T, ?> func, Object id) {
        String fieldName = ReflectionFieldName.getFieldName(func);
        Specification<T> where = Specification.where((root, query, criteriaBuilder) -> query.where(criteriaBuilder.equal(root.get(fieldName), id))
                                                                                            .getRestriction());
        return baseJpaRepository.findOne(where);
    }
    
    
    /**
     * @param queryWrapper
     * @return
     */
    @Override
    public Optional<T> getOne(LambdaQueryWrapper<T> queryWrapper) {
        Specification<T> where = getSpecification(queryWrapper);
        return baseJpaRepository.findOne(where);
    }
    
    /**
     * @param queryWrapper
     * @return
     */
    @Override
    public List<T> list(LambdaQueryWrapper<T> queryWrapper) {
        Specification<T> where = getSpecification(queryWrapper);
        return baseJpaRepository.findAll(where);
    }
    
    /**
     * @param queryWrapper
     * @return
     */
    @Override
    public long count(LambdaQueryWrapper<T> queryWrapper) {
        Specification<T> where = getSpecification(queryWrapper);
        return baseJpaRepository.count(where);
    }
    
    private Specification<T> getSpecification(LambdaQueryWrapper<T> queryWrapper) {
        return Specification.where((root, query, criteriaBuilder) -> {
            List<Predicate> list = new ArrayList<>();
            queryWrapper.getEqData().forEach((tsFunction, o) -> {
                Predicate equal = criteriaBuilder.equal(root.get(ReflectionFieldName.getFieldName(tsFunction)), o);
                list.add(equal);
            });
            
            queryWrapper.getLikeData().forEach((tsFunction, o) -> {
                Predicate equal = criteriaBuilder.like(root.get(ReflectionFieldName.getFieldName(tsFunction)), o.toString() + "%");
                list.add(equal);
            });
            
            queryWrapper.getInData().forEach((tsFunction, o) -> {
                Predicate equal = criteriaBuilder.in(root.get(ReflectionFieldName.getFieldName(tsFunction)).in(o));
                list.add(equal);
            });
            
            Predicate[] predicates = new Predicate[list.size()];
            CriteriaQuery<?> where = query.where(list.toArray(predicates));
            
            queryWrapper.getOrderByDescData().forEach(tsFunction -> {
                where.orderBy(criteriaBuilder.desc(root.get(ReflectionFieldName.getFieldName(tsFunction))));
            });
            
            queryWrapper.getOrderByAscData().forEach(tsFunction -> {
                where.orderBy(criteriaBuilder.asc(root.get(ReflectionFieldName.getFieldName(tsFunction))));
            });
            return where.getRestriction();
        });
    }
    
    
    /**
     * @param saveBatch
     * @return
     */
    @Override
    public List<T> saveOrUpdateBatch(Collection<T> saveBatch) {
        return getBaseJpaRepository().saveAllAndFlush(saveBatch);
    }
    
    /**
     * @param page
     * @param queryWrapper
     * @return
     */
    @Override
    public Page<T> page(Page<T> page, LambdaQueryWrapper<T> queryWrapper) {
        Specification<T> specification = getSpecification(queryWrapper);
        org.springframework.data.domain.Page<T> tPage = baseJpaRepository.findAll(specification,
                PageRequest.of((int) page.getCurrent(), (int) page.getSize()));
        Page<T> toPage = new Page<>();
        toPage.setCurrent(tPage.getNumber());
        toPage.setTotal(tPage.getSize());
        toPage.setRecords(tPage.getContent());
        return toPage;
    }
    
    /**
     * @param page
     * @return
     */
    @Override
    public Page<T> page(Page<T> page) {
        org.springframework.data.domain.Page<T> tPage = baseJpaRepository.findAll(Specification.where(null),
                PageRequest.of((int) page.getCurrent(), (int) page.getSize()));
        Page<T> toPage = new Page<>();
        toPage.setCurrent(tPage.getNumber());
        toPage.setTotal(tPage.getSize());
        toPage.setRecords(tPage.getContent());
        return toPage;
    }
    
    /**
     * @param queryWrapper
     */
    @Override
    public void remove(LambdaQueryWrapper<T> queryWrapper) {
        Specification<T> specification = getSpecification(queryWrapper);
        List<T> list = baseJpaRepository.findAll(specification);
        if (CollUtil.isNotEmpty(list)) {
            baseJpaRepository.deleteAll(list);
        }
    }
    
    /**
     * @param entity
     * @param sourceWrapper
     */
    @Override
    public void update(T entity, LambdaQueryWrapper<T> sourceWrapper) {
        Specification<T> specification = getSpecification(sourceWrapper);
        Optional<T> one = baseJpaRepository.findOne(specification);
        if (one.isPresent()) {
            BeanUtils.copyProperties(entity,one,"id");
        }
    }
}
