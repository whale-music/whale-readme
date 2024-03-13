package org.api.admin.model.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRes {
    @Schema(name = "id")
    private Long id;
    
    @Schema(name = "用户名")
    private String username;
    
    @Schema(name = "token")
    private String accessToken;
    
    @Schema(name = "用于调用刷新`accessToken`的接口时所需的`token`")
    private String refreshToken;
    
    @Schema(name = "当前登陆用户的角色")
    private Collection<String> roles;
    
    @Schema(name = "`accessToken`的过期时间（格式'xxxx/xx/xx xx:xx:xx'）")
    private Long expires;
}
