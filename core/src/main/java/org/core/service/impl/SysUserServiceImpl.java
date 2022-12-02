package org.core.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.core.mapper.SysUserMapper;
import org.core.pojo.SysUserPojo;
import org.core.service.SysUserService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统用户表 服务实现类
 * </p>
 *
 * @author Sakura
 * @since 2022-12-02
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUserPojo> implements SysUserService {

}
