package org.musicbox.service.impl;

import org.musicbox.pojo.SysUserPojo;
import org.musicbox.mapper.SysUserMapper;
import org.musicbox.service.SysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统用户表 服务实现类
 * </p>
 *
 * @author Sakura
 * @since 2022-10-22
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUserPojo> implements SysUserService {

}
