package org.core.jpa.service;

import org.core.jpa.entity.TbUserCollectEntity;
import org.core.jpa.model.dto.TbUserCollectEntityDTO;
import org.core.jpa.model.vo.TbUserCollectEntityQueryVO;
import org.core.jpa.model.vo.TbUserCollectEntityUpdateVO;
import org.core.jpa.model.vo.TbUserCollectEntityVO;
import org.core.jpa.repository.TbUserCollectEntityRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class TbUserCollectEntityService {
    
    @Autowired
    private TbUserCollectEntityRepository tbUserCollectEntityRepository;
    
    public Long save(TbUserCollectEntityVO vO) {
        TbUserCollectEntity bean = new TbUserCollectEntity();
        BeanUtils.copyProperties(vO, bean);
        bean = tbUserCollectEntityRepository.save(bean);
        return bean.getUserId();
    }
    
    public void delete(Long id) {
        tbUserCollectEntityRepository.deleteById(id);
    }
    
    public void update(Long id, TbUserCollectEntityUpdateVO vO) {
        TbUserCollectEntity bean = requireOne(id);
        BeanUtils.copyProperties(vO, bean);
        tbUserCollectEntityRepository.save(bean);
    }
    
    public TbUserCollectEntityDTO getById(Long id) {
        TbUserCollectEntity original = requireOne(id);
        return toDTO(original);
    }
    
    public Page<TbUserCollectEntityDTO> query(TbUserCollectEntityQueryVO vO) {
        throw new UnsupportedOperationException();
    }
    
    private TbUserCollectEntityDTO toDTO(TbUserCollectEntity original) {
        TbUserCollectEntityDTO bean = new TbUserCollectEntityDTO();
        BeanUtils.copyProperties(original, bean);
        return bean;
    }
    
    private TbUserCollectEntity requireOne(Long id) {
        return tbUserCollectEntityRepository.findById(id)
                                            .orElseThrow(() -> new NoSuchElementException("Resource not found: " + id));
    }
}
