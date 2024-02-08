package org.api.admin.model.req;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RefreshTokenRes {
    private String refreshToken;
}
