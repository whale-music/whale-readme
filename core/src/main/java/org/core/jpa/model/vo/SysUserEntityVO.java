package org.core.jpa.model.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;


@Data
@Schema(name = "保存 系统用户表")
public class SysUserEntityVO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    
    /**
     * 系统用户ID
     */
    @NotNull(message = "id can not null")
    @Schema(name = "系统用户ID")
    private Long id;
    
    
    /**
     * 登录用户名
     */
    @NotNull(message = "username can not null")
    @Schema(name = "登录用户名")
    private String username;
    
    
    /**
     * 登录显示昵称
     */
    @NotNull(message = "nickname can not null")
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
