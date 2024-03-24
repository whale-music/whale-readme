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
@Table(name = "tb_user_album")
@IdClass(TbUserAlbumEntityPK.class)
public class TbUserAlbumEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 2405172432150251807L;
    
    @Id
    @GeneratedValue(generator = "IdGenerator", strategy = GenerationType.AUTO)
    @GenericGenerator(name = "IdGenerator", type = ManualInsertGenerator.class)
    @Column(name = "user_id", nullable = false)
    private Long userId;
    @Id
    @GeneratedValue(generator = "IdGenerator", strategy = GenerationType.AUTO)
    @GenericGenerator(name = "IdGenerator", type = ManualInsertGenerator.class)
    @Column(name = "album_id", nullable = false)
    private Long albumId;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    private SysUserEntity sysUserByUserId;
    @ManyToOne
    @JoinColumn(name = "album_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    private TbAlbumEntity tbAlbumByAlbumId;
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        
        TbUserAlbumEntity that = (TbUserAlbumEntity) o;
        
        return new EqualsBuilder().append(userId, that.userId).append(albumId, that.albumId).isEquals();
    }
    
    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(userId).append(albumId).toHashCode();
    }
}
