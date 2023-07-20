package org.api.admin.model.res;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.core.mybatis.pojo.SysUserPojo;

import java.util.Collection;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class UserRes extends SysUserPojo {
    @ApiModelProperty("token")
    private String token;
    
    @ApiModelProperty("用户角色")
    private Collection<String> roles;
    
    @ApiModelProperty("token过期时间")
    private Long expiryTime;
}
