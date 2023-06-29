package org.core.mybatis.iservice.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.core.mybatis.iservice.TbPluginTaskService;
import org.core.mybatis.mapper.TbPluginTaskMapper;
import org.core.mybatis.pojo.TbPluginTaskPojo;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 插件任务表 服务实现类
 * </p>
 *
 * @author Sakura
 * @since 2023-06-28
 */
@Service
public class TbPluginTaskServiceImpl extends ServiceImpl<TbPluginTaskMapper, TbPluginTaskPojo> implements TbPluginTaskService {

}
