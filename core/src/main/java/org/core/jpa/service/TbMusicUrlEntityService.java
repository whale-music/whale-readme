package org.core.jpa.service;

import org.core.jpa.entity.TbMusicUrlEntity;
import org.core.jpa.model.dto.TbMusicUrlEntityDTO;
import org.core.jpa.model.vo.TbMusicUrlEntityQueryVO;
import org.core.jpa.model.vo.TbMusicUrlEntityUpdateVO;
import org.core.jpa.model.vo.TbMusicUrlEntityVO;
import org.core.jpa.repository.TbMusicUrlEntityRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class TbMusicUrlEntityService {
    
    @Autowired
    private TbMusicUrlEntityRepository tbMusicUrlEntityRepository;
    
    public Long save(TbMusicUrlEntityVO vO) {
        TbMusicUrlEntity bean = new TbMusicUrlEntity();
        BeanUtils.copyProperties(vO, bean);
        bean = tbMusicUrlEntityRepository.save(bean);
        return bean.getId();
    }
    
    public void delete(Long id) {
        tbMusicUrlEntityRepository.deleteById(id);
    }
    
    public void update(Long id, TbMusicUrlEntityUpdateVO vO) {
        TbMusicUrlEntity bean = requireOne(id);
        BeanUtils.copyProperties(vO, bean);
        tbMusicUrlEntityRepository.save(bean);
    }
    
    public TbMusicUrlEntityDTO getById(Long id) {
        TbMusicUrlEntity original = requireOne(id);
        return toDTO(original);
    }
    
    public Page<TbMusicUrlEntityDTO> query(TbMusicUrlEntityQueryVO vO) {
        throw new UnsupportedOperationException();
    }
    
    private TbMusicUrlEntityDTO toDTO(TbMusicUrlEntity original) {
        TbMusicUrlEntityDTO bean = new TbMusicUrlEntityDTO();
        BeanUtils.copyProperties(original, bean);
        return bean;
    }
    
    private TbMusicUrlEntity requireOne(Long id) {
        return tbMusicUrlEntityRepository.findById(id)
                                         .orElseThrow(() -> new NoSuchElementException("Resource not found: " + id));
    }
}
