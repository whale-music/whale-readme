package org.core.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.io.Serializable;
import java.util.Objects;

public class TbMvArtistEntityPK implements Serializable {
    @Column(name = "mv_id", nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mvId;
    @Column(name = "artist_id", nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long artistId;
    
    public Long getMvId() {
        return mvId;
    }
    
    public void setMvId(Long mvId) {
        this.mvId = mvId;
    }
    
    public Long getArtistId() {
        return artistId;
    }
    
    public void setArtistId(Long artistId) {
        this.artistId = artistId;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TbMvArtistEntityPK that = (TbMvArtistEntityPK) o;
        return Objects.equals(mvId, that.mvId) && Objects.equals(artistId, that.artistId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(mvId, artistId);
    }
}
