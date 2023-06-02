package org.core.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Configuration
@ConfigurationProperties(
        prefix = "save-config"
)
public class SaveConfig {
    // 默认保存模式
    private String saveMode;
    
    // 主机
    private String host;
    
    // 对象地址
    private List<String> objectSave;
    
    // 保存地址，必须是对象地址中的值，顺序从0开始
    private Integer assignObjectSave;
    
    // 图片存储地址
    private List<String> imgSave;
    
    // 上传图片,从0开始
    private Integer assignImgSave;
    
    // 访问账户
    private String accessKey;
    
    // 访问密钥(密码)
    private String secretKey;
}
