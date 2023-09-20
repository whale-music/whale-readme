package org.core.jpa.entity;

import jakarta.persistence.*;
import org.core.jpa.config.ManualInsertGenerator;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "tb_history")
public class TbHistoryEntity implements Serializable {
    public static final long serialVersionUID = 3852131611450316352L;
    
    @Id
    @GeneratedValue(generator = "IdGenerator", strategy = GenerationType.AUTO)
    @GenericGenerator(name = "IdGenerator", type = ManualInsertGenerator.class)
    @Column(name = "id", nullable = false)
    private Long id;
    @Basic
    @Column(name = "user_id", nullable = false)
    private Long userId;
    @Basic
    @Column(name = "middle_id", nullable = false)
    private Long middleId;
    @Basic
    @Column(name = "type", nullable = true)
    private Byte type;
    @Basic
    @Column(name = "count", nullable = true)
    private Integer count;
    @Basic
    @Column(name = "played_time", nullable = true)
    private Long playedTime;
    @Basic
    @Column(name = "create_time", nullable = true)
    private Timestamp createTime;
    @Basic
    @Column(name = "update_time", nullable = true)
    private Timestamp updateTime;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    private SysUserEntity sysUserByUserId;
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public Long getMiddleId() {
        return middleId;
    }
    
    public void setMiddleId(Long middleId) {
        this.middleId = middleId;
    }
    
    public Byte getType() {
        return type;
    }
    
    public void setType(Byte type) {
        this.type = type;
    }
    
    public Integer getCount() {
        return count;
    }
    
    public void setCount(Integer count) {
        this.count = count;
    }
    
    public Long getPlayedTime() {
        return playedTime;
    }
    
    public void setPlayedTime(Long playedTime) {
        this.playedTime = playedTime;
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
        
        TbHistoryEntity that = (TbHistoryEntity) o;
        
        if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) {
            return false;
        }
        if (getUserId() != null ? !getUserId().equals(that.getUserId()) : that.getUserId() != null) {
            return false;
        }
        if (getMiddleId() != null ? !getMiddleId().equals(that.getMiddleId()) : that.getMiddleId() != null) {
            return false;
        }
        if (getType() != null ? !getType().equals(that.getType()) : that.getType() != null) {
            return false;
        }
        if (getCount() != null ? !getCount().equals(that.getCount()) : that.getCount() != null) {
            return false;
        }
        if (getPlayedTime() != null ? !getPlayedTime().equals(that.getPlayedTime()) : that.getPlayedTime() != null) {
            return false;
        }
        if (getCreateTime() != null ? !getCreateTime().equals(that.getCreateTime()) : that.getCreateTime() != null) {
            return false;
        }
        if (getUpdateTime() != null ? !getUpdateTime().equals(that.getUpdateTime()) : that.getUpdateTime() != null) {
            return false;
        }
        return getSysUserByUserId() != null ? getSysUserByUserId().equals(that.getSysUserByUserId()) : that.getSysUserByUserId() == null;
    }
    
    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getUserId() != null ? getUserId().hashCode() : 0);
        result = 31 * result + (getMiddleId() != null ? getMiddleId().hashCode() : 0);
        result = 31 * result + (getType() != null ? getType().hashCode() : 0);
        result = 31 * result + (getCount() != null ? getCount().hashCode() : 0);
        result = 31 * result + (getPlayedTime() != null ? getPlayedTime().hashCode() : 0);
        result = 31 * result + (getCreateTime() != null ? getCreateTime().hashCode() : 0);
        result = 31 * result + (getUpdateTime() != null ? getUpdateTime().hashCode() : 0);
        result = 31 * result + (getSysUserByUserId() != null ? getSysUserByUserId().hashCode() : 0);
        return result;
    }
    
    public SysUserEntity getSysUserByUserId() {
        return sysUserByUserId;
    }
    
    public void setSysUserByUserId(SysUserEntity sysUserByUserId) {
        this.sysUserByUserId = sysUserByUserId;
    }
}
