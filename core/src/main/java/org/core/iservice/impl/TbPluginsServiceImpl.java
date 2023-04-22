package org.core.iservice.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.core.iservice.TbPluginService;
import org.core.mapper.TbPluginMapper;
import org.core.pojo.TbPluginPojo;
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
