package org.core.jpa.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;


@Data
@ApiModel("保存 系统用户表")
public class SysUserEntityVO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    
    /**
     * 系统用户ID
     */
    @NotNull(message = "id can not null")
    @ApiModelProperty("系统用户ID")
    private Long id;
    
    
    /**
     * 登录用户名
     */
    @NotNull(message = "username can not null")
    @ApiModelProperty("登录用户名")
    private String username;
    
    
    /**
     * 登录显示昵称
     */
    @NotNull(message = "nickname can not null")
    @ApiModelProperty("登录显示昵称")
    private String nickname;
    
    
    /**
     * 用户密码
     */
    @ApiModelProperty("用户密码")
    private String password;
    
    
    /**
     * 个性签名
     */
    @ApiModelProperty("个性签名")
    private String signature;
    
    
    /**
     * 账户类型
     */
    @ApiModelProperty("账户类型")
    private Integer accountType;
    
    
    /**
     * 最后登录IP
     */
    @ApiModelProperty("最后登录IP")
    private String lastLoginIp;
    
    
    /**
     * 最后登录时间
     */
    @ApiModelProperty("最后登录时间")
    private LocalDateTime lastLoginTime;
    
    
    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
    
    
    /**
     * 修改时间
     */
    @ApiModelProperty("修改时间")
    private LocalDateTime updateTime;
    
}
