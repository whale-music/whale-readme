package org.core.mybatis.iservice.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.core.mybatis.iservice.SysLogoService;
import org.core.mybatis.mapper.SysLogoMapper;
import org.core.mybatis.pojo.SysLogPojo;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统日志 服务实现类
 * </p>
 *
 * @author Sakura
 * @since 2024-03-25
 */
@Service
public class SysLogServiceImpl extends ServiceImpl<SysLogoMapper, SysLogPojo> implements SysLogoService {

}
