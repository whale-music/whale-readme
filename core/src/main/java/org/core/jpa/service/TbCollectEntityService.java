package org.core.jpa.service;

import org.core.jpa.entity.TbCollectEntity;
import org.core.jpa.model.dto.TbCollectEntityDTO;
import org.core.jpa.model.vo.TbCollectEntityQueryVO;
import org.core.jpa.model.vo.TbCollectEntityUpdateVO;
import org.core.jpa.model.vo.TbCollectEntityVO;
import org.core.jpa.repository.TbCollectEntityRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class TbCollectEntityService {
    
    private final TbCollectEntityRepository tbCollectEntityRepository;
    
    public TbCollectEntityService(TbCollectEntityRepository tbCollectEntityRepository) {
        this.tbCollectEntityRepository = tbCollectEntityRepository;
    }
    
    public Long save(TbCollectEntityVO vO) {
        TbCollectEntity bean = new TbCollectEntity();
        BeanUtils.copyProperties(vO, bean);
        bean = tbCollectEntityRepository.save(bean);
        return bean.getId();
    }
    
    public void delete(Long id) {
        tbCollectEntityRepository.deleteById(id);
    }
    
    public void update(Long id, TbCollectEntityUpdateVO vO) {
        TbCollectEntity bean = requireOne(id);
        BeanUtils.copyProperties(vO, bean);
        tbCollectEntityRepository.save(bean);
    }
    
    public TbCollectEntityDTO getById(Long id) {
        TbCollectEntity original = requireOne(id);
        return toDTO(original);
    }
    
    public Page<TbCollectEntityDTO> query(TbCollectEntityQueryVO vO) {
        throw new UnsupportedOperationException();
    }
    
    private TbCollectEntityDTO toDTO(TbCollectEntity original) {
        TbCollectEntityDTO bean = new TbCollectEntityDTO();
        BeanUtils.copyProperties(original, bean);
        return bean;
    }
    
    private TbCollectEntity requireOne(Long id) {
        return tbCollectEntityRepository.findById(id)
                                        .orElseThrow(() -> new NoSuchElementException("Resource not found: " + id));
    }
}
