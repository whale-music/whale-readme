package org.core.pojo;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class CollectMusicPojoPK implements Serializable {
    @Column(name = "collect_id")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long collectId;
    @Column(name = "music_id")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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
        CollectMusicPojoPK that = (CollectMusicPojoPK) o;
        return collectId == that.collectId && musicId == that.musicId;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(collectId, musicId);
    }
}
