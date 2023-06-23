package org.core.jpa.service;

import org.core.jpa.entity.TbCollectMusicEntity;
import org.core.jpa.model.dto.TbCollectMusicEntityDTO;
import org.core.jpa.model.vo.TbCollectMusicEntityQueryVO;
import org.core.jpa.model.vo.TbCollectMusicEntityUpdateVO;
import org.core.jpa.model.vo.TbCollectMusicEntityVO;
import org.core.jpa.repository.TbCollectMusicEntityRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class TbCollectMusicEntityService {
    
    @Autowired
    private TbCollectMusicEntityRepository tbCollectMusicEntityRepository;
    
    public Long save(TbCollectMusicEntityVO vO) {
        TbCollectMusicEntity bean = new TbCollectMusicEntity();
        BeanUtils.copyProperties(vO, bean);
        bean = tbCollectMusicEntityRepository.save(bean);
        return bean.getCollectId();
    }
    
    public void delete(Long id) {
        tbCollectMusicEntityRepository.deleteById(id);
    }
    
    public void update(Long id, TbCollectMusicEntityUpdateVO vO) {
        TbCollectMusicEntity bean = requireOne(id);
        BeanUtils.copyProperties(vO, bean);
        tbCollectMusicEntityRepository.save(bean);
    }
    
    public TbCollectMusicEntityDTO getById(Long id) {
        TbCollectMusicEntity original = requireOne(id);
        return toDTO(original);
    }
    
    public Page<TbCollectMusicEntityDTO> query(TbCollectMusicEntityQueryVO vO) {
        throw new UnsupportedOperationException();
    }
    
    private TbCollectMusicEntityDTO toDTO(TbCollectMusicEntity original) {
        TbCollectMusicEntityDTO bean = new TbCollectMusicEntityDTO();
        BeanUtils.copyProperties(original, bean);
        return bean;
    }
    
    private TbCollectMusicEntity requireOne(Long id) {
        return tbCollectMusicEntityRepository.findById(id)
                                             .orElseThrow(() -> new NoSuchElementException("Resource not found: " + id));
    }
}
