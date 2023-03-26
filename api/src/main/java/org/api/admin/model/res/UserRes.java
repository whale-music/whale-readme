package org.api.admin.model.res;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.core.pojo.SysUserPojo;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class UserRes extends SysUserPojo {
    @ApiModelProperty("token")
    private String token;
    
    @ApiModelProperty("token过期时间")
    private Long expiryTime;
}
