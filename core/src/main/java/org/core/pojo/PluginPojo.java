package org.core.pojo;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "tb_plugin", schema = "whale_music")
public class PluginPojo {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    @Column(name = "id")
    private Long id;
    @Basic
    @Column(name = "plugin_name")
    private String pluginName;
    @Basic
    @Column(name = "create_name")
    private String createName;
    @Basic
    @Column(name = "code")
    private String code;
    @Basic
    @Column(name = "description")
    private String description;
    @Basic
    @Column(name = "user_id")
    private Long userId;
    @Basic
    @Column(name = "create_time")
    @CreatedDate
    private LocalDateTime createTime;
    @Basic
    @Column(name = "update_time")
    @LastModifiedDate
    private LocalDateTime updateTime;
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getPluginName() {
        return pluginName;
    }
    
    public void setPluginName(String pluginName) {
        this.pluginName = pluginName;
    }
    
    public String getCreateName() {
        return createName;
    }
    
    public void setCreateName(String createName) {
        this.createName = createName;
    }
    
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public LocalDateTime getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
    
    public LocalDateTime getUpdateTime() {
        return updateTime;
    }
    
    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PluginPojo that = (PluginPojo) o;
        return id == that.id && userId == that.userId && Objects.equals(pluginName, that.pluginName) && Objects.equals(createName,
                that.createName) && Objects.equals(code, that.code) && Objects.equals(description,
                that.description) && Objects.equals(createTime, that.createTime) && Objects.equals(updateTime, that.updateTime);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, pluginName, createName, code, description, userId, createTime, updateTime);
    }
}
