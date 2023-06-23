package org.core.jpa.service;

import org.core.jpa.entity.TbArtistEntity;
import org.core.jpa.model.dto.TbArtistEntityDTO;
import org.core.jpa.model.vo.TbArtistEntityQueryVO;
import org.core.jpa.model.vo.TbArtistEntityUpdateVO;
import org.core.jpa.model.vo.TbArtistEntityVO;
import org.core.jpa.repository.TbArtistEntityRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class TbArtistEntityService {
    
    @Autowired
    private TbArtistEntityRepository tbArtistEntityRepository;
    
    public Long save(TbArtistEntityVO vO) {
        TbArtistEntity bean = new TbArtistEntity();
        BeanUtils.copyProperties(vO, bean);
        bean = tbArtistEntityRepository.save(bean);
        return bean.getId();
    }
    
    public void delete(Long id) {
        tbArtistEntityRepository.deleteById(id);
    }
    
    public void update(Long id, TbArtistEntityUpdateVO vO) {
        TbArtistEntity bean = requireOne(id);
        BeanUtils.copyProperties(vO, bean);
        tbArtistEntityRepository.save(bean);
    }
    
    public TbArtistEntityDTO getById(Long id) {
        TbArtistEntity original = requireOne(id);
        return toDTO(original);
    }
    
    public Page<TbArtistEntityDTO> query(TbArtistEntityQueryVO vO) {
        throw new UnsupportedOperationException();
    }
    
    private TbArtistEntityDTO toDTO(TbArtistEntity original) {
        TbArtistEntityDTO bean = new TbArtistEntityDTO();
        BeanUtils.copyProperties(original, bean);
        return bean;
    }
    
    private TbArtistEntity requireOne(Long id) {
        return tbArtistEntityRepository.findById(id)
                                       .orElseThrow(() -> new NoSuchElementException("Resource not found: " + id));
    }
}
