package org.core.jpa.entity;

import jakarta.persistence.*;
import org.core.jpa.config.ManualInsertGenerator;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "tb_music_artist")
@IdClass(TbMusicArtistEntityPK.class)
public class TbMusicArtistEntity implements Serializable {
    public static final long serialVersionUID = 3852731638112316352L;
    
    @Id
    @GeneratedValue(generator = "IdGenerator", strategy = GenerationType.AUTO)
    @GenericGenerator(name = "IdGenerator", type = ManualInsertGenerator.class)
    @Column(name = "music_id", nullable = false)
    private Long musicId;
    @Id
    @GeneratedValue(generator = "IdGenerator", strategy = GenerationType.AUTO)
    @GenericGenerator(name = "IdGenerator", type = ManualInsertGenerator.class)
    @Column(name = "artist_id", nullable = false)
    private Long artistId;
    @ManyToOne
    @JoinColumn(name = "music_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    private TbMusicEntity tbMusicByMusicId;
    @ManyToOne
    @JoinColumn(name = "artist_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    private TbArtistEntity tbArtistByArtistId;
    
    public Long getMusicId() {
        return musicId;
    }
    
    public void setMusicId(Long musicId) {
        this.musicId = musicId;
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
        TbMusicArtistEntity that = (TbMusicArtistEntity) o;
        return Objects.equals(musicId, that.musicId) && Objects.equals(artistId, that.artistId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(musicId, artistId);
    }
    
    public TbMusicEntity getTbMusicByMusicId() {
        return tbMusicByMusicId;
    }
    
    public void setTbMusicByMusicId(TbMusicEntity tbMusicByMusicId) {
        this.tbMusicByMusicId = tbMusicByMusicId;
    }
    
    public TbArtistEntity getTbArtistByArtistId() {
        return tbArtistByArtistId;
    }
    
    public void setTbArtistByArtistId(TbArtistEntity tbArtistByArtistId) {
        this.tbArtistByArtistId = tbArtistByArtistId;
    }
}
