package org.core.jpa.service;

import org.core.jpa.entity.TbUserArtistEntity;
import org.core.jpa.model.dto.TbUserArtistEntityDTO;
import org.core.jpa.model.vo.TbUserArtistEntityQueryVO;
import org.core.jpa.model.vo.TbUserArtistEntityUpdateVO;
import org.core.jpa.model.vo.TbUserArtistEntityVO;
import org.core.jpa.repository.TbUserArtistEntityRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class TbUserArtistEntityService {
    
    @Autowired
    private TbUserArtistEntityRepository tbUserArtistEntityRepository;
    
    public Long save(TbUserArtistEntityVO vO) {
        TbUserArtistEntity bean = new TbUserArtistEntity();
        BeanUtils.copyProperties(vO, bean);
        bean = tbUserArtistEntityRepository.save(bean);
        return bean.getUserId();
    }
    
    public void delete(Long id) {
        tbUserArtistEntityRepository.deleteById(id);
    }
    
    public void update(Long id, TbUserArtistEntityUpdateVO vO) {
        TbUserArtistEntity bean = requireOne(id);
        BeanUtils.copyProperties(vO, bean);
        tbUserArtistEntityRepository.save(bean);
    }
    
    public TbUserArtistEntityDTO getById(Long id) {
        TbUserArtistEntity original = requireOne(id);
        return toDTO(original);
    }
    
    public Page<TbUserArtistEntityDTO> query(TbUserArtistEntityQueryVO vO) {
        throw new UnsupportedOperationException();
    }
    
    private TbUserArtistEntityDTO toDTO(TbUserArtistEntity original) {
        TbUserArtistEntityDTO bean = new TbUserArtistEntityDTO();
        BeanUtils.copyProperties(original, bean);
        return bean;
    }
    
    private TbUserArtistEntity requireOne(Long id) {
        return tbUserArtistEntityRepository.findById(id)
                                           .orElseThrow(() -> new NoSuchElementException("Resource not found: " + id));
    }
}
