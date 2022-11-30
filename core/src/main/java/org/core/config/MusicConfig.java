package org.core.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Configuration
@ConfigurationProperties(
        prefix = "music-config"
)
public class MusicConfig {
    // 默认保存模式
    private String saveMode;
    // 保存名称(文件夹)
    private String saveName;
    // 地址
    private String host;
    // 访问账户
    private String accessKey;
    // 访问密钥(密码)
    private String secretKey;
}
