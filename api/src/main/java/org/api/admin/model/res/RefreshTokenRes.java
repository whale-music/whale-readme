package org.api.admin.model.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenRes {
    private String accessToken;
    private String refreshToken;
    private Long expires;
}
