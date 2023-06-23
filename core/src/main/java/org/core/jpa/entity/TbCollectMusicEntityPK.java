package org.core.jpa.entity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class TbCollectMusicEntityPK implements Serializable {
    @Column(name = "collect_id", nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long collectId;
    @Column(name = "music_id", nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long musicId;
    
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
        TbCollectMusicEntityPK that = (TbCollectMusicEntityPK) o;
        return Objects.equals(collectId, that.collectId) && Objects.equals(musicId, that.musicId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(collectId, musicId);
    }
}
