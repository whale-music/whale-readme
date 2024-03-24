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
@Table(name = "tb_collect_music")
@IdClass(TbCollectMusicEntityPK.class)
public class TbCollectMusicEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 385211638450316352L;
    
    @Id
    @GeneratedValue(generator = "IdGenerator", strategy = GenerationType.AUTO)
    @GenericGenerator(name = "IdGenerator", type = ManualInsertGenerator.class)
    @Column(name = "collect_id", nullable = false)
    private Long collectId;
    @Id
    @GeneratedValue(generator = "IdGenerator", strategy = GenerationType.AUTO)
    @GenericGenerator(name = "IdGenerator", type = ManualInsertGenerator.class)
    @Column(name = "music_id", nullable = false)
    private Long musicId;
    @Basic
    @Column(name = "sort", nullable = false)
    private Long sort;
    @ManyToOne
    @JoinColumn(name = "collect_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    private TbCollectEntity tbCollectByCollectId;
    @ManyToOne
    @JoinColumn(name = "music_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    private TbMusicEntity tbMusicByMusicId;
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        
        TbCollectMusicEntity that = (TbCollectMusicEntity) o;
        
        return new EqualsBuilder().append(collectId, that.collectId)
                                  .append(musicId, that.musicId)
                                  .append(sort, that.sort)
                                  .isEquals();
    }
    
    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(collectId).append(musicId).append(sort).toHashCode();
    }
}
