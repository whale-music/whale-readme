package org.core.jpa.entity;

import jakarta.persistence.*;
import org.core.jpa.config.ManualInsertGenerator;
import org.hibernate.annotations.GenericGenerator;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "tb_mv")
public class TbMvEntity {
    @Id
    @GeneratedValue(generator = "IdGenerator", strategy = GenerationType.AUTO)
    @GenericGenerator(name = "IdGenerator", type = ManualInsertGenerator.class)
    @Column(name = "id", nullable = false)
    private Long id;
    @Basic
    @Column(name = "source_id", nullable = false)
    private Long sourceId;
    @Basic
    @Column(name = "title", nullable = true, length = 255)
    private String title;
    @Basic
    @Column(name = "description", nullable = true, length = -1)
    private String description;
    @Basic
    @Column(name = "duration", nullable = true)
    private Integer duration;
    @Basic
    @Column(name = "user_id", nullable = true)
    private Long userId;
    @Basic
    @Column(name = "publish_time", nullable = true)
    private Timestamp publishTime;
    @Basic
    @Column(name = "create_time", nullable = true)
    private Timestamp createTime;
    @Basic
    @Column(name = "update_time", nullable = true)
    private Timestamp updateTime;
    @OneToMany(mappedBy = "tbMvByMiddleId")
    private Collection<TbHistoryEntity> tbHistoriesById;
    @OneToMany(mappedBy = "tbMvByMiddleId")
    private Collection<TbMiddlePicEntity> tbMiddlePicsById;
    @OneToMany(mappedBy = "tbMvByMiddleId")
    private Collection<TbMiddleTagEntity> tbMiddleTagsById;
    @ManyToOne
    @JoinColumn(name = "source_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    private TbResourceEntity tbResourceBySourceId;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false)
    private SysUserEntity sysUserByUserId;
    @OneToMany(mappedBy = "tbMvByMvId")
    private Collection<TbMvArtistEntity> tbMvArtistsById;
    @OneToMany(mappedBy = "tbMvByMvId")
    private Collection<TbUserMvEntity> tbUserMvsById;
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getSourceId() {
        return sourceId;
    }
    
    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Integer getDuration() {
        return duration;
    }
    
    public void setDuration(Integer duration) {
        this.duration = duration;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public Timestamp getPublishTime() {
        return publishTime;
    }
    
    public void setPublishTime(Timestamp publishTime) {
        this.publishTime = publishTime;
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
        TbMvEntity that = (TbMvEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(sourceId, that.sourceId) && Objects.equals(title,
                that.title) && Objects.equals(description, that.description) && Objects.equals(duration,
                that.duration) && Objects.equals(userId, that.userId) && Objects.equals(publishTime,
                that.publishTime) && Objects.equals(createTime, that.createTime) && Objects.equals(updateTime, that.updateTime);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, sourceId, title, description, duration, userId, publishTime, createTime, updateTime);
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
    
    public TbResourceEntity getTbResourceBySourceId() {
        return tbResourceBySourceId;
    }
    
    public void setTbResourceBySourceId(TbResourceEntity tbResourceBySourceId) {
        this.tbResourceBySourceId = tbResourceBySourceId;
    }
    
    public SysUserEntity getSysUserByUserId() {
        return sysUserByUserId;
    }
    
    public void setSysUserByUserId(SysUserEntity sysUserByUserId) {
        this.sysUserByUserId = sysUserByUserId;
    }
    
    public Collection<TbMvArtistEntity> getTbMvArtistsById() {
        return tbMvArtistsById;
    }
    
    public void setTbMvArtistsById(Collection<TbMvArtistEntity> tbMvArtistsById) {
        this.tbMvArtistsById = tbMvArtistsById;
    }
    
    public Collection<TbUserMvEntity> getTbUserMvsById() {
        return tbUserMvsById;
    }
    
    public void setTbUserMvsById(Collection<TbUserMvEntity> tbUserMvsById) {
        this.tbUserMvsById = tbUserMvsById;
    }
}
