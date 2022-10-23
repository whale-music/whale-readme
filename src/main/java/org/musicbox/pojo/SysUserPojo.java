package org.musicbox.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 系统用户表
 * </p>
 *
 * @author Sakura
 * @since 2022-10-23
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("sys_user")
public class SysUserPojo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 系统用户ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    /**
     * 登录用户名
     */
    @TableField("username")
    private String username;
    
    /**
     * 登录显示昵称
     */
    @TableField("nickname")
    private String nickname;
    
    /**
     * 用户密码
     */
    @TableField("password")
    private String password;
    
    /**
     * 头像URL
     */
    @TableField("avatar_url")
    private String avatarUrl;
    
    /**
     * 背景照片URL
     */
    @TableField("background_url")
    private String backgroundUrl;
    
    /**
     * 个性签名
     */
    @TableField("signature")
    private String signature;
    
    /**
     * 账户类型
     */
    @TableField("account_type")
    private Integer accountType;
    
    /**
     * 最后登录IP
     */
    @TableField("last_login_ip")
    private String lastLoginIp;
    
    /**
     * 最后登录时间
     */
    @TableField("last_login_time")
    private LocalDateTime lastLoginTime;
    
    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;
    
    /**
     * 修改时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;
    
    
}
