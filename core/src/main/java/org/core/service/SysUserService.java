package org.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.core.pojo.SysUserPojo;

/**
 * <p>
 * 系统用户表 服务类
 * </p>
 *
 * @author Sakura
 * @since 2022-12-02
 */
public interface SysUserService extends IService<SysUserPojo> {
    void createAccount(SysUserPojo user);
    
    SysUserPojo login(String phone, String password);
}
