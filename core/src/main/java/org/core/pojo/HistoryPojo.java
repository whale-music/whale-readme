package org.core.pojo;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "tb_history", schema = "whale_music")
public class HistoryPojo {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    @Column(name = "music_id")
    private Long musicId;
    @Basic
    @Column(name = "count")
    private Integer count;
    @Basic
    @Column(name = "type")
    private Integer type;
    @Basic
    @Column(name = "create_time")
    @CreatedDate
    private LocalDateTime createTime;
    @Basic
    @Column(name = "update_time")
    @LastModifiedDate
    private LocalDateTime updateTime;
    @OneToOne
    @JoinColumn(name = "music_id", referencedColumnName = "id", nullable = false)
    private AlbumPojo tbAlbumByMusicId;
    @OneToOne
    @JoinColumn(name = "music_id", referencedColumnName = "id", nullable = false)
    private CollectPojo tbCollectByMusicId;
    @OneToOne
    @JoinColumn(name = "music_id", referencedColumnName = "id", nullable = false)
    private MusicPojo tbMusicByMusicId;
    
    public Long getMusicId() {
        return musicId;
    }
    
    public void setMusicId(Long musicId) {
        this.musicId = musicId;
    }
    
    public Integer getCount() {
        return count;
    }
    
    public void setCount(Integer count) {
        this.count = count;
    }
    
    public Integer getType() {
        return type;
    }
    
    public void setType(Integer type) {
        this.type = type;
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
        HistoryPojo that = (HistoryPojo) o;
        return musicId == that.musicId && Objects.equals(count, that.count) && Objects.equals(type, that.type) && Objects.equals(
                createTime,
                that.createTime) && Objects.equals(updateTime, that.updateTime);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(musicId, count, type, createTime, updateTime);
    }
    
    public AlbumPojo getTbAlbumByMusicId() {
        return tbAlbumByMusicId;
    }
    
    public void setTbAlbumByMusicId(AlbumPojo tbAlbumByMusicId) {
        this.tbAlbumByMusicId = tbAlbumByMusicId;
    }
    
    public CollectPojo getTbCollectByMusicId() {
        return tbCollectByMusicId;
    }
    
    public void setTbCollectByMusicId(CollectPojo tbCollectByMusicId) {
        this.tbCollectByMusicId = tbCollectByMusicId;
    }
    
    public MusicPojo getTbMusicByMusicId() {
        return tbMusicByMusicId;
    }
    
    public void setTbMusicByMusicId(MusicPojo tbMusicByMusicId) {
        this.tbMusicByMusicId = tbMusicByMusicId;
    }
}
