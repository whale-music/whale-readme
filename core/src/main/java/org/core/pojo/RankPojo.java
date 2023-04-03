package org.core.pojo;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "tb_rank", schema = "whale_music")
public class RankPojo {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    @Column(name = "id")
    private Long id;
    @Basic
    @Column(name = "user_id", insertable = false, updatable = false)
    private Long userId;
    @Basic
    @Column(name = "broadcast_id")
    private Integer broadcastId;
    @Basic
    @Column(name = "broadcast_type")
    private Integer broadcastType;
    @Basic
    @Column(name = "broadcast_count")
    private Integer broadcastCount;
    @Basic
    @Column(name = "create_time")
    @CreatedDate
    private LocalDateTime createTime;
    @Basic
    @Column(name = "update_time")
    @LastModifiedDate
    private LocalDateTime updateTime;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private SysUserPojo sysUserByUserId;
    
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
    
    public Integer getBroadcastId() {
        return broadcastId;
    }
    
    public void setBroadcastId(Integer broadcastId) {
        this.broadcastId = broadcastId;
    }
    
    public Integer getBroadcastType() {
        return broadcastType;
    }
    
    public void setBroadcastType(Integer broadcastType) {
        this.broadcastType = broadcastType;
    }
    
    public Integer getBroadcastCount() {
        return broadcastCount;
    }
    
    public void setBroadcastCount(Integer broadcastCount) {
        this.broadcastCount = broadcastCount;
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
        RankPojo that = (RankPojo) o;
        return id == that.id && userId == that.userId && Objects.equals(broadcastId, that.broadcastId) && Objects.equals(broadcastType,
                that.broadcastType) && Objects.equals(broadcastCount, that.broadcastCount) && Objects.equals(createTime,
                that.createTime) && Objects.equals(updateTime, that.updateTime);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, userId, broadcastId, broadcastType, broadcastCount, createTime, updateTime);
    }
    
    public SysUserPojo getSysUserByUserId() {
        return sysUserByUserId;
    }
    
    public void setSysUserByUserId(SysUserPojo sysUserByUserId) {
        this.sysUserByUserId = sysUserByUserId;
    }
}
