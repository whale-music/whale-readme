package org.core.config;

import lombok.extern.slf4j.Slf4j;
import org.core.utils.IDUtil;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;

@Slf4j
public class OrderIdGeneratorConfig implements IdentifierGenerator {
    
    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        return IDUtil.getID();
    }
}