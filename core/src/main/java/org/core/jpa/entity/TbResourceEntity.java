package org.core.jpa.entity;

import jakarta.persistence.*;
import org.core.jpa.config.ManualInsertGenerator;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "tb_resource")
public class TbResourceEntity implements Serializable {
    public static final long serialVersionUID = 3852711638450316352L;
    
    @Id
    @GeneratedValue(generator = "IdGenerator", strategy = GenerationType.AUTO)
    @GenericGenerator(name = "IdGenerator", type = ManualInsertGenerator.class)
    @Column(name = "id", nullable = false)
    private Long id;
    @Basic
    @Column(name = "music_id", nullable = false)
    private Long musicId;
    @Basic
    @Column(name = "rate", nullable = true)
    private Integer rate;
    @Basic
    @Column(name = "path", nullable = true, length = 512)
    private String path;
    @Basic
    @Column(name = "md5", nullable = false, length = 32)
    private String md5;
    @Basic
    @Column(name = "level", nullable = true, length = 8)
    private String level;
    @Basic
    @Column(name = "encode_type", nullable = true, length = 10)
    private String encodeType;
    @Basic
    @Column(name = "size", nullable = true)
    private Long size;
    @Basic
    @Column(name = "user_id", nullable = true)
    private Long userId;
    @Basic
    @Column(name = "create_time", nullable = true)
    private Timestamp createTime;
    @Basic
    @Column(name = "update_time", nullable = true)
    private Timestamp updateTime;
    @OneToMany(mappedBy = "tbResourceBySourceId")
    private Collection<TbMvEntity> tbMvsById;
    @OneToMany(mappedBy = "tbResourceByResourceId")
    private Collection<TbOriginEntity> tbOriginsById;
    @ManyToOne
    @JoinColumn(name = "music_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    private TbMusicEntity tbMusicByMusicId;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false)
    private SysUserEntity sysUserByUserId;
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getMusicId() {
        return musicId;
    }
    
    public void setMusicId(Long musicId) {
        this.musicId = musicId;
    }
    
    public Integer getRate() {
        return rate;
    }
    
    public void setRate(Integer rate) {
        this.rate = rate;
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
    
    public String getLevel() {
        return level;
    }
    
    public void setLevel(String level) {
        this.level = level;
    }
    
    public String getEncodeType() {
        return encodeType;
    }
    
    public void setEncodeType(String encodeType) {
        this.encodeType = encodeType;
    }
    
    public Long getSize() {
        return size;
    }
    
    public void setSize(Long size) {
        this.size = size;
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
        TbResourceEntity that = (TbResourceEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(musicId, that.musicId) && Objects.equals(rate,
                that.rate) && Objects.equals(path, that.path) && Objects.equals(md5, that.md5) && Objects.equals(level,
                that.level) && Objects.equals(encodeType, that.encodeType) && Objects.equals(size, that.size) && Objects.equals(
                userId,
                that.userId) && Objects.equals(createTime, that.createTime) && Objects.equals(updateTime, that.updateTime);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, musicId, rate, path, md5, level, encodeType, size, userId, createTime, updateTime);
    }
    
    public Collection<TbMvEntity> getTbMvsById() {
        return tbMvsById;
    }
    
    public void setTbMvsById(Collection<TbMvEntity> tbMvsById) {
        this.tbMvsById = tbMvsById;
    }
    
    public Collection<TbOriginEntity> getTbOriginsById() {
        return tbOriginsById;
    }
    
    public void setTbOriginsById(Collection<TbOriginEntity> tbOriginsById) {
        this.tbOriginsById = tbOriginsById;
    }
    
    public TbMusicEntity getTbMusicByMusicId() {
        return tbMusicByMusicId;
    }
    
    public void setTbMusicByMusicId(TbMusicEntity tbMusicByMusicId) {
        this.tbMusicByMusicId = tbMusicByMusicId;
    }
    
    public SysUserEntity getSysUserByUserId() {
        return sysUserByUserId;
    }
    
    public void setSysUserByUserId(SysUserEntity sysUserByUserId) {
        this.sysUserByUserId = sysUserByUserId;
    }
}
