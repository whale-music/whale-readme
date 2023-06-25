package org.core.jpa.entity;

import jakarta.persistence.*;
import org.core.jpa.config.ManualInsertGenerator;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "tb_lyric")
public class TbLyricEntity implements Serializable {
    public static final long serialVersionUID = 3852731638450116352L;
    
    @Id
    @GeneratedValue(generator = "IdGenerator", strategy = GenerationType.AUTO)
    @GenericGenerator(name = "IdGenerator", type = ManualInsertGenerator.class)
    @Column(name = "id", nullable = false)
    private Long id;
    @Basic
    @Column(name = "music_id", nullable = false)
    private Long musicId;
    @Basic
    @Column(name = "type", nullable = false, length = 24)
    private String type;
    @Basic
    @Column(name = "lyric", nullable = false, length = Integer.MAX_VALUE)
    private String lyric;
    @Basic
    @Column(name = "create_time", nullable = false)
    private Timestamp createTime;
    @Basic
    @Column(name = "update_time", nullable = false)
    private Timestamp updateTime;
    @ManyToOne
    @JoinColumn(name = "music_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    private TbMusicEntity tbMusicByMusicId;
    
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
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public String getLyric() {
        return lyric;
    }
    
    public void setLyric(String lyric) {
        this.lyric = lyric;
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
        TbLyricEntity that = (TbLyricEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(musicId, that.musicId) && Objects.equals(type,
                that.type) && Objects.equals(lyric, that.lyric) && Objects.equals(createTime, that.createTime) && Objects.equals(
                updateTime,
                that.updateTime);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, musicId, type, lyric, createTime, updateTime);
    }
    
    public TbMusicEntity getTbMusicByMusicId() {
        return tbMusicByMusicId;
    }
    
    public void setTbMusicByMusicId(TbMusicEntity tbMusicByMusicId) {
        this.tbMusicByMusicId = tbMusicByMusicId;
    }
}
