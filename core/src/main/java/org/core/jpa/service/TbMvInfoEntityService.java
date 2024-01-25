package org.core.jpa.service;

import org.core.jpa.repository.TbMvInfoEntityRepository;
import org.springframework.stereotype.Service;

@Service
public class TbMvInfoEntityService {
    
    private final TbMvInfoEntityRepository tbMvEntityRepository;
    
    public TbMvInfoEntityService(TbMvInfoEntityRepository tbMvEntityRepository) {
        this.tbMvEntityRepository = tbMvEntityRepository;
    }
}
