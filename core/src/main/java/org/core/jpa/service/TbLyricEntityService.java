package org.core.jpa.service;

import org.core.jpa.entity.TbLyricEntity;
import org.core.jpa.model.dto.TbLyricEntityDTO;
import org.core.jpa.model.vo.TbLyricEntityQueryVO;
import org.core.jpa.model.vo.TbLyricEntityUpdateVO;
import org.core.jpa.model.vo.TbLyricEntityVO;
import org.core.jpa.repository.TbLyricEntityRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class TbLyricEntityService {
    
    @Autowired
    private TbLyricEntityRepository tbLyricEntityRepository;
    
    public Long save(TbLyricEntityVO vO) {
        TbLyricEntity bean = new TbLyricEntity();
        BeanUtils.copyProperties(vO, bean);
        bean = tbLyricEntityRepository.save(bean);
        return bean.getId();
    }
    
    public void delete(Long id) {
        tbLyricEntityRepository.deleteById(id);
    }
    
    public void update(Long id, TbLyricEntityUpdateVO vO) {
        TbLyricEntity bean = requireOne(id);
        BeanUtils.copyProperties(vO, bean);
        tbLyricEntityRepository.save(bean);
    }
    
    public TbLyricEntityDTO getById(Long id) {
        TbLyricEntity original = requireOne(id);
        return toDTO(original);
    }
    
    public Page<TbLyricEntityDTO> query(TbLyricEntityQueryVO vO) {
        throw new UnsupportedOperationException();
    }
    
    private TbLyricEntityDTO toDTO(TbLyricEntity original) {
        TbLyricEntityDTO bean = new TbLyricEntityDTO();
        BeanUtils.copyProperties(original, bean);
        return bean;
    }
    
    private TbLyricEntity requireOne(Long id) {
        return tbLyricEntityRepository.findById(id)
                                      .orElseThrow(() -> new NoSuchElementException("Resource not found: " + id));
    }
}
