package org.core.mybatis.iservice.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.core.mybatis.iservice.TbPluginService;
import org.core.mybatis.mapper.TbPluginMapper;
import org.core.mybatis.pojo.TbPluginPojo;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 插件表 服务实现类
 * </p>
 *
 * @author Sakura
 * @since 2023-06-28
 */
@Service
public class TbPluginServiceImpl extends ServiceImpl<TbPluginMapper, TbPluginPojo> implements TbPluginService {

}
