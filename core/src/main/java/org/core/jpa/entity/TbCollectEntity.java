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
@Table(name = "tb_collect")
public class TbCollectEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 3852731611450316352L;
    
    @Id
    @GeneratedValue(generator = "IdGenerator", strategy = GenerationType.AUTO)
    @GenericGenerator(name = "IdGenerator", type = ManualInsertGenerator.class)
    @Column(name = "id", nullable = false)
    private Long id;
    @Basic
    @Column(name = "play_list_name", nullable = false, length = 256)
    private String playListName;
    @Basic
    @Column(name = "type", nullable = false)
    private Byte type;
    @Basic
    @Column(name = "description", length = 512)
    private String description;
    @Basic
    @Column(name = "user_id")
    private Long userId;
    @Basic
    @Column(name = "sort")
    private Long sort;
    @Basic
    @Column(name = "create_time")
    private Timestamp createTime;
    @Basic
    @Column(name = "update_time")
    private Timestamp updateTime;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false)
    private SysUserEntity sysUserByUserId;
    @OneToMany(mappedBy = "tbCollectByCollectId")
    private Collection<TbCollectMusicEntity> tbCollectMusicsById;
    @OneToMany(mappedBy = "tbCollectByCollectId")
    private Collection<TbUserCollectEntity> tbUserCollectsById;
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        
        TbCollectEntity that = (TbCollectEntity) o;
        
        return new EqualsBuilder().append(id, that.id)
                                  .append(playListName, that.playListName)
                                  .append(type, that.type)
                                  .append(description, that.description)
                                  .append(userId, that.userId)
                                  .append(sort, that.sort)
                                  .append(createTime, that.createTime)
                                  .append(updateTime, that.updateTime)
                                  .isEquals();
    }
    
    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id)
                                          .append(playListName)
                                          .append(type)
                                          .append(description)
                                          .append(userId)
                                          .append(sort)
                                          .append(createTime)
                                          .append(updateTime)
                                          .toHashCode();
    }
}
