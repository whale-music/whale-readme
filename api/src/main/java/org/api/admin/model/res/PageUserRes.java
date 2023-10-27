package org.api.admin.model.res;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.core.mybatis.pojo.SysUserPojo;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class PageUserRes extends SysUserPojo {
    public PageUserRes(SysUserPojo user) {
        super(user.getId(),
                user.getUsername(),
                user.getNickname(),
                user.getPassword(),
                user.getSignature(),
                user.getAccountType(),
                user.getStatus(),
                user.getRoleName(),
                user.getLastLoginIp(),
                user.getLastLoginTime(),
                user.getLoginDevice(),
                user.getCreateTime(),
                user.getUpdateTime());
    }
}
