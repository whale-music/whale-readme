package org.core.jpa.service;

import org.core.jpa.entity.TbPluginEntity;
import org.core.jpa.model.dto.TbPluginEntityDTO;
import org.core.jpa.model.vo.TbPluginEntityQueryVO;
import org.core.jpa.model.vo.TbPluginEntityUpdateVO;
import org.core.jpa.model.vo.TbPluginEntityVO;
import org.core.jpa.repository.TbPluginEntityRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class TbPluginEntityService {
    
    @Autowired
    private TbPluginEntityRepository tbPluginEntityRepository;
    
    public Long save(TbPluginEntityVO vO) {
        TbPluginEntity bean = new TbPluginEntity();
        BeanUtils.copyProperties(vO, bean);
        bean = tbPluginEntityRepository.save(bean);
        return bean.getId();
    }
    
    public void delete(Long id) {
        tbPluginEntityRepository.deleteById(id);
    }
    
    public void update(Long id, TbPluginEntityUpdateVO vO) {
        TbPluginEntity bean = requireOne(id);
        BeanUtils.copyProperties(vO, bean);
        tbPluginEntityRepository.save(bean);
    }
    
    public TbPluginEntityDTO getById(Long id) {
        TbPluginEntity original = requireOne(id);
        return toDTO(original);
    }
    
    public Page<TbPluginEntityDTO> query(TbPluginEntityQueryVO vO) {
        throw new UnsupportedOperationException();
    }
    
    private TbPluginEntityDTO toDTO(TbPluginEntity original) {
        TbPluginEntityDTO bean = new TbPluginEntityDTO();
        BeanUtils.copyProperties(original, bean);
        return bean;
    }
    
    private TbPluginEntity requireOne(Long id) {
        return tbPluginEntityRepository.findById(id)
                                       .orElseThrow(() -> new NoSuchElementException("Resource not found: " + id));
    }
}
