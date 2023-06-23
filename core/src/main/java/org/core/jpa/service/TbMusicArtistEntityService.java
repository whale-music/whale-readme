package org.core.jpa.service;

import org.core.jpa.entity.TbMusicArtistEntity;
import org.core.jpa.model.dto.TbMusicArtistEntityDTO;
import org.core.jpa.model.vo.TbMusicArtistEntityQueryVO;
import org.core.jpa.model.vo.TbMusicArtistEntityUpdateVO;
import org.core.jpa.model.vo.TbMusicArtistEntityVO;
import org.core.jpa.repository.TbMusicArtistEntityRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class TbMusicArtistEntityService {
    
    @Autowired
    private TbMusicArtistEntityRepository tbMusicArtistEntityRepository;
    
    public Long save(TbMusicArtistEntityVO vO) {
        TbMusicArtistEntity bean = new TbMusicArtistEntity();
        BeanUtils.copyProperties(vO, bean);
        bean = tbMusicArtistEntityRepository.save(bean);
        return bean.getMusicId();
    }
    
    public void delete(Long id) {
        tbMusicArtistEntityRepository.deleteById(id);
    }
    
    public void update(Long id, TbMusicArtistEntityUpdateVO vO) {
        TbMusicArtistEntity bean = requireOne(id);
        BeanUtils.copyProperties(vO, bean);
        tbMusicArtistEntityRepository.save(bean);
    }
    
    public TbMusicArtistEntityDTO getById(Long id) {
        TbMusicArtistEntity original = requireOne(id);
        return toDTO(original);
    }
    
    public Page<TbMusicArtistEntityDTO> query(TbMusicArtistEntityQueryVO vO) {
        throw new UnsupportedOperationException();
    }
    
    private TbMusicArtistEntityDTO toDTO(TbMusicArtistEntity original) {
        TbMusicArtistEntityDTO bean = new TbMusicArtistEntityDTO();
        BeanUtils.copyProperties(original, bean);
        return bean;
    }
    
    private TbMusicArtistEntity requireOne(Long id) {
        return tbMusicArtistEntityRepository.findById(id)
                                            .orElseThrow(() -> new NoSuchElementException("Resource not found: " + id));
    }
}
