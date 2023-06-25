package org.core.jpa.entity;

import jakarta.persistence.*;
import org.core.jpa.config.ManualInsertGenerator;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "tb_collect")
public class TbCollectEntity implements Serializable {
    public static final long serialVersionUID = 3852731611450316352L;
    
    @Id
    @GeneratedValue(generator = "IdGenerator", strategy = GenerationType.AUTO)
    @GenericGenerator(name = "IdGenerator", type = ManualInsertGenerator.class)
    @Column(name = "id", nullable = false)
    private Long id;
    @Basic
    @Column(name = "play_list_name", nullable = false, length = 256)
    private String playListName;
    @Basic
    @Column(name = "type", nullable = false)
    private Byte type;
    @Basic
    @Column(name = "subscribed", nullable = false)
    private Byte subscribed;
    @Basic
    @Column(name = "description", nullable = true, length = 512)
    private String description;
    @Basic
    @Column(name = "user_id", nullable = true)
    private Long userId;
    @Basic
    @Column(name = "sort", nullable = true)
    private Long sort;
    @Basic
    @Column(name = "create_time", nullable = true)
    private Timestamp createTime;
    @Basic
    @Column(name = "update_time", nullable = true)
    private Timestamp updateTime;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false)
    private SysUserEntity sysUserByUserId;
    @OneToMany(mappedBy = "tbCollectByCollectId")
    private Collection<TbCollectMusicEntity> tbCollectMusicsById;
    @OneToMany(mappedBy = "tbCollectByMiddleId")
    private Collection<TbHistoryEntity> tbHistoriesById;
    @OneToMany(mappedBy = "tbCollectByMiddleId")
    private Collection<TbMiddlePicEntity> tbMiddlePicsById;
    @OneToMany(mappedBy = "tbCollectByMiddleId")
    private Collection<TbMiddleTagEntity> tbMiddleTagsById;
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getPlayListName() {
        return playListName;
    }
    
    public void setPlayListName(String playListName) {
        this.playListName = playListName;
    }
    
    public Byte getType() {
        return type;
    }
    
    public void setType(Byte type) {
        this.type = type;
    }
    
    public Byte getSubscribed() {
        return subscribed;
    }
    
    public void setSubscribed(Byte subscribed) {
        this.subscribed = subscribed;
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
    
    public Long getSort() {
        return sort;
    }
    
    public void setSort(Long sort) {
        this.sort = sort;
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
        TbCollectEntity that = (TbCollectEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(playListName, that.playListName) && Objects.equals(type,
                that.type) && Objects.equals(subscribed, that.subscribed) && Objects.equals(description,
                that.description) && Objects.equals(userId, that.userId) && Objects.equals(sort, that.sort) && Objects.equals(
                createTime,
                that.createTime) && Objects.equals(updateTime, that.updateTime);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, playListName, type, subscribed, description, userId, sort, createTime, updateTime);
    }
    
    public SysUserEntity getSysUserByUserId() {
        return sysUserByUserId;
    }
    
    public void setSysUserByUserId(SysUserEntity sysUserByUserId) {
        this.sysUserByUserId = sysUserByUserId;
    }
    
    public Collection<TbCollectMusicEntity> getTbCollectMusicsById() {
        return tbCollectMusicsById;
    }
    
    public void setTbCollectMusicsById(Collection<TbCollectMusicEntity> tbCollectMusicsById) {
        this.tbCollectMusicsById = tbCollectMusicsById;
    }
    
    public Collection<TbHistoryEntity> getTbHistoriesById() {
        return tbHistoriesById;
    }
    
    public void setTbHistoriesById(Collection<TbHistoryEntity> tbHistoriesById) {
        this.tbHistoriesById = tbHistoriesById;
    }
    
    public Collection<TbMiddlePicEntity> getTbMiddlePicsById() {
        return tbMiddlePicsById;
    }
    
    public void setTbMiddlePicsById(Collection<TbMiddlePicEntity> tbMiddlePicsById) {
        this.tbMiddlePicsById = tbMiddlePicsById;
    }
    
    public Collection<TbMiddleTagEntity> getTbMiddleTagsById() {
        return tbMiddleTagsById;
    }
    
    public void setTbMiddleTagsById(Collection<TbMiddleTagEntity> tbMiddleTagsById) {
        this.tbMiddleTagsById = tbMiddleTagsById;
    }
}
