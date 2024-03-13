package org.core.jpa.model.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Schema(title = "系统用户表")
public class SysUserEntityDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    /**
     * 系统用户ID
     */
    @Schema(name = "系统用户ID")
    private Long id;
    
    
    /**
     * 登录用户名
     */
    @Schema(name = "登录用户名")
    private String username;
    
    
    /**
     * 登录显示昵称
     */
    @Schema(name = "登录显示昵称")
    private String nickname;
    
    
    /**
     * 用户密码
     */
    @Schema(name = "用户密码")
    private String password;
    
    
    /**
     * 个性签名
     */
    @Schema(name = "个性签名")
    private String signature;
    
    
    /**
     * 账户类型
     */
    @Schema(name = "账户类型")
    private Integer accountType;
    
    
    /**
     * 最后登录IP
     */
    @Schema(name = "最后登录IP")
    private String lastLoginIp;
    
    
    /**
     * 最后登录时间
     */
    @Schema(name = "最后登录时间")
    private LocalDateTime lastLoginTime;
    
    
    /**
     * 创建时间
     */
    @Schema(name = "创建时间")
    private LocalDateTime createTime;
    
    
    /**
     * 修改时间
     */
    @Schema(name = "修改时间")
    private LocalDateTime updateTime;
    
}
