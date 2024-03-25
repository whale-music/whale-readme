package org.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.core.mybatis.pojo.SysUserPojo;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserLoginCacheModel extends SysUserPojo {
    private LocalDateTime generatedDate;
    
    public UserLoginCacheModel(SysUserPojo pojo, LocalDateTime generatedDate) {
        super(pojo.getId(),
                pojo.getUsername(),
                pojo.getNickname(),
                pojo.getPassword(),
                pojo.getSignature(),
                pojo.getAccountType(),
                pojo.getStatus(),
                pojo.getRoleName(),
                pojo.getLastLoginIp(),
                pojo.getLastLoginTime(),
                pojo.getLoginDevice(),
                pojo.getSubAccountPassword(),
                pojo.getCreateTime(),
                pojo.getUpdateTime());
        this.generatedDate = generatedDate;
    }
    
    public SysUserPojo getUser() {
        return this;
    }
}
