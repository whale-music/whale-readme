package org.core.pojo;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "tb_collect", schema = "whale_music")
public class CollectPojo {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    @Column(name = "id")
    private Long id;
    @Basic
    @Column(name = "play_list_name")
    private String playListName;
    @Basic
    @Column(name = "pic")
    private String pic;
    @Basic
    @Column(name = "type")
    private Byte type;
    @Basic
    @Column(name = "subscribed")
    private Boolean subscribed;
    @Basic
    @Column(name = "description")
    private String description;
    @Basic
    @Column(name = "user_id", insertable = false, updatable = false)
    private Long userId;
    @Basic
    @Column(name = "sort")
    private Long sort;
    @Basic
    @Column(name = "create_time")
    @CreatedDate
    private LocalDateTime createTime;
    @Basic
    @Column(name = "update_time")
    @LastModifiedDate
    private LocalDateTime updateTime;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private SysUserPojo sysUserByUserId;
    @OneToMany(mappedBy = "tbCollectByCollectId")
    private Collection<CollectMusicPojo> tbCollectMusicsById;
    @OneToMany(mappedBy = "tbCollectByCollectId")
    private Collection<CollectTagPojo> tbCollectTagsById;
    @OneToOne(mappedBy = "tbCollectByMusicId")
    private HistoryPojo tbHistoryById;
    
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
    
    public String getPic() {
        return pic;
    }
    
    public void setPic(String pic) {
        this.pic = pic;
    }
    
    public Byte getType() {
        return type;
    }
    
    public void setType(Byte type) {
        this.type = type;
    }
    
    public Boolean getSubscribed() {
        return subscribed;
    }
    
    public void setSubscribed(Boolean subscribed) {
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
        CollectPojo that = (CollectPojo) o;
        return id == that.id && type == that.type && subscribed == that.subscribed && Objects.equals(playListName,
                that.playListName) && Objects.equals(pic, that.pic) && Objects.equals(description,
                that.description) && Objects.equals(userId, that.userId) && Objects.equals(sort, that.sort) && Objects.equals(
                createTime,
                that.createTime) && Objects.equals(updateTime, that.updateTime);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, playListName, pic, type, subscribed, description, userId, sort, createTime, updateTime);
    }
    
    public SysUserPojo getSysUserByUserId() {
        return sysUserByUserId;
    }
    
    public void setSysUserByUserId(SysUserPojo sysUserByUserId) {
        this.sysUserByUserId = sysUserByUserId;
    }
    
    public Collection<CollectMusicPojo> getTbCollectMusicsById() {
        return tbCollectMusicsById;
    }
    
    public void setTbCollectMusicsById(Collection<CollectMusicPojo> tbCollectMusicsById) {
        this.tbCollectMusicsById = tbCollectMusicsById;
    }
    
    public Collection<CollectTagPojo> getTbCollectTagsById() {
        return tbCollectTagsById;
    }
    
    public void setTbCollectTagsById(Collection<CollectTagPojo> tbCollectTagsById) {
        this.tbCollectTagsById = tbCollectTagsById;
    }
    
    public HistoryPojo getTbHistoryById() {
        return tbHistoryById;
    }
    
    public void setTbHistoryById(HistoryPojo tbHistoryById) {
        this.tbHistoryById = tbHistoryById;
    }
}
