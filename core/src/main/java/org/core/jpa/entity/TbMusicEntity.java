package org.core.jpa.entity;

import jakarta.persistence.*;
import org.core.jpa.config.ManualInsertGenerator;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "tb_music")
public class TbMusicEntity implements Serializable {
    public static final long serialVersionUID = 3852731638450316352L;
    
    @Id
    @GeneratedValue(generator = "IdGenerator", strategy = GenerationType.AUTO)
    @GenericGenerator(name = "IdGenerator", type = ManualInsertGenerator.class)
    @Column(name = "id", nullable = false)
    private Long id;
    @Basic
    @Column(name = "music_name", nullable = true, length = 128)
    private String musicName;
    @Basic
    @Column(name = "alias_name", nullable = true, length = 512)
    private String aliasName;
    @Basic
    @Column(name = "album_id", nullable = true)
    private Long albumId;
    @Basic
    @Column(name = "sort", nullable = false)
    private Long sort;
    @Basic
    @Column(name = "user_id", nullable = true)
    private Long userId;
    @Basic
    @Column(name = "time_length", nullable = true)
    private Integer timeLength;
    @Basic
    @Column(name = "update_time", nullable = true)
    private Timestamp updateTime;
    @Basic
    @Column(name = "create_time", nullable = true)
    private Timestamp createTime;
    @OneToMany(mappedBy = "tbMusicByMusicId", fetch = FetchType.EAGER)
    private Collection<TbCollectMusicEntity> tbCollectMusicsById;
    @OneToMany(mappedBy = "tbMusicByMusicId", fetch = FetchType.EAGER)
    private Collection<TbLyricEntity> tbLyricsById;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "album_id", referencedColumnName = "id", insertable = false, updatable = false)
    private TbAlbumEntity tbAlbumByAlbumId;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false)
    private SysUserEntity sysUserByUserId;
    @OneToMany(mappedBy = "tbMusicByMusicId", fetch = FetchType.EAGER)
    private Collection<TbMusicArtistEntity> tbMusicArtistsById;
    @OneToMany(mappedBy = "tbMusicByMusicId", fetch = FetchType.EAGER)
    private Collection<TbOriginEntity> tbOriginsById;
    @OneToMany(mappedBy = "tbMusicByMusicId", fetch = FetchType.EAGER)
    private Collection<TbResourceEntity> tbResourcesById;
    
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
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public Integer getTimeLength() {
        return timeLength;
    }
    
    public void setTimeLength(Integer timeLength) {
        this.timeLength = timeLength;
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
        TbMusicEntity that = (TbMusicEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(musicName, that.musicName) && Objects.equals(aliasName,
                that.aliasName) && Objects.equals(albumId, that.albumId) && Objects.equals(sort, that.sort) && Objects.equals(
                userId,
                that.userId) && Objects.equals(timeLength, that.timeLength) && Objects.equals(updateTime,
                that.updateTime) && Objects.equals(createTime, that.createTime);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, musicName, aliasName, albumId, sort, userId, timeLength, updateTime, createTime);
    }
    
    public Collection<TbCollectMusicEntity> getTbCollectMusicsById() {
        return tbCollectMusicsById;
    }
    
    public void setTbCollectMusicsById(Collection<TbCollectMusicEntity> tbCollectMusicsById) {
        this.tbCollectMusicsById = tbCollectMusicsById;
    }
    
    public Collection<TbLyricEntity> getTbLyricsById() {
        return tbLyricsById;
    }
    
    public void setTbLyricsById(Collection<TbLyricEntity> tbLyricsById) {
        this.tbLyricsById = tbLyricsById;
    }
    
    public TbAlbumEntity getTbAlbumByAlbumId() {
        return tbAlbumByAlbumId;
    }
    
    public void setTbAlbumByAlbumId(TbAlbumEntity tbAlbumByAlbumId) {
        this.tbAlbumByAlbumId = tbAlbumByAlbumId;
    }
    
    public SysUserEntity getSysUserByUserId() {
        return sysUserByUserId;
    }
    
    public void setSysUserByUserId(SysUserEntity sysUserByUserId) {
        this.sysUserByUserId = sysUserByUserId;
    }
    
    public Collection<TbMusicArtistEntity> getTbMusicArtistsById() {
        return tbMusicArtistsById;
    }
    
    public void setTbMusicArtistsById(Collection<TbMusicArtistEntity> tbMusicArtistsById) {
        this.tbMusicArtistsById = tbMusicArtistsById;
    }
    
    public Collection<TbOriginEntity> getTbOriginsById() {
        return tbOriginsById;
    }
    
    public void setTbOriginsById(Collection<TbOriginEntity> tbOriginsById) {
        this.tbOriginsById = tbOriginsById;
    }
    
    public Collection<TbResourceEntity> getTbResourcesById() {
        return tbResourcesById;
    }
    
    public void setTbResourcesById(Collection<TbResourceEntity> tbResourcesById) {
        this.tbResourcesById = tbResourcesById;
    }
}
