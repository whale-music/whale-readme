package org.core.pojo;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;

@Entity
@Table(name = "tb_music_url", schema = "whale_music")
public class MusicUrlPojo {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    @Column(name = "id")
    private Long id;
    @Basic
    @Column(name = "music_id", insertable = false, updatable = false)
    private Long musicId;
    @Basic
    @Column(name = "rate")
    private Integer rate;
    @Basic
    @Column(name = "url")
    private String url;
    @Basic
    @Column(name = "md5")
    private String md5;
    @Basic
    @Column(name = "level")
    private String level;
    @Basic
    @Column(name = "encode_type")
    private String encodeType;
    @Basic
    @Column(name = "size")
    private Long size;
    @Basic
    @Column(name = "user_id")
    private Long userId;
    @Basic
    @Column(name = "origin")
    private String origin;
    @Basic
    @Column(name = "create_time")
    @CreatedDate
    private LocalDateTime createTime;
    @Basic
    @Column(name = "update_time")
    @LastModifiedDate
    private LocalDateTime updateTime;
    @ManyToOne
    @JoinColumn(name = "music_id", referencedColumnName = "id", nullable = false)
    private MusicPojo tbMusicByMusicId;
    
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
    
    public String getUrl() {
        return url;
    }
    
    public void setUrl(String url) {
        this.url = url;
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
    
    public String getOrigin() {
        return origin;
    }
    
    public void setOrigin(String origin) {
        this.origin = origin;
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
        MusicUrlPojo that = (MusicUrlPojo) o;
        return Objects.equals(id, that.id) && musicId == that.musicId && userId == that.userId && Objects.equals(rate, that.rate) && Objects.equals(url,
                that.url) && Objects.equals(md5, that.md5) && Objects.equals(level, that.level) && Objects.equals(encodeType,
                that.encodeType) && Objects.equals(size, that.size) && Objects.equals(origin, that.origin) && Objects.equals(
                createTime,
                that.createTime) && Objects.equals(updateTime, that.updateTime);
    }
    
    @Override
    public int hashCode() {
        int result = Objects.hash(id, musicId, rate, url, md5, level, encodeType, size, userId, createTime, updateTime);
        result = 31 * result + Arrays.hashCode(origin.toCharArray());
        return result;
    }
    
    public MusicPojo getTbMusicByMusicId() {
        return tbMusicByMusicId;
    }
    
    public void setTbMusicByMusicId(MusicPojo tbMusicByMusicId) {
        this.tbMusicByMusicId = tbMusicByMusicId;
    }
}
