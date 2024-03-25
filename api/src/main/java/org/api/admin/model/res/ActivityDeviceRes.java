package org.api.admin.model.res;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.core.model.UserLoginCacheModel;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class ActivityDeviceRes {
    List<Devices> adminDevices = new ArrayList<>();
    List<Devices> nMusicDevices = new ArrayList<>();
    List<Devices> subsonicDevices = new ArrayList<>();
    List<Devices> webdavDevices = new ArrayList<>();
    
    @Setter
    @Getter
    public static class Devices {
        @Schema(name = "缓存ID")
        private String cacheId;
        @Schema(name = "系统用户ID")
        private Long id;
        @Schema(name = "登录用户名")
        private String username;
        @Schema(name = "登录显示昵称")
        private String nickname;
        @Schema(title = "账户类型")
        private Integer accountType;
        @Schema(name = "用户是否启用 1: 启用, 0: 停用")
        private Boolean status;
        @Schema(name = "最后登录IP")
        private String lastLoginIp;
        @Schema(name = "缓存生成时间")
        private LocalDateTime generatedDate;
        
        public Devices(String cacheId, UserLoginCacheModel pojo) {
            this.cacheId = cacheId;
            this.id = pojo.getId();
            this.username = pojo.getUsername();
            this.nickname = pojo.getNickname();
            this.accountType = pojo.getAccountType();
            this.status = pojo.getStatus();
            this.lastLoginIp = pojo.getLastLoginIp();
            this.generatedDate = pojo.getGeneratedDate();
        }
    }
}
