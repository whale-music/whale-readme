package org.core.jpa.service;

import org.core.jpa.entity.TbPluginMsgEntity;
import org.core.jpa.model.dto.TbPluginMsgEntityDTO;
import org.core.jpa.model.vo.TbPluginMsgEntityQueryVO;
import org.core.jpa.model.vo.TbPluginMsgEntityUpdateVO;
import org.core.jpa.model.vo.TbPluginMsgEntityVO;
import org.core.jpa.repository.TbPluginMsgEntityRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class TbPluginMsgEntityService {
    
    @Autowired
    private TbPluginMsgEntityRepository tbPluginMsgEntityRepository;
    
    public Long save(TbPluginMsgEntityVO vO) {
        TbPluginMsgEntity bean = new TbPluginMsgEntity();
        BeanUtils.copyProperties(vO, bean);
        bean = tbPluginMsgEntityRepository.save(bean);
        return bean.getId();
    }
    
    public void delete(Long id) {
        tbPluginMsgEntityRepository.deleteById(id);
    }
    
    public void update(Long id, TbPluginMsgEntityUpdateVO vO) {
        TbPluginMsgEntity bean = requireOne(id);
        BeanUtils.copyProperties(vO, bean);
        tbPluginMsgEntityRepository.save(bean);
    }
    
    public TbPluginMsgEntityDTO getById(Long id) {
        TbPluginMsgEntity original = requireOne(id);
        return toDTO(original);
    }
    
    public Page<TbPluginMsgEntityDTO> query(TbPluginMsgEntityQueryVO vO) {
        throw new UnsupportedOperationException();
    }
    
    private TbPluginMsgEntityDTO toDTO(TbPluginMsgEntity original) {
        TbPluginMsgEntityDTO bean = new TbPluginMsgEntityDTO();
        BeanUtils.copyProperties(original, bean);
        return bean;
    }
    
    private TbPluginMsgEntity requireOne(Long id) {
        return tbPluginMsgEntityRepository.findById(id)
                                          .orElseThrow(() -> new NoSuchElementException("Resource not found: " + id));
    }
}
