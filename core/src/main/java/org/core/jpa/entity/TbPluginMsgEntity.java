package org.core.jpa.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "tb_plugin_msg")
public class TbPluginMsgEntity implements Serializable {
    public static final long serialVersionUID = 949660729920259853L;
    
    @Id
    @GeneratedValue(generator = "IdGenerator", strategy = GenerationType.AUTO)
    @GenericGenerator(name = "IdGenerator", strategy = "org.core.jpa.config.ManualInsertGenerator")
    @Column(name = "id", nullable = false)
    private Long id;
    @Basic
    @Column(name = "plugin_id", nullable = false)
    private Long pluginId;
    @Basic
    @Column(name = "task_id", nullable = false)
    private Long taskId;
    @Basic
    @Column(name = "user_id", nullable = false)
    private Long userId;
    @Basic
    @Column(name = "level", nullable = true)
    private Byte level;
    @Basic
    @Column(name = "msg", nullable = true, length = Integer.MAX_VALUE)
    private String msg;
    @Basic
    @Column(name = "create_time", nullable = false)
    private Timestamp createTime;
    @Basic
    @Column(name = "update_time", nullable = false)
    private Timestamp updateTime;
    @ManyToOne
    @JoinColumn(name = "plugin_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    private TbPluginEntity tbPluginByPluginId;
    @ManyToOne
    @JoinColumn(name = "task_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    private TbPluginTaskEntity tbPluginTaskByTaskId;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    private SysUserEntity sysUserByUserId;
    
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
    
    public Byte getLevel() {
        return level;
    }
    
    public void setLevel(Byte level) {
        this.level = level;
    }
    
    public String getMsg() {
        return msg;
    }
    
    public void setMsg(String msg) {
        this.msg = msg;
    }
    
    public Timestamp getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }
    
    public Timestamp getUpdateTime() {
        return updateTime;
    }
    
    public void setUpdateTime(Timestamp updateTime) {
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
        TbPluginMsgEntity that = (TbPluginMsgEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(pluginId, that.pluginId) && Objects.equals(taskId,
                that.taskId) && Objects.equals(userId, that.userId) && Objects.equals(level, that.level) && Objects.equals(msg,
                that.msg) && Objects.equals(createTime, that.createTime) && Objects.equals(updateTime, that.updateTime);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, pluginId, taskId, userId, level, msg, createTime, updateTime);
    }
    
    public TbPluginEntity getTbPluginByPluginId() {
        return tbPluginByPluginId;
    }
    
    public void setTbPluginByPluginId(TbPluginEntity tbPluginByPluginId) {
        this.tbPluginByPluginId = tbPluginByPluginId;
    }
    
    public TbPluginTaskEntity getTbPluginTaskByTaskId() {
        return tbPluginTaskByTaskId;
    }
    
    public void setTbPluginTaskByTaskId(TbPluginTaskEntity tbPluginTaskByTaskId) {
        this.tbPluginTaskByTaskId = tbPluginTaskByTaskId;
    }
    
    public SysUserEntity getSysUserByUserId() {
        return sysUserByUserId;
    }
    
    public void setSysUserByUserId(SysUserEntity sysUserByUserId) {
        this.sysUserByUserId = sysUserByUserId;
    }
}
