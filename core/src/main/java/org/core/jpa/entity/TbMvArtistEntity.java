package org.core.jpa.entity;

import jakarta.persistence.*;
import org.core.jpa.config.ManualInsertGenerator;
import org.hibernate.annotations.GenericGenerator;

import java.util.Objects;

@Entity
@Table(name = "tb_mv_artist")
@IdClass(TbMvArtistEntityPK.class)
public class TbMvArtistEntity {
    @Id
    @GeneratedValue(generator = "IdGenerator", strategy = GenerationType.AUTO)
    @GenericGenerator(name = "IdGenerator", type = ManualInsertGenerator.class)
    @Column(name = "mv_id", nullable = false)
    private Long mvId;
    @Id
    @GeneratedValue(generator = "IdGenerator", strategy = GenerationType.AUTO)
    @GenericGenerator(name = "IdGenerator", type = ManualInsertGenerator.class)
    @Column(name = "artist_id", nullable = false)
    private Long artistId;
    @ManyToOne
    @JoinColumn(name = "mv_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    private TbMvEntity tbMvByMvId;
    @ManyToOne
    @JoinColumn(name = "artist_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    private TbArtistEntity tbArtistByArtistId;
    
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
        TbMvArtistEntity that = (TbMvArtistEntity) o;
        return Objects.equals(mvId, that.mvId) && Objects.equals(artistId, that.artistId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(mvId, artistId);
    }
    
    public TbMvEntity getTbMvByMvId() {
        return tbMvByMvId;
    }
    
    public void setTbMvByMvId(TbMvEntity tbMvByMvId) {
        this.tbMvByMvId = tbMvByMvId;
    }
    
    public TbArtistEntity getTbArtistByArtistId() {
        return tbArtistByArtistId;
    }
    
    public void setTbArtistByArtistId(TbArtistEntity tbArtistByArtistId) {
        this.tbArtistByArtistId = tbArtistByArtistId;
    }
}
