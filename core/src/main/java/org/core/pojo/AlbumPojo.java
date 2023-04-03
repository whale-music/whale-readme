package org.core.pojo;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "tb_album", schema = "whale_music")
public class AlbumPojo {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    @Column(name = "id")
    private Long id;
    @Basic
    @Column(name = "album_name")
    private String albumName;
    @Basic
    @Column(name = "sub_type")
    private String subType;
    @Basic
    @Column(name = "description")
    private String description;
    @Basic
    @Column(name = "company")
    private String company;
    @Basic
    @Column(name = "pic")
    private String pic;
    @Basic
    @Column(name = "publish_time")
    private LocalDateTime publishTime;
    @Basic
    @Column(name = "update_time")
    @LastModifiedDate
    private LocalDateTime updateTime;
    @Basic
    @Column(name = "create_time")
    @CreatedDate
    private LocalDateTime createTime;
    @OneToMany(mappedBy = "tbAlbumByAlbumId")
    private Collection<AlbumArtistPojo> tbAlbumArtistsById;
    @OneToOne(mappedBy = "tbAlbumByMusicId")
    private HistoryPojo tbHistoryById;
    @OneToMany(mappedBy = "tbAlbumByAlbumId")
    private Collection<MusicPojo> tbMusicsById;
    @OneToMany(mappedBy = "tbAlbumByAlbumId")
    private Collection<UserAlbumPojo> tbUserAlbumsById;
    
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
    
    public String getPic() {
        return pic;
    }
    
    public void setPic(String pic) {
        this.pic = pic;
    }
    
    public LocalDateTime getPublishTime() {
        return publishTime;
    }
    
    public void setPublishTime(LocalDateTime publishTime) {
        this.publishTime = publishTime;
    }
    
    public LocalDateTime getUpdateTime() {
        return updateTime;
    }
    
    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
    
    public LocalDateTime getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(LocalDateTime createTime) {
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
        AlbumPojo that = (AlbumPojo) o;
        return id == that.id && Objects.equals(albumName, that.albumName) && Objects.equals(subType,
                that.subType) && Objects.equals(description, that.description) && Objects.equals(company,
                that.company) && Objects.equals(pic, that.pic) && Objects.equals(publishTime,
                that.publishTime) && Objects.equals(updateTime, that.updateTime) && Objects.equals(createTime, that.createTime);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, albumName, subType, description, company, pic, publishTime, updateTime, createTime);
    }
    
    public Collection<AlbumArtistPojo> getTbAlbumArtistsById() {
        return tbAlbumArtistsById;
    }
    
    public void setTbAlbumArtistsById(Collection<AlbumArtistPojo> tbAlbumArtistsById) {
        this.tbAlbumArtistsById = tbAlbumArtistsById;
    }
    
    public HistoryPojo getTbHistoryById() {
        return tbHistoryById;
    }
    
    public void setTbHistoryById(HistoryPojo tbHistoryById) {
        this.tbHistoryById = tbHistoryById;
    }
    
    public Collection<MusicPojo> getTbMusicsById() {
        return tbMusicsById;
    }
    
    public void setTbMusicsById(Collection<MusicPojo> tbMusicsById) {
        this.tbMusicsById = tbMusicsById;
    }
    
    public Collection<UserAlbumPojo> getTbUserAlbumsById() {
        return tbUserAlbumsById;
    }
    
    public void setTbUserAlbumsById(Collection<UserAlbumPojo> tbUserAlbumsById) {
        this.tbUserAlbumsById = tbUserAlbumsById;
    }
}
