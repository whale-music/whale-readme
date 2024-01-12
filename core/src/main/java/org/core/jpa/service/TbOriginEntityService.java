package org.core.jpa.service;

import org.core.jpa.entity.TbOriginEntity;
import org.core.jpa.model.dto.TbOriginEntityDTO;
import org.core.jpa.model.vo.TbOriginEntityQueryVO;
import org.core.jpa.model.vo.TbOriginEntityUpdateVO;
import org.core.jpa.model.vo.TbOriginEntityVO;
import org.core.jpa.repository.TbOriginEntityRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class TbOriginEntityService {
    
    private final TbOriginEntityRepository tbOriginEntityRepository;
    
    public TbOriginEntityService(TbOriginEntityRepository tbOriginEntityRepository) {
        this.tbOriginEntityRepository = tbOriginEntityRepository;
    }
    
    public Long save(TbOriginEntityVO vO) {
        TbOriginEntity bean = new TbOriginEntity();
        BeanUtils.copyProperties(vO, bean);
        bean = tbOriginEntityRepository.save(bean);
        return bean.getId();
    }
    
    public void delete(Long id) {
        tbOriginEntityRepository.deleteById(id);
    }
    
    public void update(Long id, TbOriginEntityUpdateVO vO) {
        TbOriginEntity bean = requireOne(id);
        BeanUtils.copyProperties(vO, bean);
        tbOriginEntityRepository.save(bean);
    }
    
    public TbOriginEntityDTO getById(Long id) {
        TbOriginEntity original = requireOne(id);
        return toDTO(original);
    }
    
    public Page<TbOriginEntityDTO> query(TbOriginEntityQueryVO vO) {
        throw new UnsupportedOperationException();
    }
    
    private TbOriginEntityDTO toDTO(TbOriginEntity original) {
        TbOriginEntityDTO bean = new TbOriginEntityDTO();
        BeanUtils.copyProperties(original, bean);
        return bean;
    }
    
    private TbOriginEntity requireOne(Long id) {
        return tbOriginEntityRepository.findById(id)
                                       .orElseThrow(() -> new NoSuchElementException("Resource not found: " + id));
    }
}
