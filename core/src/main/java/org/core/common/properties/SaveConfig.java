package org.core.common.properties;

import cn.hutool.core.text.CharSequenceUtil;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.core.common.enums.SaveModeEnum;
import org.core.common.exception.BaseException;
import org.core.common.result.ResultCode;
import org.core.oss.service.impl.alist.enums.ResourceEnum;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Configuration
@ConfigurationProperties(
        prefix = "save-config"
)
public class SaveConfig {
    // 默认保存模式
    private SaveModeEnum saveMode;
    
    // 扫描过滤文件
    private ScanFilter scanFilter = new ScanFilter();
    
    // 主机
    private String host;
    
    // 扫描路径深度
    private Integer deep = 5;
    
    // 文件缓存时间
    private Long bufferTime = 7_200_000L;
    
    // 对象地址
    private List<String> objectSave = new ArrayList<>();
    
    // 保存地址，必须是对象地址中的值，顺序从0开始
    private Integer assignObjectSave = 0;
    
    // 图片存储地址
    private List<String> imgSave = new ArrayList<>();
    
    // 上传图片,从0开始
    private Integer assignImgSave = 0;
    
    // MV存储地址
    private List<String> mvSave = new ArrayList<>();
    
    // MV图片,从0开始
    private Integer assignMvSave = 0;
    
    // 访问账户
    private String accessKey;
    
    // 访问密钥(密码)
    private String secretKey;
    
    public ScanFilter getScanFilter() {
        return scanFilter;
    }
    
    public SaveModeEnum getSaveMode() {
        return saveMode;
    }
    
    public void setSaveMode(SaveModeEnum saveMode) {
        this.saveMode = saveMode;
    }
    
    public void setScanFilter(ScanFilter scanFilter) {
        this.scanFilter = scanFilter;
    }
    
    public Set<String> getScanFilter(ResourceEnum type) {
        switch (type.name().toLowerCase()) {
            case "music", "audio" -> {
                return this.getScanFilter().getAudio();
            }
            case "pic" -> {
                return this.getScanFilter().getPic();
            }
            case "video", "mv" -> {
                return this.getScanFilter().getVideo();
            }
            default -> throw new BaseException(ResultCode.PARAM_IS_BLANK);
        }
    }
    
    public static class ScanFilter {
        private Set<String> audio = new HashSet<>();
        private Set<String> pic = new HashSet<>();
        private Set<String> video = new HashSet<>();
        
        public Set<String> getAudio() {
            return this.audio;
        }
        
        public void setAudio(Set<String> audio) {
            this.audio = audio;
        }
        
        public Set<String> getPic() {
            return this.pic;
        }
        
        public void setPic(Set<String> pic) {
            this.pic = pic;
        }
        
        public Set<String> getVideo() {
            return this.video;
        }
        
        public void setVideo(Set<String> video) {
            this.video = video;
        }
    }
    
    /**
     * 获取WebConfig的路径映射
     *
     * @return host
     */
    public String getHostMapping() {
        return host;
    }
    
    public String getHost() {
        return CharSequenceUtil.removeSuffix(host, "/");
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
    
    public Integer getDeep() {
        return deep;
    }
    
    public void setDeep(Integer deep) {
        this.deep = deep;
    }
    
    public Long getBufferTime() {
        return bufferTime;
    }
    
    public void setBufferTime(Long bufferTime) {
        this.bufferTime = bufferTime;
    }
    
    public void setObjectSave(List<String> objectSave) {
        this.objectSave = objectSave;
    }
    
    public Integer getAssignObjectSave() {
        return assignObjectSave;
    }
    
    public void setAssignObjectSave(Integer assignObjectSave) {
        this.assignObjectSave = assignObjectSave;
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
        this.assignImgSave = assignImgSave;
    }
    
    public List<String> getMvSave() {
        return mvSave;
    }
    
    public void setMvSave(List<String> mvSave) {
        this.mvSave = mvSave;
    }
    
    public Integer getAssignMvSave() {
        return assignMvSave;
    }
    
    public void setAssignMvSave(Integer assignMvSave) {
        this.assignMvSave = assignMvSave;
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
