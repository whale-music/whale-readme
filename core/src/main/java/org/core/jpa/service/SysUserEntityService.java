package org.core.jpa.service;

import org.core.jpa.entity.SysUserEntity;
import org.core.jpa.model.dto.SysUserEntityDTO;
import org.core.jpa.model.vo.SysUserEntityQueryVO;
import org.core.jpa.model.vo.SysUserEntityUpdateVO;
import org.core.jpa.model.vo.SysUserEntityVO;
import org.core.jpa.repository.SysUserEntityRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class SysUserEntityService {
    
    private final SysUserEntityRepository sysUserEntityRepository;
    
    public SysUserEntityService(SysUserEntityRepository sysUserEntityRepository) {
        this.sysUserEntityRepository = sysUserEntityRepository;
    }
    
    public Long save(SysUserEntityVO vO) {
        SysUserEntity bean = new SysUserEntity();
        BeanUtils.copyProperties(vO, bean);
        bean = sysUserEntityRepository.save(bean);
        return bean.getId();
    }
    
    public void delete(Long id) {
        sysUserEntityRepository.deleteById(id);
    }
    
    public void update(Long id, SysUserEntityUpdateVO vO) {
        SysUserEntity bean = requireOne(id);
        BeanUtils.copyProperties(vO, bean);
        sysUserEntityRepository.save(bean);
    }
    
    public SysUserEntityDTO getById(Long id) {
        SysUserEntity original = requireOne(id);
        return toDTO(original);
    }
    
    public Page<SysUserEntityDTO> query(SysUserEntityQueryVO vO) {
        throw new UnsupportedOperationException();
    }
    
    private SysUserEntityDTO toDTO(SysUserEntity original) {
        SysUserEntityDTO bean = new SysUserEntityDTO();
        BeanUtils.copyProperties(original, bean);
        return bean;
    }
    
    private SysUserEntity requireOne(Long id) {
        return sysUserEntityRepository.findById(id)
                                      .orElseThrow(() -> new NoSuchElementException("Resource not found: " + id));
    }
}
