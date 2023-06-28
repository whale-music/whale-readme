package org.core.jpa.service;

import org.core.jpa.entity.TbMvArtistEntity;
import org.core.jpa.model.dto.TbMvArtistEntityDTO;
import org.core.jpa.model.vo.TbMvArtistEntityQueryVO;
import org.core.jpa.model.vo.TbMvArtistEntityUpdateVO;
import org.core.jpa.model.vo.TbMvArtistEntityVO;
import org.core.jpa.repository.TbMvArtistEntityRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class TbMvArtistEntityService {
    
    @Autowired
    private TbMvArtistEntityRepository tbMvArtistEntityRepository;
    
    public Long save(TbMvArtistEntityVO vO) {
        TbMvArtistEntity bean = new TbMvArtistEntity();
        BeanUtils.copyProperties(vO, bean);
        bean = tbMvArtistEntityRepository.save(bean);
        return bean.getMvId();
    }
    
    public void delete(Long id) {
        tbMvArtistEntityRepository.deleteById(id);
    }
    
    public void update(Long id, TbMvArtistEntityUpdateVO vO) {
        TbMvArtistEntity bean = requireOne(id);
        BeanUtils.copyProperties(vO, bean);
        tbMvArtistEntityRepository.save(bean);
    }
    
    public TbMvArtistEntityDTO getById(Long id) {
        TbMvArtistEntity original = requireOne(id);
        return toDTO(original);
    }
    
    public Page<TbMvArtistEntityDTO> query(TbMvArtistEntityQueryVO vO) {
        throw new UnsupportedOperationException();
    }
    
    private TbMvArtistEntityDTO toDTO(TbMvArtistEntity original) {
        TbMvArtistEntityDTO bean = new TbMvArtistEntityDTO();
        BeanUtils.copyProperties(original, bean);
        return bean;
    }
    
    private TbMvArtistEntity requireOne(Long id) {
        return tbMvArtistEntityRepository.findById(id)
                                         .orElseThrow(() -> new NoSuchElementException("Resource not found: " + id));
    }
}
