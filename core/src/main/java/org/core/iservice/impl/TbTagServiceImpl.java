package org.core.iservice.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.core.iservice.TbTagService;
import org.core.mapper.TbTagMapper;
import org.core.pojo.TbTagPojo;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 标签表（风格） 服务实现类
 * </p>
 *
 * @author Sakura
 * @since 2022-12-02
 */
@Service
public class TbTagServiceImpl extends ServiceImpl<TbTagMapper, TbTagPojo> implements TbTagService {

}
