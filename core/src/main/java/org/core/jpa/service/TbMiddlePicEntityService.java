package org.core.jpa.service;

import org.core.jpa.entity.TbMiddlePicEntity;
import org.core.jpa.model.dto.TbMiddlePicEntityDTO;
import org.core.jpa.model.vo.TbMiddlePicEntityQueryVO;
import org.core.jpa.model.vo.TbMiddlePicEntityUpdateVO;
import org.core.jpa.model.vo.TbMiddlePicEntityVO;
import org.core.jpa.repository.TbMiddlePicEntityRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class TbMiddlePicEntityService {
    
    @Autowired
    private TbMiddlePicEntityRepository tbMiddlePicEntityRepository;
    
    public Long save(TbMiddlePicEntityVO vO) {
        TbMiddlePicEntity bean = new TbMiddlePicEntity();
        BeanUtils.copyProperties(vO, bean);
        bean = tbMiddlePicEntityRepository.save(bean);
        return bean.getId();
    }
    
    public void delete(Long id) {
        tbMiddlePicEntityRepository.deleteById(id);
    }
    
    public void update(Long id, TbMiddlePicEntityUpdateVO vO) {
        TbMiddlePicEntity bean = requireOne(id);
        BeanUtils.copyProperties(vO, bean);
        tbMiddlePicEntityRepository.save(bean);
    }
    
    public TbMiddlePicEntityDTO getById(Long id) {
        TbMiddlePicEntity original = requireOne(id);
        return toDTO(original);
    }
    
    public Page<TbMiddlePicEntityDTO> query(TbMiddlePicEntityQueryVO vO) {
        throw new UnsupportedOperationException();
    }
    
    private TbMiddlePicEntityDTO toDTO(TbMiddlePicEntity original) {
        TbMiddlePicEntityDTO bean = new TbMiddlePicEntityDTO();
        BeanUtils.copyProperties(original, bean);
        return bean;
    }
    
    private TbMiddlePicEntity requireOne(Long id) {
        return tbMiddlePicEntityRepository.findById(id)
                                          .orElseThrow(() -> new NoSuchElementException("Resource not found: " + id));
    }
}
