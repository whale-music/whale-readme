package org.core.mybatis.iservice.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.core.mybatis.iservice.TbResourceService;
import org.core.mybatis.mapper.TbResourceMapper;
import org.core.mybatis.pojo.TbResourcePojo;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 存储地址 服务实现类
 * </p>
 *
 * @author Sakura
 * @since 2023-06-28
 */
@Service
public class TbResourceServiceImpl extends ServiceImpl<TbResourceMapper, TbResourcePojo> implements TbResourceService {

}
