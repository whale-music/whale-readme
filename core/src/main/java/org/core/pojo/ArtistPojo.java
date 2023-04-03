package org.core.pojo;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "tb_artist", schema = "whale_music")
public class ArtistPojo {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    @Column(name = "id")
    private Long id;
    @Basic
    @Column(name = "artist_name")
    private String artistName;
    @Basic
    @Column(name = "alias_name")
    private String aliasName;
    @Basic
    @Column(name = "sex")
    private String sex;
    @Basic
    @Column(name = "pic")
    private String pic;
    @Basic
    @Column(name = "birth")
    private Date birth;
    @Basic
    @Column(name = "location")
    private String location;
    @Basic
    @Column(name = "introduction")
    private String introduction;
    @Basic
    @Column(name = "create_time")
    @CreatedDate
    private LocalDateTime createTime;
    @Basic
    @Column(name = "update_time")
    @LastModifiedDate
    private LocalDateTime updateTime;
    @OneToMany(mappedBy = "tbArtistByArtistId")
    private Collection<AlbumArtistPojo> tbAlbumArtistsById;
    @OneToMany(mappedBy = "tbArtistByArtistId")
    private Collection<UserArtistPojo> tbUserArtistsById;
    
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
    
    public String getPic() {
        return pic;
    }
    
    public void setPic(String pic) {
        this.pic = pic;
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
        ArtistPojo that = (ArtistPojo) o;
        return id == that.id && Objects.equals(artistName, that.artistName) && Objects.equals(aliasName,
                that.aliasName) && Objects.equals(sex, that.sex) && Objects.equals(pic, that.pic) && Objects.equals(birth,
                that.birth) && Objects.equals(location, that.location) && Objects.equals(introduction,
                that.introduction) && Objects.equals(createTime, that.createTime) && Objects.equals(updateTime, that.updateTime);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, artistName, aliasName, sex, pic, birth, location, introduction, createTime, updateTime);
    }
    
    public Collection<AlbumArtistPojo> getTbAlbumArtistsById() {
        return tbAlbumArtistsById;
    }
    
    public void setTbAlbumArtistsById(Collection<AlbumArtistPojo> tbAlbumArtistsById) {
        this.tbAlbumArtistsById = tbAlbumArtistsById;
    }
    
    public Collection<UserArtistPojo> getTbUserArtistsById() {
        return tbUserArtistsById;
    }
    
    public void setTbUserArtistsById(Collection<UserArtistPojo> tbUserArtistsById) {
        this.tbUserArtistsById = tbUserArtistsById;
    }
}
