package org.api.admin.model.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenRes {
    private Long id;
    private String username;
    private Set<String> roles;
    private String accessToken;
    private String refreshToken;
    private Long expires;
}
