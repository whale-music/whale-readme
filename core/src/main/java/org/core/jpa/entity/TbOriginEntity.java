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
@Table(name = "tb_origin")
public class TbOriginEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1036972429906703454L;
    
    @Id
    @GeneratedValue(generator = "IdGenerator", strategy = GenerationType.AUTO)
    @GenericGenerator(name = "IdGenerator", type = ManualInsertGenerator.class)
    @Column(name = "id", nullable = false)
    private Long id;
    @Basic
    @Column(name = "music_id")
    private Long musicId;
    @Basic
    @Column(name = "resource_id")
    private Long resourceId;
    @Basic
    @Column(name = "origin", nullable = false, length = 256)
    private String origin;
    @Basic
    @Column(name = "origin_url", length = 256)
    private String originUrl;
    @ManyToOne
    @JoinColumn(name = "music_id", referencedColumnName = "id", insertable = false, updatable = false)
    private TbMusicEntity tbMusicByMusicId;
    @ManyToOne
    @JoinColumn(name = "resource_id", referencedColumnName = "id", insertable = false, updatable = false)
    private TbResourceEntity tbResourceByResourceId;
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        
        TbOriginEntity that = (TbOriginEntity) o;
        
        return new EqualsBuilder().append(id, that.id)
                                  .append(musicId, that.musicId)
                                  .append(resourceId, that.resourceId)
                                  .append(origin, that.origin)
                                  .append(originUrl, that.originUrl)
                                  .isEquals();
    }
    
    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).append(musicId).append(resourceId).append(origin).append(originUrl).toHashCode();
    }
}
