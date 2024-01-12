package org.core.jpa.service;

import org.core.jpa.entity.TbUserMvEntity;
import org.core.jpa.model.dto.TbUserMvEntityDTO;
import org.core.jpa.model.vo.TbUserMvEntityQueryVO;
import org.core.jpa.model.vo.TbUserMvEntityUpdateVO;
import org.core.jpa.model.vo.TbUserMvEntityVO;
import org.core.jpa.repository.TbUserMvEntityRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class TbUserMvEntityService {
    
    private final TbUserMvEntityRepository tbUserMvEntityRepository;
    
    public TbUserMvEntityService(TbUserMvEntityRepository tbUserMvEntityRepository) {
        this.tbUserMvEntityRepository = tbUserMvEntityRepository;
    }
    
    public Long save(TbUserMvEntityVO vO) {
        TbUserMvEntity bean = new TbUserMvEntity();
        BeanUtils.copyProperties(vO, bean);
        bean = tbUserMvEntityRepository.save(bean);
        return bean.getUserId();
    }
    
    public void delete(Long id) {
        tbUserMvEntityRepository.deleteById(id);
    }
    
    public void update(Long id, TbUserMvEntityUpdateVO vO) {
        TbUserMvEntity bean = requireOne(id);
        BeanUtils.copyProperties(vO, bean);
        tbUserMvEntityRepository.save(bean);
    }
    
    public TbUserMvEntityDTO getById(Long id) {
        TbUserMvEntity original = requireOne(id);
        return toDTO(original);
    }
    
    public Page<TbUserMvEntityDTO> query(TbUserMvEntityQueryVO vO) {
        throw new UnsupportedOperationException();
    }
    
    private TbUserMvEntityDTO toDTO(TbUserMvEntity original) {
        TbUserMvEntityDTO bean = new TbUserMvEntityDTO();
        BeanUtils.copyProperties(original, bean);
        return bean;
    }
    
    private TbUserMvEntity requireOne(Long id) {
        return tbUserMvEntityRepository.findById(id)
                                       .orElseThrow(() -> new NoSuchElementException("Resource not found: " + id));
    }
}
