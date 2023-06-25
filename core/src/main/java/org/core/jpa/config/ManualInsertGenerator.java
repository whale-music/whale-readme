package org.core.jpa.config;

import com.github.yitter.idgen.YitIdHelper;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

/**
 * 自定义的主键生成策略，如果填写了主键id，如果数据库中没有这条记录，则新增指定id的记录；否则更新记录
 * <p>
 * 如果不填写主键id，则利用数据库本身的自增策略指定id
 * <p>
 */
public class ManualInsertGenerator implements IdentifierGenerator {
    /**
     * Generate a new identifier.
     *
     * @param session The session from which the request originates
     * @param object  the entity or collection (idbag) for which the id is being generated
     * @return a new identifier
     * @throws HibernateException Indicates trouble generating the identifier
     */
    @Override
    public Object generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        final Object id = session.getEntityPersister(null, object).getIdentifier(object, session);
        if (id != null && Integer.parseInt(String.valueOf(id)) > 0) {
            return id;
        } else {
            return YitIdHelper.nextId();
        }
    }
}
