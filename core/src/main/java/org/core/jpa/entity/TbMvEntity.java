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
import java.util.Collection;

@Setter
@Getter
@Entity
@Table(name = "tb_mv")
public class TbMvEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 2405432543551807L;
    
    @Id
    @GeneratedValue(generator = "IdGenerator", strategy = GenerationType.AUTO)
    @GenericGenerator(name = "IdGenerator", type = ManualInsertGenerator.class)
    @Column(name = "id", nullable = false)
    private Long id;
    @Basic
    @Column(name = "mv_id")
    private Long mvId;
    @Basic
    @Column(name = "path", nullable = false)
    private String path;
    @Basic
    @Column(name = "md5", nullable = false, length = 32)
    private String md5;
    @Basic
    @Column(name = "duration")
    private Long duration;
    @Basic
    @Column(name = "user_id")
    private Long userId;
    @Basic
    @Column(name = "create_time")
    private Timestamp createTime;
    @Basic
    @Column(name = "update_time")
    private Timestamp updateTime;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false)
    private SysUserEntity sysUserByUserId;
    @OneToMany(mappedBy = "tbMvByMvId")
    private Collection<TbMvArtistEntity> tbMvArtistsById;
    @OneToMany(mappedBy = "tbMvByMvId")
    private Collection<TbUserMvEntity> tbUserMvsById;
    @ManyToOne
    @JoinColumn(name = "mv_id", referencedColumnName = "id", insertable = false, updatable = false)
    private TbMvInfoEntity tbMvInfoEntity;
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        
        TbMvEntity that = (TbMvEntity) o;
        
        return new EqualsBuilder().append(id, that.id)
                                  .append(mvId, that.mvId)
                                  .append(path, that.path)
                                  .append(md5, that.md5)
                                  .append(duration, that.duration)
                                  .append(userId, that.userId)
                                  .append(createTime, that.createTime)
                                  .append(updateTime, that.updateTime)
                                  .isEquals();
    }
    
    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id)
                                          .append(mvId)
                                          .append(path)
                                          .append(md5)
                                          .append(duration)
                                          .append(userId)
                                          .append(createTime)
                                          .append(updateTime)
                                          .toHashCode();
    }
}
