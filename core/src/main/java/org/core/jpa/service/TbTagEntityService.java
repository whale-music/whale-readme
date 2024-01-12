package org.core.jpa.service;

import org.core.jpa.entity.TbTagEntity;
import org.core.jpa.model.dto.TbTagEntityDTO;
import org.core.jpa.model.vo.TbTagEntityQueryVO;
import org.core.jpa.model.vo.TbTagEntityUpdateVO;
import org.core.jpa.model.vo.TbTagEntityVO;
import org.core.jpa.repository.TbTagEntityRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class TbTagEntityService {
    
    private final TbTagEntityRepository tbTagEntityRepository;
    
    public TbTagEntityService(TbTagEntityRepository tbTagEntityRepository) {
        this.tbTagEntityRepository = tbTagEntityRepository;
    }
    
    public Long save(TbTagEntityVO vO) {
        TbTagEntity bean = new TbTagEntity();
        BeanUtils.copyProperties(vO, bean);
        bean = tbTagEntityRepository.save(bean);
        return bean.getId();
    }
    
    public void delete(Long id) {
        tbTagEntityRepository.deleteById(id);
    }
    
    public void update(Long id, TbTagEntityUpdateVO vO) {
        TbTagEntity bean = requireOne(id);
        BeanUtils.copyProperties(vO, bean);
        tbTagEntityRepository.save(bean);
    }
    
    public TbTagEntityDTO getById(Long id) {
        TbTagEntity original = requireOne(id);
        return toDTO(original);
    }
    
    public Page<TbTagEntityDTO> query(TbTagEntityQueryVO vO) {
        throw new UnsupportedOperationException();
    }
    
    private TbTagEntityDTO toDTO(TbTagEntity original) {
        TbTagEntityDTO bean = new TbTagEntityDTO();
        BeanUtils.copyProperties(original, bean);
        return bean;
    }
    
    private TbTagEntity requireOne(Long id) {
        return tbTagEntityRepository.findById(id)
                                    .orElseThrow(() -> new NoSuchElementException("Resource not found: " + id));
    }
}
