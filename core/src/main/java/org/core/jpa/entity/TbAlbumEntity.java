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
@Table(name = "tb_album")
public class TbAlbumEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 2405172432150251807L;
    
    @Id
    @GeneratedValue(generator = "IdGenerator", strategy = GenerationType.AUTO)
    @GenericGenerator(name = "IdGenerator", type = ManualInsertGenerator.class)
    @Column(name = "id", nullable = false)
    private Long id;
    @Basic
    @Column(name = "album_name", nullable = false, length = 512)
    private String albumName;
    @Basic
    @Column(name = "sub_type", nullable = true, length = 128)
    private String subType;
    @Basic
    @Column(name = "description", nullable = true, length = Integer.MAX_VALUE)
    private String description;
    @Basic
    @Column(name = "company", nullable = true, length = 256)
    private String company;
    @Basic
    @Column(name = "publish_time", nullable = true)
    private Timestamp publishTime;
    @Basic
    @Column(name = "user_id", nullable = true)
    private Long userId;
    @Basic
    @Column(name = "update_time", nullable = true)
    private Timestamp updateTime;
    @Basic
    @Column(name = "create_time", nullable = true)
    private Timestamp createTime;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false)
    private SysUserEntity sysUserByUserId;
    @OneToMany(mappedBy = "tbAlbumByAlbumId")
    private Collection<TbMusicEntity> tbMusicsById;
    @OneToMany(mappedBy = "tbAlbumByAlbumId")
    private Collection<TbUserAlbumEntity> tbUserAlbumsById;
    
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        
        TbAlbumEntity that = (TbAlbumEntity) o;
        
        return new EqualsBuilder().append(id, that.id)
                                  .append(albumName, that.albumName)
                                  .append(subType, that.subType)
                                  .append(description, that.description)
                                  .append(company, that.company)
                                  .append(publishTime, that.publishTime)
                                  .append(userId, that.userId)
                                  .append(updateTime, that.updateTime)
                                  .append(createTime, that.createTime)
                                  .isEquals();
    }
    
    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id)
                                          .append(albumName)
                                          .append(subType)
                                          .append(description)
                                          .append(company)
                                          .append(publishTime)
                                          .append(userId)
                                          .append(updateTime)
                                          .append(createTime)
                                          .toHashCode();
    }
}
