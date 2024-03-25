package org.core.jpa.service;

import org.core.jpa.model.dto.SysDictTypeEntityDTO;
import org.core.jpa.model.vo.SysDictTypeEntityQueryVO;
import org.core.jpa.repository.SysLogEntityRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class SysLogEntityService {
    
    private final SysLogEntityRepository sysLogEntityRepository;
    
    public SysLogEntityService(SysLogEntityRepository sysDictTypeEntityRepository) {
        this.sysLogEntityRepository = sysDictTypeEntityRepository;
    }
    
    public void delete(Long id) {
        sysLogEntityRepository.deleteById(id);
    }
    
    public Page<SysDictTypeEntityDTO> query(SysDictTypeEntityQueryVO vO) {
        throw new UnsupportedOperationException();
    }
}
