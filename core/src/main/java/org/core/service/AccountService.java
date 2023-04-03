package org.core.service;


import org.core.iservice.SysUserService;
import org.core.pojo.SysUserPojo;

public interface AccountService extends SysUserService {
    
    void createAccount(SysUserPojo user);
    
    SysUserPojo login(String phone, String password);
}
