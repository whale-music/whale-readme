package org.core.mybatis.model.convert;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.core.mybatis.pojo.SysUserPojo;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class UserConvert extends SysUserPojo {
    public UserConvert(SysUserPojo user) {
        setId(user.getId());
        setUsername(user.getUsername());
        setNickname(user.getNickname());
        setPassword(user.getPassword());
        setSignature(user.getSignature());
        setAccountType(user.getAccountType());
        setStatus(user.getStatus());
        setRoleName(user.getRoleName());
        setLastLoginIp(user.getLastLoginIp());
        setLastLoginTime(user.getLastLoginTime());
        setCreateTime(user.getCreateTime());
        setUpdateTime(user.getUpdateTime());
    }
    
    private String avatarUrl;
    private String backgroundPicUrl;
}
