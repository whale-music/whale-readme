package org.core.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serial;
import java.io.Serializable;

@Setter
@Getter
public class TbMvArtistEntityPK implements Serializable {
    
    @Serial
    private static final long serialVersionUID = 385211638450316352L;
    
    @Column(name = "mv_id", nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mvId;
    @Column(name = "artist_id", nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long artistId;
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        
        TbMvArtistEntityPK that = (TbMvArtistEntityPK) o;
        
        return new EqualsBuilder().append(mvId, that.mvId).append(artistId, that.artistId).isEquals();
    }
    
    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(mvId).append(artistId).toHashCode();
    }
}
