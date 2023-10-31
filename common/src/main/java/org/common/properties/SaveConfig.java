package org.common.properties;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.common.exception.BaseException;
import org.common.result.ResultCode;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

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
    private List<String> objectSave = new ArrayList<>();
    
    // 保存地址，必须是对象地址中的值，顺序从0开始
    private Integer assignObjectSave;
    
    // 图片存储地址
    private List<String> imgSave = new ArrayList<>();
    
    // 上传图片,从0开始
    private Integer assignImgSave;
    
    // 访问账户
    private String accessKey;
    
    // 访问密钥(密码)
    private String secretKey;
    
    public String getSaveMode() {
        return saveMode;
    }
    
    public void setSaveMode(String saveMode) {
        this.saveMode = saveMode;
    }
    
    public String getHost() {
        if (host.charAt(host.length() - 1) == '/') {
            return host.substring(0, host.length() - 1);
        } else {
            return host;
        }
    }
    
    public void setHost(String host) {
        this.host = host;
    }
    
    public List<String> getObjectSave() {
        if (objectSave.get(getAssignObjectSave()) == null) {
            throw new BaseException(ResultCode.STORAGE_PATH_DOES_NOT_EXIST);
        }
        return objectSave;
    }
    
    public void setObjectSave(List<String> objectSave) {
        this.objectSave = objectSave;
    }
    
    public Integer getAssignObjectSave() {
        return assignObjectSave;
    }
    
    public void setAssignObjectSave(Integer assignObjectSave) {
        this.assignObjectSave = assignObjectSave == null ? 0 : assignObjectSave;
    }
    
    public List<String> getImgSave() {
        return imgSave;
    }
    
    public void setImgSave(List<String> imgSave) {
        this.imgSave = imgSave;
    }
    
    public Integer getAssignImgSave() {
        return assignImgSave;
    }
    
    public void setAssignImgSave(Integer assignImgSave) {
        this.assignImgSave = assignImgSave == null ? 0 : assignImgSave;
    }
    
    public String getAccessKey() {
        return accessKey;
    }
    
    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }
    
    public String getSecretKey() {
        return secretKey;
    }
    
    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
}
