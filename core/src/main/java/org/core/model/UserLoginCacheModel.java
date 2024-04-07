package org.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.core.mybatis.pojo.SysUserPojo;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserLoginCacheModel extends SysUserPojo {
    private LocalDateTime generatedDate;
    
    private Date expires;
    
    private String token;
    private String tokenKey;
    
    private String refreshToken;
    private String refreshTokenKey;
    
    public UserLoginCacheModel(SysUserPojo pojo, String tokenKey, String token, String refreshTokenKey, String refreshToken, Date expires, LocalDateTime generatedDate) {
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
        this.token = token;
        this.tokenKey = tokenKey;
        this.refreshToken = refreshToken;
        this.refreshTokenKey = refreshTokenKey;
        this.expires = expires;
        this.generatedDate = generatedDate;
    }
    
    public UserLoginCacheModel(SysUserPojo pojo, String token, String tokenKey, LocalDateTime generatedDate) {
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
        this.token = token;
        this.tokenKey = tokenKey;
        this.generatedDate = generatedDate;
    }
    
    public SysUserPojo getUser() {
        return this;
    }
}
