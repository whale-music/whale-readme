package org.core.mybatis.iservice.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.core.mybatis.iservice.TbTagService;
import org.core.mybatis.mapper.TbTagMapper;
import org.core.mybatis.pojo.TbTagPojo;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 标签表（风格） 服务实现类
 * </p>
 *
 * @author Sakura
 * @since 2023-06-28
 */
@Service
public class TbTagServiceImpl extends ServiceImpl<TbTagMapper, TbTagPojo> implements TbTagService {

}
