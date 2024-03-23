package org.core.jpa.service;

import org.core.jpa.entity.TbHistoryEntity;
import org.core.jpa.model.dto.TbHistoryEntityDTO;
import org.core.jpa.model.vo.TbHistoryEntityQueryVO;
import org.core.jpa.model.vo.TbHistoryEntityUpdateVO;
import org.core.jpa.model.vo.TbHistoryEntityVO;
import org.core.jpa.repository.TbHistoryEntityRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class TbHistoryEntityService {
    
    private final TbHistoryEntityRepository tbHistoryEntityRepository;
    
    public TbHistoryEntityService(TbHistoryEntityRepository tbHistoryEntityRepository) {
        this.tbHistoryEntityRepository = tbHistoryEntityRepository;
    }
    
    public Long save(TbHistoryEntityVO vO) {
        TbHistoryEntity bean = new TbHistoryEntity();
        BeanUtils.copyProperties(vO, bean);
        bean = tbHistoryEntityRepository.save(bean);
        return bean.getId();
    }
    
    public void delete(Long id) {
        tbHistoryEntityRepository.deleteById(id);
    }
    
    public void delete(List<Long> ids) {
        tbHistoryEntityRepository.deleteAllByIdInBatch(ids);
    }
    
    public void update(Long id, TbHistoryEntityUpdateVO vO) {
        TbHistoryEntity bean = requireOne(id);
        BeanUtils.copyProperties(vO, bean);
        tbHistoryEntityRepository.save(bean);
    }
    
    public TbHistoryEntityDTO getById(Long id) {
        TbHistoryEntity original = requireOne(id);
        return toDTO(original);
    }
    
    public Page<TbHistoryEntityDTO> query(TbHistoryEntityQueryVO vO) {
        throw new UnsupportedOperationException();
    }
    
    private TbHistoryEntityDTO toDTO(TbHistoryEntity original) {
        TbHistoryEntityDTO bean = new TbHistoryEntityDTO();
        BeanUtils.copyProperties(original, bean);
        return bean;
    }
    
    private TbHistoryEntity requireOne(Long id) {
        return tbHistoryEntityRepository.findById(id)
                                        .orElseThrow(() -> new NoSuchElementException("Resource not found: " + id));
    }
    
    public Page<TbHistoryEntity> listByNikeName(Long userId, Byte type, Pageable page) {
        return tbHistoryEntityRepository.findByUserIdEqualsAndTypeEquals(userId, type, page);
    }
    
    public Page<TbHistoryEntity> list(PageRequest page) {
        return tbHistoryEntityRepository.findAll(page);
    }
}
