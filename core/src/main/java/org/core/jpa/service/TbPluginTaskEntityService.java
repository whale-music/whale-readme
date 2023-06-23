package org.core.jpa.service;

import org.core.jpa.entity.TbPluginTaskEntity;
import org.core.jpa.model.dto.TbPluginTaskEntityDTO;
import org.core.jpa.model.vo.TbPluginTaskEntityQueryVO;
import org.core.jpa.model.vo.TbPluginTaskEntityUpdateVO;
import org.core.jpa.model.vo.TbPluginTaskEntityVO;
import org.core.jpa.repository.TbPluginTaskEntityRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class TbPluginTaskEntityService {
    
    @Autowired
    private TbPluginTaskEntityRepository tbPluginTaskEntityRepository;
    
    public Long save(TbPluginTaskEntityVO vO) {
        TbPluginTaskEntity bean = new TbPluginTaskEntity();
        BeanUtils.copyProperties(vO, bean);
        bean = tbPluginTaskEntityRepository.save(bean);
        return bean.getId();
    }
    
    public void delete(Long id) {
        tbPluginTaskEntityRepository.deleteById(id);
    }
    
    public void update(Long id, TbPluginTaskEntityUpdateVO vO) {
        TbPluginTaskEntity bean = requireOne(id);
        BeanUtils.copyProperties(vO, bean);
        tbPluginTaskEntityRepository.save(bean);
    }
    
    public TbPluginTaskEntityDTO getById(Long id) {
        TbPluginTaskEntity original = requireOne(id);
        return toDTO(original);
    }
    
    public Page<TbPluginTaskEntityDTO> query(TbPluginTaskEntityQueryVO vO) {
        throw new UnsupportedOperationException();
    }
    
    private TbPluginTaskEntityDTO toDTO(TbPluginTaskEntity original) {
        TbPluginTaskEntityDTO bean = new TbPluginTaskEntityDTO();
        BeanUtils.copyProperties(original, bean);
        return bean;
    }
    
    private TbPluginTaskEntity requireOne(Long id) {
        return tbPluginTaskEntityRepository.findById(id)
                                           .orElseThrow(() -> new NoSuchElementException("Resource not found: " + id));
    }
}
