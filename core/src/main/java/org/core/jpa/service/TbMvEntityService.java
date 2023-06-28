package org.core.jpa.service;

import org.core.jpa.entity.TbMvEntity;
import org.core.jpa.model.dto.TbMvEntityDTO;
import org.core.jpa.model.vo.TbMvEntityQueryVO;
import org.core.jpa.model.vo.TbMvEntityUpdateVO;
import org.core.jpa.model.vo.TbMvEntityVO;
import org.core.jpa.repository.TbMvEntityRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class TbMvEntityService {
    
    @Autowired
    private TbMvEntityRepository tbMvEntityRepository;
    
    public Long save(TbMvEntityVO vO) {
        TbMvEntity bean = new TbMvEntity();
        BeanUtils.copyProperties(vO, bean);
        bean = tbMvEntityRepository.save(bean);
        return bean.getId();
    }
    
    public void delete(Long id) {
        tbMvEntityRepository.deleteById(id);
    }
    
    public void update(Long id, TbMvEntityUpdateVO vO) {
        TbMvEntity bean = requireOne(id);
        BeanUtils.copyProperties(vO, bean);
        tbMvEntityRepository.save(bean);
    }
    
    public TbMvEntityDTO getById(Long id) {
        TbMvEntity original = requireOne(id);
        return toDTO(original);
    }
    
    public Page<TbMvEntityDTO> query(TbMvEntityQueryVO vO) {
        throw new UnsupportedOperationException();
    }
    
    private TbMvEntityDTO toDTO(TbMvEntity original) {
        TbMvEntityDTO bean = new TbMvEntityDTO();
        BeanUtils.copyProperties(original, bean);
        return bean;
    }
    
    private TbMvEntity requireOne(Long id) {
        return tbMvEntityRepository.findById(id)
                                   .orElseThrow(() -> new NoSuchElementException("Resource not found: " + id));
    }
}
