package org.core.jpa.service;

import org.core.jpa.entity.TbAlbumEntity;
import org.core.jpa.model.dto.TbAlbumEntityDTO;
import org.core.jpa.model.vo.TbAlbumEntityQueryVO;
import org.core.jpa.model.vo.TbAlbumEntityUpdateVO;
import org.core.jpa.model.vo.TbAlbumEntityVO;
import org.core.jpa.repository.TbAlbumEntityRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class TbAlbumEntityService {
    
    private final TbAlbumEntityRepository tbAlbumEntityRepository;
    
    public TbAlbumEntityService(TbAlbumEntityRepository tbAlbumEntityRepository) {
        this.tbAlbumEntityRepository = tbAlbumEntityRepository;
    }
    
    public Long save(TbAlbumEntityVO vO) {
        TbAlbumEntity bean = new TbAlbumEntity();
        BeanUtils.copyProperties(vO, bean);
        bean = tbAlbumEntityRepository.save(bean);
        return bean.getId();
    }
    
    public void delete(Long id) {
        tbAlbumEntityRepository.deleteById(id);
    }
    
    public void update(Long id, TbAlbumEntityUpdateVO vO) {
        TbAlbumEntity bean = requireOne(id);
        BeanUtils.copyProperties(vO, bean);
        tbAlbumEntityRepository.save(bean);
    }
    
    public TbAlbumEntityDTO getById(Long id) {
        TbAlbumEntity original = requireOne(id);
        return toDTO(original);
    }
    
    public Page<TbAlbumEntityDTO> query(TbAlbumEntityQueryVO vO) {
        throw new UnsupportedOperationException();
    }
    
    private TbAlbumEntityDTO toDTO(TbAlbumEntity original) {
        TbAlbumEntityDTO bean = new TbAlbumEntityDTO();
        BeanUtils.copyProperties(original, bean);
        return bean;
    }
    
    private TbAlbumEntity requireOne(Long id) {
        return tbAlbumEntityRepository.findById(id)
                                      .orElseThrow(() -> new NoSuchElementException("Resource not found: " + id));
    }
}
