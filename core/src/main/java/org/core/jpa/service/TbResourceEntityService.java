package org.core.jpa.service;

import org.core.jpa.entity.TbResourceEntity;
import org.core.jpa.model.dto.TbResourceEntityDTO;
import org.core.jpa.model.vo.TbResourceEntityQueryVO;
import org.core.jpa.model.vo.TbResourceEntityUpdateVO;
import org.core.jpa.model.vo.TbResourceEntityVO;
import org.core.jpa.repository.TbResourceEntityRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class TbResourceEntityService {
    
    @Autowired
    private TbResourceEntityRepository tbResourceEntityRepository;
    
    public Long save(TbResourceEntityVO vO) {
        TbResourceEntity bean = new TbResourceEntity();
        BeanUtils.copyProperties(vO, bean);
        bean = tbResourceEntityRepository.save(bean);
        return bean.getId();
    }
    
    public void delete(Long id) {
        tbResourceEntityRepository.deleteById(id);
    }
    
    public void update(Long id, TbResourceEntityUpdateVO vO) {
        TbResourceEntity bean = requireOne(id);
        BeanUtils.copyProperties(vO, bean);
        tbResourceEntityRepository.save(bean);
    }
    
    public TbResourceEntityDTO getById(Long id) {
        TbResourceEntity original = requireOne(id);
        return toDTO(original);
    }
    
    public Page<TbResourceEntityDTO> query(TbResourceEntityQueryVO vO) {
        throw new UnsupportedOperationException();
    }
    
    private TbResourceEntityDTO toDTO(TbResourceEntity original) {
        TbResourceEntityDTO bean = new TbResourceEntityDTO();
        BeanUtils.copyProperties(original, bean);
        return bean;
    }
    
    private TbResourceEntity requireOne(Long id) {
        return tbResourceEntityRepository.findById(id)
                                         .orElseThrow(() -> new NoSuchElementException("Resource not found: " + id));
    }
}
