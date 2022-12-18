package org.api.admin.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.core.pojo.SysUserPojo;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class UserVo extends SysUserPojo {
    @ApiModelProperty("token")
    private String token;
    
    @ApiModelProperty("token过期时间")
    private Long expiryTime;
}
