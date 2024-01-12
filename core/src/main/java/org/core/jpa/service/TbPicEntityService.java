package org.core.jpa.service;

import org.core.jpa.entity.TbPicEntity;
import org.core.jpa.model.dto.TbPicEntityDTO;
import org.core.jpa.model.vo.TbPicEntityQueryVO;
import org.core.jpa.model.vo.TbPicEntityUpdateVO;
import org.core.jpa.model.vo.TbPicEntityVO;
import org.core.jpa.repository.TbPicEntityRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class TbPicEntityService {
    
    private final TbPicEntityRepository tbPicEntityRepository;
    
    public TbPicEntityService(TbPicEntityRepository tbPicEntityRepository) {
        this.tbPicEntityRepository = tbPicEntityRepository;
    }
    
    public Long save(TbPicEntityVO vO) {
        TbPicEntity bean = new TbPicEntity();
        BeanUtils.copyProperties(vO, bean);
        bean = tbPicEntityRepository.save(bean);
        return bean.getId();
    }
    
    public void delete(Long id) {
        tbPicEntityRepository.deleteById(id);
    }
    
    public void update(Long id, TbPicEntityUpdateVO vO) {
        TbPicEntity bean = requireOne(id);
        BeanUtils.copyProperties(vO, bean);
        tbPicEntityRepository.save(bean);
    }
    
    public TbPicEntityDTO getById(Long id) {
        TbPicEntity original = requireOne(id);
        return toDTO(original);
    }
    
    public Page<TbPicEntityDTO> query(TbPicEntityQueryVO vO) {
        throw new UnsupportedOperationException();
    }
    
    private TbPicEntityDTO toDTO(TbPicEntity original) {
        TbPicEntityDTO bean = new TbPicEntityDTO();
        BeanUtils.copyProperties(original, bean);
        return bean;
    }
    
    private TbPicEntity requireOne(Long id) {
        return tbPicEntityRepository.findById(id)
                                    .orElseThrow(() -> new NoSuchElementException("Resource not found: " + id));
    }
}
