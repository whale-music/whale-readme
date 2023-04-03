package org.core.pojo;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "tb_music", schema = "whale_music")
public class MusicPojo {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    @Column(name = "id")
    private Long id;
    @Basic
    @Column(name = "music_name")
    private String musicName;
    @Basic
    @Column(name = "alias_name")
    private String aliasName;
    @Basic
    @Column(name = "pic")
    private String pic;
    @Basic
    @Column(name = "lyric")
    private String lyric;
    @Basic
    @Column(name = "k_lyric")
    private String kLyric;
    @Basic
    @Column(name = "album_id", insertable = false, updatable = false)
    private Long albumId;
    @Basic
    @Column(name = "sort")
    private Long sort;
    @Basic
    @Column(name = "time_length")
    private Integer timeLength;
    @Basic
    @Column(name = "update_time")
    @LastModifiedDate
    private LocalDateTime updateTime;
    @Basic
    @Column(name = "create_time")
    @CreatedDate
    private LocalDateTime createTime;
    @OneToMany(mappedBy = "tbMusicByMusicId")
    private Collection<CollectMusicPojo> tbCollectMusicsById;
    @OneToOne(mappedBy = "tbMusicByMusicId")
    private HistoryPojo tbHistoryById;
    @ManyToOne
    @JoinColumn(name = "album_id", referencedColumnName = "id")
    private AlbumPojo tbAlbumByAlbumId;
    @OneToMany(mappedBy = "tbMusicByMusicId")
    private Collection<MusicUrlPojo> tbMusicUrlsById;
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getMusicName() {
        return musicName;
    }
    
    public void setMusicName(String musicName) {
        this.musicName = musicName;
    }
    
    public String getAliasName() {
        return aliasName;
    }
    
    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }
    
    public String getPic() {
        return pic;
    }
    
    public void setPic(String pic) {
        this.pic = pic;
    }
    
    public String getLyric() {
        return lyric;
    }
    
    public void setLyric(String lyric) {
        this.lyric = lyric;
    }
    
    public String getkLyric() {
        return kLyric;
    }
    
    public void setkLyric(String kLyric) {
        this.kLyric = kLyric;
    }
    
    public Long getAlbumId() {
        return albumId;
    }
    
    public void setAlbumId(Long albumId) {
        this.albumId = albumId;
    }
    
    public Long getSort() {
        return sort;
    }
    
    public void setSort(Long sort) {
        this.sort = sort;
    }
    
    public Integer getTimeLength() {
        return timeLength;
    }
    
    public void setTimeLength(Integer timeLength) {
        this.timeLength = timeLength;
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
        MusicPojo that = (MusicPojo) o;
        return id == that.id && Objects.equals(musicName, that.musicName) && Objects.equals(aliasName,
                that.aliasName) && Objects.equals(pic, that.pic) && Objects.equals(lyric, that.lyric) && Objects.equals(kLyric,
                that.kLyric) && Objects.equals(albumId, that.albumId) && Objects.equals(sort, that.sort) && Objects.equals(
                timeLength,
                that.timeLength) && Objects.equals(updateTime, that.updateTime) && Objects.equals(createTime, that.createTime);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, musicName, aliasName, pic, lyric, kLyric, albumId, sort, timeLength, updateTime, createTime);
    }
    
    public Collection<CollectMusicPojo> getTbCollectMusicsById() {
        return tbCollectMusicsById;
    }
    
    public void setTbCollectMusicsById(Collection<CollectMusicPojo> tbCollectMusicsById) {
        this.tbCollectMusicsById = tbCollectMusicsById;
    }
    
    public HistoryPojo getTbHistoryById() {
        return tbHistoryById;
    }
    
    public void setTbHistoryById(HistoryPojo tbHistoryById) {
        this.tbHistoryById = tbHistoryById;
    }
    
    public AlbumPojo getTbAlbumByAlbumId() {
        return tbAlbumByAlbumId;
    }
    
    public void setTbAlbumByAlbumId(AlbumPojo tbAlbumByAlbumId) {
        this.tbAlbumByAlbumId = tbAlbumByAlbumId;
    }
    
    public Collection<MusicUrlPojo> getTbMusicUrlsById() {
        return tbMusicUrlsById;
    }
    
    public void setTbMusicUrlsById(Collection<MusicUrlPojo> tbMusicUrlsById) {
        this.tbMusicUrlsById = tbMusicUrlsById;
    }
}
