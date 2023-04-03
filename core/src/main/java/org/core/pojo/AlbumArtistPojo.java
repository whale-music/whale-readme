package org.core.pojo;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "tb_album_artist", schema = "whale_music")
@IdClass(AlbumArtistPojoPK.class)
public class AlbumArtistPojo {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    @Column(name = "album_id")
    private Long albumId;
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    @Column(name = "artist_id")
    private Long artistId;
    @ManyToOne
    @JoinColumn(name = "album_id", referencedColumnName = "id", nullable = false)
    private AlbumPojo tbAlbumByAlbumId;
    @ManyToOne
    @JoinColumn(name = "artist_id", referencedColumnName = "id", nullable = false)
    private ArtistPojo tbArtistByArtistId;
    
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
        AlbumArtistPojo that = (AlbumArtistPojo) o;
        return albumId == that.albumId && artistId == that.artistId;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(albumId, artistId);
    }
    
    public AlbumPojo getTbAlbumByAlbumId() {
        return tbAlbumByAlbumId;
    }
    
    public void setTbAlbumByAlbumId(AlbumPojo tbAlbumByAlbumId) {
        this.tbAlbumByAlbumId = tbAlbumByAlbumId;
    }
    
    public ArtistPojo getTbArtistByArtistId() {
        return tbArtistByArtistId;
    }
    
    public void setTbArtistByArtistId(ArtistPojo tbArtistByArtistId) {
        this.tbArtistByArtistId = tbArtistByArtistId;
    }
}
