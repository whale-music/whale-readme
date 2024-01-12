package org.core.jpa.service;

import org.core.jpa.entity.SysDictDataEntity;
import org.core.jpa.model.dto.SysDictDataEntityDTO;
import org.core.jpa.model.vo.SysDictDataEntityQueryVO;
import org.core.jpa.model.vo.SysDictDataEntityUpdateVO;
import org.core.jpa.model.vo.SysDictDataEntityVO;
import org.core.jpa.repository.SysDictDataEntityRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class SysDictDataEntityService {
    
    private final SysDictDataEntityRepository sysDictDataEntityRepository;
    
    public SysDictDataEntityService(SysDictDataEntityRepository sysDictDataEntityRepository) {
        this.sysDictDataEntityRepository = sysDictDataEntityRepository;
    }
    
    public Long save(SysDictDataEntityVO vO) {
        SysDictDataEntity bean = new SysDictDataEntity();
        BeanUtils.copyProperties(vO, bean);
        bean = sysDictDataEntityRepository.save(bean);
        return bean.getId();
    }
    
    public void delete(Long id) {
        sysDictDataEntityRepository.deleteById(id);
    }
    
    public void update(Long id, SysDictDataEntityUpdateVO vO) {
        SysDictDataEntity bean = requireOne(id);
        BeanUtils.copyProperties(vO, bean);
        sysDictDataEntityRepository.save(bean);
    }
    
    public SysDictDataEntityDTO getById(Long id) {
        SysDictDataEntity original = requireOne(id);
        return toDTO(original);
    }
    
    public Page<SysDictDataEntityDTO> query(SysDictDataEntityQueryVO vO) {
        throw new UnsupportedOperationException();
    }
    
    private SysDictDataEntityDTO toDTO(SysDictDataEntity original) {
        SysDictDataEntityDTO bean = new SysDictDataEntityDTO();
        BeanUtils.copyProperties(original, bean);
        return bean;
    }
    
    private SysDictDataEntity requireOne(Long id) {
        return sysDictDataEntityRepository.findById(id)
                                          .orElseThrow(() -> new NoSuchElementException("Resource not found: " + id));
    }
}
