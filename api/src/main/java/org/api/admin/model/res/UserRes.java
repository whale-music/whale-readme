package org.api.admin.model.res;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRes {
    @ApiModelProperty("id")
    private Long id;
    
    @ApiModelProperty("用户名")
    private String username;
    
    @ApiModelProperty("token")
    private String accessToken;
    
    @ApiModelProperty("用于调用刷新`accessToken`的接口时所需的`token`")
    private String refreshToken;
    
    @ApiModelProperty("当前登陆用户的角色")
    private Collection<String> roles;
    
    @ApiModelProperty("`accessToken`的过期时间（格式'xxxx/xx/xx xx:xx:xx'）")
    private Long expires;
}
