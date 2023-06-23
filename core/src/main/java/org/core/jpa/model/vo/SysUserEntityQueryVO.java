package org.core.jpa.model.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@ApiModel("自定义查询 系统用户表")
public class SysUserEntityQueryVO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    
    /**
     * 系统用户ID
     */
    @ApiModelProperty("系统用户ID")
    private Long id;
    
    
    /**
     * 登录用户名
     */
    @ApiModelProperty("登录用户名")
    private String username;
    
    
    /**
     * 登录显示昵称
     */
    @ApiModelProperty("登录显示昵称")
    private String nickname;
    
    
    /**
     * 用户密码
     */
    @ApiModelProperty("用户密码")
    private String password;
    
    
    /**
     * 头像URL ID
     */
    @ApiModelProperty("头像URL ID")
    private Long avatarUrl;
    
    
    /**
     * 背景照片URL
     */
    @ApiModelProperty("背景照片URL")
    private String backgroundUrl;
    
    
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
    private Date lastLoginTime;
    
    
    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;
    
    
    /**
     * 修改时间
     */
    @ApiModelProperty("修改时间")
    private Date updateTime;
    
}
