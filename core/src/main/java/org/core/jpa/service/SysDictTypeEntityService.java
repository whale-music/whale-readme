package org.core.jpa.service;

import org.core.jpa.entity.SysDictTypeEntity;
import org.core.jpa.model.dto.SysDictTypeEntityDTO;
import org.core.jpa.model.vo.SysDictTypeEntityQueryVO;
import org.core.jpa.model.vo.SysDictTypeEntityUpdateVO;
import org.core.jpa.model.vo.SysDictTypeEntityVO;
import org.core.jpa.repository.SysDictTypeEntityRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class SysDictTypeEntityService {
    
    @Autowired
    private SysDictTypeEntityRepository sysDictTypeEntityRepository;
    
    public Long save(SysDictTypeEntityVO vO) {
        SysDictTypeEntity bean = new SysDictTypeEntity();
        BeanUtils.copyProperties(vO, bean);
        bean = sysDictTypeEntityRepository.save(bean);
        return bean.getId();
    }
    
    public void delete(Long id) {
        sysDictTypeEntityRepository.deleteById(id);
    }
    
    public void update(Long id, SysDictTypeEntityUpdateVO vO) {
        SysDictTypeEntity bean = requireOne(id);
        BeanUtils.copyProperties(vO, bean);
        sysDictTypeEntityRepository.save(bean);
    }
    
    public SysDictTypeEntityDTO getById(Long id) {
        SysDictTypeEntity original = requireOne(id);
        return toDTO(original);
    }
    
    public Page<SysDictTypeEntityDTO> query(SysDictTypeEntityQueryVO vO) {
        throw new UnsupportedOperationException();
    }
    
    private SysDictTypeEntityDTO toDTO(SysDictTypeEntity original) {
        SysDictTypeEntityDTO bean = new SysDictTypeEntityDTO();
        BeanUtils.copyProperties(original, bean);
        return bean;
    }
    
    private SysDictTypeEntity requireOne(Long id) {
        return sysDictTypeEntityRepository.findById(id)
                                          .orElseThrow(() -> new NoSuchElementException("Resource not found: " + id));
    }
}
