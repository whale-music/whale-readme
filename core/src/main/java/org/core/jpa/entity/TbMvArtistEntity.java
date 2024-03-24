package org.core.jpa.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.core.jpa.config.ManualInsertGenerator;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serial;
import java.io.Serializable;

@Setter
@Getter
@Entity
@Table(name = "tb_mv_artist")
@IdClass(TbMvArtistEntityPK.class)
public class TbMvArtistEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 2405432543551807L;
    
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
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        
        TbMvArtistEntity that = (TbMvArtistEntity) o;
        
        return new EqualsBuilder().append(mvId, that.mvId).append(artistId, that.artistId).isEquals();
    }
    
    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(mvId).append(artistId).toHashCode();
    }
}
