package org.api.admin.model.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsersUploadRes {
    private String avatarUrl;
    private String username;
    private Long musicCount;
    private Long albumCount;
    private Long artistCount;
    private Long playlistCount;
}
