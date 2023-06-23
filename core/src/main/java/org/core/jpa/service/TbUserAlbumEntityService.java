package org.core.jpa.service;

import org.core.jpa.entity.TbUserAlbumEntity;
import org.core.jpa.model.dto.TbUserAlbumEntityDTO;
import org.core.jpa.model.vo.TbUserAlbumEntityQueryVO;
import org.core.jpa.model.vo.TbUserAlbumEntityUpdateVO;
import org.core.jpa.model.vo.TbUserAlbumEntityVO;
import org.core.jpa.repository.TbUserAlbumEntityRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class TbUserAlbumEntityService {
    
    @Autowired
    private TbUserAlbumEntityRepository tbUserAlbumEntityRepository;
    
    public Long save(TbUserAlbumEntityVO vO) {
        TbUserAlbumEntity bean = new TbUserAlbumEntity();
        BeanUtils.copyProperties(vO, bean);
        bean = tbUserAlbumEntityRepository.save(bean);
        return bean.getUserId();
    }
    
    public void delete(Long id) {
        tbUserAlbumEntityRepository.deleteById(id);
    }
    
    public void update(Long id, TbUserAlbumEntityUpdateVO vO) {
        TbUserAlbumEntity bean = requireOne(id);
        BeanUtils.copyProperties(vO, bean);
        tbUserAlbumEntityRepository.save(bean);
    }
    
    public TbUserAlbumEntityDTO getById(Long id) {
        TbUserAlbumEntity original = requireOne(id);
        return toDTO(original);
    }
    
    public Page<TbUserAlbumEntityDTO> query(TbUserAlbumEntityQueryVO vO) {
        throw new UnsupportedOperationException();
    }
    
    private TbUserAlbumEntityDTO toDTO(TbUserAlbumEntity original) {
        TbUserAlbumEntityDTO bean = new TbUserAlbumEntityDTO();
        BeanUtils.copyProperties(original, bean);
        return bean;
    }
    
    private TbUserAlbumEntity requireOne(Long id) {
        return tbUserAlbumEntityRepository.findById(id)
                                          .orElseThrow(() -> new NoSuchElementException("Resource not found: " + id));
    }
}
