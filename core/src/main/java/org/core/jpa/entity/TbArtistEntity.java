package org.core.jpa.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "tb_artist")
public class TbArtistEntity implements Serializable {
    public static final long serialVersionUID = 3852731638450316352L;
    
    @Id
    @GeneratedValue(generator = "IdGenerator", strategy = GenerationType.AUTO)
    @GenericGenerator(name = "IdGenerator", strategy = "org.core.jpa.config.ManualInsertGenerator")
    @Column(name = "id", nullable = false)
    private Long id;
    @Basic
    @Column(name = "artist_name", nullable = false, length = 128)
    private String artistName;
    @Basic
    @Column(name = "alias_name", nullable = true, length = 255)
    private String aliasName;
    @Basic
    @Column(name = "sex", nullable = true, length = 64)
    private String sex;
    @Basic
    @Column(name = "birth", nullable = true)
    private Date birth;
    @Basic
    @Column(name = "location", nullable = true, length = 64)
    private String location;
    @Basic
    @Column(name = "introduction", nullable = true, length = Integer.MAX_VALUE)
    private String introduction;
    @Basic
    @Column(name = "user_id", nullable = true)
    private Long userId;
    @Basic
    @Column(name = "create_time", nullable = true)
    private Timestamp createTime;
    @Basic
    @Column(name = "update_time", nullable = true)
    private Timestamp updateTime;
    @OneToMany(mappedBy = "tbArtistByArtistId")
    private Collection<TbAlbumArtistEntity> tbAlbumArtistsById;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false)
    private SysUserEntity sysUserByUserId;
    @OneToMany(mappedBy = "tbArtistByMiddleId")
    private Collection<TbMiddlePicEntity> tbMiddlePicsById;
    @OneToMany(mappedBy = "tbArtistByMiddleId")
    private Collection<TbMiddleTagEntity> tbMiddleTagsById;
    @OneToMany(mappedBy = "tbArtistByArtistId")
    private Collection<TbMusicArtistEntity> tbMusicArtistsById;
    @OneToMany(mappedBy = "tbArtistByArtistId")
    private Collection<TbUserArtistEntity> tbUserArtistsById;
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getArtistName() {
        return artistName;
    }
    
    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }
    
    public String getAliasName() {
        return aliasName;
    }
    
    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }
    
    public String getSex() {
        return sex;
    }
    
    public void setSex(String sex) {
        this.sex = sex;
    }
    
    public Date getBirth() {
        return birth;
    }
    
    public void setBirth(Date birth) {
        this.birth = birth;
    }
    
    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
    
    public String getIntroduction() {
        return introduction;
    }
    
    public void setIntroduction(String introduction) {
        this.introduction = introduction;
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
        TbArtistEntity that = (TbArtistEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(artistName, that.artistName) && Objects.equals(aliasName,
                that.aliasName) && Objects.equals(sex, that.sex) && Objects.equals(birth, that.birth) && Objects.equals(location,
                that.location) && Objects.equals(introduction, that.introduction) && Objects.equals(userId,
                that.userId) && Objects.equals(createTime, that.createTime) && Objects.equals(updateTime, that.updateTime);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, artistName, aliasName, sex, birth, location, introduction, userId, createTime, updateTime);
    }
    
    public Collection<TbAlbumArtistEntity> getTbAlbumArtistsById() {
        return tbAlbumArtistsById;
    }
    
    public void setTbAlbumArtistsById(Collection<TbAlbumArtistEntity> tbAlbumArtistsById) {
        this.tbAlbumArtistsById = tbAlbumArtistsById;
    }
    
    public SysUserEntity getSysUserByUserId() {
        return sysUserByUserId;
    }
    
    public void setSysUserByUserId(SysUserEntity sysUserByUserId) {
        this.sysUserByUserId = sysUserByUserId;
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
    
    public Collection<TbMusicArtistEntity> getTbMusicArtistsById() {
        return tbMusicArtistsById;
    }
    
    public void setTbMusicArtistsById(Collection<TbMusicArtistEntity> tbMusicArtistsById) {
        this.tbMusicArtistsById = tbMusicArtistsById;
    }
    
    public Collection<TbUserArtistEntity> getTbUserArtistsById() {
        return tbUserArtistsById;
    }
    
    public void setTbUserArtistsById(Collection<TbUserArtistEntity> tbUserArtistsById) {
        this.tbUserArtistsById = tbUserArtistsById;
    }
}
