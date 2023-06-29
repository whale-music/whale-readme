package org.core.mybatis.iservice.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.core.mybatis.iservice.TbUserMvService;
import org.core.mybatis.mapper.TbUserMvMapper;
import org.core.mybatis.pojo.TbUserMvPojo;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户收藏mv 服务实现类
 * </p>
 *
 * @author Sakura
 * @since 2023-06-28
 */
@Service
public class TbUserMvServiceImpl extends ServiceImpl<TbUserMvMapper, TbUserMvPojo> implements TbUserMvService {

}
