package org.core.mybatis.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 系统用户表
 * </p>
 *
 * @author Sakura
 * @since 2023-06-25
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@TableName("sys_user")
@ApiModel(value = "SysUserPojo对象", description = "系统用户表")
public class SysUserPojo extends Model<SysUserPojo> implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("系统用户ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("登录用户名")
    @TableField("username")
    private String username;

    @ApiModelProperty("登录显示昵称")
    @TableField("nickname")
    private String nickname;

    @ApiModelProperty("用户密码")
    @TableField("password")
    private String password;

    @ApiModelProperty("个性签名")
    @TableField("signature")
    private String signature;

    @ApiModelProperty("账户类型")
    @TableField("account_type")
    private Integer accountType;

    @ApiModelProperty("最后登录IP")
    @TableField("last_login_ip")
    private String lastLoginIp;

    @ApiModelProperty("最后登录时间")
    @TableField("last_login_time")
    private LocalDateTime lastLoginTime;

    @ApiModelProperty("创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("修改时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;


}
