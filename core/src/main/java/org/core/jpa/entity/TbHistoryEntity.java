package org.core.jpa.entity;

import jakarta.persistence.*;
import org.core.jpa.config.ManualInsertGenerator;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "tb_history")
public class TbHistoryEntity implements Serializable {
    public static final long serialVersionUID = 3852711638450316352L;
    
    @Id
    @GeneratedValue(generator = "IdGenerator", strategy = GenerationType.AUTO)
    @GenericGenerator(name = "IdGenerator", type = ManualInsertGenerator.class)
    @Column(name = "id", nullable = false)
    private Long id;
    @Basic
    @Column(name = "user_id", nullable = false)
    private Long userId;
    @Basic
    @Column(name = "middle_id", nullable = true)
    private Long middleId;
    @Basic
    @Column(name = "type", nullable = true)
    private Byte type;
    @Basic
    @Column(name = "count", nullable = true)
    private Integer count;
    @Basic
    @Column(name = "create_time", nullable = true)
    private Timestamp createTime;
    @Basic
    @Column(name = "update_time", nullable = true)
    private Timestamp updateTime;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    private SysUserEntity sysUserByUserId;
    @ManyToOne
    @JoinColumn(name = "middle_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    private TbAlbumEntity tbAlbumByMiddleId;
    @ManyToOne
    @JoinColumn(name = "middle_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    private TbCollectEntity tbCollectByMiddleId;
    @ManyToOne
    @JoinColumn(name = "middle_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    private TbMusicEntity tbMusicByMiddleId;
    
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
        return Objects.equals(id, that.id) && Objects.equals(userId, that.userId) && Objects.equals(middleId,
                that.middleId) && Objects.equals(type, that.type) && Objects.equals(count, that.count) && Objects.equals(
                createTime,
                that.createTime) && Objects.equals(updateTime, that.updateTime);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, userId, middleId, type, count, createTime, updateTime);
    }
    
    public SysUserEntity getSysUserByUserId() {
        return sysUserByUserId;
    }
    
    public void setSysUserByUserId(SysUserEntity sysUserByUserId) {
        this.sysUserByUserId = sysUserByUserId;
    }
    
    public TbAlbumEntity getTbAlbumByMiddleId() {
        return tbAlbumByMiddleId;
    }
    
    public void setTbAlbumByMiddleId(TbAlbumEntity tbAlbumByMiddleId) {
        this.tbAlbumByMiddleId = tbAlbumByMiddleId;
    }
    
    public TbCollectEntity getTbCollectByMiddleId() {
        return tbCollectByMiddleId;
    }
    
    public void setTbCollectByMiddleId(TbCollectEntity tbCollectByMiddleId) {
        this.tbCollectByMiddleId = tbCollectByMiddleId;
    }
    
    public TbMusicEntity getTbMusicByMiddleId() {
        return tbMusicByMiddleId;
    }
    
    public void setTbMusicByMiddleId(TbMusicEntity tbMusicByMiddleId) {
        this.tbMusicByMiddleId = tbMusicByMiddleId;
    }
}
