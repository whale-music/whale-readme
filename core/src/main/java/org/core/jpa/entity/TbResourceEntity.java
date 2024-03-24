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
@Table(name = "tb_resource")
public class TbResourceEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 3852711638450316352L;
    
    @Id
    @GeneratedValue(generator = "IdGenerator", strategy = GenerationType.AUTO)
    @GenericGenerator(name = "IdGenerator", type = ManualInsertGenerator.class)
    @Column(name = "id", nullable = false)
    private Long id;
    @Basic
    @Column(name = "music_id", nullable = true)
    private Long musicId;
    @Basic
    @Column(name = "rate", nullable = true)
    private Integer rate;
    @Basic
    @Column(name = "path", nullable = true, length = 512)
    private String path;
    @Basic
    @Column(name = "md5", nullable = false, length = 32)
    private String md5;
    @Basic
    @Column(name = "level", nullable = true, length = 8)
    private String level;
    @Basic
    @Column(name = "encode_type", nullable = true, length = 10)
    private String encodeType;
    @Basic
    @Column(name = "size", nullable = true)
    private Long size;
    @Basic
    @Column(name = "user_id", nullable = true)
    private Long userId;
    @Basic
    @Column(name = "create_time", nullable = true)
    private Timestamp createTime;
    @Basic
    @Column(name = "update_time", nullable = true)
    private Timestamp updateTime;
    @OneToMany(mappedBy = "tbResourceByResourceId")
    private Collection<TbOriginEntity> tbOriginsById;
    @ManyToOne
    @JoinColumn(name = "music_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    private TbMusicEntity tbMusicByMusicId;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false)
    private SysUserEntity sysUserByUserId;
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        
        TbResourceEntity that = (TbResourceEntity) o;
        
        return new EqualsBuilder().append(id, that.id)
                                  .append(musicId, that.musicId)
                                  .append(rate, that.rate)
                                  .append(path, that.path)
                                  .append(md5, that.md5)
                                  .append(level, that.level)
                                  .append(encodeType, that.encodeType)
                                  .append(size, that.size)
                                  .append(userId, that.userId)
                                  .append(createTime, that.createTime)
                                  .append(updateTime, that.updateTime)
                                  .isEquals();
    }
    
    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id)
                                          .append(musicId)
                                          .append(rate)
                                          .append(path)
                                          .append(md5)
                                          .append(level)
                                          .append(encodeType)
                                          .append(size)
                                          .append(userId)
                                          .append(createTime)
                                          .append(updateTime)
                                          .toHashCode();
    }
    
}
