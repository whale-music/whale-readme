package org.core.jpa.entity;


import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.io.Serializable;
import java.util.Objects;

public class TbAlbumArtistEntityPK implements Serializable {
    @Column(name = "album_id", nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long albumId;
    @Column(name = "artist_id", nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long artistId;
    
    public Long getAlbumId() {
        return albumId;
    }
    
    public void setAlbumId(Long albumId) {
        this.albumId = albumId;
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
        TbAlbumArtistEntityPK that = (TbAlbumArtistEntityPK) o;
        return Objects.equals(albumId, that.albumId) && Objects.equals(artistId, that.artistId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(albumId, artistId);
    }
}
