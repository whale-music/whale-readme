package org.core.jpa.entity;


import jakarta.persistence.*;
import org.core.jpa.config.ManualInsertGenerator;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "tb_album")
public class TbAlbumEntity implements Serializable {
    public static final long serialVersionUID = 2405172432150251807L;
    
    @Id
    @GeneratedValue(generator = "IdGenerator", strategy = GenerationType.AUTO)
    @GenericGenerator(name = "IdGenerator", type = ManualInsertGenerator.class)
    @Column(name = "id", nullable = false)
    private Long id;
    @Basic
    @Column(name = "album_name", nullable = false, length = 512)
    private String albumName;
    @Basic
    @Column(name = "sub_type", nullable = true, length = 128)
    private String subType;
    @Basic
    @Column(name = "description", nullable = true, length = Integer.MAX_VALUE)
    private String description;
    @Basic
    @Column(name = "company", nullable = true, length = 256)
    private String company;
    @Basic
    @Column(name = "publish_time", nullable = true)
    private Timestamp publishTime;
    @Basic
    @Column(name = "user_id", nullable = true)
    private Long userId;
    @Basic
    @Column(name = "update_time", nullable = true)
    private Timestamp updateTime;
    @Basic
    @Column(name = "create_time", nullable = true)
    private Timestamp createTime;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false)
    private SysUserEntity sysUserByUserId;
    @OneToMany(mappedBy = "tbAlbumByAlbumId", fetch = FetchType.EAGER)
    private Collection<TbAlbumArtistEntity> tbAlbumArtistsById;
    @OneToMany(mappedBy = "tbAlbumByAlbumId")
    private Collection<TbMusicEntity> tbMusicsById;
    @OneToMany(mappedBy = "tbAlbumByAlbumId")
    private Collection<TbUserAlbumEntity> tbUserAlbumsById;
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getAlbumName() {
        return albumName;
    }
    
    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }
    
    public String getSubType() {
        return subType;
    }
    
    public void setSubType(String subType) {
        this.subType = subType;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getCompany() {
        return company;
    }
    
    public void setCompany(String company) {
        this.company = company;
    }
    
    public Timestamp getPublishTime() {
        return publishTime;
    }
    
    public void setPublishTime(Timestamp publishTime) {
        this.publishTime = publishTime;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public Timestamp getUpdateTime() {
        return updateTime;
    }
    
    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }
    
    public Timestamp getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TbAlbumEntity that = (TbAlbumEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(albumName, that.albumName) && Objects.equals(subType,
                that.subType) && Objects.equals(description, that.description) && Objects.equals(company,
                that.company) && Objects.equals(publishTime, that.publishTime) && Objects.equals(userId,
                that.userId) && Objects.equals(updateTime, that.updateTime) && Objects.equals(createTime, that.createTime);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, albumName, subType, description, company, publishTime, userId, updateTime, createTime);
    }
    
    public SysUserEntity getSysUserByUserId() {
        return sysUserByUserId;
    }
    
    public void setSysUserByUserId(SysUserEntity sysUserByUserId) {
        this.sysUserByUserId = sysUserByUserId;
    }
    
    public Collection<TbAlbumArtistEntity> getTbAlbumArtistsById() {
        return tbAlbumArtistsById;
    }
    
    public void setTbAlbumArtistsById(Collection<TbAlbumArtistEntity> tbAlbumArtistsById) {
        this.tbAlbumArtistsById = tbAlbumArtistsById;
    }
    
    public Collection<TbMusicEntity> getTbMusicsById() {
        return tbMusicsById;
    }
    
    public void setTbMusicsById(Collection<TbMusicEntity> tbMusicsById) {
        this.tbMusicsById = tbMusicsById;
    }
    
    public Collection<TbUserAlbumEntity> getTbUserAlbumsById() {
        return tbUserAlbumsById;
    }
    
    public void setTbUserAlbumsById(Collection<TbUserAlbumEntity> tbUserAlbumsById) {
        this.tbUserAlbumsById = tbUserAlbumsById;
    }
}
