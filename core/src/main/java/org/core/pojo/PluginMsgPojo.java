package org.core.pojo;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "tb_plugin_msg", schema = "whale_music")
public class PluginMsgPojo {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    @Column(name = "id")
    private Long id;
    @Basic
    @Column(name = "plugin_id")
    private Long pluginId;
    @Basic
    @Column(name = "task_id")
    private Long taskId;
    @Basic
    @Column(name = "user_id")
    private Long userId;
    @Basic
    @Column(name = "msg")
    private String msg;
    @Basic
    @Column(name = "create_time")
    private Long createTime;
    @Basic
    @Column(name = "update_time")
    private Long updateTime;
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getPluginId() {
        return pluginId;
    }
    
    public void setPluginId(Long pluginId) {
        this.pluginId = pluginId;
    }
    
    public Long getTaskId() {
        return taskId;
    }
    
    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public String getMsg() {
        return msg;
    }
    
    public void setMsg(String msg) {
        this.msg = msg;
    }
    
    public Long getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }
    
    public Long getUpdateTime() {
        return updateTime;
    }
    
    public void setUpdateTime(Long updateTime) {
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
        PluginMsgPojo that = (PluginMsgPojo) o;
        return id == that.id && pluginId == that.pluginId && taskId == that.taskId && userId == that.userId && createTime == that.createTime && updateTime == that.updateTime && Objects.equals(
                msg,
                that.msg);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, pluginId, taskId, userId, msg, createTime, updateTime);
    }
}
