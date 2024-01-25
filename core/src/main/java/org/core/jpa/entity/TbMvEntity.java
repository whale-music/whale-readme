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
    @Column(name = "mv_id")
    private Long mvId;
    @Basic
    @Column(name = "path", nullable = false)
    private String path;
    @Basic
    @Column(name = "md5", nullable = false)
    private String md5;
    @Basic
    @Column(name = "duration", nullable = true)
    private Long duration;
    @Basic
    @Column(name = "user_id", nullable = true)
    private Long userId;
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
    @ManyToOne
    @JoinColumn(name = "mv_id", referencedColumnName = "id", insertable = false, updatable = false)
    private TbMvInfoEntity tbMvInfoEntity;
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getMvId() {
        return mvId;
    }
    
    public void setMvId(Long mvId) {
        this.mvId = mvId;
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
    
    public TbMvInfoEntity getTbMvInfoEntity() {
        return tbMvInfoEntity;
    }
    
    public void setTbMvInfoEntity(TbMvInfoEntity tbMvInfoEntity) {
        this.tbMvInfoEntity = tbMvInfoEntity;
    }
}
