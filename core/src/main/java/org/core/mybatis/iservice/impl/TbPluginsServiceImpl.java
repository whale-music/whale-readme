package org.core.mybatis.iservice.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.core.mybatis.iservice.TbPluginService;
import org.core.mybatis.mapper.TbPluginMapper;
import org.core.mybatis.pojo.TbPluginPojo;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Sakura
 * @since 2023-03-29
 */
@Service
public class TbPluginsServiceImpl extends ServiceImpl<TbPluginMapper, TbPluginPojo> implements TbPluginService {

}
