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
import java.sql.Timestamp;

@Setter
@Getter
@Entity
@Table(name = "tb_lyric")
public class TbLyricEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 3852731638450116352L;
    
    @Id
    @GeneratedValue(generator = "IdGenerator", strategy = GenerationType.AUTO)
    @GenericGenerator(name = "IdGenerator", type = ManualInsertGenerator.class)
    @Column(name = "id", nullable = false)
    private Long id;
    @Basic
    @Column(name = "music_id", nullable = false)
    private Long musicId;
    @Basic
    @Column(name = "type", nullable = false, length = 24)
    private String type;
    @Basic
    @Column(name = "lyric", nullable = false, length = Integer.MAX_VALUE)
    private String lyric;
    @Basic
    @Column(name = "create_time", nullable = false)
    private Timestamp createTime;
    @Basic
    @Column(name = "update_time", nullable = false)
    private Timestamp updateTime;
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
        
        TbLyricEntity that = (TbLyricEntity) o;
        
        return new EqualsBuilder().append(id, that.id)
                                  .append(musicId, that.musicId)
                                  .append(type, that.type)
                                  .append(lyric, that.lyric)
                                  .append(createTime, that.createTime)
                                  .append(updateTime, that.updateTime)
                                  .isEquals();
    }
    
    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).append(musicId).append(type).append(lyric).append(createTime).append(updateTime).toHashCode();
    }
}
