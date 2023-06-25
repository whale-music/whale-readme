package org.core.jpa.entity;

import jakarta.persistence.*;
import org.core.jpa.config.ManualInsertGenerator;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.util.Objects;

/**
 * 歌手和专辑中间表
 */
@Entity
@Table(name = "tb_album_artist")
@IdClass(TbAlbumArtistEntityPK.class)
public class TbAlbumArtistEntity implements Serializable {
    public static final long serialVersionUID = 3112731638450316352L;
    
    @Id
    @GeneratedValue(generator = "IdGenerator", strategy = GenerationType.AUTO)
    @GenericGenerator(name = "IdGenerator", type = ManualInsertGenerator.class)
    @Column(name = "album_id", nullable = false)
    private Long albumId;
    @Id
    @GeneratedValue(generator = "IdGenerator", strategy = GenerationType.AUTO)
    @GenericGenerator(name = "IdGenerator", type = ManualInsertGenerator.class)
    @Column(name = "artist_id", nullable = false)
    private Long artistId;
    @ManyToOne
    @JoinColumn(name = "album_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    private TbAlbumEntity tbAlbumByAlbumId;
    @ManyToOne
    @JoinColumn(name = "artist_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    private TbArtistEntity tbArtistByArtistId;
    
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
        TbAlbumArtistEntity that = (TbAlbumArtistEntity) o;
        return Objects.equals(albumId, that.albumId) && Objects.equals(artistId, that.artistId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(albumId, artistId);
    }
    
    public TbAlbumEntity getTbAlbumByAlbumId() {
        return tbAlbumByAlbumId;
    }
    
    public void setTbAlbumByAlbumId(TbAlbumEntity tbAlbumByAlbumId) {
        this.tbAlbumByAlbumId = tbAlbumByAlbumId;
    }
    
    public TbArtistEntity getTbArtistByArtistId() {
        return tbArtistByArtistId;
    }
    
    public void setTbArtistByArtistId(TbArtistEntity tbArtistByArtistId) {
        this.tbArtistByArtistId = tbArtistByArtistId;
    }
}
