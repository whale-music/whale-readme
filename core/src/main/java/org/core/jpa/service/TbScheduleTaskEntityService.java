package org.core.jpa.service;

import org.core.jpa.entity.TbScheduleTaskEntity;
import org.core.jpa.model.dto.TbScheduleTaskEntityDTO;
import org.core.jpa.model.vo.TbScheduleTaskEntityQueryVO;
import org.core.jpa.model.vo.TbScheduleTaskEntityUpdateVO;
import org.core.jpa.model.vo.TbScheduleTaskEntityVO;
import org.core.jpa.repository.TbScheduleTaskEntityRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class TbScheduleTaskEntityService {
    
    private final TbScheduleTaskEntityRepository tbScheduleTaskEntityRepository;
    
    public TbScheduleTaskEntityService(TbScheduleTaskEntityRepository tbScheduleTaskEntityRepository) {
        this.tbScheduleTaskEntityRepository = tbScheduleTaskEntityRepository;
    }
    
    public Long save(TbScheduleTaskEntityVO vO) {
        TbScheduleTaskEntity bean = new TbScheduleTaskEntity();
        BeanUtils.copyProperties(vO, bean);
        bean = tbScheduleTaskEntityRepository.save(bean);
        return bean.getId();
    }
    
    public void delete(Long id) {
        tbScheduleTaskEntityRepository.deleteById(id);
    }
    
    public void update(Long id, TbScheduleTaskEntityUpdateVO vO) {
        TbScheduleTaskEntity bean = requireOne(id);
        BeanUtils.copyProperties(vO, bean);
        tbScheduleTaskEntityRepository.save(bean);
    }
    
    public TbScheduleTaskEntityDTO getById(Long id) {
        TbScheduleTaskEntity original = requireOne(id);
        return toDTO(original);
    }
    
    public Page<TbScheduleTaskEntityDTO> query(TbScheduleTaskEntityQueryVO vO) {
        throw new UnsupportedOperationException();
    }
    
    private TbScheduleTaskEntityDTO toDTO(TbScheduleTaskEntity original) {
        TbScheduleTaskEntityDTO bean = new TbScheduleTaskEntityDTO();
        BeanUtils.copyProperties(original, bean);
        return bean;
    }
    
    private TbScheduleTaskEntity requireOne(Long id) {
        return tbScheduleTaskEntityRepository.findById(id)
                                             .orElseThrow(() -> new NoSuchElementException("Resource not found: " + id));
    }
}
