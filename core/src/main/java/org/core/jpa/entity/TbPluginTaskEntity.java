package org.core.jpa.entity;

import jakarta.persistence.*;
import org.core.jpa.config.ManualInsertGenerator;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "tb_plugin_task")
public class TbPluginTaskEntity implements Serializable {
    public static final long serialVersionUID = 1634466859314138296L;
    
    @Id
    @GeneratedValue(generator = "IdGenerator", strategy = GenerationType.AUTO)
    @GenericGenerator(name = "IdGenerator", type = ManualInsertGenerator.class)
    @Column(name = "id", nullable = false)
    private Long id;
    @Basic
    @Column(name = "plugin_id", nullable = false)
    private Long pluginId;
    @Basic
    @Column(name = "status", nullable = false)
    private Byte status;
    @Basic
    @Column(name = "params", nullable = true, length = Integer.MAX_VALUE)
    private String params;
    @Basic
    @Column(name = "user_id", nullable = false)
    private Long userId;
    @Basic
    @Column(name = "create_time", nullable = false)
    private Timestamp createTime;
    @Basic
    @Column(name = "update_time", nullable = false)
    private Timestamp updateTime;
    @OneToMany(mappedBy = "tbPluginTaskByTaskId")
    private Collection<TbPluginMsgEntity> tbPluginMsgsById;
    @ManyToOne
    @JoinColumn(name = "plugin_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    private TbPluginEntity tbPluginByPluginId;
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
    
    public Byte getStatus() {
        return status;
    }
    
    public void setStatus(Byte status) {
        this.status = status;
    }
    
    public String getParams() {
        return params;
    }
    
    public void setParams(String params) {
        this.params = params;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
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
        TbPluginTaskEntity that = (TbPluginTaskEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(pluginId, that.pluginId) && Objects.equals(status,
                that.status) && Objects.equals(params, that.params) && Objects.equals(userId, that.userId) && Objects.equals(
                createTime,
                that.createTime) && Objects.equals(updateTime, that.updateTime);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, pluginId, status, params, userId, createTime, updateTime);
    }
    
    public Collection<TbPluginMsgEntity> getTbPluginMsgsById() {
        return tbPluginMsgsById;
    }
    
    public void setTbPluginMsgsById(Collection<TbPluginMsgEntity> tbPluginMsgsById) {
        this.tbPluginMsgsById = tbPluginMsgsById;
    }
    
    public TbPluginEntity getTbPluginByPluginId() {
        return tbPluginByPluginId;
    }
    
    public void setTbPluginByPluginId(TbPluginEntity tbPluginByPluginId) {
        this.tbPluginByPluginId = tbPluginByPluginId;
    }
    
    public SysUserEntity getSysUserByUserId() {
        return sysUserByUserId;
    }
    
    public void setSysUserByUserId(SysUserEntity sysUserByUserId) {
        this.sysUserByUserId = sysUserByUserId;
    }
}
