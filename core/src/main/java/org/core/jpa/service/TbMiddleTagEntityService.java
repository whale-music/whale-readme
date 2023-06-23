package org.core.jpa.service;

import org.core.jpa.entity.TbMiddleTagEntity;
import org.core.jpa.model.dto.TbMiddleTagEntityDTO;
import org.core.jpa.model.vo.TbMiddleTagEntityQueryVO;
import org.core.jpa.model.vo.TbMiddleTagEntityUpdateVO;
import org.core.jpa.model.vo.TbMiddleTagEntityVO;
import org.core.jpa.repository.TbMiddleTagEntityRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class TbMiddleTagEntityService {
    
    @Autowired
    private TbMiddleTagEntityRepository tbMiddleTagEntityRepository;
    
    public Long save(TbMiddleTagEntityVO vO) {
        TbMiddleTagEntity bean = new TbMiddleTagEntity();
        BeanUtils.copyProperties(vO, bean);
        bean = tbMiddleTagEntityRepository.save(bean);
        return bean.getId();
    }
    
    public void delete(Long id) {
        tbMiddleTagEntityRepository.deleteById(id);
    }
    
    public void update(Long id, TbMiddleTagEntityUpdateVO vO) {
        TbMiddleTagEntity bean = requireOne(id);
        BeanUtils.copyProperties(vO, bean);
        tbMiddleTagEntityRepository.save(bean);
    }
    
    public TbMiddleTagEntityDTO getById(Long id) {
        TbMiddleTagEntity original = requireOne(id);
        return toDTO(original);
    }
    
    public Page<TbMiddleTagEntityDTO> query(TbMiddleTagEntityQueryVO vO) {
        throw new UnsupportedOperationException();
    }
    
    private TbMiddleTagEntityDTO toDTO(TbMiddleTagEntity original) {
        TbMiddleTagEntityDTO bean = new TbMiddleTagEntityDTO();
        BeanUtils.copyProperties(original, bean);
        return bean;
    }
    
    private TbMiddleTagEntity requireOne(Long id) {
        return tbMiddleTagEntityRepository.findById(id)
                                          .orElseThrow(() -> new NoSuchElementException("Resource not found: " + id));
    }
}
