package org.core.pojo;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "tb_collect_music", schema = "whale_music")
@IdClass(CollectMusicPojoPK.class)
public class CollectMusicPojo {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    @Column(name = "collect_id")
    private Long collectId;
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    @Column(name = "music_id")
    private Long musicId;
    @ManyToOne
    @JoinColumn(name = "collect_id", referencedColumnName = "id", nullable = false)
    private CollectPojo tbCollectByCollectId;
    @ManyToOne
    @JoinColumn(name = "music_id", referencedColumnName = "id", nullable = false)
    private MusicPojo tbMusicByMusicId;
    
    public Long getCollectId() {
        return collectId;
    }
    
    public void setCollectId(Long collectId) {
        this.collectId = collectId;
    }
    
    public Long getMusicId() {
        return musicId;
    }
    
    public void setMusicId(Long musicId) {
        this.musicId = musicId;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CollectMusicPojo that = (CollectMusicPojo) o;
        return collectId == that.collectId && musicId == that.musicId;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(collectId, musicId);
    }
    
    public CollectPojo getTbCollectByCollectId() {
        return tbCollectByCollectId;
    }
    
    public void setTbCollectByCollectId(CollectPojo tbCollectByCollectId) {
        this.tbCollectByCollectId = tbCollectByCollectId;
    }
    
    public MusicPojo getTbMusicByMusicId() {
        return tbMusicByMusicId;
    }
    
    public void setTbMusicByMusicId(MusicPojo tbMusicByMusicId) {
        this.tbMusicByMusicId = tbMusicByMusicId;
    }
}
