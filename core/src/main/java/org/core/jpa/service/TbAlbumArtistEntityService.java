package org.core.jpa.service;

import org.core.jpa.entity.TbAlbumArtistEntity;
import org.core.jpa.model.dto.TbAlbumArtistEntityDTO;
import org.core.jpa.model.vo.TbAlbumArtistEntityQueryVO;
import org.core.jpa.model.vo.TbAlbumArtistEntityUpdateVO;
import org.core.jpa.model.vo.TbAlbumArtistEntityVO;
import org.core.jpa.repository.TbAlbumArtistEntityRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class TbAlbumArtistEntityService {
    
    @Autowired
    private TbAlbumArtistEntityRepository tbAlbumArtistEntityRepository;
    
    public Long save(TbAlbumArtistEntityVO vO) {
        TbAlbumArtistEntity bean = new TbAlbumArtistEntity();
        BeanUtils.copyProperties(vO, bean);
        bean = tbAlbumArtistEntityRepository.save(bean);
        return bean.getTbAlbumByAlbumId().getId();
    }
    
    public void delete(Long id) {
        tbAlbumArtistEntityRepository.deleteById(id);
    }
    
    public void update(Long id, TbAlbumArtistEntityUpdateVO vO) {
        TbAlbumArtistEntity bean = requireOne(id);
        BeanUtils.copyProperties(vO, bean);
        tbAlbumArtistEntityRepository.save(bean);
    }
    
    public TbAlbumArtistEntityDTO getById(Long id) {
        TbAlbumArtistEntity original = requireOne(id);
        return toDTO(original);
    }
    
    public Page<TbAlbumArtistEntityDTO> query(TbAlbumArtistEntityQueryVO vO) {
        throw new UnsupportedOperationException();
    }
    
    private TbAlbumArtistEntityDTO toDTO(TbAlbumArtistEntity original) {
        TbAlbumArtistEntityDTO bean = new TbAlbumArtistEntityDTO();
        BeanUtils.copyProperties(original, bean);
        return bean;
    }
    
    private TbAlbumArtistEntity requireOne(Long id) {
        return tbAlbumArtistEntityRepository.findById(id)
                                            .orElseThrow(() -> new NoSuchElementException("Resource not found: " + id));
    }
}
