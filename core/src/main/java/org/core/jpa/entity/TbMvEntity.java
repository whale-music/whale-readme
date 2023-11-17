package org.core.jpa.entity;

import jakarta.persistence.*;
import org.core.jpa.config.ManualInsertGenerator;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Collection;

@Entity
@Table(name = "tb_mv")
public class TbMvEntity implements Serializable {
    public static final long serialVersionUID = 2405432543551807L;
    
    @Id
    @GeneratedValue(generator = "IdGenerator", strategy = GenerationType.AUTO)
    @GenericGenerator(name = "IdGenerator", type = ManualInsertGenerator.class)
    @Column(name = "id", nullable = false)
    private Long id;
    @Basic
    @Column(name = "title", nullable = true, length = 255)
    private String title;
    @Basic
    @Column(name = "path", nullable = false)
    private String path;
    @Basic
    @Column(name = "md5", nullable = false)
    private String md5;
    @Basic
    @Column(name = "description", nullable = true, length = -1)
    private String description;
    @Basic
    @Column(name = "duration", nullable = true)
    private Long duration;
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
    
    public Long getDuration() {
        return duration;
    }
    
    public void setDuration(Long duration) {
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
    
    public String getPath() {
        return path;
    }
    
    public void setPath(String path) {
        this.path = path;
    }
    
    public String getMd5() {
        return md5;
    }
    
    public void setMd5(String md5) {
        this.md5 = md5;
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
